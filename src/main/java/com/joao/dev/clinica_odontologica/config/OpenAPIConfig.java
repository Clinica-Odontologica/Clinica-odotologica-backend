package com.joao.dev.clinica_odontologica.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Clinica Odonotologica API",
                version = "1.0",
                description = "Endpoints para Clinica Odontologica Backend"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local server"
                )
        }
)
public class OpenAPIConfig {

}
