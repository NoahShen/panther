package com.github.noahshen.panther.network.message;

data class RegisterRequest(
        val nodeAddress: String,
        val networkId: String,
        val networkEnv: String
)
