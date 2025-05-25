package com.example.uusprjekti

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubePlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val currentVideoId by rememberUpdatedState(videoId)
    AndroidView(
        modifier = modifier,
        factory = { context: Context ->
            val youTubePlayerView = YouTubePlayerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(currentVideoId, 0f)
                }
            })
            youTubePlayerView
        },
        update = { view ->
        },
        onRelease = { view ->
            view.release()
        }
    )
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
