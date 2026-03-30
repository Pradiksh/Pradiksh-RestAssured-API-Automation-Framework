package com.testautomation.apitesting.tests;

import com.testautomation.apitest.utils.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class POSTAPIRequest extends BaseTest {
    @Test
    public void createBooking() {
        //creating two JSON objects
        JSONObject booking = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        booking.put("firstname", "Pradiksh");
        booking.put("lastname", "Soman");
        booking.put("totalprice", 1000);
        booking.put("depositpaid", true);
        booking.put("additionalneeds", "Bed");
        booking.put("bookingdates", bookingDates);

        bookingDates.put("checkin", "2023-05-27");
        bookingDates.put("checkout", "2025-05-23");
        Response response =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(booking.toString())
                        .baseUri("https://restful-booker.herokuapp.com/booking")
//                .log().body()
                        .when()
                        .post()
                        .then()
                        .assertThat()
//                        This is used to log the response body but we are using BaseTest class for this
//                        .log().body()
                        .statusCode(200)
                        .body("booking.firstname", Matchers.equalTo("Pradiksh"))
                        .extract()
                        .response();
       int bookingId =  response.path("bookingid");

       RestAssured
               .given()
                   .contentType(ContentType.JSON)
                   .pathParam("bookingid",bookingId)
                   .baseUri("https://restful-booker.herokuapp.com/booking")
               .when()
                   .get("{bookingid}")
               .then()
               .assertThat()
               .statusCode(200)
               .log().body()
               .body("firstname",Matchers.equalTo("Pradiksh"))
               .body("lastname",Matchers.equalTo("Soman"));



    }
}
