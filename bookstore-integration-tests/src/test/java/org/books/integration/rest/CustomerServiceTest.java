package org.books.integration.rest;


import com.jayway.restassured.matcher.RestAssuredMatchers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;
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
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

@Test(groups = "CustomerRestTest")
public class CustomerServiceTest extends BookstoreArquillianTest {


    private static final String LASTNAME = "Lastname";

    private final String customerXsd = this.getClass().getResource("/xml/customers.xsd").getPath();

    @ArquillianResource
    URL deploymentUrl;

    private String customerNumber2;
    private String customerNumer1;

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

        ResponseBodyExtractionOptions body = given().contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(201).
                contentType("text/plain").
                body(startsWith("C-")).extract().body();

        customerNumer1 = body.asString();

    }

    @Test(dependsOnMethods = "createCustomerXmlTest")
    @RunAsClient
    public void createCustomerJsonTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_request_2.json").getPath(), StandardCharsets.UTF_8);

        ResponseBodyExtractionOptions body = given().contentType("application/json").
                body(content).
                when().
                post(deploymentUrl.toString() + "customers/").
                then().
                statusCode(201).
                contentType("text/plain").
                body(startsWith("C-")).
                extract().body();

        customerNumber2 = body.asString();

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
                get(deploymentUrl.toString() + "customers/" + customerNumer1).
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
                get(deploymentUrl.toString() + "customers/" + customerNumer1).
                then().
                statusCode(200).
                contentType("application/json").
                header(CONTENT_LENGTH, notNullValue()).
                body("email", equalTo("test_rest@test.com")).
                body("address.street", equalTo("Street")).
                body("creditCard.type", equalTo("MasterCard"));

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
                put(deploymentUrl.toString() + "customers/" + customerNumer1).
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
                put(deploymentUrl.toString() + "customers/" + customerNumer1).
                then().
                statusCode(400);

    }

    @Test(dependsOnMethods = {"createCustomerXmlTest", "createCustomerJsonTest"})
    @RunAsClient
    public void updateCustomerJsonTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_customer_update_request.json").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers/" + customerNumber2).
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
                put(deploymentUrl.toString() + "customers/" + customerNumber2).
                then().
                statusCode(204).
                body(IsEmptyString.isEmptyOrNullString());

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "customers/" + customerNumber2).
                then().log().all().
                statusCode(200).
                contentType("application/xml").
                header(CONTENT_LENGTH, notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(customerXsd))).
                body("customer.lastName", equalTo("Lastname Updated"));

    }

}
