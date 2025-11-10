package com.crm.crmSoporteTecnico.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "jwt.keys")
public record RsaKeyProperties(Resource publicKeyLocation, Resource privateKeyLocation) {
}
