package org.books.integration;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

@ArquillianSuiteDeployment
public class BookstoreArquillianTest extends Arquillian {

    @Deployment
    public static Archive<?> createDeployment() {

        final JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "bookstore-ejb.jar")
                .addPackages(true, "org.books.application")
                .addAsManifestResource("test-ejb-jar.xml", "ejb-jar.xml");

        final JavaArchive jpa = ShrinkWrap.create(JavaArchive.class, "bookstore-jpa.jar")
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addPackages(true, "org.books.data");

        EnterpriseArchive enterpriseArchive = ShrinkWrap.create(EnterpriseArchive.class)
                .addAsModule(ejb)
                .addAsModule(jpa);

        return enterpriseArchive;
    }

}
