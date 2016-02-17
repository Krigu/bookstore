
package org.books.rest.resources;

import java.util.List;
import java.util.logging.Logger;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
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
    @Produces({APPLICATION_XML})
    public Response findBookByISBN(@PathParam("isbn") String isbn){
        try {
            BookDTO book = catalogService.findBook(isbn);
            return Response.ok().entity(Entity.xml(book)).build();
        } catch (BookNotFoundException ex) {
            LOGGER.info("Catalog service : book not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    @GET
    @Produces({APPLICATION_XML})
    public Response searchBooksByKeywords(@QueryParam("keywords") String keywords){
        //No keywords
        if (keywords == null || keywords.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<BookInfo> books =catalogService.searchBooks(keywords.replace("%20", " "));
        return Response.ok().entity(Entity.xml(books)).build();
    }
}
