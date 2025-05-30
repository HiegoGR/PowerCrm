package com.teste.PowerCrm.service.event;

import com.teste.PowerCrm.config.RabbitMQConfig;
import com.teste.PowerCrm.dto.FipeRequestDTO;
import com.teste.PowerCrm.entity.Vehicle;
import com.teste.PowerCrm.repository.VehicleRepository;
import com.teste.PowerCrm.service.FipeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
@Service
public class FipeConsumer {

    private final VehicleRepository vehicleRepository;
    private final FipeService fipeService;

    public FipeConsumer(VehicleRepository vehicleRepository, FipeService fipeService) {
        this.vehicleRepository = vehicleRepository;
        this.fipeService = fipeService;
    }

    @RabbitListener(queues = RabbitMQConfig.FIPE_QUEUE)
    public void process2(FipeRequestDTO msg) {
        log.info("Recebido da fila: {}", msg);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(msg.getVehicleId());

        vehicleOpt.ifPresent(vehicle -> {
            Double preco = fipeService.buscarPrecoFipe(msg.getBrandId(), msg.getModelId(), msg.getAnoCodigo());
            log.info("Preço obtido: {}", preco);
            vehicle.setFipePrice(preco);
            vehicleRepository.save(vehicle);
            log.info("Veículo atualizado com preço FIPE.");
        });
    }
}
