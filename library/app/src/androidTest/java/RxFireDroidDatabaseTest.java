import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.goddchen.android.rxfiredroid.database.RxFireDroidDatabase;

/**
 * Created by goddc on 04.12.2016.
 */

@RunWith(AndroidJUnit4.class)
public class RxFireDroidDatabaseTest {

    @Test
    public void setReadDeleteTest() {
        RxFireDroidDatabase.setValue("test-value", true)
                .blockingAwait();
        Assert.assertTrue(RxFireDroidDatabase.getValues("test-value").blockingGet()
                .getValue(Boolean.class));
        RxFireDroidDatabase.deleteValues("test-value").blockingAwait();
        Assert.assertFalse(RxFireDroidDatabase.getValues("test-value").blockingGet().exists());
    }
}
