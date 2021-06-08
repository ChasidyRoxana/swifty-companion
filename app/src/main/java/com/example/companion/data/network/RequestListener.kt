package com.example.companion.data.network

interface RequestListener<T> {
    fun onLoading(isLoading: Boolean)
    fun onError(t: Throwable)
    fun onSuccess(data: T, action: (() -> Unit)? = null)
}