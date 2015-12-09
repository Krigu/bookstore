package org.books.data.entity;

import org.junit.Assert;
import org.junit.Test;


public class ModellTest extends AbstractJpaTest {

    @Test
    public void testPersistenceManagerIsActive() {
        Assert.assertTrue(em.isOpen());
    }

}