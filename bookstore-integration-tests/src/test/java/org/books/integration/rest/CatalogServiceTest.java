package org.books.integration.rest;

import com.jayway.restassured.response.Response;
import org.books.BookstoreArquillianTest;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;

import static com.jayway.restassured.RestAssured.get;

/**
 *
 * @author tjd
 */
public class CatalogServiceTest  extends BookstoreArquillianTest {

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
        // TODO
        Assert.assertEquals(response.getStatusCode(), 500);
    }
}
