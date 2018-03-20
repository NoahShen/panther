package com.github.noahshen.panther.network.message;

data class RegisterResponse(
        val success: Boolean,
        val message: String,
        val nodeId: String,
        val nodeAddress: String,
        val version: String,
        val networkId: String,
        val networkEnv: String
)
