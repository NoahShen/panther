package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.persistence.TransactionEntity
import com.github.noahshen.panther.persistence.TransactionStorage

class TransactionLevelDbStorage : TransactionStorage, LevelDbStorage {

    constructor(dataBaseFilePath: String) : super(dataBaseFilePath)

    override fun loadTransaction(transId: String): TransactionEntity? {
        val transactionBytes: ByteArray = leveldb.get(transId.toByteArray())
        if (transactionBytes.isEmpty()) {
            return null
        }
        return gson.fromJson(String(transactionBytes), TransactionEntity::class.java)
    }

    override fun saveTransaction(transactionEntity: TransactionEntity) {
        leveldb.put(transactionEntity.transHash.toByteArray(), gson.toJson(transactionEntity).toByteArray())
    }
}