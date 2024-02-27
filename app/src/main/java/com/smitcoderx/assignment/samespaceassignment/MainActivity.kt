package com.smitcoderx.assignment.samespaceassignment

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.smitcoderx.assignment.samespaceassignment.forYou.ForYouViewModel
import com.smitcoderx.assignment.samespaceassignment.navigation.Navigation
import com.smitcoderx.assignment.samespaceassignment.ui.theme.SameSpaceAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val forYouViewModel by viewModels<ForYouViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tabs = listOf(
            "For You", "Top Tracks"
        )
        setContent {
            SameSpaceAssignmentTheme {
                val data = forYouViewModel.songListLiveData.observeAsState().value
                Navigation(
                    tabs,
                    forYouViewModel,
                    data
                )
            }
        }
    }
}
