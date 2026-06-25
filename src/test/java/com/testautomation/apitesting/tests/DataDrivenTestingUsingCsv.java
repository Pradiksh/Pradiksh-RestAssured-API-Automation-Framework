package com.testautomation.apitesting.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.testautomation.apitest.utils.FileNameConstants;
import com.testautomation.apitesting.Listener.RestAssuredListener;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataDrivenTestingUsingCsv {
    @Test(dataProvider = "CSVTestData")
    public void DataDrivenTestingUsingCsv(Map<String,String>testdata){
        int totalprice = Integer.parseInt(testdata.get("totalprice"));
        try {
            BookingDates bookingDates = new BookingDates("2023-03-25", "2023-03-30");
            Booking booking = new Booking(testdata.get("firstname"), testdata.get("lastname"), "breakfast", totalprice, true, bookingDates);

            //serialization
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
            System.out.println(requestBody);


            Response response =
                    RestAssured
                            .given().filter(new RestAssuredListener())
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .baseUri("https://restful-booker.herokuapp.com/booking")

                            .when()
                            .post()

                            .then()
                            .assertThat().statusCode(200)

                            .extract()
                            .response();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(testdata.get("firstname"));
    }
    @DataProvider(name = "CSVTestData")
    public Object[][] getTestData(){
        Object[][] objArray =null;
        Map<String,String> map = null;
        List<Map<String,String>> testdatalist = null;

        try {
            CSVReader csvreader = new CSVReader(new FileReader(FileNameConstants.CSV_TEST_DATA));
            testdatalist = new ArrayList<Map<String,String>>();
            String[]line = null;
            int count = 0;
            while((line=csvreader.readNext())!=null){
                if(count==0){
count++;
continue;
                }
                map = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);

                map.put("firstname",line[0]);
                map.put("lastname",line[1]);
                map.put("totalprice",line[2]);

                testdatalist.add(map);

            }
            objArray = new Object[testdatalist.size()][1];
            for (int i = 0;i<testdatalist.size();i++){
                objArray[i][0] =testdatalist.get(i);

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return objArray;
    }
}
