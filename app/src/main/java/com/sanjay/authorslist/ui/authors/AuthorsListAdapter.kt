/*
 * AuthorsListAdapter.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui.authors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.authorslist.R
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.ui.view.ListFooterViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_author.view.*


class AuthorsListAdapter(
    private val onItemClick: (Author, ImageView) -> Unit,
    private val retry: () -> Unit
) : PagedListAdapter<Author, RecyclerView.ViewHolder>(diffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        /**
         * DiffUtils is used improve the performance by finding difference between two lists and updating only the new items
         */
        private val diffCallback = object : DiffUtil.ItemCallback<Author>() {
            override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    private val onItemClickListener = View.OnClickListener {
        val author = it.tag as Author
        onItemClick.invoke(author, it.iv_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) AuthorViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as AuthorViewHolder).bind(getItem(position)!!, onItemClickListener)
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
        notifyItemChanged(super.getItemCount())
    }
}

/**
 * ViewHolder to display author information
 */
class AuthorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        author: Author,
        onItemClickListener: View.OnClickListener
    ) {
        if (author.avatarUrl.isNotEmpty()) {
            Picasso.get().load(author.avatarUrl).into(itemView.iv_profile)
        } else {
            itemView.iv_profile.setImageResource(R.drawable.ic_tour_guide)
        }
        itemView.txt_name.text = author.name
        itemView.txt_username.text = author.userName
        itemView.txt_email.text = author.email

        itemView.tag = author
        itemView.setOnClickListener(onItemClickListener)
    }

    companion object {
        fun create(parent: ViewGroup): AuthorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_author, parent, false)
            return AuthorViewHolder(view)
        }
    }
}

