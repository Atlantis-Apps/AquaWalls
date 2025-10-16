package com.atlantis.aquawalls

import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.atlantis.aquawalls.ui.theme.AquaWallsTheme
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

data class Wallpaper(
    val name: String,
    val assetPath: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaWallsTheme {
                AquaWallsApp()
            }
        }
    }
}

@Composable
fun AquaWallsApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { WallpaperListScreen(navController) }
        composable("preview/{assetPath}") { backStackEntry ->
            val assetPath = backStackEntry.arguments?.getString("assetPath") ?: ""
            PreviewScreen(assetPath = assetPath, navController = navController)
        }
        composable("changelog") { ChangelogScreen(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperListScreen(navController: NavController) {
    val wallpapers = listOf(
        Wallpaper("Atlantis Hex", "wallpapers/wall2.png"),
        Wallpaper("Deep Sea City", "wallpapers/wall1.png"),
        Wallpaper("Blue Energy Flow", "wallpapers/wall3.png")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AquaWalls") },
                actions = {
                    IconButton(onClick = { navController.navigate("changelog") }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Changelog",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(wallpapers) { wallpaper ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("preview/${Uri.encode(wallpaper.assetPath)}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/${wallpaper.assetPath}")
                            .crossfade(true)
                            .build(),
                        contentDescription = wallpaper.name,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = wallpaper.name,
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun PreviewScreen(assetPath: String, navController: NavController) {
    val context = LocalContext.current
    val imageUri = "file:///android_asset/$assetPath"

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Button(
            onClick = { applyWallpaperFromAssets(context, assetPath) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Apply Wallpaper")
        }
    }
}

fun applyWallpaperFromAssets(context: Context, assetPath: String) {
    try {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val inputStream: InputStream = context.assets.open(assetPath)
        val cacheFile = File(context.cacheDir, "temp_wallpaper.png")
        FileOutputStream(cacheFile).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        val uri = Uri.fromFile(cacheFile)
        val bitmap = android.graphics.BitmapFactory.decodeFile(uri.path)
        wallpaperManager.setBitmap(bitmap)
        Toast.makeText(context, "Wallpaper applied successfully!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error applying wallpaper: ${e.message ?: e.toString()}",
            Toast.LENGTH_LONG
        ).show()
        e.printStackTrace()
    }
}