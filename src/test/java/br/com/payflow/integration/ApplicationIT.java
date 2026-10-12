package br.com.payflow.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import br.com.payflow.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ApplicationIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Dado o banco vazio , quando a aplicação sobe ,então o Flyway cria a tabela merchants")
    void shouldCreateMerchantsTable() {
        Integer total = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(*)
                        FROM information_schema.tables
                        WHERE table_schema = 'public' AND table_name = 'merchants'
                        """,
                Integer.class);
        assertThat(total).isEqualTo(1);
    }

    @Test
    @DisplayName("Dado o banco vazio , quando a aplicação sobe , então o Flyway registra a versão 1 com sucesso")
    void shouldRegisterVersionOneInFlywayHistory() {
        Boolean success = jdbcTemplate.queryForObject(
                "SELECT success FROM flyway_schema_history WHERE version = ?",
                Boolean.class,
                "1");
        assertThat(success).isTrue();
    }

    @Test
    @DisplayName("Dado o banco vazio, quando a aplicação sobe, então tabela merchants tem a coluna created_at ")
    void shouldHaveCreatedAtColumnInMerchants() {
        Integer total = jdbcTemplate.queryForObject(
                """
                        SELECT COUNT(*)
                        FROM information_schema.columns
                        WHERE table_schema = 'public' AND table_name = 'merchants' AND column_name = 'created_at'
                        """,
                Integer.class);
        assertThat(total).isEqualTo(1);
    }
}

