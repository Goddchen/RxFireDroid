package de.goddchen.android.rxfiredroid.tests;

import junit.framework.Assert;

import org.junit.Test;

import de.goddchen.android.rxfiredroid.database.RxFireDroidDatabase;

/**
 * Created by goddc on 04.12.2016.
 */

public class RxFireDroidDatabaseTest {

    @Test
    public void toLowerCaseTest() {
        Assert.assertEquals("test", RxFireDroidDatabase.toLowerCase("Test").blockingGet());
        Assert.assertEquals(" test", RxFireDroidDatabase.toLowerCase(" Test").blockingGet());
        Assert.assertEquals("", RxFireDroidDatabase.toLowerCase("").blockingGet());
        Assert.assertEquals(" ", RxFireDroidDatabase.toLowerCase(" ").blockingGet());
        Assert.assertEquals("test", RxFireDroidDatabase.toLowerCase("test").blockingGet());
        Assert.assertEquals("test12", RxFireDroidDatabase.toLowerCase("test12").blockingGet());
        Assert.assertEquals("test.,-", RxFireDroidDatabase.toLowerCase("test.,-").blockingGet());
        Assert.assertEquals(null, RxFireDroidDatabase.toLowerCase(null).blockingGet());
    }

    @Test
    public void escapeKeyTest() {
        Assert.assertEquals("test", RxFireDroidDatabase.escapeKey("test").blockingGet());
        Assert.assertEquals("test,", RxFireDroidDatabase.escapeKey("test.").blockingGet());
        Assert.assertEquals("test,test", RxFireDroidDatabase.escapeKey("test.test").blockingGet());
        Assert.assertEquals("test,test", RxFireDroidDatabase.escapeKey("test+test").blockingGet());
        Assert.assertEquals(null, RxFireDroidDatabase.escapeKey(null).blockingGet());
    }

}
