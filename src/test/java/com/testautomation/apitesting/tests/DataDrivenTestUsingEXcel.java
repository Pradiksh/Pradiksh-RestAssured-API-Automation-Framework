package com.testautomation.apitesting.tests;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testautomation.apitest.utils.FileNameConstants;
import com.testautomation.apitesting.Listener.RestAssuredListener;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataDrivenTestUsingEXcel {
    @Test(dataProvider = "getTestDataUsingExcel")
    public void DataDrivenTestUsingEXcel(Map<String,String>testData){
        System.out.println(testData.get("FirstName"));
        int totalprice = Integer.parseInt(testData.get("totalprice"));
        try {
            BookingDates bookingDates = new BookingDates("2023-03-25", "2023-03-30");
            Booking booking = new Booking(testData.get("firstname"), testData.get("lastname"), "breakfast", totalprice, true, bookingDates);

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
        System.out.println(testData.get("firstname"));
    }
    @DataProvider(name = "getTestDataUsingExcel")
    public Object[][] getTestData(){
        String query = "select * from Sheet1 where Run = 'Yes'";

        Object[][] objarray = null;
        Map<String,String> map =null;
        List<Map<String,String>> testDataList = null;

        Fillo fillo = new Fillo();
        Connection connection = null;
        Recordset recordset = null;

        try {
            connection = fillo.getConnection(FileNameConstants.Excel_TEST_DATA);
           recordset= connection.executeQuery(query);

           testDataList = new ArrayList<Map<String,String>>();

           while(recordset.next()){
               map = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);

               for(String field: recordset.getFieldNames()){
                   map.put(field, recordset.getField(field));
               }
               testDataList.add(map);

           }
objarray= new Object[testDataList.size()][1];
           for(int i =0; i<testDataList.size();i++){
               objarray[i][0]=testDataList.get(i);
           }

        } catch (FilloException e) {
            throw new RuntimeException(e);
        }

        return objarray;

    }
}
