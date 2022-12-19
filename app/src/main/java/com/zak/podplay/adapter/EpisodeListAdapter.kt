package com.zak.podplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zak.podplay.databinding.EpisodeItemBinding
import com.zak.podplay.util.DateUtils.dateToShortDate
import com.zak.podplay.util.HtmlUtils
import com.zak.podplay.viewmodel.PodcastViewModel

interface EpisodeListAdapterListener {
    fun onSelectedEpisode(episodeViewData: PodcastViewModel.EpisodeViewData)
}

class EpisodeListAdapter(
    private var episodeViewList: List<PodcastViewModel.EpisodeViewData>?,
    private val episodeListAdapterListener: EpisodeListAdapterListener
) : RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {

    inner class ViewHolder(
        dataBinding: EpisodeItemBinding,
        val episodeListAdapterListener: EpisodeListAdapterListener
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        init {
            dataBinding.root.setOnClickListener {
                episodeViewData?.let {
                    episodeListAdapterListener.onSelectedEpisode(it)
                }
            }
        }

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
            LayoutInflater.from(parent.context), parent, false), episodeListAdapterListener)
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