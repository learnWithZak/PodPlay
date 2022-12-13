package com.zak.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.zak.podplay.db.PodPlayDatabase
import com.zak.podplay.db.PodcastDao
import com.zak.podplay.model.Episode
import com.zak.podplay.model.Podcast
import com.zak.podplay.repository.PodcastRepo
import com.zak.podplay.util.DateUtils
import kotlinx.coroutines.launch
import java.util.*

class PodcastViewModel(application: Application) : AndroidViewModel(application) {

    var podcastRepo: PodcastRepo? = null
    private var activePodcast: Podcast? = null
    val podcastDao: PodcastDao = PodPlayDatabase.getInstance(application, viewModelScope).podcastDao()
    var activePodcastViewData: PodcastViewData? = null
    private val _podcastLiveData = MutableLiveData<PodcastViewData?>()
    val podcastLiveData: LiveData<PodcastViewData?> = _podcastLiveData
    var livePodcastSummaryData: LiveData<List<SearchViewModel.PodcastSummaryViewData>>? = null

    data class PodcastViewData(
        var subscribed: Boolean = false,
        var feedTitle: String? = "",
        var feedUrl: String? = "",
        var feedDesc: String? = "",
        var imageUrl: String? = "",
        var episodes: List<EpisodeViewData>
    )

    data class EpisodeViewData(
        var guid: String? = "",
        var title: String? = "",
        var description: String? = "",
        var mediaUrl: String? = "",
        var releaseDate: Date? = null,
        var duration: String? = ""
    )

    private fun episodesToEpisodesView(episodes: List<Episode>): List<EpisodeViewData> {
        return episodes.map {
            EpisodeViewData(
                guid = it.guid,
                title = it.titre,
                description = it.description,
                mediaUrl = it.medialUrl,
                releaseDate = it.releaseDate,
                duration = it.duration
            )
        }
    }

    private fun podcastToPodcastView(podcast: Podcast): PodcastViewData {
        return PodcastViewData(
            subscribed = false,
            feedTitle = podcast.feedTitle,
            feedUrl = podcast.feedUrl,
            feedDesc = podcast.feedDesc,
            imageUrl = podcast.imageUrl,
            episodes = episodesToEpisodesView(podcast.episodes)
        )
    }

    fun getPodcast(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
        podcastSummaryViewData.feedUrl?.let { url ->
            viewModelScope.launch {
                podcastRepo?.getPodcast(url)?.let {
                    it.feedTitle = podcastSummaryViewData.name ?: ""
                    it.imageUrl = podcastSummaryViewData.imageUrl ?: ""
                    _podcastLiveData.value = podcastToPodcastView(it)
                    activePodcast = it
                } ?: run {
                    _podcastLiveData.value = null
                }
            }
        } ?: run {
            _podcastLiveData.value = null
        }
    }

    fun saveActivePodcast() {
        val repo = podcastRepo ?: return
        activePodcast?.let {
            repo.save(it)
        }
    }

    private fun podcastToSummaryView(podcast: Podcast): SearchViewModel.PodcastSummaryViewData {
        return SearchViewModel.PodcastSummaryViewData(
            podcast.feedTitle,
            DateUtils.dateToShortDate(podcast.lastUpdated),
            podcast.imageUrl,
            podcast.feedUrl
        )
    }

    fun getPodcasts(): LiveData<List<SearchViewModel.PodcastSummaryViewData>>? {
        val repo = podcastRepo ?: return null
        if (livePodcastSummaryData == null) {
            val liveData = repo.getAll()
            livePodcastSummaryData = Transformations.map(liveData) { podcastList ->
                podcastList.map { podcast ->
                    podcastToSummaryView(podcast)
                }
            }
        }
        return livePodcastSummaryData
    }
}