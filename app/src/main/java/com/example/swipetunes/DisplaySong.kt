package com.example.swipetunes


import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RecommendationResponse(
    @SerialName("tracks")
    val items: List<DisplaySong>?
)
@Keep
@Serializable
data class DisplaySong(
    @SerialName("name")
    val name: String?,
    @SerialName("id")
    val trackID: String?,
    @SerialName("artists")
    val artists: List<Artists>?,
    @SerialName("album")
    val albumCoverLinks: Album?,
    @SerialName("preview_url")
    val preview: String?
) : java.io.Serializable {
    val final_artists = artists?.joinToString(separator=", ") { it -> "${it.name}" }
    val albumCover = albumCoverLinks?.images?.get(0)?.url
}

@Keep
@Serializable
data class Artists(
    @SerialName("name")
    val name: String
) : java.io.Serializable

@Keep
@Serializable
data class Album(
    @SerialName("images")
    val images: List<Image>?
) : java.io.Serializable

@Keep
@Serializable
data class Image(
    @SerialName("url")
    val url: String
) : java.io.Serializable