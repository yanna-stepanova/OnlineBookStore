package com.yanna.stepanova.config;

import java.util.Properties;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final DockerImageName DB_IMAGE = DockerImageName.parse("mysql:8.0.36");
    private static CustomMySqlContainer mySqlContainer;

    public CustomMySqlContainer(DockerImageName dockerImage) {
        super(dockerImage);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (mySqlContainer == null) {
            mySqlContainer = new CustomMySqlContainer(DB_IMAGE);
        }
        return mySqlContainer;
    }

    @Override
    public void start() {
        super.start();
        Properties props = new Properties();
        props.put("TEST_DB_URL", mySqlContainer.getJdbcUrl());
        props.put("TEST_DB_USERNAME", mySqlContainer.getUsername());
        props.put("TEST_DB_PASSWORD", mySqlContainer.getPassword());
        System.setProperties(props);
    }

    @Override
    public void stop() {
    }
}
