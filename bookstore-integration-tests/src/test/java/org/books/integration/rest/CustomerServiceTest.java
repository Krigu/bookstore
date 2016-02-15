package org.books.integration.rest;


import org.books.BookstoreArquillianTest;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.annotations.Test;

import java.net.URL;

import static com.jayway.restassured.RestAssured.get;

public class CustomerServiceTest extends BookstoreArquillianTest {

    @ArquillianResource
    URL deploymentUrl;

    @Test
    @OperateOnDeployment("rest")
    public void testGetCustomer() {
        String s = get(deploymentUrl.toString() + "customers/1").asString();
        System.out.println(s);

    }
}
