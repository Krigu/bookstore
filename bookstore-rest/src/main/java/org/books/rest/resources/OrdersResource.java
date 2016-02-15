package org.books.rest.resources;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import javax.ws.rs.core.Response;
import org.books.application.OrderService;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;

@Path("orders")
public class OrdersResource {

    private static final Logger LOGGER = Logger.getLogger(OrdersResource.class.getName());
    @EJB
    private OrderService orderService;

    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public Response placeOrder(String customerNr, List<OrderItemDTO> items) {
        //No item ?
        if (items == null || items.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            OrderDTO order = orderService.placeOrder(customerNr, items);
            return Response.status(Response.Status.CREATED).entity(Entity.xml(order)).build();
        } catch (CustomerNotFoundException | BookNotFoundException ex) {
            LOGGER.info("Rest service : customer or book not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (PaymentFailedException ex) {
            LOGGER.info("Rest service : customer or book not found");
            return Response.status(Response.Status.PAYMENT_REQUIRED).build();
        }
    }

    @GET
    @Path("{number}")
    public Response findOrderByNumber(@PathParam("number") String number) {
        System.out.println("org.books.rest.resources.OrdersResource.findOrderByNumber()");
        try {
            OrderDTO order = orderService.findOrder(number);
            return Response.ok().entity(Entity.xml(order)).build();
        } catch (OrderNotFoundException ex) {
            LOGGER.info("Rest service : order not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response searchOrdersOfCustomer(@QueryParam("customerNr") String number, @QueryParam("year") int year) {
        if (number == null || number.isEmpty() || year == 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            List<OrderInfo> orders = orderService.searchOrders(number, year);
            return Response.ok().entity(Entity.xml(orders)).build();
        } catch (CustomerNotFoundException ex) {
            LOGGER.info("Rest service : customer not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{number}")
    public void cancelOrder(@PathParam("number") String number) {
        if (number == null || number.isEmpty()) {
            throw new WebApplicationException(Response.status(Response.Status.NO_CONTENT).build());
        }

        try {
            orderService.cancelOrder(number);
        } catch (OrderNotFoundException ex) {
            LOGGER.info("Rest service : order not found");
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        } catch (OrderAlreadyShippedException ex) {
            LOGGER.info("Rest service : order not cancelable");
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
        }
    }
}
