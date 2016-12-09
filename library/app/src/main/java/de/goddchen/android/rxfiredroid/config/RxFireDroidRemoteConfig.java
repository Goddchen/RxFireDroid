package de.goddchen.android.rxfiredroid.config;

import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Goddchen on 09.12.2016.
 */

@SuppressWarnings("unused")
public class RxFireDroidRemoteConfig {

    public static Completable setDefaults(@XmlRes int xmlRes) {
        return Completable.fromAction(() -> FirebaseRemoteConfig.getInstance().setDefaults(xmlRes))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable setDefaults(@XmlRes int xmlRes, @NonNull String namespace) {
        return Completable.fromAction(() ->
                FirebaseRemoteConfig.getInstance().setDefaults(xmlRes, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable setDefault(@NonNull Map<String, Object> values) {
        return Completable.fromAction(() -> FirebaseRemoteConfig.getInstance().setDefaults(values))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable setDefault(@NonNull Map<String, Object> values, @NonNull String namespace) {
        return Completable.fromAction(() ->
                FirebaseRemoteConfig.getInstance().setDefaults(values, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Boolean> getBoolean(@NonNull String key) {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getBoolean(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Boolean> getBoolean(@NonNull String key, @NonNull String namespace) {
        return Single.fromCallable(() ->
                FirebaseRemoteConfig.getInstance().getBoolean(key, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<byte[]> getByteArray(@NonNull String key) {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getByteArray(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<byte[]> getByteArray(@NonNull String key, @NonNull String namespace) {
        return Single.fromCallable(() ->
                FirebaseRemoteConfig.getInstance().getByteArray(key, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Double> getDouble(@NonNull String key) {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getDouble(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Double> getDouble(@NonNull String key, @NonNull String namespace) {
        return Single.fromCallable(() ->
                FirebaseRemoteConfig.getInstance().getDouble(key, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseRemoteConfigInfo> getRemoteConfigInfo() {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<String> getKeysByPrefix(@NonNull String prefix) {
        return Observable.fromIterable(() ->
                FirebaseRemoteConfig.getInstance().getKeysByPrefix(prefix).iterator())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Long> getLong(@NonNull String key) {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getLong(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Long> getLong(@NonNull String key, @NonNull String namespace) {
        return Single.fromCallable(() ->
                FirebaseRemoteConfig.getInstance().getLong(key, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getString(@NonNull String key) {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getString(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getString(@NonNull String key, @NonNull String namespace) {
        return Single.fromCallable(() ->
                FirebaseRemoteConfig.getInstance().getString(key, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable setConfigSettings(@NonNull FirebaseRemoteConfigSettings settings) {
        return Completable.fromAction(() ->
                FirebaseRemoteConfig.getInstance().setConfigSettings(settings))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseRemoteConfigValue> getValue(@NonNull String key) {
        return Single.fromCallable(() -> FirebaseRemoteConfig.getInstance().getValue(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseRemoteConfigValue> getValue(@NonNull String key, @NonNull String namespace) {
        return Single.fromCallable(() ->
                FirebaseRemoteConfig.getInstance().getValue(key, namespace))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
