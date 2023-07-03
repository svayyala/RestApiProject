package com.springboot.firstrestapi.myfirstrestapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserDetailsRepository repository;

    public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
       repository.save(new UserDetails("Sudheer", "Admin"));
       repository.save(new UserDetails("Rahul", "Admin"));
       repository.save(new UserDetails("Jhon", "User"));

        List<UserDetails> userDetails =  repository.findByRole("Admin");
        userDetails.forEach(user -> logger.info(user.toString()));

    }
}
