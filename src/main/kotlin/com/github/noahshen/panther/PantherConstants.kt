package com.github.noahshen.panther

import com.github.noahshen.panther.core.Block
import com.github.noahshen.panther.utils.CryptoUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.spongycastle.util.encoders.Hex

object PantherConstants {

    val MAX_PAYLOAD_CAPACITY = 8 * 1024 * 1024

    val MAX_TXS_PER_BLOCK = 500

    val COINBASE_SENDER_ADDRESS = Hex.decode("0000000000000000000000000000000000000000")


    fun getGenesisBlock(): Block {
        val genesisBlock = Block(
                1, 0, ByteArray(0),
                Hex.decode("1234567890123456789012345678901234567890"),
                DateTime(2018, 1, 1, 0, 0, DateTimeZone.UTC), 0, 0,
                CryptoUtils.merkleRoot(emptyList()), emptyList()
        )
        return genesisBlock
    }

}