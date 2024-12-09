package com.example.androidcookbook.ui

import androidx.compose.runtime.Composable

data class CookbookUiState (
    val topBarState: TopBarState = TopBarState.Auth,
    val bottomBarState: BottomBarState = BottomBarState.NoBottomBar,
    val canNavigateBack: Boolean = false,
) {
    sealed interface TopBarState {
        data object Default : TopBarState
        data object Auth : TopBarState
        data class Custom(val topAppBar: @Composable () -> Unit) : TopBarState
    }

    sealed interface BottomBarState {
        data object Default : BottomBarState
        data object NoBottomBar : BottomBarState
    }
}