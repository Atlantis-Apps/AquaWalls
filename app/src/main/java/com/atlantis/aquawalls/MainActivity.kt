package com.atlantis.aquawalls

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
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class Wallpaper(
    val name: String,
    val assetPath: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AquaWallsApp() }
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
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box {
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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .align(Alignment.BottomCenter)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = wallpaper.name,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
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
    }
}