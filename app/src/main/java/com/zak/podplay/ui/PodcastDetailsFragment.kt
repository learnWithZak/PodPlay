package com.zak.podplay.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.zak.podplay.R
import com.zak.podplay.databinding.FragmentPodcastDetailsBinding
import com.zak.podplay.viewmodel.PodcastViewModel

class PodcastDetailsFragment : Fragment() {

    companion object {
        fun newInstance(): PodcastDetailsFragment {
            return PodcastDetailsFragment()
        }
    }

    private lateinit var databinding: FragmentPodcastDetailsBinding
    private val podcastViewModel: PodcastViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        databinding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)
        return databinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateControls()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_details, menu)
    }

    private fun updateControls() {
        val viewData = podcastViewModel.activePodcastViewData ?: return
        databinding.feedTitleTextView.text = viewData.feedTitle
        databinding.feedDescTextView.text = viewData.feedDesc
        activity?.let { activity ->
            Glide.with(activity).load(viewData.imageUrl).into(databinding.feedImageView)
        }
    }
}
