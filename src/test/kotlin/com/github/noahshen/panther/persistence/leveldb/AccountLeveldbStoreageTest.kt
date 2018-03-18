package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.core.Account
import com.github.noahshen.panther.persistence.AccountEntity
import org.junit.Before
import org.junit.Test
import java.io.File

class AccountLeveldbStoreageTest {

    lateinit var accountStorage: AccountLeveldbStoreage

    @Before
    fun setUp() {
        val url = AccountLeveldbStoreageTest::class.java.getResource("accountSaveAndLoadTestStorage")
        val file = File(url.toURI())
        if (file.exists()) {
            file.delete()
        }
        accountStorage = AccountLeveldbStoreage(url.toString())

    }

    @Test
    fun saveAccount() {
        val account1 = Account.createAccount()
        val publicKey = account1.publicKey

        val account = AccountEntity(publicKey.encoded, account1.nonce, account1.balance)
        accountStorage.saveAccount(account)
    }

}