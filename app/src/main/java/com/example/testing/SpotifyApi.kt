package com.example.spotifydemo.data

import retrofit2.http.*

// Response data
data class SpotifyTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)

data class SpotifyPlaylistResponse(
    val items: List<PlaylistItem>
)

data class PlaylistItem(
    val id: String,
    val name: String,
    val external_urls: ExternalUrls,
    val images: List<ImageObject>
)

data class ExternalUrls(val spotify: String)
data class ImageObject(val url: String)

interface SpotifyAuthApi {
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getAccessToken(
        @Header("Authorization") auth: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): SpotifyTokenResponse
}

interface SpotifyWebApi {
    @GET("playlists/{playlist_id}")
    suspend fun getPlaylist(
        @Header("Authorization") auth: String,
        @Path("playlist_id") playlistId: String
    ): PlaylistResponse

}

data class PlaylistResponse(
    val name: String,
    val tracks: Tracks
)

data class Tracks(val items: List<TrackItem>)
data class TrackItem(val track: TrackDetail)
data class TrackDetail(
    val name: String,
    val artists: List<Artist>,
    val album: Album
)

data class Artist(val name: String)
data class Album(val images: List<ImageObject>)

