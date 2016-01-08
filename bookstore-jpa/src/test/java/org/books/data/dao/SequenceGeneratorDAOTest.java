package org.books.data.dao;

import org.books.data.BasisJpaTest;
import org.books.data.util.AnnotationInjector;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.fail;


public class SequenceGeneratorDAOTest extends BasisJpaTest {

    private SequenceGeneratorDAO sg;

    @BeforeMethod
    public void before() {
        sg = new SequenceGeneratorDAO();
        try {
            AnnotationInjector.injectPersistenceContext(sg, em);
        } catch (IllegalAccessException e) {
            fail();
        }

    }

    @Test
    public void testGetNextValue() throws Exception {
        transaction.begin();
        Assert.assertEquals( sg.getNextValue("test_sequence"), (Long) 1L);
        Assert.assertEquals( sg.getNextValue("test_sequence"), (Long) 2L);
        Assert.assertEquals( sg.getNextValue("test_sequence"), (Long) 3L);

        Assert.assertEquals( sg.getNextValue("test_sequence1"), (Long) 1L);

        Assert.assertEquals( sg.getNextValue("test_sequence"), (Long) 4L);
        transaction.commit();
    }
}