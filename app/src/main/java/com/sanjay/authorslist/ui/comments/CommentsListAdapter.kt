/*
 * CommentsListAdapter.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.authorslist.R
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.remote.model.Comment
import com.sanjay.authorslist.utils.Utility.convertUTCtoLocalTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_comment.view.*
import kotlinx.android.synthetic.main.item_list_footer.view.*


class CommentsListAdapter(
    private val retry: () -> Unit
) : PagedListAdapter<Comment, RecyclerView.ViewHolder>(diffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        /**
         * DiffUtils is used improve the performance by finding difference between two lists and updating only the new items
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) CommentsViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as CommentsViewHolder).bind(getItem(position)!!)
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
 * ViewHolder to display comment's information
 */
class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(comment: Comment) {
        if (comment.avatarUrl?.isNotEmpty() == true) {
            Picasso.get().load(comment.avatarUrl).into(itemView.iv_profile)
        } else {
            itemView.iv_profile.setImageResource(R.drawable.ic_tour_guide)
        }
        itemView.txt_username.text = comment.userName
        itemView.txt_email.text = comment.email
        itemView.txt_date.text = convertUTCtoLocalTime(comment.date)
        itemView.txt_comment_body.text = comment.body

    }

    companion object {
        fun create(parent: ViewGroup): CommentsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_comment, parent, false)
            return CommentsViewHolder(view)
        }
    }
}

/**
 * ViewHolder to display loader at the bottom of the list while fetching next paged data
 */
class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progress_bar.visibility =
            if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == State.ERROR) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false)
            view.txt_error.setOnClickListener { retry() }
            return ListFooterViewHolder(view)
        }
    }
}