package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.core.Account
import com.github.noahshen.panther.persistence.AccountEntity
import org.junit.Before
import org.junit.Test
import java.io.File

class AccountLevelDbStorageTest {

    lateinit var accountStorage: AccountLevelDbStorage

    @Before
    fun setUp() {
        val projectPath = System.getProperty("user.dir")
        println(projectPath)
        val filePath = projectPath + "/dbfiles/accountSaveLoadTestStorage"
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
            file.deleteRecursively()
        }
        accountStorage = AccountLevelDbStorage(filePath)

    }

    @Test
    fun saveAndLoadAccount() {
        val account1 = Account.createAccount()
        val publicKey = account1.publicKey

        val accountEntity1 = AccountEntity(publicKey.encoded, account1.nonce, account1.balance)
        accountStorage.saveAccount(accountEntity1)

        val accountEntity2: AccountEntity? = accountStorage.loadAccount(publicKey.encoded)
        assert(accountEntity2!!.publicKey.contentEquals(publicKey.encoded))
    }

}