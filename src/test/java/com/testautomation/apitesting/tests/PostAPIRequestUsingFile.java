package com.testautomation.apitesting.tests;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitest.utils.BaseTest;
import com.testautomation.apitest.utils.FileNameConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class PostAPIRequestUsingFile extends BaseTest {
    @Test
    public void postApiRequest() throws IOException {
        String ReqBody = FileUtils.readFileToString(new File(FileNameConstants.Post_API_Request_Body),"UTF-8");
Response response =
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(ReqBody)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                    .post()
                .then()
                    .assertThat()
                    .statusCode(200)
                .extract()
                .response();

       JSONArray jsonarrayFirstName =  JsonPath.read(response.body().asString(),"$.booking..firstname");
     String firstname = (String) jsonarrayFirstName.get(0);

     Assert.assertEquals(firstname,"Pradiksh");

     JSONArray jsonarrayLastName = JsonPath.read(response.body().asString(),"$.booking..lastname");
     String lastname = (String)jsonarrayLastName.get(0);

     Assert.assertEquals(lastname,"Soman");

     JSONArray jsonarraycheckin = JsonPath.read(response.body().asString(),"$.booking.bookingdates..checkin");
     String checkin = (String)jsonarraycheckin.get(0);

     Assert.assertEquals(checkin,"2023-05-27");

     int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");

     RestAssured
             .given()
                 .contentType(ContentType.JSON)
                 .baseUri("https://restful-booker.herokuapp.com/booking")
             .when()
                 .get("/{bookingId}",bookingId)
             .then()
                 .assertThat()
                 .statusCode(200);

    }
}
