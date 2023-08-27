package de.dkh.cafemanagementbackend.config

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileReader
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException


@Configuration
class KeyGeneratorConfig {

    @Bean
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generatePrivateKey(): PrivateKey {
        val pemParser = PEMParser(FileReader("src/main/resources/private_ec.pem"))
        val converter = JcaPEMKeyConverter()
        val privateKeyInfo = PrivateKeyInfo.getInstance((pemParser.readObject() as PEMKeyPair).privateKeyInfo)
        return converter.getPrivateKey(privateKeyInfo)
    }

    @Bean
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generatePublicKey(): PublicKey {
        val pemParser = PEMParser(FileReader("src/main/resources/public_ec.pem"))
        val converter = JcaPEMKeyConverter()
        val publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject())
        return converter.getPublicKey(publicKeyInfo)
    }

}