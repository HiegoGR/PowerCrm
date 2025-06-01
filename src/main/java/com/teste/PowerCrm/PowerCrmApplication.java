package com.teste.PowerCrm;

import com.teste.PowerCrm.service.MarcaModeloService;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableCaching
public class PowerCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerCrmApplication.class, args);
	}

	@Bean
	@Profile("!test")
	public ApplicationRunner initMarcaModelo(MarcaModeloService marcaModeloService) {
		return args -> marcaModeloService.sincronizarModeloEMarca();
	}
}
