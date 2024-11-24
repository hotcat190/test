package com.example.androidcookbook.ui.features.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.R
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedCard
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun UserProfileScreen(
    userId: Int,
    modifier: Modifier = Modifier
) {
    val userProfileViewModel = hiltViewModel<UserProfileViewModel, UserProfileViewModel.UserProfileViewModelFactory> {
        it.create(userId)
    }
    val userProfileUiState = userProfileViewModel.uiState
    RefreshableScreen(
        onRefresh = { userProfileViewModel.refresh() }
    ) {
        when (userProfileUiState) {
            is UserProfileUiState.Loading -> {
                Text(
                    "Loading"
                )
            }
            is UserProfileUiState.Success -> {
                val user = userProfileUiState.user
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        UserAvatar(avatarPath = user.avatar)
                        UserInfo()
                        Column(modifier = Modifier.wrapContentHeight()) {
                            NewsfeedCard()
                            NewsfeedCard()
                            NewsfeedCard()
                        }
                    }
                }
            }

            UserProfileUiState.Failure -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        UserAvatar(avatarPath = null)
                        UserInfo()
                        Column(modifier = Modifier.wrapContentHeight()) {
                            NewsfeedCard()
                            NewsfeedCard()
                            NewsfeedCard()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    avatarPath: String? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_5),
            contentDescription = null,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        AsyncImage(
//            painter = painterResource(id = R.drawable.default_avatar),
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarPath)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier =
            Modifier
                .size(125.dp)
                .offset(x = 20.dp, y = 140.dp)
                .border(shape = CircleShape, width = 5.dp, color = Color.White)
                .padding(5.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_avatar),
            error = painterResource(R.drawable.default_avatar),
        )
    }
}

@Composable
fun UserInfo() {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .height(70.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Guest",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
            )
        )
        Text(
            text = "",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
            )
        )
        Row {
            Text(
                text = "0",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                )
            )
            Text(
                text = " followers",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                )
            )
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    AndroidCookbookTheme(darkTheme = true) {
        UserProfileScreen(
            userId = 0
        )
    }
}

@Preview
@Composable
fun AvtPreview() {
    UserAvatar()
}