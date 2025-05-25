package project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiceCSV {
    private static ServiceCSV instance;

    private ServiceCSV() {
    }

    public static ServiceCSV getInstance() {
        if (instance == null) {
            instance = new ServiceCSV();
        }
        return instance;
    }

    public void writeCSV(String information) {
        Path path = Paths.get("actions.csv");
        String data = information + " , " +  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n";

        try {
            Files.writeString(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}













