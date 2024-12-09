package com.example.androidcookbook.ui

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.providers.AccessTokenProvider
import com.example.androidcookbook.data.providers.DataStoreManager
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.domain.model.auth.SignInRequest
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.nav.utils.sharedViewModel
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.apiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CookbookViewModel @Inject constructor(
    private val accessTokenProvider: AccessTokenProvider,
    private val dataStoreManager: DataStoreManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _uiState = MutableStateFlow(CookbookUiState())
    val uiState: StateFlow<CookbookUiState> = _uiState.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.token.combine(dataStoreManager.isLoggedIn) { token, isLoggedIn ->
                Pair(
                    token,
                    isLoggedIn
                )
            }.collect { (token, isLoggedIn) ->

                if (token != accessTokenProvider.accessToken.value && token != null) {
                    accessTokenProvider.updateAccessToken(token)
                }
                _isLoggedIn.value = isLoggedIn

                launch {
                    dataStoreManager.username.combine(dataStoreManager.password) { username, password ->
                        Pair(
                            username,
                            password
                        )
                    }.collect { (username, password) ->

                        if (user.value.id == 0 && username != null && password != null) {
                            val response = authRepository.login(SignInRequest(username, password))
                            response.onSuccess {
                                updateUser(data, username, password)
                            }
                        }
                    }
                }
            }
        }


    }

    fun updateCanNavigateBack(updatedCanNavigateBack: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                canNavigateBack = updatedCanNavigateBack
            )
        }
    }

    fun updateTopBarState(topBarState: CookbookUiState.TopBarState) {
        _uiState.update { it.copy(topBarState = topBarState) }
    }

    fun updateBottomBarState(bottomBarState: CookbookUiState.BottomBarState) {
        _uiState.update { it.copy(bottomBarState = bottomBarState) }
    }


    fun updateUser(response: SignInResponse, username: String, password: String) {
        _user.update { response.user }
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.saveToken(response.accessToken)
            dataStoreManager.saveUsername(username)
            dataStoreManager.savePassword(password)
        }
        accessTokenProvider.updateAccessToken(response.accessToken)
    }


    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearLoginState()
        }
    }
}