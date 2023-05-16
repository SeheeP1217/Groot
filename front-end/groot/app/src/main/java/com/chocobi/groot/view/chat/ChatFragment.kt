package com.chocobi.groot.view.chat

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.chocobi.groot.R
import com.chocobi.groot.Thread.ThreadUtil
import com.chocobi.groot.data.GlobalVariables
import com.chocobi.groot.data.UserData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ChatFragment : Fragment() {

    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        Log.d("받아온", arguments.toString())

        val userPK = arguments?.getString("userPK")
        val nickName = arguments?.getString("nickName")
        val profile = arguments?.getString("profile")
        val roomId = arguments?.getString("roomId")

        Log.d("받아온 데이터", userPK.toString())
        Log.d("받아온 데이터", nickName.toString())
        Log.d("받아온 데이터", profile.toString())
        Log.d("받아온 데이터", roomId.toString())

        val categoryNameTextView = view.findViewById<TextView>(R.id.categoryName)
        val categoryIcon = view.findViewById<ImageView>(R.id.categoryIcon)
        val categoryProfileImg = view.findViewById<CircleImageView>(R.id.categoryProfileImg)
        categoryNameTextView
        categoryNameTextView.text = nickName
        categoryNameTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        categoryIcon.visibility = View.GONE
        categoryProfileImg.visibility = View.VISIBLE
        if(!profile.isNullOrBlank()) {

        categoryProfileImg.post {
            ThreadUtil.startThread {
                val futureTarget: FutureTarget<Bitmap> = Glide.with(requireContext())
                    .asBitmap()
                    .load(profile)
                    .submit(categoryProfileImg.width, categoryProfileImg.height)

                val bitmap = futureTarget.get()

                ThreadUtil.startUIThread(0) {
                    categoryProfileImg.setImageBitmap(bitmap)
                }
            }
        }
        }

//        ================================================================
//        ================================================================
//        뒤로 가기 버튼 처리해야 하는 곳
        val backBtn = view.findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
//        ================================================================
//        ================================================================




        mDbRef = Firebase.database.reference








        return view
    }

}