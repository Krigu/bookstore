package org.books.rest.resources;

import org.books.application.CustomerService;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.data.dto.CustomerDTO;
import org.books.data.dto.CustomerInfo;
import org.books.rest.jaxb.Registration;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    @Produces(TEXT_PLAIN)
    public Response createCustomer(@Valid Registration registration) {
        try {
            CustomerDTO customer = registration.getCustomer();
            String password = registration.getPassword();
            CustomerDTO customerDTO = customerService.registerCustomer(customer, password);
            return Response.status(201).entity(customerDTO.getNumber()).build();
        } catch (CustomerAlreadyExistsException e) {
            return Response.status(409).entity("email address already used").build();
        }
    }

    @GET
    @Path("{number}")
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findCustomerByNumber(@PathParam("number") String number) {
        try {
            org.books.data.dto.CustomerDTO customer = customerService.findCustomer(number);
            //String s = Entity.json
            return Response.ok().entity(customer).build();
        } catch (CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        }
    }

    @GET
    @Produces({APPLICATION_XML, APPLICATION_JSON})
    public Response findCustomerByName(@NotNull @QueryParam("name") String name) {

        List<CustomerInfo> list = customerService.searchCustomers(name);
        final GenericEntity<List<CustomerInfo>> entity = new GenericEntity<List<CustomerInfo>>(list) {
        };
        return Response.ok().entity(entity).build();
    }


    @PUT
    @Path("{number}")
    @Consumes({APPLICATION_XML, APPLICATION_JSON})
    public Response updateCustomer(@PathParam("number") String number, @Valid CustomerDTO customer) {
        System.out.println("Number: " + number);
        try {
            customer.setNumber(number);
            customerService.updateCustomer(customer);
            System.out.println("customer updated: " + customer.getLastName());
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (CustomerNotFoundException e) {
            System.out.println("CustomerNotFoundException!!");
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        } catch (CustomerAlreadyExistsException e) {
            System.out.println("CustomerAlreadyExistsException!!");
            return Response.status(Response.Status.CONFLICT).entity("new email already used").build();
        }
    }
}
