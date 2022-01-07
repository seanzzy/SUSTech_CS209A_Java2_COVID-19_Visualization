package edu.sustech.datavisualizer.controller;

import edu.sustech.datavisualizer.DataVisualizerApplication;
import edu.sustech.datavisualizer.utils.DataLoader;
import edu.sustech.datavisualizer.utils.RequestData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/renew")
    public String renew() {
        DataVisualizerApplication.log.info("Start renew source");
        if (RequestData.getProvinceData()) DataLoader.readProvinceData();
        for (String source : RequestData.getSources()) {
            if (RequestData.bySource(source)) {
                DataLoader.readData(source);
            }
        }
        DataVisualizerApplication.log.info("Finish renew source");
        return "Renew Finished!";
    }
}
