package de.goddchen.android.rxfiredroid.tests;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.goddchen.android.rxfiredroid.config.RxFireDroidRemoteConfig;
import io.reactivex.functions.Predicate;

/**
 * Created by goddc on 04.12.2016.
 */

@RunWith(AndroidJUnit4.class)
public class RxFireDroidRemoteConfigTest {

    @Test
    public void staticDefaultsTest() throws InterruptedException {
        RxFireDroidRemoteConfig.getBoolean("static-default")
                .test()
                .await()
                .assertValue(false);
        RxFireDroidRemoteConfig.getString("static-default")
                .test()
                .await()
                .assertValue("");
        RxFireDroidRemoteConfig.getByteArray("static-default")
                .test()
                .await()
                .assertValue(new Predicate<byte[]>() {
                    @Override
                    public boolean test(byte[] bytes) throws Exception {
                        return bytes.length == 0;
                    }
                });
        RxFireDroidRemoteConfig.getDouble("static-default")
                .test()
                .await()
                .assertValue(0D);
        RxFireDroidRemoteConfig.getLong("static-default")
                .test()
                .await()
                .assertValue(0L);
    }
}
