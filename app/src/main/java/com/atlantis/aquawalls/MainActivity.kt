package com.atlantis.aquawalls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
    NavHost(navController = navController, startDestination = "grid") {
        composable("grid") { WallpaperGrid(navController) }
        composable("preview/{url}") { backStackEntry ->
            backStackEntry.arguments?.getString("url")?.let { encoded ->
                val decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
                WallpaperPreview(decoded, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperGrid(navController: androidx.navigation.NavHostController) {
    val api = remember { WallpaperApi.create() }
    var wallpapers by remember { mutableStateOf(emptyList<String>()) }

    // ✅ Safe API call with error handling
    LaunchedEffect(Unit) {
        try {
            val result = api.getWallpapers()
            wallpapers = result.wallpapers
        } catch (e: Exception) {
            e.printStackTrace()
            wallpapers = emptyList() // avoid crash if API fails
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("AquaWalls") }) }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = padding
        ) {
            items(wallpapers) { url ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                            navController.navigate("preview/$encoded")
                        }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop, // grid stays cropped for nice look
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperPreview(url: String, navController: androidx.navigation.NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}