package com.github.noahshen.panther.utils

import org.spongycastle.crypto.params.ECDomainParameters
import org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.spongycastle.jce.ECNamedCurveTable
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.jce.spec.ECPublicKeySpec
import java.security.*
import java.security.Security.insertProviderAt
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec


/**
 *
 */
class CryptoUtil {

    companion object {
        init {
            insertProviderAt(BouncyCastleProvider(), 1)
        }

        fun generateAddress(publicKey: PublicKey): ByteArray {
            val digest = MessageDigest.getInstance("KECCAK-256", "SC")
            digest.update(publicKey.encoded)
            val hash = digest.digest()

            return hash.drop(12).toByteArray()
        }

        /**
         *
         */
        fun generateKeyPair(): KeyPair? {
            val gen = KeyPairGenerator.getInstance("EC", "SC")
            gen.initialize(ECGenParameterSpec("secp256k1"), SecureRandom())
            val keyPair = gen.generateKeyPair()
            return keyPair
        }


        /**
         * SHA-256
         */
        fun sha256(msg: ByteArray): ByteArray {
            val digest = MessageDigest.getInstance("SHA-256", "SC")
            digest.update(msg)
            val hash = digest.digest()

            return hash
        }

        /**
         * SHA3
         */
        fun sha3(msg: ByteArray): ByteArray {
            return sha256((msg))
        }

        fun deserializePrivateKey(bytes: ByteArray): PrivateKey {
            val kf = KeyFactory.getInstance("EC", "SC")
            return kf.generatePrivate(PKCS8EncodedKeySpec(bytes))
        }

        fun deserializePublicKey(bytes: ByteArray): PublicKey {
            val kf = KeyFactory.getInstance("EC", "SC")
            return kf.generatePublic(X509EncodedKeySpec(bytes))
        }

        /**
         *
         */
        fun generatePublicKey(privateKey: PrivateKey): PublicKey? {
            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val kf = KeyFactory.getInstance("EC", "SC")

            val curve = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)

            return if (privateKey is BCECPrivateKey) {
                val d = privateKey.d
                val point = curve.g.multiply(d)
                val pubKeySpec = ECPublicKeySpec(point, spec)
                val publicKey = kf.generatePublic(pubKeySpec) as ECPublicKey
                publicKey
            } else {
                null
            }
        }
    }

}