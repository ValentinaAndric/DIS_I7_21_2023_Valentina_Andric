package dis.reservation.restaurant_service;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

interface PostgresTestContainer {

    DockerImageName imageName = DockerImageName.parse("pgvector/pgvector:pg16");

    PostgreSQLContainer<?> pgVector = new PostgreSQLContainer<>(imageName);

    @BeforeAll
    static void startPostgresContainer() {

        pgVector.withDatabaseName("restaurant_service_db");
        pgVector.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", pgVector::getJdbcUrl);
        registry.add("spring.datasource.username", pgVector::getUsername);
        registry.add("spring.datasource.password", pgVector::getPassword);

    }
}
