package org.books.integration.rest;

import static com.jayway.restassured.RestAssured.get;
import com.jayway.restassured.response.Response;
import java.net.URL;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.jayway.restassured.RestAssured.get;

/**
 *
 * @author tjd
 */
public class CatalogServiceTest {

    @ArquillianResource
    URL deploymentUrl;

    @Test
    @RunAsClient
    public void findBookTest() {
        Response response = get(deploymentUrl.toString() + "books/123456");
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test
    @RunAsClient
    public void findBookByKeywordsTest() {
        Response response = get(deploymentUrl.toString() + "books?keywords=java%20ee");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
