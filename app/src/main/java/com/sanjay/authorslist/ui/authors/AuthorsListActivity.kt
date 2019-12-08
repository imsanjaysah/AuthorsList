package com.sanjay.authorslist.ui.authors

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanjay.authorslist.R
import com.sanjay.authorslist.constants.State
import com.sanjay.authorslist.ui.BaseActivity
import com.sanjay.authorslist.ui.posts.PostsListActivity
import kotlinx.android.synthetic.main.activity_authors_list.*
import kotlinx.android.synthetic.main.content_authors_list.*
import javax.inject.Inject

class AuthorsListActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AuthorsViewModel
    private lateinit var authorsListAdapter: AuthorsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authors_list)
        toolbar.title = getString(R.string.app_name)
        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AuthorsViewModel::class.java)
        initAdapter()
        initState()
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        authorsListAdapter = AuthorsListAdapter({ author, imageView ->

            PostsListActivity.start(this, author, imageView)
        }, {
            //On click of retry textview call the api again
            viewModel.retry()
        })
        recycler_view_authors.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        //set the adapter
        recycler_view_authors.adapter = authorsListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.authorsList?.observe(this, Observer {
            authorsListAdapter.submitList(it)
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
                authorsListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}
