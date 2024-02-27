package com.smitcoderx.assignment.samespaceassignment.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smitcoderx.assignment.samespaceassignment.R
import com.smitcoderx.assignment.samespaceassignment.api.SameSpaceAPI
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouModel
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouViewModel
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun PlayerScreen(viewModel: ForYouViewModel, data: ForYouModel?, id: Int?) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            this.prepare()
            Log.d("TAG", "Play: ${this.playerError.toString()}")
        }
    }


    var pageSelectedIndex by remember {
        mutableIntStateOf(id!!)
    }

    val pagerState = rememberPagerState {
        data?.data!!.count()
    }


    LaunchedEffect(key1 = pageSelectedIndex) {
        pagerState.animateScrollToPage(pageSelectedIndex)

    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            pageSelectedIndex = pagerState.currentPage
            player.seekTo(pagerState.currentPage, 0)
        }
    }


    LaunchedEffect(player.currentMediaItemIndex, id) {
        Log.d("TAG", "CurrentItem: ${player.currentMediaItemIndex}")
        pageSelectedIndex = player.currentMediaItemIndex
        pagerState.animateScrollToPage(
            pageSelectedIndex, animationSpec = tween(500)
        )
    }

    LaunchedEffect(Unit) {
        data?.data?.forEach {
            val mediaItem = MediaItem.fromUri(it.url.toString())
            player.addMediaItem(mediaItem)
        }
    }


    val playing = remember {
        mutableStateOf(false)
    }
    val currentPosition = remember {
        mutableLongStateOf(0)
    }
    val totalDuration = remember {
        mutableLongStateOf(0)
    }

    val sliderPosition = remember {
        mutableLongStateOf(0)
    }

    val progressSize = remember {
        mutableStateOf(IntSize(0, 0))
    }
    LaunchedEffect(player.isPlaying) {
        playing.value = player.isPlaying

    }
    LaunchedEffect(key1 = player.currentPosition, key2 = player.isPlaying) {
        delay(1000)
        currentPosition.longValue = player.currentPosition
    }

    LaunchedEffect(currentPosition.longValue) {
        sliderPosition.longValue = currentPosition.longValue
    }

    LaunchedEffect(player.duration) {
        if (player.duration > 0) {
            totalDuration.longValue = player.duration
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }


    val configuration = LocalConfiguration.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        HexToJetpackColor.getColor(data?.data?.get(pageSelectedIndex)?.accent.toString()),
                        Color.Black
                    ),
                )
            )
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            pageSize = PageSize.Fixed((configuration.screenWidthDp * 2 / (2.5)).dp),
            contentPadding = PaddingValues(horizontal = 35.dp)
        ) { page ->
            Log.d("TAG", "PlayerScreenIndex: $pageSelectedIndex")
            ImageSlider(modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    val alphaLerp = lerp(
                        start = 0.4f, stop = 1f, amount = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    val scaleLerp = lerp(
                        start = 1f, stop = 1f, amount = 1f - pageOffset.coerceIn(0f, .5f)
                    )
                    alpha = alphaLerp
                    scaleX = scaleLerp
                    scaleY = scaleLerp
                }
                .padding(6.dp), imageUrl = data?.data?.get(page)?.cover.toString())
        }
        MusicData(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            data = data!!,
            pageIndex = pageSelectedIndex,
            player = player,
            playing,
            currentPosition,
            totalDuration,
            progressSize,
            sliderPosition
        )
    }
}

@Composable
private fun ImageSlider(modifier: Modifier, imageUrl: String) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(SameSpaceAPI.IMAGE_URL + imageUrl).crossfade(true).build(),
            contentDescription = "Album Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        )
    }
}

@Composable
private fun MusicData(
    modifier: Modifier,
    data: ForYouModel,
    pageIndex: Int,
    player: ExoPlayer,
    playing: MutableState<Boolean>,
    currentPosition: MutableLongState,
    totalDuration: MutableLongState,
    progressSize: MutableState<IntSize>,
    sliderPosition: MutableLongState,
) {

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = data.data?.get(pageIndex)?.name.toString(), color = Color.White, fontSize = 22.sp
        )
        Text(
            text = data.data?.get(pageIndex)?.artist.toString(),
            color = Color.DarkGray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(54.dp))
        TrackSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(horizontal = 8.dp),
            value = sliderPosition.longValue.toFloat(),
            onValueChange = {
                sliderPosition.longValue = it.toLong()
            },
            onValueChangeFinished = {
                currentPosition.longValue = sliderPosition.longValue
                player.seekTo(sliderPosition.longValue)
            },
            songDuration = totalDuration.longValue.toFloat()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = convertLongToText(currentPosition.longValue),
                color = Color.DarkGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier.weight(1f),
                text = convertLongToText(totalDuration.longValue),
                color = Color.DarkGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Right
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Control(
                icon = R.drawable.ic_fast_rewind,
                size = 60.dp,
                bg = Color.Transparent,
                onClick = {
                    player.seekToPreviousMediaItem()
                })
            Control(icon = if (playing.value) R.drawable.ic_pause else R.drawable.ic_play,
                size = 80.dp,
                bg = Color.White,
                onClick = {
                    if (playing.value) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    playing.value = player.isPlaying

                })
            Control(
                icon = R.drawable.ic_fast_forward,
                bg = Color.Transparent,
                size = 60.dp,
                onClick = {
                    player.seekToNextMediaItem()

                })
        }
    }
}

@Composable
fun Control(icon: Int, size: Dp, bg: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bg)
            .clickable {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(size / 2),
            painter = painterResource(id = icon),
            tint = Color.DarkGray,
            contentDescription = null
        )
    }
}


@Composable
fun TrackSlider(
    modifier: Modifier,
    value: Float,
    onValueChange: (newValue: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    songDuration: Float
) {
    Slider(modifier = modifier, value = value, onValueChange = {
        onValueChange(it)
    }, onValueChangeFinished = {

        onValueChangeFinished()

    }, valueRange = 0f..songDuration, colors = SliderDefaults.colors(
        thumbColor = Color.White,
        activeTrackColor = Color.DarkGray,
        inactiveTrackColor = Color.Gray,
    )
    )
}


object HexToJetpackColor {
    fun getColor(colorString: String): Color {
        return Color(android.graphics.Color.parseColor(colorString))
    }
}

fun convertLongToText(long: Long): String {
    val sec = long / 1000
    val minutes = sec / 60
    val seconds = sec % 60

    val minutesString = if (minutes < 10) {
        "0${minutes}"
    } else {
        minutes.toString()
    }
    val secondsString = if (seconds < 10) {
        "0${seconds}"
    } else {
        seconds.toString()
    }
    return "$minutesString:$secondsString"
}
