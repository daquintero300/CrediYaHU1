package co.com.pragma.r2dbc.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MySQLConnectionPool {

    public static final String URL = "jdbc:mysql://localhost:3306/autentication";
    public static final String USER = "root";
    public static final String PASSWORD = "admin";
}
