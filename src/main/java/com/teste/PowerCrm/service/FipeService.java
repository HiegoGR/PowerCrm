package com.teste.PowerCrm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class FipeService {

    @Value("${fipe.carros.urlbase}")
    private String urlBase;

    private final RestTemplate restTemplate;

    public FipeService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    @Cacheable(value = "fipePriceCache", key = "#brandId + '-' + #modelId + '-' + #anoComCodigo")
    public Double buscarPrecoFipe(Long brandId, Long modelId, String anoComCodigo) {
        String url = urlBase + brandId + "/modelos/" + modelId + "/anos/" + anoComCodigo;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("Valor")) {
            String valor = response.get("Valor").toString().replace("R$", "").replace(".", "").replace(",", ".");
            return Double.parseDouble(valor.trim());
        }

        throw new RuntimeException("Preço FIPE não encontrado.");
    }

    @SuppressWarnings("unchecked")
    public Optional<List<Map<String, String>>> consultarMarcaModeloAno(Long brandId, Long modelId) {
        String url = String.format("%s%d/modelos/%d/anos", urlBase, brandId, modelId);
        try {
            ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Erro ao consultar FIPE: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    public Optional<String> buscarCodigoAnoModeloNaFipe(Long brandId, Long modelId, Integer ano) {
        return consultarMarcaModeloAno(brandId, modelId)
                .flatMap(lista -> lista.stream()
                        .filter(item -> {
                            String nome = item.get("nome");
                            return nome != null && nome.contains(String.valueOf(ano));
                        })
                        .map(item -> item.get("codigo"))
                        .findFirst()
                );
    }
}
