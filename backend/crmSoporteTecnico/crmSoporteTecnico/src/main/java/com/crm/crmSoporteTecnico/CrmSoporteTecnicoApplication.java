package com.crm.crmSoporteTecnico;

import com.crm.crmSoporteTecnico.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties({RsaKeyProperties.class})
@SpringBootApplication
@EnableAsync
public class CrmSoporteTecnicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmSoporteTecnicoApplication.class, args);
	}

}
