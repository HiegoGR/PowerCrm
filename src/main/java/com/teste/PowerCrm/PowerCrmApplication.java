package com.teste.PowerCrm;

import com.teste.PowerCrm.service.MarcaModeloService;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableCaching
public class PowerCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerCrmApplication.class, args);
	}

	@Bean
	public ApplicationRunner initMarcaModelo(MarcaModeloService marcaModeloService) {
		return args -> {
			Logger log = LoggerFactory.getLogger(PowerCrmApplication.class);
			log.info("Inserindo dados de Marca e Modelo ao iniciar aplicação");
			marcaModeloService.sincronizarModeloEMarca();
		};
	}
}
