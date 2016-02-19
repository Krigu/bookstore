package org.books.integration.rest;

import com.jayway.restassured.matcher.RestAssuredMatchers;
import org.books.BookstoreArquillianTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author tjd
 */
@Test(groups = {"OrderServiceRestTest"}, dependsOnGroups = {"CatalogRestTest", "CustomerRestTest"})
public class OrderServiceTest extends BookstoreArquillianTest {

    private final String ordersxsd = this.getClass().getResource("/xml/orders.xsd").getPath();


    @ArquillianResource
    URL deploymentUrl;


    @Test
    @RunAsClient
    public void createOrderNoBodyTest() throws IOException {

        given().contentType("application/xml").
                when().
                post(deploymentUrl.toString() + "orders/").
                then().
                statusCode(400);

    }

    @Test
    @RunAsClient
    public void createInvalidOrderTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/invalid_order.xml").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                accept("application/xml").
                contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "orders").
                then().
                statusCode(400);

    }

    @Test
    @RunAsClient
    public void createOrderInvalidCustomerNummerTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_order_wrong_customer_nr.xml").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                accept("application/xml").
                contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "orders").
                then().
                statusCode(404);

    }

    @Test
    @RunAsClient
    public void createOrderInvalidPaymentTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_order_invalid_payment.xml").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                accept("application/xml").
                contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "orders").
                then().
                statusCode(402);

    }



    @Test
    @RunAsClient
    public void createOrderTest() throws IOException {

        String content = readFile(this.getClass().getResource("/xml/valid_order.xml").getPath(), StandardCharsets.UTF_8);

        given().log().all().
                accept("application/xml").
                contentType("application/xml").
                body(content).
                when().
                post(deploymentUrl.toString() + "orders").
                then().log().all().
                statusCode(201).
                header(CONTENT_LENGTH, notNullValue()).
                contentType("application/xml").
                body(RestAssuredMatchers.matchesXsd(new File(ordersxsd))).
                body("order.status", is("accepted")).
                body("order.customerInfo.number", is("C-1")).
                body("order.items.size()", is(2));

    }

    @Test
    @RunAsClient
    public void findOrderByInvalidNumberTest() {

        given().
                accept("application/xml").
                get(deploymentUrl.toString() + "orders/O-1124123").
                then().
                statusCode(404);

    }

    @Test(dependsOnMethods = "createOrderTest")
    @RunAsClient
    public void findOrderByNumber()  {

        given().log().all().
                accept("application/xml").
                get(deploymentUrl.toString() + "orders/O-1").
                then().log().all().
                statusCode(200).
                contentType("application/xml").
                header(CONTENT_LENGTH, notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(ordersxsd))).
                body("order.customerInfo.lastName", equalTo("Lastname")).
                body("order.items.size()", is(2));

    }
}
