package de.goddchen.android.rxfiredroid.tests;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.goddchen.android.rxfiredroid.database.RxFireDroidDatabase;
import io.reactivex.functions.Function;

/**
 * Created by goddc on 04.12.2016.
 */

@RunWith(AndroidJUnit4.class)
public class RxFireDroidDatabaseTest {

    @Test
    public void setReadTest() {
        RxFireDroidDatabase.setValue("test-value", true)
                .blockingAwait();
        Assert.assertTrue(RxFireDroidDatabase.getValues("test-value").blockingGet()
                .getValue(Boolean.class));
    }

    @Test
    public void setDeleteTest() {
        RxFireDroidDatabase.setValue("test-value", true)
                .blockingAwait();
        RxFireDroidDatabase.deleteValues("test-value").blockingAwait();
        Assert.assertFalse(RxFireDroidDatabase.getValues("test-value").blockingGet().exists());
    }

    @Test
    public void pathFormatTest() {
        RxFireDroidDatabase.getRef("test/%s", "arg")
                .map(new Function<DatabaseReference, String>() {
                    @Override
                    public String apply(DatabaseReference databaseReference) throws Exception {
                        return databaseReference.getKey();
                    }
                })
                .test()
                .assertValue("arg");
    }
}
