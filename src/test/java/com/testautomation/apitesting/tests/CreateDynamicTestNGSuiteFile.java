package com.testautomation.apitesting.tests;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.testautomation.apitest.utils.FileNameConstants;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateDynamicTestNGSuiteFile {
    public static void main(String[] args){
        //Create TEstNg Object
        TestNG testNg = new TestNG();

        //Create Suite Object
        XmlSuite xmlSuite = new XmlSuite();
        xmlSuite.setName("Suite1");
        xmlSuite.setParallel(XmlSuite.ParallelMode.METHODS);
        xmlSuite.setThreadCount(5);
        xmlSuite.setVerbose(2);


        //Create Test Object
        XmlTest xmlTest = new XmlTest(xmlSuite);
        xmlTest.setName("TestName");
        xmlTest.setPreserveOrder(true);


        // Create Class Object

        XmlClass xmlClass = new XmlClass("com.testautomation.apitesting.tests.EndToEndAPITest");

        // Add all test methods
        List<XmlInclude> allMethods = new ArrayList<XmlInclude>();

//
//        allMethods.add(new XmlInclude("End2EndApiRequest"));
//        allMethods.add(new XmlInclude("End2EndApiRequest2"));
//        allMethods.add(new XmlInclude("End2EndApiRequest3"));

        //Doing the same via Excel file using fillo

        Fillo fillo = new Fillo();

        Recordset recorsdset = null;
        Connection connection = null;

        try {
            connection = fillo.getConnection(FileNameConstants.Test_Runner);

            String Query = "Select * from Sheet1";
            recorsdset = connection.executeQuery(Query);

            while(recorsdset.next()){
                if(recorsdset.getField("Run").equals("Yes")){
                    allMethods.add(new XmlInclude(recorsdset.getField("TestMethod")));
                }
            }

        } catch (FilloException e) {
            throw new RuntimeException(e);
        }


        xmlClass.setIncludedMethods(allMethods);
        xmlTest.getClasses().add(xmlClass);


        //Add TestNG Suites
List<XmlSuite> SuiteList = new ArrayList<XmlSuite>();
SuiteList.add(xmlSuite);
testNg.setXmlSuites(SuiteList);

        //RunTestNG Object
        testNg.run();

        //Generate TEstng Suite file using File Writer

        try {
            FileWriter fileWriter = new FileWriter(new File("src/Suites/RunTimeTEstNGSuiteFile.xml"));
            fileWriter.write(xmlSuite.toXml());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
