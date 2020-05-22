package com.example.chess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class ChessApplication {

    private static String port;
    //直接用的@value
    @Value("${server.port}")
    public void setPort(String _port) {
        port = _port;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChessApplication.class, args);
        System.out.println("链接地址： http://localhost:"+ port);
    }

}
