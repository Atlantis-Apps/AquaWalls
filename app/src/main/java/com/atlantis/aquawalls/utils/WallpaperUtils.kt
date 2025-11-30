package com.atlantis.aquawalls

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun applyWallpaperFromAssets(context: Context, assetPath: String) {
    try {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val inputStream: InputStream = context.assets.open(assetPath)

        val cacheFile = File(context.cacheDir, "temp_wallpaper.jpg")
        FileOutputStream(cacheFile).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()

        val bitmap = BitmapFactory.decodeFile(cacheFile.absolutePath)
        wallpaperManager.setBitmap(bitmap)

        Toast.makeText(context, "Wallpaper applied!", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error applying wallpaper: ${e.message ?: e}",
            Toast.LENGTH_LONG
        ).show()
        e.printStackTrace()
    }
}