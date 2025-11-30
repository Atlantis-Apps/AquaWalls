package com.atlantis.aquawalls.ui.requests

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var requester by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wallpaper Request") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Wallpaper Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Details / Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            OutlinedTextField(
                value = requester,
                onValueChange = { requester = it },
                label = { Text("Your Name (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (title.text.isBlank()) {
                        Toast.makeText(context, "Enter a title!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val message = """
                        Wallpaper Request:
                        
                        Title: ${title.text}
                        Notes: ${notes.text.ifBlank { "None" }}
                        Requested By: ${requester.text.ifBlank { "Anonymous" }}
                    """.trimIndent()

                    clipboard.setText(androidx.compose.ui.text.AnnotatedString(message))
                    Toast.makeText(context, "Copied! Paste it into message/email.", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Copy Request")
            }

            Button(
                onClick = {
                    val subject = Uri.encode("Wallpaper Request: ${title.text}")
                    val body = Uri.encode(
                        "Title: ${title.text}\nNotes: ${notes.text}\nRequester: ${requester.text}"
                    )

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("mailto:atlantisosrom@gmail.com?subject=$subject&body=$body")
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Send via Email")
            }
        }
    }
}