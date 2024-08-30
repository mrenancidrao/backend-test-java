package com.backendtestjava.service.impl;

import com.backendtestjava.model.Parking;
import com.backendtestjava.repository.ParkingRepository;
import com.backendtestjava.service.AbstractParkingService;
import org.springframework.stereotype.Service;

@Service("motorcycle")
public class MotorcycleParkingServiceImpl extends AbstractParkingService {
    ParkingRepository parkingRepository;

    @Override
    public Parking parkVehicle(Parking parking) {

        //aqui logica de validar se tem vaga

        return parkingRepository.save(parking);
    }

    @Override
    public Parking unparkVehicle(Parking parking) {
        //aqui logica de validações se encontra veiculo e estacionamento e tirar da controller

        return parkingRepository.save(parking);
    }
}
