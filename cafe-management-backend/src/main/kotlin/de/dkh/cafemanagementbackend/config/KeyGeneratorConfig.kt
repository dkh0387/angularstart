package de.dkh.cafemanagementbackend.config

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileReader
import java.security.*
import java.security.spec.InvalidKeySpecException


@Configuration
class KeyGeneratorConfig {

    @Value("\${jwt.privateKey}")
    private val privateKey: String? = null //Encoded private key string

    @Value("\${jwt.publicKey}")
    private val publicKey: String? = null //Encoded public key string


    @Bean
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generatePrivateKey(): PrivateKey {
        /*Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.addProvider(BouncyCastleProvider())
                val pemParser = PEMParser(FileReader("src/main/resources/private_ec.pem"))
                val content = pemParser.readPemObject().content
                val keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider())
                return keyFactory.generatePrivate(PKCS8EncodedKeySpec(content))*/
        val pemParser = PEMParser(FileReader("src/main/resources/private_ec.pem"))
        val converter = JcaPEMKeyConverter()
        val privateKeyInfo = PrivateKeyInfo.getInstance((pemParser.readObject() as PEMKeyPair).privateKeyInfo)
        return converter.getPrivateKey(privateKeyInfo)
    }

    @Bean
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generatePublicKey(): PublicKey {
        val pemParser = PEMParser(FileReader("src/main/resources/public_ec.pem"))
        /*val content = pemParser.readPemObject().content
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(X509EncodedKeySpec(content))*/
        val converter = JcaPEMKeyConverter()
        val publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject())
        return converter.getPublicKey(publicKeyInfo)
    }

    private fun removeEncapsulationBoundaries(key: String): String {
        return key.replace("\n".toRegex(), "")
            .replace(" ".toRegex(), "")
            .replace("-{5}[a-zA-Z]*-{5}".toRegex(), "")
    }
}