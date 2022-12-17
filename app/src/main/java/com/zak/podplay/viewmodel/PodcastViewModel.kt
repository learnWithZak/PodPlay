package com.zak.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.zak.podplay.db.PodPlayDatabase
import com.zak.podplay.db.PodcastDao
import com.zak.podplay.model.Episode
import com.zak.podplay.model.Podcast
import com.zak.podplay.repository.PodcastRepo
import com.zak.podplay.util.DateUtils
import com.zak.podplay.viewmodel.SearchViewModel.PodcastSummaryViewData
import java.util.*

class PodcastViewModel(application: Application) : AndroidViewModel(application) {

    var podcastRepo: PodcastRepo? = null
    private val _podcastLiveData = MutableLiveData<PodcastViewData?>()
    val podcastLiveData: LiveData<PodcastViewData?> = _podcastLiveData
    var livePodcastSummaryData: LiveData<List<PodcastSummaryViewData>>? = null

    val podcastDao : PodcastDao = PodPlayDatabase
        .getInstance(application, viewModelScope)
        .podcastDao()

    private var activePodcast: Podcast? = null

    suspend fun getPodcast(podcastSummaryViewData: PodcastSummaryViewData) {
        podcastSummaryViewData.feedUrl?.let { url ->
            podcastRepo?.getPodcast(url)?.let {
                it.feedTitle = podcastSummaryViewData.name ?: ""
                it.imageUrl = podcastSummaryViewData.imageUrl ?: ""
                _podcastLiveData.value = podcastToPodcastView(it)
                activePodcast = it
            } ?: run {
                _podcastLiveData.value = null
            }
        } ?: run {
            _podcastLiveData.value = null
        }
    }

    fun getPodcasts(): LiveData<List<PodcastSummaryViewData>>? {
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

    fun saveActivePodcast() {
        val repo = podcastRepo ?: return
        activePodcast?.let {
            repo.save(it)
        }
    }

    private fun podcastToPodcastView(podcast: Podcast): PodcastViewData {
        return PodcastViewData(
            podcast.id != null,
            podcast.feedTitle,
            podcast.feedUrl,
            podcast.feedDesc,
            podcast.imageUrl,
            episodesToEpisodesView(podcast.episodes)
        )
    }

    private fun podcastToSummaryView(podcast: Podcast):
            PodcastSummaryViewData {
        return PodcastSummaryViewData(
            podcast.feedTitle,
            DateUtils.dateToShortDate(podcast.lastUpdated),
            podcast.imageUrl,
            podcast.feedUrl)
    }

    private fun episodesToEpisodesView(episodes: List<Episode>): List<EpisodeViewData> {
        return episodes.map {
            EpisodeViewData(it.guid, it.title, it.description, it.mediaUrl, it.releaseDate, it.duration)
        }
    }

    fun deleteActivePodcast() {
        val repo = podcastRepo ?: return
        activePodcast?.let {
            repo.delete(it)
        }
    }

    suspend fun setActivePodcast(feedUrl: String): PodcastSummaryViewData? {
        val repo = podcastRepo ?: return null
        val podcast = repo.getPodcast(feedUrl)
        return if (podcast == null) {
            null
        } else {
            _podcastLiveData.value = podcastToPodcastView(podcast)
            activePodcast = podcast
            podcastToSummaryView(podcast)
        }
    }

    data class PodcastViewData(var subscribed: Boolean = false, var feedTitle: String? = "",
                               var feedUrl: String? = "", var feedDesc: String? = "",
                               var imageUrl: String? = "", var episodes: List<EpisodeViewData>)

    data class EpisodeViewData(var guid: String? = "", var title: String? = "",
                               var description: String? = "", var mediaUrl: String? = "",
                               var releaseDate: Date? = null, var duration: String? = "")
}