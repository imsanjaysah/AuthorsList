/*
 * PostsListAdapter.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.authorslist.R
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.ui.view.ListFooterViewHolder
import com.sanjay.authorslist.utils.Utility.convertUTCtoLocalTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_post.view.*


class PostsListAdapter(
    private val onItemClick: (Post) -> Unit,
    private val retry: () -> Unit
) : PagedListAdapter<Post, RecyclerView.ViewHolder>(diffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        /**
         * DiffUtils is used improve the performance by finding difference between two lists and updating only the new items
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    private val onItemClickListener = View.OnClickListener {
        val post = it.tag as Post
        onItemClick.invoke(post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) PostViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as PostViewHolder).bind(getItem(position)!!, onItemClickListener)
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyDataSetChanged()
    }
}

/**
 * ViewHolder to display post's information
 */
class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        post: Post,
        onItemClickListener: View.OnClickListener
    ) {
        if (post.imageUrl?.isNotEmpty() == true) {
            Picasso.get().load(post.imageUrl).into(itemView.iv_profile)
        } else {
            itemView.iv_profile.setImageResource(R.drawable.ic_tour_guide)
        }
        itemView.txt_post_title.text = post.title
        itemView.txt_post_body.text = post.body
        itemView.txt_date.text = convertUTCtoLocalTime(post.date)

        itemView.tag = post
        itemView.setOnClickListener(onItemClickListener)
    }

    companion object {
        fun create(parent: ViewGroup): PostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_post, parent, false)
            return PostViewHolder(view)
        }
    }
}