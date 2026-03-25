package com.testautomation.apitestng.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Enabled;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GETAPIRequest {
    @Test
    public void getAllBookings() {
        Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                    .get()
                .then()
                    .assertThat()
                    .statusCode(200)
                    .statusLine("HTTP/1.1 200 OK")
                .header("Content-Type","application/json; charset=utf-8")

                    .extract()
                    .response();
        Assert.assertTrue(response.getBody().asString().contains("394"));




    }
    @Test
    public void createBooking(){
        //creating two JSON objects
        JSONObject booking = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        booking.put("firstname","Pradiksh");
        booking.put("lastname","Soman");
        booking.put("totalprice",1000);
        booking.put("depositpaid",true);
        booking.put("additionalneeds","Bed");
        booking.put("bookingdates",bookingDates);

        bookingDates.put("checkin","2023-05-27");
        bookingDates.put("checkout","2025-05-23");
Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(booking.toString())
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                    .post()
                .then()
                    .assertThat()
                    .statusCode(200)
                .body("booking.firstname", Matchers.equalTo("Pradiksh"))
        .extract()
                .response();



    }
}
