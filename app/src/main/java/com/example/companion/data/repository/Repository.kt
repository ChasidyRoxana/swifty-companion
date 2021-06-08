package com.example.companion.data.repository

import android.content.res.Resources
import android.util.Log
import com.example.companion.R
import com.example.companion.data.mapper.ApiMapper
import com.example.companion.data.model.EmptyResponseBody
import com.example.companion.data.model.TokenApi
import com.example.companion.data.model.UserApi
import com.example.companion.data.network.CompanionApi
import com.example.companion.data.network.RequestListener
import com.example.companion.domain.model.Token
import com.example.companion.domain.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Repository(
    private val api: CompanionApi,
    private val mapper: ApiMapper,
    private val resources: Resources
) {

    private var accessToken: Token? = null
    private var login: String = ""
    private var requestListener: RequestListener<User>? = null
    private val tokenListener = createTokenListener()

    fun getUserProfile(login: String) {
        this.login = login
        requestListener?.onLoading(true)
        updateTokenIfRequired {
            accessToken?.let { token ->
                requestUserInformation(token.accessToken)
            }
        }
    }

    fun setRequestListener(requestListener: RequestListener<User>) {
        this.requestListener = requestListener
    }

    fun clearRequestListener() {
        requestListener = null
    }

    private fun createTokenListener() =
        object : RequestListener<Token> {
            override fun onLoading(isLoading: Boolean) {
            }

            override fun onSuccess(data: Token, action: (() -> Unit)?) {
                accessToken = data
                action?.invoke()
            }

            override fun onError(t: Throwable) {
                onErrorRequestHandler(t)
            }
        }

    private fun updateTokenIfRequired(onSuccessAction: () -> Unit) {
        if (accessToken != null) {
            accessToken?.let { token ->
                val tokenCreatedTime = token.createdAt // в токене время хранится в секундах
                val currentTime = Date().time / 1000 // секунда = миллисекунда * 1000
                if (currentTime - tokenCreatedTime > token.expiresIn) {
                    requestToken(onSuccessAction)
                } else {
                    onSuccessAction.invoke()
                }
            }
        } else {
            requestToken(onSuccessAction)
        }
    }

    private fun requestUserInformation(accessToken: String) {
        val userCallback = api.getUserInformation(accessToken, login)
        userCallbackHandler(userCallback)
    }

    private fun requestToken(actionAfterUpdate: () -> Unit) {
        Log.i("Repository", "Request token")
        val tokenCallback = api.getToken(getGrantType(), getClientId(), getClientSecret())
        tokenCallbackHandler(tokenCallback, actionAfterUpdate)
    }

    private fun tokenCallbackHandler(tokenCallback: Call<TokenApi>, actionAfterUpdate: () -> Unit) {
        tokenCallback.enqueue(object : Callback<TokenApi> {
            override fun onResponse(call: Call<TokenApi>, response: Response<TokenApi>) {
                val tokenApi: TokenApi? = response.body()
                if (tokenApi != null) {
                    tokenListener.onSuccess(mapper.mapToToken(tokenApi), actionAfterUpdate)
                } else {
                    tokenListener.onError(EmptyResponseBody())
                }
            }

            override fun onFailure(call: Call<TokenApi>, t: Throwable) {
                tokenListener.onError(t)
            }
        })
    }

    private fun userCallbackHandler(userCallback: Call<UserApi>) {
        userCallback.enqueue(object : Callback<UserApi> {

            override fun onResponse(call: Call<UserApi>, response: Response<UserApi>) {
                val userApi: UserApi? = response.body()
                if (userApi != null) {
                    onSuccessRequestHandler(mapper.mapToUser(userApi))
                } else {
                    onErrorRequestHandler(EmptyResponseBody())
                }
            }

            override fun onFailure(call: Call<UserApi>, t: Throwable) {
                onErrorRequestHandler(t)
            }
        })
    }

    private fun onSuccessRequestHandler(user: User) {
        requestListener?.onLoading(false)
        requestListener?.onSuccess(user)
    }

    private fun onErrorRequestHandler(t: Throwable) {
        requestListener?.onLoading(false)
        requestListener?.onError(t)
    }

    private fun getGrantType() = resources.getString(R.string.grant_type)

    private fun getClientId() = resources.getString(R.string.client_id)

    private fun getClientSecret() = resources.getString(R.string.client_secret)
}