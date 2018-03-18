package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.persistence.AccountEntity
import com.github.noahshen.panther.persistence.AccountStorage

class AccountLevelDbStorage : AccountStorage, LevelDbStorage {

    constructor(dataBaseFilePath: String) : super(dataBaseFilePath)

    override fun saveAccount(accountEntity: AccountEntity) {
        leveldb.put(accountEntity.publicKey, gson.toJson(accountEntity).toByteArray())
    }

    override fun loadAccount(publicKey: ByteArray): AccountEntity? {
        val accountBytes: ByteArray = leveldb.get(publicKey)
        if (accountBytes.isEmpty()) {
            return null
        }
        return gson.fromJson(String(accountBytes), AccountEntity::class.java)
    }
}