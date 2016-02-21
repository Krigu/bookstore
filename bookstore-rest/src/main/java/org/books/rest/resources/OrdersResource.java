package org.books.rest.resources;

import org.books.application.OrderService;
import org.books.application.exception.*;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderRequest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Path("orders")
@RequestScoped
public class OrdersResource {

    private static final Logger LOGGER = Logger.getLogger(OrdersResource.class.getName());
    @EJB
    private OrderService orderService;

    @POST
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response placeOrder(@Valid OrderRequest orderRequest) {

        //No item ?
        if (orderRequest == null || orderRequest.getItems().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            OrderDTO order = orderService.placeOrder(orderRequest.getCustomerNr(), orderRequest.getItems());
            return Response.status(Response.Status.CREATED).entity(order).build();
        } catch (CustomerNotFoundException | BookNotFoundException ex) {
            LOGGER.info("Rest service : customer or book not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (PaymentFailedException ex) {
            LOGGER.info("Rest service : payment failed");
            return Response.status(Response.Status.PAYMENT_REQUIRED).build();
        }
    }

    @GET
    @Path("{number}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findOrderByNumber(@PathParam("number") String number) {
        LOGGER.info("org.books.rest.resources.OrdersResource.findOrderByNumber()");
        try {
            OrderDTO order = orderService.findOrder(number);
            return Response.ok().entity(order).build();
        } catch (OrderNotFoundException ex) {
            LOGGER.info("Rest service : order not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response searchOrdersOfCustomer(@QueryParam("customerNr") String number, @QueryParam("year") int year) {
        if (number == null || number.isEmpty() || year == 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            List<OrderInfo> orders = orderService.searchOrders(number, year);
            final GenericEntity<List<OrderInfo>> entity = new GenericEntity<List<OrderInfo>>(orders) {
            };
            return Response.ok().entity(entity).build();
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
