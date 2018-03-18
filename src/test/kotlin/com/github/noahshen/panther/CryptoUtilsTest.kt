package com.github.noahshen.panther


import com.github.noahshen.panther.utils.CryptoUtils
import org.junit.Before
import org.junit.Test


class CryptoUtilsTest {

    @Before
    fun setUp() {
    }

    @Test
    fun testGenerateKeyPair() {
        var keyPair = CryptoUtils.generateKeyPair()
        println(keyPair)
        println(keyPair.public)
        println(keyPair.private)
        assert(keyPair.public != null)
        assert(keyPair.private != null)
    }


}
