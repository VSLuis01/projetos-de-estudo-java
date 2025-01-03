package br.com.luisedu.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    //@Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setUrl(this.url);
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        ds.setDriverClassName(driver);

        return ds;
    }

    /**
     * Configuration Hikari
     * https://github.com/brettwooldridge/HikariCP
     */
    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();

        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        config.setMaximumPoolSize(10); // maximo de conexões liberadas
        config.setMinimumIdle(1); // tamanho inicial do pool. Vai começar com 1 conexão e pode ir até 10
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(600000); // 600.000ms -> 10 minutos
        config.setConnectionTimeout(100000); // timeout para conceder uma conexão
        config.setConnectionTestQuery("SELECT 1"); // query teste para verificar a conexão com o DB

        return new HikariDataSource(config);
    }
}
