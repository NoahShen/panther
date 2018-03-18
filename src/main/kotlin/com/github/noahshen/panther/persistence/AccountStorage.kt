package com.github.noahshen.panther.persistence

interface AccountStorage {

    fun loadAccount(publicKey: ByteArray): AccountEntity?

    fun saveAccount(accountEntity: AccountEntity)

}