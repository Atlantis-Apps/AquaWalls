package com.atlantis.aquawalls

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.atlantis.aquawalls.ui.changelog.ChangelogScreen
import com.atlantis.aquawalls.ui.requests.RequestsScreen
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLDecoder

// --------------------------------------
// DATA MODELS
// --------------------------------------

data class Category(
    val id: String,
    val title: String,
    val coverAsset: String
)

data class Wallpaper(
    val name: String,
    val assetPath: String
)

// --------------------------------------
// STATIC CATEGORY LIST
// --------------------------------------

private val categories = listOf(
    Category("atlantis", "Atlantis", "categories/atlantis.jpg"),
    Category("anime", "Anime", "categories/anime.jpg"),
    Category("minimal", "Minimal", "categories/minimal.jpg"),
    Category("space", "Space", "categories/space.jpg"),
    Category("neon", "Neon", "categories/neon.jpg"),

    // ⭐ THE PIXEL PROJECT (TPP) restored
    Category("tpp", "The Pixel Project", "categories/tpp.jpg")
)

// --------------------------------------
// WALLPAPER LISTS
// --------------------------------------

private fun wallpapersFor(categoryId: String): List<Wallpaper> = when (categoryId) {
    "atlantis" -> listOf(
        Wallpaper("Atlantis 1", "wallpapers/atlantis/at1.jpg"),
        Wallpaper("Atlantis 2", "wallpapers/atlantis/at2.jpg"),
        Wallpaper("Atlantis 3", "wallpapers/atlantis/at3.jpg")
    )
    "anime" -> listOf(
        Wallpaper("Anime 1", "wallpapers/anime/an1.jpg"),
        Wallpaper("Anime 2", "wallpapers/anime/an2.jpg"),
        Wallpaper("Anime 3", "wallpapers/anime/an3.jpg")
    )
    "minimal" -> listOf(
        Wallpaper("Minimal 1", "wallpapers/minimal/mi1.jpg"),
        Wallpaper("Minimal 2", "wallpapers/minimal/mi2.jpg"),
        Wallpaper("Minimal 3", "wallpapers/minimal/mi3.jpg")
    )
    "space" -> listOf(
        Wallpaper("Space 1", "wallpapers/space/sp1.jpg"),
        Wallpaper("Space 2", "wallpapers/space/sp2.jpg"),
        Wallpaper("Space 3", "wallpapers/space/sp3.jpg")
    )
    "neon" -> listOf(
        Wallpaper("Neon 1", "wallpapers/neon/ne1.jpg"),
        Wallpaper("Neon 2", "wallpapers/neon/ne2.jpg"),
        Wallpaper("Neon 3", "wallpapers/neon/ne3.jpg")
    )

    // ⭐ FULL TPP WALLPAPERS (33 images)
    "tpp" -> listOf(
        Wallpaper("Orange P", "wallpapers/tpp/tpp1.jpg"),
        Wallpaper("Lime P", "wallpapers/tpp/tpp2.jpg"),
        Wallpaper("Mixed Green P", "wallpapers/tpp/tpp3.jpg"),
        Wallpaper("White P", "wallpapers/tpp/tpp4.jpg"),
        Wallpaper("Dark Blue P", "wallpapers/tpp/tpp5.jpg"),
        Wallpaper("Purple P", "wallpapers/tpp/tpp6.jpg"),
        Wallpaper("Blue P", "wallpapers/tpp/tpp7.jpg"),
        Wallpaper("Green P", "wallpapers/tpp/tpp8.jpg"),
        Wallpaper("Dark Yellow P", "wallpapers/tpp/tpp9.jpg"),
        Wallpaper("Gray P", "wallpapers/tpp/tpp10.jpg"),
        Wallpaper("Art P", "wallpapers/tpp/tpp11.jpg"),
        Wallpaper("Art v2 P", "wallpapers/tpp/tpp12.jpg"),
        Wallpaper("Gradient Green O", "wallpapers/tpp/tpp13.jpg"),
        Wallpaper("Gradient Blue O", "wallpapers/tpp/tpp14.jpg"),
        Wallpaper("Gradient Red O", "wallpapers/tpp/tpp15.jpg"),
        Wallpaper("Gradient Red P", "wallpapers/tpp/tpp16.jpg"),
        Wallpaper("Gradient Lime P", "wallpapers/tpp/tpp17.jpg"),
        Wallpaper("Gradient Blue P", "wallpapers/tpp/tpp18.jpg"),
        Wallpaper("Gradient Purple P", "wallpapers/tpp/tpp19.jpg"),
        Wallpaper("Gradient Green P", "wallpapers/tpp/tpp20.jpg"),
        Wallpaper("Gradient Pink P", "wallpapers/tpp/tpp21.jpg"),
        Wallpaper("Random TPP", "wallpapers/tpp/tpp22.jpg"),
        Wallpaper("Plain TPP", "wallpapers/tpp/tpp23.jpg"),
        Wallpaper("Nordic Pixel", "wallpapers/tpp/tpp24.jpg"),
        Wallpaper("Boochi Pixel", "wallpapers/tpp/tpp25.jpg"),
        Wallpaper("Pixel Ball Green", "wallpapers/tpp/tpp26.jpg"),
        Wallpaper("Pixel Ball Pink", "wallpapers/tpp/tpp27.jpg"),
        Wallpaper("Pixel Ball Purple", "wallpapers/tpp/tpp28.jpg"),
        Wallpaper("Pixel Ball Blue", "wallpapers/tpp/tpp29.jpg"),
        Wallpaper("TPP Wall Yellow", "wallpapers/tpp/tpp30.jpg"),
        Wallpaper("TPP Wall Green", "wallpapers/tpp/tpp31.jpg"),
        Wallpaper("TPP Wall Purple", "wallpapers/tpp/tpp32.jpg"),
        Wallpaper("TPP Wall Pinkish", "wallpapers/tpp/tpp33.jpg")
    )

    else -> emptyList()
}

// --------------------------------------
// ACTIVITY
// --------------------------------------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dark = isSystemInDarkTheme()
            val scheme = if (dark) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = scheme) {
                AquaWallsApp()
            }
        }
    }
}

// --------------------------------------
// NAVIGATION GRAPH
// --------------------------------------

@Composable
fun AquaWallsApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        // HOME
        composable("home") {
            CategoryListScreen(
                onCategoryClick = { id -> navController.navigate("category/$id") },
                onChangelogClick = { navController.navigate("changelog") },

                // ⭐ FIX: Requests icon now works!!
                onRequestsClick = { navController.navigate("requests") }
            )
        }

        // CATEGORY
        composable(
            route = "category/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            CategoryScreen(
                categoryId = id,
                onBack = { navController.popBackStack() },
                onOpenPreview = { path ->
                    navController.navigate("preview/${Uri.encode(path)}")
                }
            )
        }

        // PREVIEW
        composable(
            "preview/{assetPath}",
            arguments = listOf(navArgument("assetPath") { type = NavType.StringType })
        ) { backStack ->
            val encoded = backStack.arguments?.getString("assetPath") ?: ""
            val decoded = URLDecoder.decode(encoded, "UTF-8")
            PreviewScreen(
                assetPath = decoded,
                onBack = { navController.popBackStack() }
            )
        }

        // CHANGELOG
        composable("changelog") {
            ChangelogScreen(onBack = { navController.popBackStack() })
        }

        // REQUESTS
        composable("requests") {
            RequestsScreen(onBack = { navController.popBackStack() })
        }
    }
}

// --------------------------------------
// HOME SCREEN
// --------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    onCategoryClick: (String) -> Unit,
    onChangelogClick: () -> Unit,
    onRequestsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AquaWalls") },
                actions = {
                    IconButton(onClick = onChangelogClick) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onRequestsClick) {
                Icon(Icons.Default.Image, contentDescription = "Requests")
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(categories) { cat ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onCategoryClick(cat.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(Modifier.height(140.dp)) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("file:///android_asset/${cat.coverAsset}")
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.25f)))
                        Text(
                            cat.title,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

// --------------------------------------
// CATEGORY SCREEN
// --------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryId: String,
    onBack: () -> Unit,
    onOpenPreview: (String) -> Unit
) {
    val wallpapers = wallpapersFor(categoryId)
    val title = categories.find { it.id == categoryId }?.title ?: "Wallpapers"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(wallpapers) { w ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onOpenPreview(w.assetPath) },
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    AsyncImage(
                        model = "file:///android_asset/${w.assetPath}",
                        contentDescription = w.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        w.name,
                        modifier = Modifier.padding(12.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// --------------------------------------
// PREVIEW SCREEN
// --------------------------------------

@Composable
fun PreviewScreen(
    assetPath: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            model = "file:///android_asset/$assetPath",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Button(
            onClick = { applyWallpaperFromAssets(context, assetPath) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Text("Apply Wallpaper")
        }
    }
}