package com.atlantis.aquawalls

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogScreen(navController: androidx.navigation.NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Changelog") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "AquaWalls v1.0",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Initial Release 🎉",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
    text = """
    🚀 Version 1.0 — “Atlantis SilentStorm”

    • Brand-new AquaWalls App – a custom AtlantisOS wallpaper manager
    • Atlantis Theme Integration – polished Material You / Material 3 UI with dynamic color
    • Wallpaper Browser – view three Atlantis-inspired wallpapers:
        - Atlantis Hex
        - Deep Sea City
        - Blue Energy Flow
    • Full Wallpaper Preview + Apply directly
    • In-App Changelog Screen – top-right info icon
    • Offline Asset Storage (/assets/wallpapers/)
    • Crash-safe file handling and permission fixes
    • Adaptive App Icon – Atlantis blue circular logo
    • CI/CD Setup – GitHub Actions automatic builds
    • Material 3 migration and dynamic color support
    • Dark/Light theme support
    • Atlantis branding, fonts, and accent colors
    • Stable v1.0 public release 🎉
    """.trimIndent(),
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurface
)
        }
    }
}