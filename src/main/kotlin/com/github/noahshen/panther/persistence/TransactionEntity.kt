package com.github.noahshen.panther.persistence

import java.math.BigInteger


/**
 *
 */
data class TransactionEntity(
        val transHash: String,
        val senderAddress: ByteArray,
        val receiverAddress: ByteArray,
        val amount: BigInteger,
        val time: Long,
        val requestPublicKey: ByteArray,
        var signature: ByteArray = ByteArray(0)
) {

}
