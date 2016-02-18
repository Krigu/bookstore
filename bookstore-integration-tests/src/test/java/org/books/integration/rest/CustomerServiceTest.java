package org.books.integration.rest;


import com.jayway.restassured.matcher.RestAssuredMatchers;
import com.jayway.restassured.response.Response;
import org.books.BookstoreArquillianTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CustomerServiceTest extends BookstoreArquillianTest {


    private static final String LASTNAME = "Lastname";
    private final String customerXsd = this.getClass().getResource("/xml/customers.xsd").getPath();

    @ArquillianResource
    URL deploymentUrl;


    @Test
    @RunAsClient
    public void getInvalidCustomerTest() {
        Response response = get(deploymentUrl.toString() + "customers/9898745");
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test
    @RunAsClient
    public void createCustomerTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_request.xml").getPath(), StandardCharsets.UTF_8);

        given().contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(201).
                body(equalTo("C-1"));

    }

    @Test(dependsOnMethods = "createCustomerTest")
    @RunAsClient
    public void getCustomerXmlTest() throws IOException {

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers/C-1").
        then().
                statusCode(200).
                contentType("application/xml").
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd)));

    }

    @Test(dependsOnMethods = "createCustomerTest")
    @RunAsClient
    public void getCustomerJsonTest() throws IOException {

        given().log().all().
                accept("application/json").
                get(deploymentUrl.toString() + "customers/C-1").
        then().
                statusCode(200).
                contentType("application/json").
                body("email", equalTo("test@test.com")).
                body("address.street", equalTo("Street")).
                body("creditCard.type", equalTo("Visa"));

    }


    @Test(dependsOnMethods = "createCustomerTest")
    @RunAsClient
    public void getCustomerByNameInvalidNameTest() throws IOException {

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers").
                then().
                statusCode(400);

    }

    @Test(dependsOnMethods = "createCustomerTest")
    @RunAsClient
    public void getCustomerByNameTest() throws IOException {

        given().log().all().
                accept("application/xml").
                parameter("name", LASTNAME).
                get(deploymentUrl.toString() + "customers").
                then().
                statusCode(200).
                contentType("application/xml").
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd))).
                body("customerInfoes.customerInfo.lastName", equalTo(LASTNAME));

    }



}
