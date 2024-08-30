package com.backendtestjava.service.impl;

import com.backendtestjava.model.Parking;
import com.backendtestjava.repository.ParkingRepository;
import com.backendtestjava.service.AbstractParkingService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("car")
@Primary
@AllArgsConstructor
public class CarParkingServiceImpl extends AbstractParkingService {

    ParkingRepository parkingRepository;

    @Override
    public Parking parkVehicle(Parking parking) {

        //aqui logica de validar se tem vaga

        return parkingRepository.save(parking);
    }

    @Override
    public Parking unparkVehicle(Parking parking) {
        //aqui logica de validações se encontra veiculo e estacionamento

        return parkingRepository.save(parking);
    }
}
