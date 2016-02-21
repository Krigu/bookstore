
package org.books.rest.resources;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.books.application.CatalogService;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

@Path("books")
@RequestScoped
public class CatalogResource {

    private static final Logger LOGGER = Logger.getLogger(OrdersResource.class.getName());
    @EJB
    private CatalogService catalogService;

    @GET
    @Path("{isbn}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findBookByISBN(@PathParam("isbn") String isbn) {
        try {
            BookDTO book = catalogService.findBook(isbn);
            Response build = Response.ok().entity(book).build();
            return build;
        } catch (BookNotFoundException ex) {
            LOGGER.info("Catalog service : book not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ERROR getting book", e);
            return Response.status(500).build();
        }
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response searchBooksByKeywords(@QueryParam("keywords") String keywords) {
        //No keywords
        if (keywords == null || keywords.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<BookInfo> books = catalogService.searchBooks(keywords.replace("%20", " "));
        
        final GenericEntity<List<BookInfo>> entity = new GenericEntity<List<BookInfo>>(books) {};
        
        return Response.ok().entity(entity).build();
    }
}
