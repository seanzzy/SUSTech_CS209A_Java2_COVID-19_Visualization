package edu.sustech.datavisualizer;

import edu.sustech.datavisualizer.utils.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class DataVisualizerApplication {
    public static final Logger log = LoggerFactory.getLogger(SpringApplication.class);
    public static void main(String[] args) {
        DataLoader.readData("JHU");
        DataLoader.readData("WHO");
        DataLoader.readProvinceData();
        DataLoader.readChinaMap();
        log.info("INIT FINISHED!");
        SpringApplication.run(DataVisualizerApplication.class, args);
    }
}
