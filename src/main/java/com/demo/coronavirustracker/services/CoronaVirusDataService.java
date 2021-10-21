package com.demo.coronavirustracker.services;

import com.demo.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.stream.Location;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";


    private List<LocationStats> allStats = new ArrayList<LocationStats>();
    @Scheduled(cron="* * 1 * * *")
//    to run this method every second
    @PostConstruct
    public  List<LocationStats> fetchVirusData() throws IOException, InterruptedException {

        List<LocationStats> newStats = new ArrayList<LocationStats>();
//        Creating a new instance of the list because there might be an issuse of concurrency if the list is accessed at the time.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();

        HttpResponse<String> httpResponse =client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(httpResponse.body());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
            System.out.println(locationStats);
            newStats.add(locationStats);
        }
        this.allStats=newStats;

        return allStats;

    }
    public LocationStats fetchVirusData(String country) throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<LocationStats>();
//        Creating a new instance of the list because there might be an issuse of concurrency if the list is accessed at the time.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();

        HttpResponse<String> httpResponse =client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(httpResponse.body());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        boolean flag=true;
        for(CSVRecord record : records){
            LocationStats locationStats = new LocationStats();
            if(record.get("Country/Region").toLowerCase().equals(country.toLowerCase())){
                flag=false;
                locationStats.setState(record.get("Province/State"));
                locationStats.setCountry(record.get("Country/Region"));
                locationStats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
                return locationStats;
            }
        }
        return new LocationStats();
    }

    public LocationStats fetchVirusDataHighest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();

        HttpResponse<String> httpResponse =client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(httpResponse.body());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        String countryTemp="", stateTemp="";
        Integer latestTotalCasesTemp=0;
        int max=-1;
        for(CSVRecord record : records){
            if(Integer.parseInt(record.get(record.size()-1))>max){
                max=Integer.parseInt(record.get(record.size()-1));
                countryTemp= record.get("Country/Region");
                stateTemp= record.get("Province/State");
            }
        }
        LocationStats locationStats = new LocationStats();
        locationStats.setLatestTotalCases(max);
        locationStats.setState(stateTemp);
        locationStats.setCountry(countryTemp);
        return locationStats;
    }
}
