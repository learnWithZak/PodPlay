package com.zak.podplay.ui

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zak.podplay.R
import com.zak.podplay.adapter.PodcastListAdapter
import com.zak.podplay.databinding.ActivityPodcastBinding
import com.zak.podplay.model.Podcast
import com.zak.podplay.repository.ItunesRepo
import com.zak.podplay.repository.PodcastRepo
import com.zak.podplay.service.ItunesService
import com.zak.podplay.viewmodel.PodcastViewModel
import com.zak.podplay.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PodcastActivity : AppCompatActivity(), PodcastListAdapter.PodcastListAdapterListener {

    companion object {
        private const val TAG_DETAILS_FRAGMENT = "DetailsFragments"
    }

    val TAG = javaClass.simpleName
    private lateinit var databinding: ActivityPodcastBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var podcastListAdapter: PodcastListAdapter
    private lateinit var searchMenuItem: MenuItem
    private val podcastViewModel by viewModels<PodcastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(databinding.root)
        setupToolbar()
        setupViewModels()
        updateControls()
        handleIntent(intent)
        addBackStackListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        searchMenuItem = menu.findItem(R.id.search_item)
        val searchView = searchMenuItem.actionView as SearchView
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        if (supportFragmentManager.backStackEntryCount > 0) {
            databinding.podcastRecyclerView.visibility = View.INVISIBLE
        }
        if (databinding.podcastRecyclerView.visibility == View.INVISIBLE) {
            searchMenuItem.isVisible = false
        }
        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY) ?: return
            performSearch(query)
        }
    }

    private fun performSearch(term: String) {
        showProgressBar()
        GlobalScope.launch {
            val results = searchViewModel.searchPodcasts(term)
            withContext(Dispatchers.Main) {
                hideProgressBar()
                databinding.toolbar.title = term
                podcastListAdapter.setSearchData(results)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(databinding.toolbar)
    }

    private fun setupViewModels() {
        val service = ItunesService.instance
        searchViewModel.itunesRepo = ItunesRepo(service)
        podcastViewModel.podcastRepo = PodcastRepo()
    }

    private fun updateControls() {
        databinding.podcastRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        databinding.podcastRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(databinding.podcastRecyclerView.context, layoutManager.orientation)

        databinding.podcastRecyclerView.addItemDecoration(dividerItemDecoration)

        podcastListAdapter = PodcastListAdapter(null, this, this)
        databinding.podcastRecyclerView.adapter = podcastListAdapter
    }

    override fun onShowDetails(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
        val feedUrl = podcastSummaryViewData.feedUrl ?: return
        showProgressBar()
        val podcast = podcastViewModel.getPodcast(podcastSummaryViewData)
        hideProgressBar()
        if (podcast != null) {
            showDetailsFragment()
        } else {
            showError("Error loading feed $feedUrl")
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok_button), null)
            .create()
            .show()
    }

    private fun showProgressBar() {
        databinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        databinding.progressBar.visibility = View.INVISIBLE
    }

    private fun showDetailsFragment() {
        val podcastDetailsFragment = createPodcastDetailsFragment()
        supportFragmentManager.beginTransaction().add(
            R.id.podcastDetailsContainer,
            podcastDetailsFragment, TAG_DETAILS_FRAGMENT)
            .addToBackStack("DetailsFragment")
            .commit()
        databinding.podcastRecyclerView.visibility = View.INVISIBLE
        searchMenuItem.isVisible = false
    }

    private fun createPodcastDetailsFragment(): PodcastDetailsFragment {
        var podcastDetailsFragment = supportFragmentManager.findFragmentByTag(TAG_DETAILS_FRAGMENT) as PodcastDetailsFragment?

        if (podcastDetailsFragment == null) {
            podcastDetailsFragment = PodcastDetailsFragment.newInstance()
        }

        return podcastDetailsFragment
    }

    private fun addBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                databinding.podcastRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}