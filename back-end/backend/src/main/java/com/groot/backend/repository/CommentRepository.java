package com.groot.backend.repository;

import com.groot.backend.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByArticleId(Long articleId);
    Page<CommentEntity> findAllByArticleId(Long articleId, PageRequest pageRequest);
    void deleteById(Long commentId);
}
