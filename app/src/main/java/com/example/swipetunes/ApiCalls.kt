package com.example.swipetunes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

private const val CLIENT_ID = BuildConfig.CLIENT_ID

private const val REDIRECT_URI = BuildConfig.REDIRECT_URI

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

public fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest {
    return AuthorizationRequest.Builder(CLIENT_ID, type, REDIRECT_URI)
        .setShowDialog(false)
        .setScopes(arrayOf("user-read-recently-played,user-library-modify,user-read-email,user-read-private,user-top-read"))
        .build()
}

public fun fetchTopTracks(token: String?): TopTracksResponse? {
    val request: Request = Request.Builder()
        .url("https://api.spotify.com/v1/me/top/tracks?limit=5&offset=2")
        .addHeader("Authorization", "Bearer $token")
        .build()
    val client = OkHttpClient()
    client.newCall(request).execute().use {response ->
        if (!response.isSuccessful) {
            Log.e("Error: ", "Failed to fetch articles: $response")
        } else {
            val jsonObject = JSONObject(response.body!!.string())
            val parsedJson = createJson().decodeFromString(
                TopTracksResponse.serializer(),
                jsonObject.toString()
            )
//            Log.d("Status: ","$parsedJson")
            return parsedJson
        }
    }
    return null
}

public fun getRecommendations(topTracks: List<TopTracks>, token: String?): RecommendationResponse? {
    val seed_tracks = topTracks.joinToString (separator = ",") { it -> "${it.id}" }
    Log.d("Status: ", "Seed Tracks: $seed_tracks")
    val request: Request = Request.Builder()
        .url("https://api.spotify.com/v1/recommendations?seed_tracks=$seed_tracks")
        .addHeader("Authorization", "Bearer $token")
        .build()
    val client = OkHttpClient()
    client.newCall(request).execute().use {response ->
        if (!response.isSuccessful) {
            Log.e("Error: ", "Failed to fetch articles: $response")
        } else {
            Log.d("Status: ", "got recommendations")
            val jsonObject = JSONObject(response.body!!.string())
            val parsedResponses = createJson().decodeFromString(
                RecommendationResponse.serializer(),
                jsonObject.toString()
            )
            Log.d("Status: ","$parsedResponses")
            parsedResponses::class.simpleName?.let { Log.d("testone", it) }
            return parsedResponses
        }
    }
    return null
}