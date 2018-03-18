package com.github.noahshen.panther.persistence

interface BlockStorage {

    fun loadBlock(blockHash: ByteArray): BlockEntity?

    fun saveBlock(blockEntity: BlockEntity)

}