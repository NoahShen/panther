package com.github.noahshen.panther

import org.spongycastle.util.encoders.Hex

object PantherConstants {

    val MAX_PAYLOAD_apacity = 8 * 1024 * 1024

    val MAX_TXS_PER_BLOCK = 500

    val COINBASE_SENDER_ADDRESS = Hex.decode("0000000000000000000000000000000000000000")


}