package com.smitcoderx.assignment.samespaceassignment.navigation

sealed class Screens(val route: String) {
    object ForYouScreen : Screens("forYou_screen")
    object PlayerScreen : Screens("player_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}