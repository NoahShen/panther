package com.github.noahshen.panther.core

import com.github.noahshen.panther.utils.CryptoUtils
import java.math.BigInteger
import java.security.PrivateKey
import java.security.PublicKey

/**
 *
 */
class Account(
    val publicKey: PublicKey,
    val privateKey: PrivateKey,
    val nonce: BigInteger
)  {

    companion object {

        fun createAccount(): Account {
            val keyPair = CryptoUtils.generateKeyPair()
            return Account(keyPair.public, keyPair.private, BigInteger.ZERO)
        }


    }

    override fun toString(): String {
        return "Account(publicKey=$publicKey, privateKey=$privateKey, nonce=$nonce)"
    }


}