package com.testautomation.apitestng.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class GETAPIRequest {
    @Test
    public void getAllBookings(){
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                .get();
    }
}
