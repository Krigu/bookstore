package org.books.data.entity;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public abstract class AbstractJpaTest {

    protected static final Logger LOGGER = Logger.getLogger(AbstractJpaTest.class.getName());
    protected static EntityManagerFactory emf;
    protected static EntityManager em;
    private static Connection con;
    private static DatabaseConnection dbConnection;
    private static FlatXmlDataSet dataSet;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("bookstore");
        em = emf.createEntityManager();

        con = DriverManager.getConnection("jdbc:derby:memory:test;create=true", "app", "app");
        dbConnection = new DatabaseConnection(con);

        InputStream strm = AbstractJpaTest.class.getClassLoader().getResourceAsStream("bookstore_data.xml");
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        dataSet = builder.build(strm);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }



    @Before
    public void insertTestData() throws Exception {
        em.clear();
        emf.getCache().evictAll();

        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, dataSet);
    }

    @After
    public void deleteTestData() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(dbConnection, dataSet);
    }
}
