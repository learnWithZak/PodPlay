package com.zak.podplay.repository

import com.zak.podplay.model.Podcast
import com.zak.podplay.service.RssFeedService
import kotlinx.coroutines.launch
import javax.xml.bind.JAXBElement.GlobalScope

class PodcastRepo {

    fun getPodcast(feedUrl: String): Podcast? {
        val rssFeedService = RssFeedService.instance
        kotlinx.coroutines.GlobalScope.launch {
            rssFeedService.getFeed(feedUrl)
        }

        return Podcast(
            feedUrl = feedUrl,
            feedTitle = "No name",
            feedDesc = "No Description",
            imageUrl = "No image"
        )
    }
}