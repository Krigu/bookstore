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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@ArquillianSuiteDeployment
public class BookstoreArquillianTest extends Arquillian {

    protected static final String CONTENT_LENGTH = "Content-Length";

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

    protected static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);

    }


}
