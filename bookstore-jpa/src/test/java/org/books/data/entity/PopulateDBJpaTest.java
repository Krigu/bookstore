package org.books.data.entity;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public abstract class PopulateDBJpaTest extends BasisJpaTest {

    protected static final Logger LOGGER = Logger.getLogger(PopulateDBJpaTest.class.getName());

    private static Connection con;
    private static DatabaseConnection dbConnection;
    private static FlatXmlDataSet dataSet;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BasisJpaTest.setUpBeforeClass();
        con = DriverManager.getConnection("jdbc:derby:memory:test;create=true", "app", "app");
        dbConnection = new DatabaseConnection(con);

        InputStream strm = PopulateDBJpaTest.class.getClassLoader().getResourceAsStream("bookstore_data.xml");
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        dataSet = builder.build(strm);

    }

    @BeforeMethod
    public void insertTestData() throws Exception {
        em.clear();
        emf.getCache().evictAll();

        DatabaseOperation.CLEAN_INSERT.execute(dbConnection, dataSet);
    }

    @AfterMethod
    public void deleteTestData() throws Exception {
        rollback();
        DatabaseOperation.DELETE_ALL.execute(dbConnection, dataSet);
    }
}
