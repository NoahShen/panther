package com.github.noahshen.panther.persistence

import java.math.BigInteger

data class AccountEntity(
        val publicKey: ByteArray,
        val nonce: BigInteger,
        val balance: BigInteger
)