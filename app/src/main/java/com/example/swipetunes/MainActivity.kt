package com.example.swipetunes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.app.Activity
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import kotlinx.coroutines.*


private const val REQUEST_CODE = 0x10
class MainActivity : AppCompatActivity() {
    private val topTracks = mutableListOf<TopTracks>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val spotify_login_btn = findViewById<Button>(R.id.login_button)
        val spotify_login = findViewById<ImageView>(R.id.login)
        val title_card = findViewById<TextView>(R.id.swipetunes)

        val colors = arrayOf(
            Color.parseColor("#9BB6FB"), // lilac
            Color.parseColor("#FB9B9B"),  // blush
            Color.parseColor("#5ECDA4") // darkmint
        )
        var currentIndex = 0

        lifecycleScope.launch {
            while (true) {

                title_card.setTextColor(colors[currentIndex])

                when (currentIndex)
                {
                    0 -> spotify_login.setImageResource(R.drawable.one)
                    1 -> spotify_login.setImageResource(R.drawable.two)
                    2 -> spotify_login.setImageResource(R.drawable.three)
                }

                delay(1500)
                currentIndex = (currentIndex + 1) % colors.size
            }
        }

        spotify_login.setOnTouchListener(object : OnSwipeTouchListener(this){
            override fun onSwipeRight() {
                Log.d("swipetest", "SWIPED")
                val request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
                AuthorizationClient.openLoginActivity(
                    this@MainActivity,
                    REQUEST_CODE,
                    request
                )


            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
            }
        })



//        spotify_login_btn.setOnClickListener {
//            val request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
//            AuthorizationClient.openLoginActivity(
//                this,
//                REQUEST_CODE,
//                request
//            )
//
//        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            Log.d("Status: ", "Got request code: $response")
            val accessToken: String? = response.accessToken
            Log.d("Status: ", "Got access token: $accessToken")
            val i = Intent(this@MainActivity, SwipeActivity::class.java)
            startActivity(i)
            lifecycleScope.launch(Dispatchers.Default) {
                val parsedJson = fetchTopTracks(accessToken)
                if (parsedJson != null) {
                    parsedJson.items?.let { list ->
                        topTracks.addAll(list)
                    }
                }
                if (topTracks.size > 0) {
                    val parsedSongs = getRecommendations(topTracks,accessToken)
                    if (parsedSongs != null) {
                        parsedSongs.items?.let { it ->
                            (application as SongApplication).db.songDao().insertAll( it.map {song ->
                                SongEntity(
                                    name = song.name,
                                    trackID = song.trackID,
                                    artist = song.final_artists,
                                    albumCover = song.albumCover,
                                    preview = song.preview
                                )
                            })
                        }
                    }
                }
            }
        }
    }


}