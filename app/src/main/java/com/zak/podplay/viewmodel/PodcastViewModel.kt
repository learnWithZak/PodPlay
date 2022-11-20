package com.zak.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zak.podplay.model.Episode
import com.zak.podplay.model.Podcast
import com.zak.podplay.repository.PodcastRepo
import java.util.*

class PodcastViewModel(application: Application): AndroidViewModel(application) {

    var podcastRepo: PodcastRepo? = null
    var activePodcastViewData: PodcastViewData? = null

    data class PodcastViewData(
        var subscribed: Boolean = false,
        var feedTitle: String? = "",
        var feedUrl: String? = "",
        var feedDesc: String? = "",
        var imageUrl: String? = "",
        var episodes: List<EpisodeViewData>
    )

    data class EpisodeViewData (
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

    fun getPodcast(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData): PodcastViewData? {
        val repo = podcastRepo ?: return null
        val feedUrl = podcastSummaryViewData.feedUrl ?: return null
        val podcast = repo.getPodcast(feedUrl)

        podcast?.let {
            it.feedTitle = podcastSummaryViewData.name ?: ""
            it.imageUrl = podcastSummaryViewData.imageUrl ?: ""
            activePodcastViewData = podcastToPodcastView(it)
            return activePodcastViewData
        }
        return null
    }
}