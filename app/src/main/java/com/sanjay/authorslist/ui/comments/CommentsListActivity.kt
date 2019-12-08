/*
 * CommentsListActivity.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui.comments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.authorslist.R
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.data.repository.remote.model.Post
import com.sanjay.authorslist.ui.BaseActivity
import com.sanjay.authorslist.utils.Utility.convertUTCtoLocalTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_comments.*
import kotlinx.android.synthetic.main.content_comments_list.*
import kotlinx.android.synthetic.main.content_post_header.*

import javax.inject.Inject


/**
 * Activity to display detail data
 */
class CommentsListActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CommentsViewModel
    private lateinit var commentsListAdapter: CommentsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comments)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collapse_tool_bar.setExpandedTitleColor(Color.TRANSPARENT);

        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CommentsViewModel::class.java)
        initAdapter()
        initState()
        (intent.getParcelableExtra(EXTRA_POST) as Post).apply {
            setData(this)
            if (savedInstanceState == null)
                viewModel.selectedPost.value = this

        }
    }

    private fun setData(post: Post) {
        supportActionBar?.title = post.title
        txt_title.text = post.title
        txt_date.text = convertUTCtoLocalTime(post.date)
        txt_post_body.text = post.body
        if (post.imageUrl?.isNotEmpty() == true) {
            Picasso.get().load(post.imageUrl).into(iv_profile)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }


    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        commentsListAdapter = CommentsListAdapter {
            //On click of retry textview call the api again
            viewModel.retry()
        }
        recycler_view_comments.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        //set the adapter
        recycler_view_comments.adapter = commentsListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.commentsList?.observe(this, Observer {
            commentsListAdapter.submitList(it)
        })
    }

    /**
     * Initializing the state
     */
    private fun initState() {
        //On click of retry textview call the api again
        txt_error.setOnClickListener { viewModel.retry() }
        //Observing the different states of the API calling, and updating the UI accordingly
        viewModel.state.observe(this, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                commentsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

    companion object {

        private const val EXTRA_POST = "Post"

        fun start(activity: Activity, post: Post) {
            activity.startActivity(Intent(activity, CommentsListActivity::class.java).apply {
                putExtra(EXTRA_POST, post)
            })
        }
    }
}