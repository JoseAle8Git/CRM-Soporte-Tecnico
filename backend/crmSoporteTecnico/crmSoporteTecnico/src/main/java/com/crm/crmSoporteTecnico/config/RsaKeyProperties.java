package com.crm.crmSoporteTecnico.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt.keys")
public record RsaKeyProperties(RSAPublicKey publicKeyLocation, RSAPrivateKey privateKeyLocation) {
}
