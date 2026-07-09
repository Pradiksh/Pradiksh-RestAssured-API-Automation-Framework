package com.testautomation.apitesting.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitest.utils.FileNameConstants;
import com.testautomation.apitesting.Listener.RestAssuredListener;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class DataDrivenTestingUsingJsonFile {
    @Test(dataProvider = "getTestData")
    public void DataDrivenTestingUsingJson(LinkedHashMap<String,String> testdata) throws JsonProcessingException {

        BookingDates bookingDates = new BookingDates("2023-03-25", "2023-03-30");
        Booking booking = new Booking(testdata.get("firstname"), testdata.get("lastname"), "breakfast", 1000, true, bookingDates);

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

    }
    @DataProvider(name = "getTestData")
    public Object[] getTestDataUsingJson() throws IOException {
        String JsonData = FileUtils.readFileToString(new File(FileNameConstants.JSON_TEST_DATA), "UTF-8");
        JSONArray jsonArray = JsonPath.read(JsonData, "$");
        Object[] obj = new Object[jsonArray.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = jsonArray.get(i);
        }
        return obj;
    }
}
