import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseUser;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.goddchen.android.rxfiredroid.auth.RxFireDroidAuth;

/**
 * Created by goddc on 04.12.2016.
 */

@RunWith(AndroidJUnit4.class)
public class RxFireDroidAuthTest {

    @Test
    public void createSignInSignOutTest() {
        FirebaseUser createdUser = RxFireDroidAuth.createUser("goddchen+test@gmail.com", "password")
                .blockingGet();
        FirebaseUser signedInUser = RxFireDroidAuth.signIn("goddchen+test@gmail.com", "password")
                .blockingGet();
        FirebaseUser signedInUser2 = RxFireDroidAuth.getCurrentUser().blockingGet();
        Assert.assertEquals(createdUser.getUid(), signedInUser.getUid());
        Assert.assertEquals(signedInUser.getUid(), signedInUser2.getUid());
        RxFireDroidAuth.signOut().blockingAwait();
        FirebaseUser signedInUser3 = RxFireDroidAuth.getCurrentUser().blockingGet();
        Assert.assertNull(signedInUser3);
    }

}
