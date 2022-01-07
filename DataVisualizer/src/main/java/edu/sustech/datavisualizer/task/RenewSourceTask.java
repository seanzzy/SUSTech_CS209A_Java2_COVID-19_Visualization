package edu.sustech.datavisualizer.task;

import edu.sustech.datavisualizer.DataVisualizerApplication;
import edu.sustech.datavisualizer.utils.DataLoader;
import edu.sustech.datavisualizer.utils.DataProcessor;
import edu.sustech.datavisualizer.utils.RequestData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RenewSourceTask {
    @Scheduled(cron = "0 0 * * * ?")
    public void execute() {
        DataVisualizerApplication.log.info("Start renew source");
        if (RequestData.getProvinceData()) DataLoader.readProvinceData();
        for (String source : RequestData.getSources()) {
            if (RequestData.bySource(source)) {
                DataLoader.readData(source);
            }
        }
        DataVisualizerApplication.log.info("Finish renew source");
    }
}