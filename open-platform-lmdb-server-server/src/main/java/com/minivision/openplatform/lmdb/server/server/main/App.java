package com.minivision.openplatform.lmdb.server.server.main;

import com.minivision.openplatform.lmdb.server.server.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(Config.class)
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }

}
