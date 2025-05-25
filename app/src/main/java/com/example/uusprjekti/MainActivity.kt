@file:Suppress("DEPRECATION")

package com.example.uusprjekti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import com.example.uusprjekti.ui.theme.UusPrjektiTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.systemBars

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        setContent {
            UusPrjektiTheme {
                AppContent()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppContent() {
    var selectedTank by remember { mutableStateOf<Tank?>(null) }
    var tanks by remember {
        mutableStateOf(
            listOf(
                Tank(
                    "Tiger I", R.drawable.tiger1, "German heavy tank used in WWII.", "Germany", R.drawable.saksa, specs = mapOf(
                        "Weight" to "54 t",
                        "Armor" to "25–120 mm",
                        "Main armament" to "8.8 cm KwK 36",
                        "Speed" to "38 km/h"
                    ),
                    youtubeURL = "https://www.youtube.com/embed/Yl_zCQZooJo"
                ),
                Tank(
                    "T-34", R.drawable.t_34, "Soviet medium tank, highly influential in WWII.", "Russia", R.drawable.venaja, specs = mapOf(
                        "Weight" to "26.5 t",
                        "Armor" to "15–60 mm",
                        "Main armament" to "76.2 mm F-34",
                        "Speed" to "53 km/h"
                    ),
                    youtubeURL = "https://www.youtube.com/embed/0v5zdE21gXU"
                ),
                Tank(
                    "M4 Sherman", R.drawable.m4, "Main US tank in WWII.", "USA", R.drawable.usa, specs = mapOf(
                        "Weight" to "30.3 t",
                        "Armor" to "12–75 mm",
                        "Main armament" to "75 mm M3",
                        "Speed" to "48 km/h"
                    ),
                    youtubeURL = "https://www.youtube.com/embed/Ak_EPTAeUxE"
                ),
                Tank(
                    "Leopard 2A8", R.drawable.leopard_2a8, "Modern German main battle tank.", "Germany", R.drawable.saksa, specs = mapOf(
                        "Weight" to "64.5 t",
                        "Armor" to "Composite, classified",
                        "Main armament" to "120 mm Rheinmetall L/55",
                        "Speed" to "68 km/h"
                    ),
                    youtubeURL = "https://www.youtube.com/embed/h4y_vX2DQ2Q"
                ),
                Tank(
                    "T-90M", R.drawable.t_90m, "Modern Russian main battle tank.", "Russia", R.drawable.venaja, specs = mapOf(
                        "Weight" to "48 t",
                        "Armor" to "Composite, ERA",
                        "Main armament" to "125 mm 2A46M-5",
                        "Speed" to "60 km/h"
                    ),
                    youtubeURL = "https://www.youtube.com/embed/RChZhu4GCrQ"
                )
            )
        )
    }
    fun updateFavorite(tankName: String, isFavorite: Boolean) {
        tanks = tanks.map { if (it.name == tankName) it.copy(isFavorite = isFavorite) else it }
        if (selectedTank != null && selectedTank!!.name == tankName) {
            selectedTank = selectedTank!!.copy(isFavorite = isFavorite)
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.kf_51),
            contentDescription = "Blurred background",
            modifier = Modifier
                .fillMaxSize()
                .blur(16.dp),
            contentScale = ContentScale.Crop
        )
        AnimatedContent(
            targetState = selectedTank?.name,
            transitionSpec = {
                scaleIn(animationSpec = tween(400)).togetherWith(scaleOut(animationSpec = tween(400)))
            },
            label = "TankScreenTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) { tankName ->
            if (tankName == null) {
                TankListScreen(
                    tanks = tanks,
                    onTankSelected = { selected -> selectedTank = selected },
                    onFavoriteToggle = ::updateFavorite
                )
            } else {
                val tank = tanks.find { it.name == tankName } ?: return@AnimatedContent
                TankDetailScreen(
                    tank = tank,
                    onBack = { selectedTank = null },
                    onFavoriteToggle = { isFav -> updateFavorite(tank.name, isFav) }
                )
            }
        }
    }
}

@Composable
fun TankListScreen(
    tanks: List<Tank>,
    onTankSelected: (Tank) -> Unit,
    onFavoriteToggle: (String, Boolean) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    val filteredTanks = tanks.filter {
        it.name.contains(searchQuery, ignoreCase = true) && (!showOnlyFavorites || it.isFavorite)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp, vertical = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search tanks", color = Color.White) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { showOnlyFavorites = !showOnlyFavorites },
                modifier = Modifier.offset(y = (-7).dp) // tahti alemmas
            ) {
                Icon(
                    painter = painterResource(if (showOnlyFavorites) R.drawable.tahti_koko else R.drawable.tahti_reuna),
                    contentDescription = "Show favorites",
                    tint = if (showOnlyFavorites) Color.Yellow else Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                onClick = {
                    if (filteredTanks.isNotEmpty()) {
                        val randomTank = filteredTanks.random()
                        onTankSelected(randomTank)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .offset(y = 3.dp) // random nappula alemmas
            ) {
                Text("Random", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(filteredTanks) { tank ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onTankSelected(tank) },
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = tank.imageRes),
                            contentDescription = tank.name,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(end = 8.dp)
                        )
                        Image(
                            painter = painterResource(id = tank.flagRes),
                            contentDescription = tank.country,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            tank.name,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                onFavoriteToggle(tank.name, !tank.isFavorite)
                            },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                            Icon(
                                painter = painterResource(if (tank.isFavorite) R.drawable.tahti_koko else R.drawable.tahti_reuna),
                                contentDescription = if (tank.isFavorite) "Unfavorite" else "Favorite",
                                tint = if (tank.isFavorite) Color.Yellow else Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TankDetailScreen(
    tank: Tank,
    onBack: () -> Unit,
    onFavoriteToggle: (Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(tank.isFavorite) }
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp,top = 16.dp)
        ) {
            Text("Back", color = Color.White)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = tank.flagRes),
                    contentDescription = tank.country,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(tank.name, style = MaterialTheme.typography.headlineSmall, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        onFavoriteToggle(isFavorite)
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    Icon(
                        painter = painterResource(if (isFavorite) R.drawable.tahti_koko else R.drawable.tahti_reuna),
                        contentDescription = if (isFavorite) "Favorite" else "Not favorite",
                        tint = if (isFavorite) Color.Yellow else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = tank.imageRes),
                contentDescription = tank.name,
                modifier = Modifier
                    .size(240.dp)
                    .shadow(16.dp, shape = MaterialTheme.shapes.medium),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(tank.description, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (tank.specs.isNotEmpty()) {
                        Text("Specifications:", color = Color.White, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            tank.specs.forEach { (key, value) ->
                                Text("$key: $value", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
            if (!tank.youtubeURL.isNullOrEmpty()) {
                val videoId = tank.youtubeURL.substringAfterLast("/")
                Spacer(modifier = Modifier.height(16.dp))
                YoutubePlayer(
                    videoId = videoId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

data class Tank(
    val name: String,
    val imageRes: Int,
    val description: String,
    val country: String, // maat
    val flagRes: Int,    // maiden liput
    var isFavorite: Boolean = false,
    val specs: Map<String, String> = emptyMap(),
    val youtubeURL: String? = ""
)
