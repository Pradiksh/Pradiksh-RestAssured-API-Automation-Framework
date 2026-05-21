package com.testautomation.apitesting.Listener;


import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestAssuredListener implements Filter {
    private static final Logger logger = LogManager.getLogger(RestAssuredListener.class);
    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification, FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        Response response =   filterContext.next(filterableRequestSpecification,filterableResponseSpecification);
//if(response.getStatusCode()!=200 && response.getStatusCode()!= 201){

    logger.info("\n Method => "+filterableRequestSpecification.getMethod()+
            "\n Uri => "+filterableRequestSpecification.getURI()+
            "\n Request body => "+filterableRequestSpecification.getBody()+
            "\n Response body => "+response.getBody().prettyPrint());
//}


        return response;
    }

}
