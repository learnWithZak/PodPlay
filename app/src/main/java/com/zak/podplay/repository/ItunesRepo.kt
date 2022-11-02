package com.zak.podplay.repository

import com.zak.podplay.service.ItunesService

class ItunesRepo(private val itunesService: ItunesService) {
    suspend fun searchByTerm(term: String) = itunesService.searchPodcastByTerm(term)
}