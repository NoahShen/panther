package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.persistence.BlockEntity
import com.github.noahshen.panther.persistence.BlockStorage

class BlockLevelDbStorage : BlockStorage, LevelDbStorage {

    constructor(dataBaseFilePath: String) : super(dataBaseFilePath) {
    }

    override fun loadBlock(blockHash: ByteArray): BlockEntity? {
        val accountBytes: ByteArray = leveldb.get(blockHash)
        if (accountBytes.isEmpty()) {
            return null
        }
        return gson.fromJson(String(accountBytes), BlockEntity::class.java)
    }

    override fun saveBlock(blockEntity: BlockEntity) {
        leveldb.put(blockEntity.blockHash, gson.toJson(blockEntity).toByteArray())
    }

}