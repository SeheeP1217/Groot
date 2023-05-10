package com.chocobi.groot.view.pot

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display.Mode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chocobi.groot.MainActivity
import com.chocobi.groot.R
import com.chocobi.groot.data.GlobalVariables
import com.chocobi.groot.data.PERMISSION_CAMERA
import com.chocobi.groot.data.RetrofitClient
import com.chocobi.groot.mlkit.kotlin.ml.ArActivity
import com.chocobi.groot.view.pot.model.Plant
import com.chocobi.groot.view.pot.model.Pot
import com.chocobi.groot.view.pot.model.PotResponse
import com.chocobi.groot.view.pot.model.PotService
import com.google.android.material.chip.Chip
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.utils.Color
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class PotDetailFragment : Fragment(), PotBottomSheetListener {


    private val TAG = "PotDetailFragment"
    private var pot: Pot? = null
    private var plant: Plant? = null
    private lateinit var characterSceneView: SceneView
    private lateinit var potNameText: TextView
    private lateinit var potPlantText: TextView
    private lateinit var potPlantImg: ImageView
    private var potId: Int = 0
    private var modelNode: ModelNode? = null

    override fun onGetDetailRequested() {
        getPotDetail(potId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreate")
        var rootView = inflater.inflate(R.layout.fragment_pot_detail, container, false)
        val mActivity = activity as MainActivity
        potId = arguments?.getInt("potId") ?: 0
        getPotDetail(potId)
        potPlantImg = rootView.findViewById(R.id.potPlantImg)
        characterSceneView = rootView.findViewById(R.id.characterSceneView)

        Log.d(TAG, "${pot}")
        potNameText = rootView.findViewById(R.id.potName)
        potPlantText = rootView.findViewById(R.id.potPlant)


        val settingBtn = rootView.findViewById<ImageButton>(R.id.settingBtn)
        settingBtn.setOnClickListener {
            val potBottomSheet = PotBottomSheet(requireContext(), this)
            potBottomSheet.setPotId(potId)
            potBottomSheet.setPotName(pot?.potName.toString())
            potBottomSheet.show(
                mActivity.supportFragmentManager,
                potBottomSheet.tag
            )
        }
//        다이어리 버튼 클릭시
        val potPostDiaryBtn = rootView.findViewById<ImageButton>(R.id.potPostDiaryBtn)
        potPostDiaryBtn.setOnClickListener {
            if (potId is Int) {
                mActivity.setPotId(potId)
            }
            mActivity.changeFragment("pot_diary_create")
        }

//        만나러가기 버튼 클릭시
        val toArBtn = rootView.findViewById<Button>(R.id.toArBtn)
        toArBtn.setOnClickListener {
            val intent = Intent(context, ArActivity::class.java)
            intent.putExtra("GLBfile", pot?.characterGLBPath.toString())
            intent.putExtra("level", pot?.level.toString())
            intent.putExtra("potName", pot?.potName.toString())
            intent.putExtra("potPlant", pot?.plantKrName.toString())
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        탭 조작

        var tabBtn1 = view.findViewById<Chip>(R.id.tabBtn1)
        var tabBtn2 = view.findViewById<Chip>(R.id.tabBtn2)
        var tabBtn3 = view.findViewById<Chip>(R.id.tabBtn3)
        var tabBtn4 = view.findViewById<Chip>(R.id.tabBtn4)
        var tabBtn5 = view.findViewById<Chip>(R.id.tabBtn5)
        tabBtn1.setOnClickListener {
            val bundle = Bundle().apply {
                putString("waterCycle", plant?.waterCycle)
                putInt("minHumidity", plant?.minHumidity ?: 0)
                putInt("maxHumidity", plant?.maxHumidity ?: 0)
                putInt("year", pot?.waterDate?.date?.year ?:0)
                putInt("month", pot?.waterDate?.date?.year ?:0)
                putInt("date", pot?.waterDate?.date?.year ?:0)
            }
            val tab1 = PotDetailTab1Fragment().apply {
                arguments = bundle
            }
            childFragmentManager.beginTransaction().replace(R.id.tab_container, tab1).commit()
        }
        tabBtn2.setOnClickListener {
            val bundle = Bundle().apply {
                putString("grwType", plant?.grwType)
                putInt("year", pot?.pruningDate?.date?.year ?:0)
                putInt("month", pot?.pruningDate?.date?.year ?:0)
                putInt("date", pot?.pruningDate?.date?.year ?:0)
            }
            val tab2 = PotDetailTab2Fragment().apply {
                arguments = bundle
            }
            childFragmentManager.beginTransaction().replace(R.id.tab_container, tab2).commit()
        }
        tabBtn3.setOnClickListener {
            val bundle = Bundle().apply {
                putString("insectInfo", plant?.insectInfo)
            }
            val tab3 = PotDetailTab3Fragment().apply {
                arguments = bundle
            }
            childFragmentManager.beginTransaction().replace(R.id.tab_container, tab3).commit()
        }
        tabBtn4.setOnClickListener {
            val bundle = Bundle().apply {
                putString("place", plant?.place)
                putString("mgmtTip", plant?.mgmtTip)
                putInt("minGrwTemp", plant?.minGrwTemp ?:0)
                putInt("maxGrwTemp", plant?.maxGrwTemp ?:0)
            }
            val tab4 = PotDetailTab4Fragment().apply {
                arguments = bundle
            }
            childFragmentManager.beginTransaction().replace(R.id.tab_container, tab4).commit()
        }
        tabBtn5.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("year", pot?.nutrientDate?.date?.year ?:0)
                putInt("month", pot?.nutrientDate?.date?.year ?:0)
                putInt("date", pot?.nutrientDate?.date?.year ?:0)
            }
            val tab5 = PotDetailTab5Fragment().apply {
                arguments = bundle
            }
            childFragmentManager.beginTransaction().replace(R.id.tab_container, tab5).commit()
        }
    }

    fun getPotDetail(potId: Int) {
        var retrofit = RetrofitClient.getClient()!!
        var potService = retrofit.create(PotService::class.java)
        potService.getPotDetail(potId).enqueue(object :
            Callback<PotResponse> {
            override fun onResponse(
                call: Call<PotResponse>,
                response: Response<PotResponse>
            ) {
                val body = response.body()
                if (body != null && response.code() == 200) {
                    Log.d(TAG, "$body")
                    Log.d(TAG, "body: $body")
                    pot = body.pot
                    Log.d(TAG, "pot: $pot")
                    plant = body.plant
                    Log.d(TAG, "plant: $plant")
                    setCharacterSceneView()
                    setPlantContent()
                } else {
                    Log.d(TAG, "실패1")
                }
            }

            override fun onFailure(call: Call<PotResponse>, t: Throwable) {
                Log.d(TAG, "실패2")
            }
        })
    }

    fun setCharacterSceneView() {
        if (modelNode != null) {
            characterSceneView.removeChild(modelNode!!)
        }

        characterSceneView.backgroundColor = Color(255.0f, 255.0f, 255.0f, 255.0f)

        modelNode = ModelNode().apply {
            loadModelGlbAsync(
                glbFileLocation = pot?.characterGLBPath
                    ?: "https://groot-a303-s3.s3.ap-northeast-2.amazonaws.com/assets/unicorn_2.glb",
                autoAnimate = false,
                scaleToUnits = 1.0f,
                centerOrigin = Position(x = 0f, y = 0f, z = 0f),
            )
        }
        if (modelNode != null) {

            characterSceneView.addChild(modelNode!!)
        }
    }


    fun setPlantContent() {
        potNameText.text = pot?.potName
        potPlantText.text = pot?.plantKrName
        GlobalVariables.changeImgView(potPlantImg, pot?.imgPath.toString(), requireContext())
        val bundle = Bundle().apply {
            putString("waterCycle", plant?.waterCycle)
            putInt("minHumidity", plant?.minHumidity ?: 0)
            putInt("maxHumidity", plant?.maxHumidity ?: 0)
            putInt("year", pot?.waterDate?.date?.year ?:0)
            putInt("month", pot?.waterDate?.date?.year ?:0)
            putInt("date", pot?.waterDate?.date?.year ?:0)
        }
        val tab1 = PotDetailTab1Fragment().apply {
            arguments = bundle
        }
        childFragmentManager.beginTransaction().replace(R.id.tab_container, tab1).commit()

    }

}

interface PotBottomSheetListener {
    fun onGetDetailRequested()
}