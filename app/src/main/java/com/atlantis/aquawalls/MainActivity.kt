package com.atlantis.aquawalls

import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
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
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AquaWallsApp() }
    }
}

data class WallpaperResponse(val wallpapers: List<String>)

interface WallpaperApi {
    @GET("Edges-Playground/AtlantisOS/AquaWalls/main/wallpapers.json")
    suspend fun getWallpapers(): WallpaperResponse
    companion object {
        fun create(): WallpaperApi = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WallpaperApi::class.java)
    }
}

@Composable
fun AquaWallsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "grid") {
        composable("grid") { WallpaperGrid(navController) }
        composable("preview/{url}") { backStackEntry ->
            backStackEntry.arguments?.getString("url")?.let { WallpaperPreview(it, navController) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperGrid(navController: androidx.navigation.NavHostController) {
    val api = remember { WallpaperApi.create() }
    var wallpapers by remember { mutableStateOf(emptyList<String>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch { wallpapers = api.getWallpapers().wallpapers }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Aqua Walls") }) }) { padding ->
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = padding) {
            items(wallpapers) { url ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { navController.navigate("preview/$url") }
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(200.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperPreview(url: String, navController: androidx.navigation.NavHostController) {
    val context = LocalContext.current
    val wallpaperManager = WallpaperManager.getInstance(context)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Preview") }) },
        bottomBar = {
            Button(
                onClick = {
                    val req = ImageRequest.Builder(context).data(url).allowHardware(false).build()
                    val drawable = coil.ImageLoader(context).execute(req).drawable
                    if (drawable is BitmapDrawable) {
                        wallpaperManager.setBitmap(drawable.bitmap)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) { Text("Set as Wallpaper") }
        }
    ) { padding ->
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().padding(padding)
        )
    }
}
