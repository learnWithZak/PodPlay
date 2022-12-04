package com.zak.podplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zak.podplay.databinding.EpisodeItemBinding
import com.zak.podplay.util.DateUtils.dateToShortDate
import com.zak.podplay.util.HtmlUtils
import com.zak.podplay.viewmodel.PodcastViewModel

class EpisodeListAdapter(
    private var episodeViewList: List<PodcastViewModel.EpisodeViewData>?
) : RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {

    inner class ViewHolder(
        dataBinding: EpisodeItemBinding
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        var episodeViewData: PodcastViewModel.EpisodeViewData? = null
        val titleTextView: TextView = dataBinding.titleView
        val descTextView: TextView = dataBinding.descView
        val durationTextView: TextView = dataBinding.durationView
        val releaseDateTextView: TextView = dataBinding.releaseDateView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): EpisodeListAdapter.ViewHolder {
        return ViewHolder(EpisodeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodeViewList = episodeViewList ?: return
        val episodeView = episodeViewList[position]

        holder.episodeViewData = episodeView
        holder.titleTextView.text = episodeView.title
        holder.descTextView.text =  HtmlUtils.htmlToSpannable(episodeView.description ?: "")
        holder.durationTextView.text = episodeView.duration
        holder.releaseDateTextView.text = episodeView.releaseDate?.let {
            dateToShortDate(it)
        }
    }

    override fun getItemCount(): Int {
        return episodeViewList?.size ?: 0
    }
}