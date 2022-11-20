package com.zak.podplay.repository

import com.zak.podplay.model.Podcast

class PodcastRepo {

    fun getPodcast(feedUrl: String): Podcast? {
        return Podcast(
            feedUrl = feedUrl,
            feedTitle = "No name",
            feedDesc = "No Description",
            imageUrl = "No image"
        )
    }
}