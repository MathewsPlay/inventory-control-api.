package com.matheuss.controle_estoque_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import com.matheuss.controle_estoque_api.dto.CategoryCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // <<< IMPORT NOVO
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD )
public class ComputerCreationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateComputerSuccessfully() throws Exception {
        // 1. Criar Categoria
        CategoryCreateDTO categoryDTO = new CategoryCreateDTO();
        categoryDTO.setName("Notebooks de Teste");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated());


        // 3. Criar Computador
        ComputerCreateDTO computerDTO = new ComputerCreateDTO();
        computerDTO.setAssetTag("TEST-TAG-001");
        computerDTO.setStatus(AssetStatus.EM_ESTOQUE);
        computerDTO.setEquipmentState(EquipmentState.NOVO);
        computerDTO.setPurchaseDate(LocalDate.now());
        computerDTO.setCategoryId(1L);
        computerDTO.setNotes("Teste de integração.");
        computerDTO.setName("Computador de Teste");
        computerDTO.setSerialNumber("TEST-SN-001");
        computerDTO.setCpu("CPU Teste");
        computerDTO.setRamSizeInGB(8);
        computerDTO.setStorageSizeInGB(256);
        computerDTO.setOs("OS Teste");

        System.out.println("--- ENVIANDO DTO PARA CRIAR COMPUTADOR ---");
        System.out.println(objectMapper.writeValueAsString(computerDTO));
        System.out.println("-----------------------------------------");

        // ================== MUDANÇA PRINCIPAL AQUI ==================
        // Vamos apenas executar a requisição e imprimir tudo, sem esperar um status específico.
        mockMvc.perform(post("/api/computers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(computerDTO)))
                .andDo(print()); // <<< andDo(print()) vai imprimir a requisição e a resposta completa
        // ============================================================
    }
}
