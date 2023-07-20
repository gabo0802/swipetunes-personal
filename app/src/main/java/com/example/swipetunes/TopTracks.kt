package com.example.swipetunes


import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TopTracksResponse(
    @SerialName("items")
    val items: List<TopTracks>?
)

@Keep
@Serializable
data class TopTracks(
    @SerialName("id")
    val id: String?,
) : java.io.Serializable
