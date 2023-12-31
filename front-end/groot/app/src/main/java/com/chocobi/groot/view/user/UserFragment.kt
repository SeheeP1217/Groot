package com.chocobi.groot.view.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chocobi.groot.MainActivity
import com.chocobi.groot.R
import com.chocobi.groot.Thread.ThreadUtil
import com.chocobi.groot.data.RetrofitClient
import com.chocobi.groot.data.UserData
import com.chocobi.groot.view.community.CommunityTab1Fragment
import com.chocobi.groot.view.community.CommunityTab2Fragment
import com.chocobi.groot.view.community.CommunityTab3Fragment
import com.chocobi.groot.view.community.CommunityTab4Fragment
import com.chocobi.groot.view.community.model.CommunityArticleListResponse
import com.chocobi.groot.view.user.model.UserService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_user, container, false)

        //        초기 화면 설정
        var nicknameText = rootView.findViewById<TextView>(R.id.nickname)
        nicknameText.text = UserData.getNickName()

        var registerDateText = rootView.findViewById<TextView>(R.id.registerDate)
        registerDateText.text = UserData.getRegisterDate().toString()

        var totalArticleText = rootView.findViewById<TextView>(R.id.totalArticle)
        getUserArticle(totalArticleText)

        var totalBookmarkText = rootView.findViewById<TextView>(R.id.totalBookmark)
        getUserBookmark(totalBookmarkText)
//        Fragment 이동 조작
        val mActivity = activity as MainActivity

//        Setting 페이지로 이동
        val settingBtn = rootView.findViewById<ImageButton>(R.id.settingBtn)
        settingBtn.setOnClickListener {
            mActivity.changeFragment("setting")
        }

        // Inflate the layout for this fragment
        return rootView
    }


    //    탭 구현
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 = view.findViewById(R.id.user_pager)
        val adapter = UserTabAdapter(this)
        viewPager.adapter = adapter

        val tabList = listOf<String>("식물", "작성글", "북마크")
        val tabLayout: TabLayout = view.findViewById(R.id.layout_tab)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

    private inner class UserTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserTab1Fragment()
                1 -> UserTab2Fragment()
                2 -> UserTab3Fragment()
                else -> UserTab1Fragment()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun getUserArticle(totalArticleText:TextView) {

        var retrofit = RetrofitClient.getClient()!!
        var userService = retrofit.create(UserService::class.java)

        userService.requestUserArticleList(0, 1).enqueue(object :
            Callback<CommunityArticleListResponse> {
            override fun onResponse(call: Call<CommunityArticleListResponse>, response: Response<CommunityArticleListResponse>) {
                if (response.code() == 200) {
                    Log.d("UserFragment", "성공")

                    val checkTotal =  response.body()?.articles?.total
                    totalArticleText.text = checkTotal.toString()
                    Log.d("UserFragment", "$checkTotal")

                } else {
                    Log.d("UserFragment", "실패1")
                }
            }

            override fun onFailure(call: Call<CommunityArticleListResponse>, t: Throwable) {
                Log.d("UserFragment", "실패2")
            }
        })
    }

    private fun getUserBookmark(totalBookmarkText:TextView) {

        var retrofit = RetrofitClient.getClient()!!
        var userService = retrofit.create(UserService::class.java)

        userService.requestUserBookmarkList(0, 1).enqueue(object :
            Callback<CommunityArticleListResponse> {
            override fun onResponse(call: Call<CommunityArticleListResponse>, response: Response<CommunityArticleListResponse>) {
                if (response.code() == 200) {
                    Log.d("UserFragment", "성공")

                    val checkTotal =  response.body()?.articles?.total
                    totalBookmarkText.text = checkTotal.toString()
                    Log.d("UserFragment", "$checkTotal")

                } else {
                    Log.d("UserFragment", "실패1")
                }
            }

            override fun onFailure(call: Call<CommunityArticleListResponse>, t: Throwable) {
                Log.d("UserFragment", "실패2")
            }
        })
    }

}