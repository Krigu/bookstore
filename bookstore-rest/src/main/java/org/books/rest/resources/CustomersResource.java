package org.books.rest.resources;

import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.rest.jaxb.Registration;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.*;

@Path("customers")
@RequestScoped
public class CustomersResource {

    @EJB
    private CustomerService customerService;

    @POST
    @Consumes(APPLICATION_XML)
    @Produces(TEXT_PLAIN)
//    @ValidateOnExecution
    public Response createCustomer(Registration registration) {
        System.out.println(registration);
        System.out.println("CustomerService: " + customerService + " is null " + customerService == null);

        try {
            CustomerDTO customer = registration.getCustomer();
            String password = registration.getPassword();
            CustomerDTO customerDTO = customerService.registerCustomer(customer, password);
            return Response.status(201).entity(customerDTO.getNumber()).build();
        } catch (CustomerAlreadyExistsException e) {
            return Response.status(400).entity("email address already used").build();
        }
    }

    @GET
    @Path("{number}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findCustomerByNumber(@PathParam("number") String number) {
        try {
            org.books.data.dto.CustomerDTO customer = customerService.findCustomer(number);
            return Response.ok().entity(customer).build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findCustomerByName(@QueryParam("name") String name) {
        if (name == null)
            return Response.status(400).entity("name missing").build();

        List<CustomerInfo> list = customerService.searchCustomers(name);
        final GenericEntity<List<CustomerInfo>> entity = new GenericEntity<List<CustomerInfo>>(list) {};
        return Response.ok().entity(entity).build();
    }
}
