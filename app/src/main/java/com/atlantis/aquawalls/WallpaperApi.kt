package com.atlantis.aquawalls

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Data model
data class WallpaperResponse(val wallpapers: List<String>)

// Retrofit interface
interface WallpaperApi {
    @GET("wallpapers.json") // Replace with your actual file path
    suspend fun getWallpapers(): WallpaperResponse

    companion object {
        fun create(): WallpaperApi {
            return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/Atlantis-Apps/AquaWalls/main/") 
                // Change to your GitHub repo where wallpapers.json lives
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WallpaperApi::class.java)
        }
    }
}
