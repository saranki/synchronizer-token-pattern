package com.sliit.synchronizertokenpattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sliit.synchronizertokenpattern.model.ServerStore;
import com.sliit.synchronizertokenpattern.model.User;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		new ServerStore().assignUserCredentials();
	}
}
