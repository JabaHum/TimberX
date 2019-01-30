package com.naman14.timberx.ui.fragments

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.naman14.timberx.R
import com.naman14.timberx.TimberMusicService
import com.naman14.timberx.databinding.FragmentCategorySongsBinding
import com.naman14.timberx.models.CategorySongData
import com.naman14.timberx.ui.adapters.SongsAdapter
import com.naman14.timberx.ui.widgets.RecyclerItemClickListener
import com.naman14.timberx.util.*
import com.naman14.timberx.models.Song
import com.naman14.timberx.util.media.getExtraBundle
import kotlinx.android.synthetic.main.fragment_album_detail.*

class CategorySongsFragment : MediaItemFragment() {

    lateinit var categorySongData: CategorySongData

    var binding by AutoClearedValue<FragmentCategorySongsBinding>(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_category_songs, container, false)

        categorySongData = arguments!![Constants.CATEGORY_SONG_DATA] as CategorySongData

        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.categorySongData = categorySongData

        val adapter = SongsAdapter().apply {
            popupMenuListener = mainViewModel.popupMenuListener

            if (categorySongData.type == TimberMusicService.TYPE_PLAYLIST) {
                playlistId = categorySongData.id
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        mediaItemFragmentViewModel.mediaItems.observe(this,
                Observer<List<MediaBrowserCompat.MediaItem>> { list ->
                    val isEmptyList = list?.isEmpty() ?: true
                    if (!isEmptyList) {
                        adapter.updateData(list as ArrayList<Song>)
                    }
                })

        recyclerView.addOnItemClick(object: RecyclerItemClickListener.OnClickListener {
            override fun onItemClick(position: Int, view: View) {
                mainViewModel.mediaItemClicked(adapter.songs!![position], getExtraBundle(adapter.songs!!.toSongIDs(), categorySongData.title))
            }
        })
    }

}
