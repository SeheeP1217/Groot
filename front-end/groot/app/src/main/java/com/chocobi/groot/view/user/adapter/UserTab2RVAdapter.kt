package com.chocobi.groot.view.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.chocobi.groot.R
import com.chocobi.groot.adapter.item.ItemViewHolder
import com.chocobi.groot.view.community.CommunityDetailFragment
import com.chocobi.groot.view.community.adapter.RecyclerViewAdapter
import com.chocobi.groot.view.community.model.CommunityArticleListResponse

class UserTab2RVAdapter: RecyclerView.Adapter<ItemViewHolder>() {
    interface RecyclerViewAdapterDelegate {
        fun onLoadMore()
    }

    private var mutableList: MutableList<CommunityArticleListResponse> = mutableListOf()


    var delegate: RecyclerViewAdapter.RecyclerViewAdapterDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_community_tab2_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.communityArticleListResponse = mutableList[position]

        holder.delegate = object : ItemViewHolder.ItemViewHolderDelegate {
            override fun onItemViewClick(communityArticleListResponse: CommunityArticleListResponse) {
                val context = holder.itemView.context
                if (context is FragmentActivity) {
                    val fragmentManager = context.supportFragmentManager
                    val communityDetailFragment = CommunityDetailFragment()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fl_container, communityDetailFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        holder.updateView()

        if (position == mutableList.size - 1) {
            delegate?.onLoadMore()

        }

    }
    fun reload(mutableList: MutableList<CommunityArticleListResponse>) {


        this.mutableList.clear()
        this.mutableList.addAll(mutableList)
        notifyDataSetChanged()
    }

    fun loadMore(mutableList: MutableList<CommunityArticleListResponse>) {
        this.mutableList.addAll(mutableList)
        notifyItemRangeChanged(this.mutableList.size - mutableList.size, mutableList.size)
    }

}