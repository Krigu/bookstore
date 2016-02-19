package org.books.integration.rest;


import com.jayway.restassured.matcher.RestAssuredMatchers;
import com.jayway.restassured.response.Response;
import org.books.BookstoreArquillianTest;
import org.hamcrest.text.IsEmptyString;
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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class CustomerServiceTest extends BookstoreArquillianTest {


    private static final String LASTNAME = "Lastname";
    private static final String CONTENT_LENGTH = "Content-Length";
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
    public void createCustomerXmlTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_request_1.xml").getPath(), StandardCharsets.UTF_8);

        given().contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(201).
                contentType("text/plain").
                body(equalTo("C-1"));

    }

    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void createCustomerJsonTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_request_2.json").getPath(), StandardCharsets.UTF_8);

        given().contentType("application/json").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(201).
                contentType("text/plain").
                body(equalTo("C-2"));

    }

    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void createCustomerInvalidJsonTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/invalid_customer_request.json").getPath(), StandardCharsets.UTF_8);

        given().contentType("application/json").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(400);

    }

    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void createCustomerInvalidJsonTest2() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/invalid_customer_request_2.json").getPath(), StandardCharsets.UTF_8);

        given().contentType("application/json").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().log().all().
                statusCode(400);

    }

    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void createSameCustomerTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_request_1.xml").getPath(), StandardCharsets.UTF_8);

        given().contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(409);

    }


    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void getCustomerXmlTest() throws IOException {

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers/C-1").
                then().log().all().
                statusCode(200).
                contentType("application/xml").
                header("Content-Length", notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd)));

    }

    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void getCustomerJsonTest() throws IOException {

        given().log().all().
                accept("application/json").
                get(deploymentUrl.toString() + "customers/C-1").
                then().
                statusCode(200).
                contentType("application/json").
                header(CONTENT_LENGTH, notNullValue()).
                body("email", equalTo("test@test.com")).
                body("address.street", equalTo("Street")).
                body("creditCard.type", equalTo("Visa"));

    }


    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void getCustomerByNameInvalidNameTest() throws IOException {

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers").
                then().
                header("Content-Length", notNullValue()).
                statusCode(400);

    }

    @Test(dependsOnMethods = {"createCustomerXmlTest", "createCustomerJsonTest"})
    @RunAsClient
    public void getCustomerByNameTest() throws IOException {

        Response response = given().log().all().
                accept("application/xml").
                parameter("name", "Last").
                get(deploymentUrl.toString() + "customers");

        response.
                then().
                statusCode(200).
                header("Content-Length", notNullValue()).
                contentType("application/xml").
                header(CONTENT_LENGTH, notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd))).
                body("customerInfoes.customerInfo.lastName", hasItems(LASTNAME, LASTNAME + "2"));

    }

    @Test(dependsOnMethods = {"createCustomerXmlTest"})
    @RunAsClient
    public void updateCustomerNotFoundXMLTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_update_request.json").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                contentType("application/json").
                body(content).
                put(deploymentUrl.toString() + "customers/C-1234").
                then().
                statusCode(404);

    }

    @Test(dependsOnMethods = {"createCustomerJsonTest", "createCustomerXmlTest"})
    @RunAsClient
    public void updateCustomerWithExistingEmailXMLTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_update_request.json").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                contentType("application/json").
                body(content).
                put(deploymentUrl.toString() + "customers/C-1").
                then().
                statusCode(409);

    }

    @Test(dependsOnMethods = {"createCustomerXmlTest"})
    @RunAsClient
    public void updateCustomerWithInvalidCustomerDataTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/invalid_customer_update_request.json").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                contentType("application/json").
                body(content).
                put(deploymentUrl.toString() + "customers/C-1").
                then().
                statusCode(400);

    }

    @Test(dependsOnMethods = {"createCustomerXmlTest", "createCustomerJsonTest"})
    @RunAsClient
    public void updateCustomerJsonTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_update_request.json").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers/C-2").
                then().log().all().
                statusCode(200).
                contentType("application/xml").
                header(CONTENT_LENGTH, notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd))).
                body("customer.lastName", equalTo("Lastname2"));

        // Update request
        given().log().all().
                contentType("application/json").
                body(content).
                put(deploymentUrl.toString() + "customers/C-2").
                then().
                statusCode(204).
                body(IsEmptyString.isEmptyOrNullString());

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers/C-2").
                then().log().all().
                statusCode(200).
                contentType("application/xml").
                header(CONTENT_LENGTH, notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd))).
                body("customer.lastName", equalTo("Lastname Updated"));

    }

}
