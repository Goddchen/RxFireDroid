import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.goddchen.android.rxfiredroid.auth.RxFireDroidAuth;

/**
 * Created by goddc on 04.12.2016.
 */

@RunWith(AndroidJUnit4.class)
public class RxFireDroidAuthTest {

    @Test
    public void wrongPasswordTest() throws InterruptedException {
        RxFireDroidAuth.signIn("goddchen+test@gmail.com", "wrong")
                .test()
                .await()
                .assertFailure(FirebaseAuthInvalidCredentialsException.class);
    }

    @Test
    public void getCurrentUserTest() {
        FirebaseUser signedInUser = RxFireDroidAuth.signIn("goddchen+test@gmail.com", "password")
                .blockingGet();
        FirebaseUser signedInUser2 = RxFireDroidAuth.getCurrentUser().blockingGet();
        Assert.assertEquals(signedInUser.getUid(), signedInUser2.getUid());
    }

    @Test
    public void signOutTest() {
        RxFireDroidAuth.signOut().blockingAwait();
        FirebaseUser signedInUser3 = RxFireDroidAuth.getCurrentUser().blockingGet();
        Assert.assertNull(signedInUser3);
    }

}
