package org.books.rest.resources;

import org.books.application.CustomerService;
import org.books.application.exception.CustomerNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

@Path("customers")
public class CustomersResource {


    @EJB
    private CustomerService customerService;

    @GET
    @Path("{number}")
    public Response findCustomerByNumber(@PathParam("number") String number) {
        try {
            org.books.data.dto.CustomerDTO customer = customerService.findCustomer(number);
            return Response.ok().entity(Entity.xml(customer)).build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
