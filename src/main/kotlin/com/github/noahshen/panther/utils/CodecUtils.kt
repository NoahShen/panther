package com.github.noahshen.panther.utils

import com.github.noahshen.panther.core.Block
import com.github.noahshen.panther.core.BlockHeader
import com.github.noahshen.panther.core.Transaction
import org.joda.time.DateTime
import org.spongycastle.asn1.*
import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec


object CodecUtils {

    /**
     *
     */
    fun encodeTransaction(transaction: Transaction): ByteArray {
        return encodeTransactionToAsn1(transaction).encoded
    }

    /**
     *
     *
     *
     */
    fun encodeTransactionToAsn1(trx: Transaction): DERSequence {
        val v = ASN1EncodableVector()

        v.add(DERBitString(trx.senderAddress))
        v.add(DERBitString(trx.receiverAddress))
        v.add(ASN1Integer(trx.amount))
        v.add(ASN1Integer(trx.time.millis))
        v.add(DERBitString(trx.requestPublicKey.encoded))
        v.add(DERBitString(trx.signature))

        return DERSequence(v)
    }

    fun encodeTransactionWithoutSignatureToAsn1(trx: Transaction): DERSequence {
        val v = ASN1EncodableVector()

        v.add(DERBitString(trx.senderAddress))
        v.add(DERBitString(trx.receiverAddress))
        v.add(ASN1Integer(trx.amount))
        v.add(ASN1Integer(trx.time.millis))
        v.add(DERBitString(trx.requestPublicKey.encoded))
        // 不要包含Signature

        return DERSequence(v)
    }

    /**
     *
     */
    fun decodeTransaction(bytes: ByteArray): Transaction? {
        val v = ASN1InputStream(bytes).readObject()
        if (v != null) {
            val seq = ASN1Sequence.getInstance(v)
            return decodeTransactionFromSeq(seq)
        }
        return null
    }

    /**
     * ASN1Sequence => Transaction
     */
    fun decodeTransactionFromSeq(seq: ASN1Sequence): Transaction? {
        val senderAddress = DERBitString.getInstance(seq.getObjectAt(0))?.bytes
        val receiverAddress = DERBitString.getInstance(seq.getObjectAt(1))?.bytes
        val amount = ASN1Integer.getInstance(seq.getObjectAt(2))?.value
        val millis = ASN1Integer.getInstance(seq.getObjectAt(3))?.value
        val publicKeyBytes = DERBitString.getInstance(seq.getObjectAt(4))?.bytes
        val signature = DERBitString.getInstance(seq.getObjectAt(5))?.bytes

        if (senderAddress != null && receiverAddress != null && amount != null && millis != null &&
                publicKeyBytes != null && signature != null
                ) {
            val kf = KeyFactory.getInstance("EC", "SC")
            val publicKey = kf.generatePublic(X509EncodedKeySpec(publicKeyBytes))

            return Transaction(senderAddress, receiverAddress, amount, DateTime(millis.toLong()), publicKey, signature)
        } else {
            return null
        }
    }

    fun intToByteArray(i: Int): ByteArray {
        return ByteBuffer.allocate(4).putInt(i).array()
    }

    fun longToByteArray(i: Long): ByteArray {
        return ByteBuffer.allocate(8).putLong(i).array()
    }

    fun byteArrayToInt(b: ByteArray): Int {
        if (b.size == 0) {
            return 0
        }

        return BigInteger(b).toInt()
    }

    fun asn1Encode(v: Any): ASN1Object {
        if (v is ByteArray) {
            return DERBitString(v)
        } else if (v is String) {
            return DERUTF8String(v)
        } else if (v is Int) {
            return ASN1Integer(v.toLong())
        } else if (v is Long) {
            return ASN1Integer(v)
        } else if (v is BigInteger) {
            return ASN1Integer(v)
        } else if (v is Array<*>) {
            val vec = ASN1EncodableVector()
            v.forEach { vec.add(it?.let { asn1Encode(it) }) }
            return DERSequence(vec)
        } else if (v is Transaction) {
            return encodeTransactionToAsn1(v)
        } else if (v is Block) {
            return encodeBlockToAsn1(v)
        } else {
            throw Exception("Can not convert type ${v.javaClass} to ASN1 object.")
        }
    }


    /**
     *
     */
    fun encodeBlock(block: Block): ByteArray {

        return encodeBlockToAsn1(block).encoded
    }

    fun encodeBlockToAsn1(block: Block): DERSequence {

        val v = ASN1EncodableVector()

        v.add(ASN1Integer(block.version.toLong()))
        v.add(ASN1Integer(block.height))
        v.add(DERBitString(block.parentHash))
        v.add(DERBitString(block.coinBase))
        v.add(ASN1Integer(block.difficulty))
        v.add(ASN1Integer(block.nonce.toLong()))
        v.add(ASN1Integer(block.time.millis))
        v.add(DERBitString(block.trxTrieRoot))

        v.add(ASN1Integer(block.transactions.size.toLong()))

        val t = ASN1EncodableVector()
        block.transactions.forEach { t.add(encodeTransactionToAsn1(it)) } // transactions
        v.add(DERSequence(t))

        return DERSequence(v)
    }

    /**
     *
     */
    fun decodeBlock(bytes: ByteArray): Block? {
        val v = ASN1InputStream(bytes).readObject()

        if (v != null) {
            val seq = ASN1Sequence.getInstance(v)
            val version = ASN1Integer.getInstance(seq.getObjectAt(0)).value
            val height = ASN1Integer.getInstance(seq.getObjectAt(1)).value
            val parentHash = DERBitString.getInstance(seq.getObjectAt(2))?.bytes
            val minerAddress = DERBitString.getInstance(seq.getObjectAt(3))?.bytes
            val difficulty = ASN1Integer.getInstance(seq.getObjectAt(4))?.value
            val nonce = ASN1Integer.getInstance(seq.getObjectAt(5))?.value
            val millis = ASN1Integer.getInstance(seq.getObjectAt(6))?.value
            val totalDifficulty = ASN1Integer.getInstance(seq.getObjectAt(7))?.value ?: BigInteger.ZERO
            val stateRoot = DERBitString.getInstance(seq.getObjectAt(8))?.bytes
            val trxTrieRoot = DERBitString.getInstance(seq.getObjectAt(9))?.bytes

            val trxSize = ASN1Integer.getInstance(seq.getObjectAt(10))?.value

            val trxValues = ASN1Sequence.getInstance(seq.getObjectAt(11))

            val trxList = mutableListOf<Transaction>()

            for (trxValue in trxValues.objects) {
                val trxObj = DERSequence.getInstance(trxValue) ?: return null
                val trx = decodeTransaction(trxObj.encoded) ?: return null
                trxList.add(trx)
            }

            if (version == null || height == null || parentHash == null || minerAddress == null ||
                    difficulty == null || nonce == null || millis == null || totalDifficulty == null ||
                    stateRoot == null || trxTrieRoot == null || trxSize == null || trxSize.toInt() != trxList.size
                    ) {
                return null
            }

            return Block(
                    version.toInt(), height.toLong(), parentHash, minerAddress,
                    DateTime(millis.toLong()), difficulty.toLong(), nonce.toLong(), trxTrieRoot, trxList
            )
        }

        return null
    }

    /**
     *
     */
    fun encodeBlockHeader(blockHeader: BlockHeader): ByteArray {

        return encodeBlockHeaderToAsn1(blockHeader).encoded
    }

    fun encodeBlockHeaderToAsn1(blockHeader: BlockHeader): DERSequence {

        val v = ASN1EncodableVector()

        v.add(ASN1Integer(blockHeader.version.toLong()))
        v.add(ASN1Integer(blockHeader.height))
        v.add(DERBitString(blockHeader.parentHash))
        v.add(DERBitString(blockHeader.coinBase))
        v.add(ASN1Integer(blockHeader.difficulty))
        v.add(ASN1Integer(blockHeader.nonce.toLong()))
        v.add(ASN1Integer(blockHeader.time.millis))
        v.add(DERBitString(blockHeader.trxTrieRoot))

        return DERSequence(v)
    }

    /**
     *
     */
    fun decodeBlockHeader(bytes: ByteArray): BlockHeader? {
        val v = ASN1InputStream(bytes).readObject()

        if (v != null) {
            val seq = ASN1Sequence.getInstance(v)
            val version = ASN1Integer.getInstance(seq.getObjectAt(0)).value
            val height = ASN1Integer.getInstance(seq.getObjectAt(1)).value
            val parentHash = DERBitString.getInstance(seq.getObjectAt(2))?.bytes
            val minerAddress = DERBitString.getInstance(seq.getObjectAt(3))?.bytes
            val difficulty = ASN1Integer.getInstance(seq.getObjectAt(4))?.value
            val nonce = ASN1Integer.getInstance(seq.getObjectAt(5))?.value
            val millis = ASN1Integer.getInstance(seq.getObjectAt(6))?.value
            val totalDifficulty = ASN1Integer.getInstance(seq.getObjectAt(7))?.value ?: BigInteger.ZERO
            val stateRoot = DERBitString.getInstance(seq.getObjectAt(8))?.bytes
            val trxTrieRoot = DERBitString.getInstance(seq.getObjectAt(9))?.bytes

            if (version == null || height == null || parentHash == null || minerAddress == null ||
                    difficulty == null || nonce == null || millis == null || totalDifficulty == null ||
                    stateRoot == null || trxTrieRoot == null
                    ) {
                return null
            }

            return BlockHeader(
                    version.toInt(), height.toLong(), parentHash, minerAddress,
                    DateTime(millis.toLong()), difficulty.toLong(), nonce.toLong(), trxTrieRoot
            )
        }

        return null
    }


}
