package com.github.noahshen.panther.core

import com.github.noahshen.panther.utils.CodecUtils
import com.github.noahshen.panther.utils.CryptoUtils
import org.joda.time.DateTime
import org.spongycastle.util.encoders.Hex
import java.math.BigInteger

/**
 *
 */
class Block(
        val version: Int,
        val height: Long,
        val parentHash: ByteArray,
        val coinBase: ByteArray,
        val time: DateTime,
        val difficulty: Long,
        val nonce: Int,
        val totalDifficulty: BigInteger,
        val stateRoot: ByteArray,
        val trxTrieRoot: ByteArray,
        val transactions: List<Transaction>
) {

    val header = BlockHeader(
            version, height, parentHash, coinBase, time, difficulty, nonce, totalDifficulty, stateRoot,
            trxTrieRoot
    )

    /**
     *
     */
    val hash: ByteArray
        get() = CryptoUtils.hashBlock(this)

    fun encode(): ByteArray {
        return CodecUtils.encodeBlock(this)
    }

    override fun toString(): String {
        return "h:$height, nonce:$nonce, dt:$difficulty, tt: $totalDifficulty, time:$time, " +
                "${transactions.size} of transactions stateRoot:${Hex.toHexString(stateRoot)} trxTrieRoot: ${Hex.toHexString(trxTrieRoot)}."
    }
}
