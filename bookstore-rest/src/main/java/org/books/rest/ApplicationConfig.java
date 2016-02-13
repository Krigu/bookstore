package org.books.rest;

import org.books.rest.resources.CustomersResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		classes.add(CustomersResource.class);
		classes.add(GZIPWriterInterceptor.class);
		return classes;
	}
}
