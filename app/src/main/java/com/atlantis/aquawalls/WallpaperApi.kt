package com.atlantis.aquawalls

import android.content.Context

data class Wallpaper(
    val name: String,
    val assetPath: String
)

object WallpaperApi {
    fun getWallpapers(context: Context): List<Wallpaper> {
        val assetManager = context.assets
        val wallpapers = mutableListOf<Wallpaper>()

        try {
            val files = assetManager.list("wallpapers")
            files?.forEach { file ->
                if (file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".jpeg")) {
                    wallpapers.add(
                        Wallpaper(
                            name = file.substringBeforeLast('.'),
                            assetPath = "file:///android_asset/wallpapers/$file"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return wallpapers
    }
}
