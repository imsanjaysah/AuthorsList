/*
 * AuthorsPostsActivity.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.ui.posts

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
import com.sanjay.authorslist.data.repository.remote.model.Author
import com.sanjay.authorslist.ui.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_author_posts.*
import kotlinx.android.synthetic.main.content_author_header.*
import kotlinx.android.synthetic.main.content_posts_list.*
import javax.inject.Inject


/**
 * Activity to display detail data
 */
class PostsListActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PostsViewModel
    private lateinit var postsListAdapter: PostsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_posts)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collapse_tool_bar.setExpandedTitleColor(Color.TRANSPARENT);

        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostsViewModel::class.java)
        initAdapter()
        initState()
        (intent.getParcelableExtra(EXTRA_AUTHOR) as Author).apply {
            setData(this)
            if (savedInstanceState == null)
                viewModel.selectedAuthor.value = this

        }
    }

    private fun setData(author: Author) {
        supportActionBar?.title = author.name

        txt_name.text = author.name
        txt_username.text = author.userName
        txt_email.text = author.email
        if (author.avatarUrl.isNotEmpty()) {
            Picasso.get().load(author.avatarUrl).into(iv_profile)
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
        postsListAdapter = PostsListAdapter({ post, imageView ->

        }, {
            //On click of retry textview call the api again
            viewModel.retry()
        })
        recycler_view_posts.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        //set the adapter
        recycler_view_posts.adapter = postsListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.postsList?.observe(this, Observer {
            postsListAdapter.submitList(it)
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
                postsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

    companion object {

        private const val EXTRA_AUTHOR = "Author"
        private const val SHARED_ELEMENT_PHOTO = "photo"

        fun start(activity: Activity, author: Author, photoImageView: ImageView) {

            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                photoImageView,
                SHARED_ELEMENT_PHOTO
            )
            activity.startActivity(Intent(activity, PostsListActivity::class.java).apply {
                putExtra(EXTRA_AUTHOR, author)
            }, options.toBundle())
        }
    }
}