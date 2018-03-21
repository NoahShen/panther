package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.persistence.LastBlockEntity
import com.github.noahshen.panther.persistence.LastBlockStorage

class LastBlockLevelDbStorage : LastBlockStorage, LevelDbStorage {

    private val lastBlock = "lastBlock".toByteArray()

    constructor(dataBaseFilePath: String) : super(dataBaseFilePath)

    override fun loadLastBlock(): LastBlockEntity? {
        val lastBlockBytes: ByteArray = leveldb.get(lastBlock)
        if (lastBlockBytes.isEmpty()) {
            return null
        }
        return gson.fromJson(String(lastBlockBytes), LastBlockEntity::class.java)
    }

    override fun saveBlock(lastBlockEntity: LastBlockEntity) {
        leveldb.put(lastBlock, gson.toJson(lastBlockEntity).toByteArray())
    }

}