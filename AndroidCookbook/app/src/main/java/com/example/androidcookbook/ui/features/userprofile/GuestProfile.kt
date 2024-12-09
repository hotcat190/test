package com.example.androidcookbook.ui.features.userprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.androidcookbook.domain.model.user.User

@Composable
fun GuestProfile() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            UserProfileHeader(avatarPath = null)
            UserInfo(User())
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Login to view your posts.")
            }
        }
    }
}