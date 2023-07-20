package com.example.swipetunes

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.*
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged

import kotlinx.coroutines.launch

import android.media.AudioManager
import android.media.MediaPlayer
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.delay


class swipeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    val entries = mutableListOf<SongEntity>()
    var select = 0

    private var song: TextView? = null
    private var artist: TextView? = null
    private var cover: ImageView? = null

    private var play: ImageView? = null
    private var pause: ImageView? = null
    lateinit var mediaPlayer: MediaPlayer

    var playLock = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val entries = mutableListOf<SongEntity>()



        lifecycleScope.launch {
            (activity?.applicationContext as SongApplication).db.songDao().getAll()
                .distinctUntilChanged()
                .collect { songs ->
                    //entries.clear()
                    entries.addAll(songs)
                    start()
                    //Log.d("tester", entries[select].artist.toString())

                    song?.text = entries[select].name.toString()
                    artist?.text = entries[select].artist.toString()


                    if (cover != null) {
                        Glide.with(activity?.applicationContext as SongApplication)
                            .load(entries[select].albumCover.toString())
                            .into(cover!!)
                    };


                }

        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_swipe, container, false)

        artist = rootView.findViewById<TextView>(R.id.artist)
        song = rootView.findViewById<TextView>(R.id.song)
        cover = rootView.findViewById<ImageView>(R.id.cover)

        play = rootView.findViewById<ImageView>(R.id.play)
        pause = rootView.findViewById<ImageView>(R.id.pause)
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        pause?.setOnClickListener(){
            pause()
        }

        play?.setOnClickListener(){
            play()
        }

        cover?.setOnTouchListener(object : OnSwipeTouchListener(activity?.applicationContext){
            override fun onSwipeRight() {

                if (playLock)
                {
                    return
                }
                lifecycleScope.launch {

                    playLock = true
                    cover!!.setBackgroundColor(resources.getColor(R.color.mint))
                    song!!.setTextColor(resources.getColor(R.color.mint))
                    artist!!.setTextColor(resources.getColor(R.color.mint))

                    delay(1000)
                    pchecker()
                    mediaPlayer.reset()
                    start()

                    cover!!.setBackgroundColor(resources.getColor(R.color.egg))
                    song!!.setTextColor(resources.getColor(R.color.black))
                    artist!!.setTextColor(resources.getColor(R.color.black))

                    song?.text = entries[select].name.toString()
                    artist?.text = entries[select].artist.toString()


                    if (cover != null) {
                        Glide.with(activity?.applicationContext as SongApplication)
                            .load(entries[select].albumCover.toString())
                            .into(cover!!)
                    };

                    playLock = false
                }
            }

            override fun onSwipeLeft() {

                if (playLock)
                {
                    return
                }

                lifecycleScope.launch {

                    playLock = true
                    cover!!.setBackgroundColor(resources.getColor(R.color.blush))
                    song!!.setTextColor(resources.getColor(R.color.blush))
                    artist!!.setTextColor(resources.getColor(R.color.blush))

                    delay(1000)
                    pchecker()
                    mediaPlayer.reset()
                    start()

                    cover!!.setBackgroundColor(resources.getColor(R.color.egg))
                    song!!.setTextColor(resources.getColor(R.color.black))
                    artist!!.setTextColor(resources.getColor(R.color.black))

                    song?.text = entries[select].name.toString()
                    artist?.text = entries[select].artist.toString()


                    if (cover != null) {
                        Glide.with(activity?.applicationContext as SongApplication)
                            .load(entries[select].albumCover.toString())
                            .into(cover!!)
                    };

                    playLock = false
                }
                }


        })


        return rootView
        //return inflater.inflate(R.layout.fragment_swipe, container, false)
    }

    fun pchecker(){
        var check = false
        select++

        while (!check)
        {
            if (entries[select].preview == null)
            {
                select++
            }
            else
            {
                check = true
            }
        }

    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    override fun onResume() {
        super.onResume()

    }

    fun pause()
    {
        if (mediaPlayer.isPlaying) {
            // if media player is playing we
            // are stopping it on below line.
            mediaPlayer.pause()
            play?.scaleX = (1).toFloat()
        }
    }

    fun play()
    {

        if (mediaPlayer.isPlaying) {
            // if media player is playing we
            // are stopping it on below line.
            mediaPlayer.seekTo(0)
            play?.scaleX = (-1).toFloat()
        }

        if (!mediaPlayer.isPlaying) {
            // if media player is playing we
            // are stopping it on below line.
            mediaPlayer.start()
            play?.scaleX = (-1).toFloat()
        }
    }
    fun stop()
    {
        if (mediaPlayer.isPlaying)
        {
            // if media player is playing we
            // are stopping it on below line.
            mediaPlayer.stop()

            // on below line we are resetting
            // our media player.
            mediaPlayer.reset()

            // on below line we are calling
            // release to release our media player.
            mediaPlayer.release()
        }
    }

    fun start(){
        try {
            // on below line we are setting audio
            // source as audio url on below line.
            mediaPlayer.setDataSource(entries[select].preview.toString())

            // on below line we are
            // preparing our media player.
            mediaPlayer.prepare()

            // on below line we are
            // starting our media player.
            mediaPlayer.start()


        } catch (e: Exception) {

            // on below line we are handling our exception.
            e.printStackTrace()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            swipeFragment().apply {

            }
    }
}