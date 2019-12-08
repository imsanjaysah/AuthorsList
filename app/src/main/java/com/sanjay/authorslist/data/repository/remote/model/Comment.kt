package com.sanjay.authorslist.data.repository.remote.model

data class Comment(
    val id: Int,
    val date: String,
    val body: String,
    val userName: String,
    val email: String,
    val avatarUrl: String?,
    val postId: Int
)
