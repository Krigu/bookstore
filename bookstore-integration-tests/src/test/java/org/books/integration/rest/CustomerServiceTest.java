package org.books.integration.rest;


import com.jayway.restassured.response.Response;
import org.books.BookstoreArquillianTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;

import static com.jayway.restassured.RestAssured.get;

public class CustomerServiceTest extends BookstoreArquillianTest {


    @ArquillianResource URL deploymentUrl;

    @Test
    @RunAsClient
    public void getCustomerTest() {
        Response response = get(deploymentUrl.toString() + "customers/9898745");
        Assert.assertEquals(response.getStatusCode(), 404);
    }
}
