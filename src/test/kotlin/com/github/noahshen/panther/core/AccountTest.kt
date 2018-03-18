package com.github.noahshen.panther.core

import org.junit.Test

class AccountTest {
    @Test
    fun testCreateAccount() {
        val account = Account.createAccount()
        print(account)
    }

}
