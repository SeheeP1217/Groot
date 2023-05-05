package com.groot.backend.service;

import com.groot.backend.dto.request.PotRegisterDTO;
import com.groot.backend.dto.response.PotListDTO;
import com.groot.backend.entity.PlantEntity;
import com.groot.backend.entity.PotEntity;
import com.groot.backend.repository.PlantRepository;
import com.groot.backend.repository.PotRepository;
import com.groot.backend.repository.UserRepository;
import com.groot.backend.util.PlantCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PotServiceImpl implements PotService{

    private final PotRepository potRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final Logger logger = LoggerFactory.getLogger(PotServiceImpl.class);
    @Override
    public Long createPot(Long userPK, PotRegisterDTO potRegisterDTO, MultipartFile multipartFile) {
        logger.info("Create pot : {}", potRegisterDTO.getPotName());
        String imgPath = "";

        // save image first
        try {
            imgPath = s3Service.upload(multipartFile, "pot");
        } catch (IOException e) {
            logger.info("Failed to upload image");
            return -2L;
        }

        logger.info("image uploaded : {}", imgPath);

        try {
            PlantEntity plantEntity = plantRepository.findById(potRegisterDTO.getPlantId()).get();
            logger.info("Plant found : {}", plantEntity.getKrName());

            PotEntity potEntity = potRepository.save(
                    PotEntity.builder()
                            .userEntity(userRepository.findById(userPK).get())
                            .plantEntity(plantEntity)
                            .characterId(PlantCodeUtil.characterCode(plantEntity.getGrwType()))
                            .name(potRegisterDTO.getPotName())
                            .imgPath(imgPath)
                            // default values might be modified
                            .temperature(potRegisterDTO.getTemperature() == 0 ? 20 : potRegisterDTO.getTemperature())
                            .illuminance(potRegisterDTO.getIlluminance() == 0 ? 500 : potRegisterDTO.getIlluminance())
                            .humidity(potRegisterDTO.getHumidity() == 0 ? 50 : potRegisterDTO.getHumidity())
                            .plantKrName(plantEntity.getKrName())
                            .build()
            );
            potRepository.save(potEntity);

            return potEntity.getId();
        } catch (IllegalArgumentException | NoSuchElementException e) {
            logger.info("Plant or User not found : plant {}, user {}", potRegisterDTO.getPlantId(), userPK);
            s3Service.delete(imgPath);
            return -1L;

        } catch (Exception e) {
            logger.info("Failed to save pot entity : {}, {}");
            logger.info("Related Exception : {}", e.getMessage());
            s3Service.delete(imgPath);
            return -3L;
        }
    }

    @Override
    public List<PotListDTO> potList(Long userPK) throws NoSuchElementException {
        logger.info("user pk : {}", userPK);

        List<PotEntity> list = potRepository.findAllByUserId(userPK);

        if(list == null || list.size() < 1) throw new NoSuchElementException();

        List<PotListDTO> ret = new ArrayList<>(list.size());

        list.forEach(potEntity -> {
            ret.add(PotListDTO.builder()
                            .potId(potEntity.getId())
                            .plantId(potEntity.getPlantId())
                            .potName(potEntity.getName())
                            .imgPath(potEntity.getImgPath())
                            .plantKrName(potEntity.getPlantKrName())
                            .dates(calcPeriod(potEntity.getCreatedDate()))
                            .createdTime(potEntity.getCreatedDate())
                            .waterDate(potEntity.getWaterDate())    // calc
                            .nutrientsDate(potEntity.getNutrientsDate())    // calc
                            .pruningDate(potEntity.getPruningDate())    // calc
                            .survival(potEntity.getSurvival())
                            .level(expToLevel(potEntity.getExperience()))   // level?
                            .characterId(potEntity.getCharacterId())    // id or path
                            .build());
        });

        return ret;
    }

    private int calcPeriod(LocalDateTime from) {
        LocalDateTime now = LocalDateTime.now();

        Period period = Period.between(from.toLocalDate(), now.toLocalDate());
        return period.getDays() + 1;
    }

    private int expToLevel(int exp) {
        return exp / 10;
    }
}
