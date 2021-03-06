package org.books.integration.rest;

import com.jayway.restassured.matcher.RestAssuredMatchers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;
import org.books.BookstoreArquillianTest;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * @author tjd
 */

@Test(groups = "CatalogRestTest")
public class CatalogServiceTest extends BookstoreArquillianTest {

    private final String catalogXsd = this.getClass().getResource("/xml/catalog.xsd").getPath();


    @ArquillianResource
    URL deploymentUrl;

    @Test
    @RunAsClient
    public void invalidISBNTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CatalogServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        given().
                accept("application/xml").
                get(deploymentUrl.toString() + "books/123456").
                then().
                statusCode(404);


    }

    @Test
    @RunAsClient
    public void getBookByISBNTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CatalogServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        given().
                accept("application/xml").
                get(deploymentUrl.toString() + "books/1935182994").
                then().
                statusCode(200).
                header(CONTENT_LENGTH, notNullValue()).
                body(RestAssuredMatchers.matchesXsd(new File(catalogXsd))).
                body("book.title", is("EJB 3 in Action"));


    }


    @Test
    @RunAsClient
    public void getBookByISBNJsonTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CatalogServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        given().
                accept("application/json").
                get(deploymentUrl.toString() + "books/1935182994").
                then().
                statusCode(200).
                header(CONTENT_LENGTH, notNullValue()).
                body("title", is("EJB 3 in Action"));


    }


    @Test
    @RunAsClient
    public void findKeywordsBadRequestTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CatalogServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        given().
                accept("application/xml").
                get(deploymentUrl.toString() + "books").
                then().
                statusCode(400);

    }

    @Test
    @RunAsClient
    public void findBookByKeywordsTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CatalogServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        given().
                accept("application/xml").
                parameter("keywords", "java ee").
                get(deploymentUrl.toString() + "books").
                then().log().all().
                statusCode(200).
                // TODO
                // header(CONTENT_LENGTH, notNullValue()).
                        body(RestAssuredMatchers.matchesXsd(new File(catalogXsd))).
                body("bookInfoes.bookInfo.size()", Matchers.greaterThan(40)).
                extract().body();
    }
}
