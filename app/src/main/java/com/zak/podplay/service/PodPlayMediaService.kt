package com.zak.podplay.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat

class PodPlayMediaService: MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        createMediaSession()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    private fun createMediaSession() {
        mediaSession = MediaSessionCompat(this, "PodPlayMediaService")
        sessionToken = mediaSession.sessionToken
        val callback = PodPlayMediaCallback(this, mediaSession)
        mediaSession.setCallback(callback)
    }
}