package com.smitcoderx.assignment.samespaceassignment.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.smitcoderx.assignment.samespaceassignment.api.SameSpaceAPI
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouModel
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouViewModel
import com.smitcoderx.assignment.samespaceassignment.navigation.Screens


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTabRow(
    tabs: List<String>,
    forYouViewModel: ForYouViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        forYouViewModel.getSongList()
    }

    val dataState = forYouViewModel.songListLiveData.observeAsState()

    var pageSelectedIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        tabs.size
    }

    LaunchedEffect(key1 = pageSelectedIndex) {
        pagerState.animateScrollToPage(pageSelectedIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            pageSelectedIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            when (index) {
                0 -> {
                    ForYouPage(
                        modifier = Modifier,
                        dataList = dataState,
                        navController = navController
                    )
                }

                1 -> {
                    TopTracksPage(modifier = Modifier)
                }
            }

        }

        Surface(
            color = Color(39, 39, 39),
        ) {

            TabRow(selectedTabIndex = pageSelectedIndex,
                backgroundColor = Color(39, 39, 39, alpha = 10),
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        listOf(
                            Color(39, 39, 39), Color.Black
                        ), 0f, 100f
                    )
                ),
                indicator = {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                    ) {
                        val selectedTab = it[pageSelectedIndex]
                        val dotRadius = 3.dp.toPx()

                        val centerX = selectedTab.left + (selectedTab.width / 2)
                        val centerY = size.height - (dotRadius * 6)

                        drawCircle(
                            color = Color.White,
                            radius = dotRadius,
                            center = Offset(centerX.toPx(), centerY)
                        )
                    }
                }) {
                tabs.forEachIndexed { index, tabItems ->
                    Tab(
                        selected = index == pageSelectedIndex,
                        onClick = { pageSelectedIndex = index },
                        text = {
                            Text(
                                text = tabItems,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (index == pageSelectedIndex) {
                                    Color.White
                                } else {
                                    Color.DarkGray
                                }
                            )
                        },
                        modifier = Modifier
                            .background(Color.Black)
                            .padding(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ForYouPage(
    modifier: Modifier,
    dataList: State<ForYouModel?>,
    navController: NavController
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(dataList.value?.data ?: listOf()) { index, item ->
                item.id?.let {
                    SongItem(
                        modifier = Modifier,
                        navController = navController,
                        index = index,
                        imageUrl = item.cover.toString(),
                        name = item.name.toString(),
                        author = item.artist.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun SongItem(
    modifier: Modifier,
    navController: NavController,
    index: Int,
    imageUrl: String,
    name: String,
    author: String,
) {

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(Screens.PlayerScreen.withArgs(index.toString()))
            }
            .padding(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(SameSpaceAPI.IMAGE_URL + imageUrl)
                .crossfade(true).size(60).build(),
            contentDescription = "Album Image",
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .width(60.dp)
                .height(60.dp)
                .padding(10.dp)
                .clip(CircleShape)
        )
        Column {
            Text(text = name, color = Color.White, fontSize = 17.sp)
            Text(text = author, color = Color.DarkGray, fontSize = 15.sp)
        }
    }
}
