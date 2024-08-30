package com.backendtestjava.service;

import com.backendtestjava.model.Address;
import com.backendtestjava.model.Establishment;
import com.backendtestjava.repository.EstablishmentRepository;
import com.backendtestjava.service.impl.EstablishmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class EstablishmentServiceTest {

    @Mock
    private EstablishmentRepository establishmentRepository;

    @InjectMocks
    private EstablishmentServiceImpl establishmentService;

    private Establishment establishment;

    @BeforeEach
    public void setUp() {
        Address address = new Address();
        address.setStreet("RUA LIMOEIRO");
        address.setNumber("20");
        address.setComplement("PROXIMO AO BANCO");
        address.setNeighborhood("BAIRRO DE FATIMA");
        address.setCity("FORTALEZA");
        address.setState("CEARA");

        establishment = new Establishment();
        establishment.setName("ESTACIONAMENTO DO FALCAO");
        establishment.setCnpj("40245027000169");
        establishment.setPhone("(85) 99784-8526");
        establishment.setAddress(address);
        establishment.setNumberCarSpaces(25);
        establishment.setNumberMotorcycleSpaces(20);
        establishment.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        establishment.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    }

    @Test
    void shouldCreateEstablishmentWithSuccess() {
        when(establishmentRepository.save(any(Establishment.class))).thenReturn(establishment);

        Establishment savedEstablishment = establishmentService.save(establishment);

        ArgumentCaptor<Establishment> establishmentCaptor = forClass(Establishment.class);
        verify(establishmentRepository).save(establishmentCaptor.capture());

        Establishment capturedEstablishment = establishmentCaptor.getValue();

        assertEquals("ESTACIONAMENTO DO FALCAO", capturedEstablishment.getName());
        assertEquals("40245027000169", capturedEstablishment.getCnpj());
        assertEquals("(85) 99784-8526", capturedEstablishment.getPhone());
        assertEquals(25, capturedEstablishment.getNumberCarSpaces());
        assertEquals(20, capturedEstablishment.getNumberMotorcycleSpaces());

        assertNotNull(capturedEstablishment.getAddress());
        assertNotNull(savedEstablishment);
    }

    @Test
    void shouldFindEstablishmentByIdWithSuccess() {
        UUID establishmentId = UUID.randomUUID();
        when(establishmentRepository.findById(establishmentId)).thenReturn(Optional.of(establishment));

        Optional<Establishment> foundEstablishment = establishmentService.findById(establishmentId);

        assertTrue(foundEstablishment.isPresent());
        assertEquals(establishment, foundEstablishment.get());
    }

    @Test
    void shouldReturnEmptyWhenEstablishmentNotFound() {
        UUID establishmentId = UUID.randomUUID();
        when(establishmentRepository.findById(establishmentId)).thenReturn(Optional.empty());

        Optional<Establishment> foundEstablishment = establishmentService.findById(establishmentId);

        assertFalse(foundEstablishment.isPresent());
    }

    @Test
    void shouldDeleteEstablishmentWithSuccess() {
        establishmentService.delete(establishment);

        verify(establishmentRepository, times(1)).delete(establishment);
    }

    @Test
    void shouldFindEstablishmentByCnpjWithSuccess() {
        String cnpj = "40245027000169";
        when(establishmentRepository.findByCnpj(cnpj)).thenReturn(Optional.of(establishment));

        Optional<Establishment> foundEstablishment = establishmentService.findByCnpj(cnpj);

        assertTrue(foundEstablishment.isPresent());
        assertEquals(establishment, foundEstablishment.get());
    }

    @Test
    void shouldReturnEmptyWhenEstablishmentNotFoundByLicencePlate() {
        String cnpj = "INVALID123";
        when(establishmentRepository.findByCnpj(cnpj)).thenReturn(Optional.empty());

        Optional<Establishment> foundEstablishment = establishmentService.findByCnpj(cnpj);

        assertFalse(foundEstablishment.isPresent());
    }
}
