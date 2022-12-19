package com.zak.podplay.service

import android.content.Context
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

class PodPlayMediaCallback(
    val context: Context,
    val mediaSession: MediaSessionCompat,
    var mediaPlayer: MediaPlayer? = null
): MediaSessionCompat.Callback() {

    private var mediaUri: Uri? = null
    private var newMedia: Boolean = false
    private var mediaExtras: Bundle? = null

    override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
        super.onPlayFromUri(uri, extras)
        println("Playing ${uri.toString()}")
        onPlay()
        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                    uri.toString()
                ).build()
        )
    }

    override fun onPlay() {
        super.onPlay()
        println("onPlay called")
        setState(PlaybackStateCompat.STATE_PLAYING)
    }

    override fun onStop() {
        super.onStop()
        println("onStop called")
    }

    override fun onPause() {
        super.onPause()
        println("onPause called")
        setState(PlaybackStateCompat.STATE_PAUSED)
    }

    private fun setState(state: Int) {
        var position: Long = -1

        val playbackState = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_STOP or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_PAUSE)
            .setState(state, position, 1.0f)
            .build()

        mediaSession.setPlaybackState(playbackState)
    }

    private fun setNewMedia(uri: Uri?) {
        newMedia = true
        mediaUri = uri
    }
}