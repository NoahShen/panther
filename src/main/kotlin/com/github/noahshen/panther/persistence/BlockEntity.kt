package com.github.noahshen.panther.persistence

/**
 *
 */
data class BlockEntity(
        val blockHash: ByteArray,
        val version: Int,
        val height: Long,
        val parentHash: ByteArray,
        val coinBase: ByteArray,
        val time: Long,
        val difficulty: Long,
        val nonce: Int,
        val trxTrieRoot: ByteArray,
        val transactions: List<String>
) {

}
