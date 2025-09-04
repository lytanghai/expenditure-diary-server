package com.expenditure_diary.expenditure_diary.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbConnectionTester implements CommandLineRunner {

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource primary;

    @Autowired
    @Qualifier("secondaryDataSource")
    private DataSource secondary;

    @Override
    public void run(String... args) {
        try (Connection conn = primary.getConnection()) {
            System.out.println("Primary DB connected: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("Primary DB connection failed: " + e.getMessage());
        }

        try (Connection conn = secondary.getConnection()) {
            System.out.println("Secondary DB connected: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("Secondary DB connection failed: " + e.getMessage());
        }
    }
}
