package org.books;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

@ArquillianSuiteDeployment
public class BookstoreArquillianTest extends Arquillian {

    @Deployment
    public static Archive<?> createEjbDeployment() {

        EnterpriseArchive enterpriseArchive = ShrinkWrap.create(EnterpriseArchive.class)
                .addAsModule(getJpaArchive())
                .addAsModule(getEjbArchive())
                .addAsModule(getRestWebArchive());

        return enterpriseArchive;
    }

    private static WebArchive getRestWebArchive() {

        File files = Maven.resolver().resolve("ch.bfh.eadj:bookstore-rest:war:0.1-SNAPSHOT").withTransitivity().asSingleFile();
        WebArchive webArchive = ShrinkWrap.create(ZipImporter.class, "bookstore-rest.war").importFrom(files).as(WebArchive.class);

        return webArchive;
    }

    private static JavaArchive getEjbArchive() {
        return ShrinkWrap.create(JavaArchive.class, "bookstore-ejb.jar")
                .addPackages(true, "org.books.application")
                .addAsManifestResource("test-ejb-jar.xml", "ejb-jar.xml");
    }

    private static JavaArchive getJpaArchive() {
        return ShrinkWrap.create(JavaArchive.class, "bookstore-jpa.jar")
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addPackages(true, "org.books.data");
    }

}
