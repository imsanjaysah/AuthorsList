package com.sanjay.authorslist.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.authorslist.R
import com.sanjay.authorslist.constants.State
import kotlinx.android.synthetic.main.content_authors_list.view.*

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