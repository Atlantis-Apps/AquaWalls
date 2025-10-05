package com.atlantis.aquawalls

import android.app.WallpaperManager
import android.net.Uri
import android.os.Bundle
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
import java.io.InputStream

// Wallpaper data
data class Wallpaper(
    val name: String,
    val assetPath: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaWallsApp()
        }
    }
}

@Composable
fun AquaWallsApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            WallpaperListScreen(navController)
        }
        composable("preview/{assetPath}") { backStackEntry ->
            val assetPath = backStackEntry.arguments?.getString("assetPath") ?: ""
            PreviewScreen(assetPath = assetPath, navController = navController)
        }
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
                title = { Text("AquaWalls", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A4D9C))
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(wallpapers) { wallpaper ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("preview/${Uri.encode(wallpaper.assetPath)}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/${wallpaper.assetPath}")
                            .allowHardware(false)
                            .crossfade(true)
                            .build(),
                        contentDescription = wallpaper.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = wallpaper.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.CenterHorizontally)
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
    val wallpaperManager = WallpaperManager.getInstance(context)

    Box(modifier = Modifier.fillMaxSize()) {
        // Show the wallpaper fullscreen
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .allowHardware(false)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Back button
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

        // Set Wallpaper button
        Button(
            onClick = {
                try {
                    val inputStream: InputStream = context.assets.open(assetPath)
                    wallpaperManager.setStream(inputStream)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A4D9C)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Text("Set Wallpaper", color = Color.White)
        }
    }
}