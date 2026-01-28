package com.matheuss.controle_estoque_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean; // 1. ESTE É O IMPORT CORRETO
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
// A anotação em si estava correta, apenas o import precisava de ajuste
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class) 
public class ControleEstoqueApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleEstoqueApiApplication.class, args);
    }
}
