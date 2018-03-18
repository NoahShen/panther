package com.github.noahshen.panther.persistence

import java.math.BigInteger
import java.security.PublicKey


/**
 *
 */
data class TransactionEntity(
        val transId: String,
        val senderAddress: ByteArray,
        val receiverAddress: ByteArray,
        val amount: BigInteger,
        val time: Long,
        val requestPublicKey: PublicKey,
        var signature: ByteArray = ByteArray(0)
) {

}
