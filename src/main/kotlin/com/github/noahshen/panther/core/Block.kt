package com.github.noahshen.panther.core

import com.github.noahshen.panther.utils.CodecUtils
import com.github.noahshen.panther.utils.CryptoUtils
import org.joda.time.DateTime
import java.util.*

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
        val trxTrieRoot: ByteArray,
        val transactions: List<Transaction>
) {

    val header = BlockHeader(
            version, height, parentHash, coinBase, time, difficulty, nonce, trxTrieRoot
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
        return "Block(version=$version, height=$height, parentHash=${Arrays.toString(parentHash)}, coinBase=${Arrays.toString(coinBase)}, time=$time, difficulty=$difficulty, nonce=$nonce, trxTrieRoot=${Arrays.toString(trxTrieRoot)}, transactions=$transactions, header=$header)"
    }

}
