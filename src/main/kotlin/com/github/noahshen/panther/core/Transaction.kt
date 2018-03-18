package com.github.noahshen.panther.core

import com.github.noahshen.panther.PantherConstants
import com.github.noahshen.panther.utils.CodecUtils
import com.github.noahshen.panther.utils.CryptoUtils
import org.joda.time.DateTime
import java.math.BigInteger
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*


/**
 *
 */
class Transaction(
    val senderAddress: ByteArray,
    val receiverAddress: ByteArray,
    val amount: BigInteger,
    val time: DateTime,
    val requestPublicKey: PublicKey,
    var signature: ByteArray = ByteArray(0)
) {

    /**
     *
     */
    val isValid: Boolean
        get() = (signature.isNotEmpty() && CryptoUtils.verifyTransactionSignature(this, signature))

    fun isCoinbaseTransaction() = senderAddress.contentEquals(PantherConstants.COINBASE_SENDER_ADDRESS)

    /**
     * 用发送方的私钥进行签名。
     */
    fun sign(privateKey: PrivateKey): ByteArray {
        signature = CryptoUtils.signTransaction(this, privateKey)
        return signature
    }

    fun hash(): ByteArray {
        return CryptoUtils.hashTransaction(this)
    }

    fun encode(): ByteArray {
        return CodecUtils.encodeTransaction(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (!Arrays.equals(senderAddress, other.senderAddress)) return false
        if (!Arrays.equals(receiverAddress, other.receiverAddress)) return false
        if (amount != other.amount) return false
        if (time != other.time) return false
        if (requestPublicKey != other.requestPublicKey) return false
        if (!Arrays.equals(signature, other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(senderAddress)
        result = 31 * result + Arrays.hashCode(receiverAddress)
        result = 31 * result + amount.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + requestPublicKey.hashCode()
        result = 31 * result + Arrays.hashCode(signature)
        return result
    }

    override fun toString(): String {
        return "Transaction(senderAddress=${Arrays.toString(senderAddress)}, receiverAddress=${Arrays.toString(receiverAddress)}, amount=$amount, time=$time, requestPublicKey=$requestPublicKey, signature=${Arrays.toString(signature)})"
    }
}
