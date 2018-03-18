package com.github.noahshen.panther.core

import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

class TransactionTest {
    @Before
    fun setUp() {
    }

    @Test
    fun sign() {

        val account1 = Account.createAccount()
        val account2 = Account.createAccount()


        val transaction = Transaction(
                account1.publicKey.encoded,
                account2.publicKey.encoded,
                BigInteger.ONE,
                DateTime.now(),
                account1.publicKey)

        val sign = transaction.sign(account1.privateKey)
        transaction.signature = sign

        val valid = transaction.isValid
        println("valid:$valid")
        assert(valid)

    }

}