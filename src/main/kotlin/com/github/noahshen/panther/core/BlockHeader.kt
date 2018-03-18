package com.github.noahshen.panther.core

import org.joda.time.DateTime
import java.math.BigInteger

data class BlockHeader(
        val version: Int,
        val height: Long,
        val parentHash: ByteArray,
        val coinBase: ByteArray,
        val time: DateTime,
        val difficulty: Long,
        val nonce: Int,
        val totalDifficulty: BigInteger,
        val stateRoot: ByteArray,
        val trxTrieRoot: ByteArray
)
