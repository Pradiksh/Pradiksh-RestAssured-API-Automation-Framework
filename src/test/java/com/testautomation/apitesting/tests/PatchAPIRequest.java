package com.testautomation.apitesting.tests;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitest.utils.FileNameConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class PatchAPIRequest {
    @Test
    public void PatchAPIRequest(){

        try {
            String ReqBody = FileUtils.readFileToString(new File(FileNameConstants.Post_API_Request_Body),"UTF-8");

            String TokenReqBody = FileUtils.readFileToString(new File(FileNameConstants.Token_API_Request_Body),"UTF-8");

            String PutAPIReqBody = FileUtils.readFileToString(new File(FileNameConstants.Put_API_Request_Body),"UTF-8");

            String PatchAPIreqBody = FileUtils.readFileToString(new File(FileNameConstants.Patch_API_Request_Body),"UTF-8");

            //POstAPIRequest
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

            int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");

            //Get APIRequest
            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .baseUri("https://restful-booker.herokuapp.com/booking")

                    .when()
                    .get("/{bookingId}", bookingId)
                    .then()
                    .assertThat()
                    .statusCode(200);


            //Token generation Put request - Json web Token concept (JWT)
            Response tokenresponse =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .body(TokenReqBody)
                            .baseUri("https://restful-booker.herokuapp.com/auth")
                            .when()
                            .post()
                            .then()
                            .assertThat()
                            .statusCode(200)
                            .extract()
                            .response();
            System.out.println(tokenresponse.asPrettyString());
            String token = JsonPath.read(tokenresponse.body().asString(),"$.token");


            //Put API Request
            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(PutAPIReqBody)
                    .header("Cookie","token="+token)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                    .when()
                    .put("/{bookingId}", bookingId)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo("Pradiksh"))
                    .body("lastname", Matchers.equalTo("Soman"));

//Patch API Request

            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(PatchAPIreqBody)
                    .header("Cookie","token="+token)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                    .when()
                    .patch("/{bookingId}", bookingId)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo("Niko"));



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
