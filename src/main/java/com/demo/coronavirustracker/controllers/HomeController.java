package com.demo.coronavirustracker.controllers;

import com.demo.coronavirustracker.models.LocationStats;
import com.demo.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/fetchVirusData")
    @ResponseBody
    List<LocationStats> fetchVirusData() throws IOException, InterruptedException {
        return coronaVirusDataService.fetchVirusData();
    }

    @GetMapping("/fetchVirusData/{country}")
    @ResponseBody
    LocationStats fetchVirusData(@PathVariable String country) throws IOException, InterruptedException {
        return coronaVirusDataService.fetchVirusData(country);
    }

    @GetMapping("/fetchHighest")
    @ResponseBody
    LocationStats fetchVirusDataHighest() throws IOException, InterruptedException {
        return coronaVirusDataService.fetchVirusDataHighest();
    }
}
