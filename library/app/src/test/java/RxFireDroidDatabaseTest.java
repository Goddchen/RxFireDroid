import junit.framework.Assert;

import org.junit.Test;

import de.goddchen.android.rxfiredroid.database.RxFireDroidDatabase;

/**
 * Created by goddc on 04.12.2016.
 */

public class RxFireDroidDatabaseTest {

    @Test
    public void toLowerCaseTest() {
        Assert.assertEquals("test", RxFireDroidDatabase.toLowerCase("Test"));
        Assert.assertEquals(" test", RxFireDroidDatabase.toLowerCase(" Test"));
    }

}
