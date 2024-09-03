package com.backendtestjava.controller;

import com.backendtestjava.model.Address;
import com.backendtestjava.model.Establishment;
import com.backendtestjava.service.EstablishmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EstablishmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstablishmentService establishmentService;

    private Establishment createExistingEstablishment(UUID establishmentId) {
        Address address = new Address();
        address.setStreet("RUA LIMOEIRO");
        address.setNumber("20");
        address.setComplement("PROXIMO AO BANCO");
        address.setNeighborhood("BAIRRO DE FATIMA");
        address.setCity("FORTALEZA");
        address.setState("CEARA");

        Establishment establishment = new Establishment();
        establishment.setId(establishmentId);
        establishment.setName("ESTACIONAMENTO DO FALCAO");
        establishment.setCnpj("40245027000169");
        establishment.setPhone("(85) 99784-8526");
        establishment.setAddress(address);
        establishment.setNumberCarSpaces(25);
        establishment.setNumberMotorcycleSpaces(20);
        return establishment;
    }

    private String createAddressJson(String street, String number, String complement, String neighborhood, String city, String state) {
        return String.format("""
                {
                    "street": "%s",
                    "number": "%s",
                    "complement": "%s",
                    "neighborhood": "%s",
                    "city": "%s",
                    "state": "%s"
                }
                """, street, number, complement, neighborhood, city, state);
    }

    private String createEstablishmentJson(String name, String cnpj, String phone, String addressJson, int numberCarSpaces, int numberMotorcycleSpaces) {
        return String.format("""
                {
                    "name": "%s",
                    "cnpj": "%s",
                    "phone": "%s",
                    "address": %s,
                    "numberCarSpaces": %s,
                    "numberMotorcycleSpaces": %s
                }
                """, name, cnpj, phone, addressJson, numberCarSpaces, numberMotorcycleSpaces);
    }

    @Test
    void shouldCreateEstablishmentWithSuccess() throws Exception {
        var addressValidJson = createAddressJson("RUA LIMOEIRO", "20", "PROXIMO AO BANCO",
                "BAIRRO DE FATIMA", "FORTALEZA", "CEARÁ");

        var establishmentJson = createEstablishmentJson("ESTACIONAMENTO DO FALCAO", "40245027000169",
                "(85) 99784-8526", addressValidJson, 25, 20);

        mockMvc.perform(post("/establishments")
                        .contentType("application/json")
                        .content(establishmentJson))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenEstablishmentDataIsInvalid() throws Exception {
        var incompleteEstablishmentJson = """
                {
                    "name": "ESTACIONAMENTO DO FALCAO",
                    "cnpj": "40245027000169",
                }
                """;

        mockMvc.perform(post("/establishments")
                        .contentType("application/json")
                        .content(incompleteEstablishmentJson))
                .andExpect(status().isBadRequest());

        var invalidEstablishmentJson = createEstablishmentJson("ESTACIONAMENTO DO FALCAO", "ASDASDASD",
                "(85) 99784-8526", null, 25, 20);

        mockMvc.perform(post("/establishments")
                        .contentType("application/json")
                        .content(invalidEstablishmentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateEstablishmentWithSuccess() throws Exception {
        UUID establishmentId = UUID.randomUUID();
        Establishment existingEstablishment = createExistingEstablishment(establishmentId);

        var addressValidJson = createAddressJson("RUA LIMOEIRO", "20", "PROXIMO AO BANCO",
                "BAIRRO DE FATIMA", "FORTALEZA", "CEARA");

        var updatedEstablishmentJson = createEstablishmentJson("ESTACIONAMENTO DO FALCAO", "40245027000169",
                "(85) 99784-8526", addressValidJson, 25, 20);

        when(establishmentService.findById(establishmentId)).thenReturn(Optional.of(existingEstablishment));

        mockMvc.perform(put("/establishments/" + establishmentId)
                        .contentType("application/json")
                        .content(updatedEstablishmentJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/establishments/" + establishmentId))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedEstablishmentJson));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentEstablishment() throws Exception {
        UUID establishmentId = UUID.randomUUID();

        var addressValidJson = createAddressJson("RUA MANOEL JOAQUIM", "185", "FRENTE ASSEMBLEIA",
                "ALDEOTA", "FORTALEZA", "CEARÁ");

        var establishmentJson = createEstablishmentJson("ESTACIONAMENTO DO FALCAO", "40245027000169",
                "(85) 99725-9946", addressValidJson, 25, 20);

        mockMvc.perform(put("/establishments/" + establishmentId)
                        .contentType("application/json")
                        .content(establishmentJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEstablishmentWithSuccess() throws Exception {
        UUID establishmentId = UUID.randomUUID();
        Establishment existingEstablishment = createExistingEstablishment(establishmentId);

        when(establishmentService.findById(establishmentId)).thenReturn(Optional.of(existingEstablishment));

        mockMvc.perform(delete("/establishments/" + establishmentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentEstablishment() throws Exception {
        UUID establishmentId = UUID.randomUUID();

        mockMvc.perform(delete("/establishments/" + establishmentId))
                .andExpect(status().isNotFound());
    }
}
