package de.goddchen.android.rxfiredroid.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Goddchen on 09.12.2015.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RxFireDroidDatabase {

    public static Single<DatabaseReference> getRootRef() {
        return Single.just(FirebaseDatabase.getInstance().getReference());
    }

    public static Single<DatabaseReference> getRef(String path) {
        return getRootRef()
                .map(databaseReference ->
                        databaseReference.child(path.startsWith("/") ?
                                path.substring(1) : path));
    }

    public static Single<DatabaseReference> getRef(String pathFormat, String... args) {
        return getRootRef()
                .map(databaseReference ->
                        databaseReference.child(
                                String.format(Locale.US, pathFormat, (Object[]) args)));
    }

    public static Maybe<String> escapeKey(String key) {
        if (key == null) {
            return Maybe.empty();
        } else {
            return Maybe.just(key
                    .replaceAll("\\.", ",")
                    .replaceAll("\\+", ","));
        }
    }

    public static Maybe<String> toLowerCase(String key) {
        if (key == null) {
            return Maybe.empty();
        } else {
            return Maybe.just(key.toLowerCase(Locale.US));
        }
    }

    public static Single<DataSnapshot> getValues(String ref) {
        return getRef(ref)
                .flatMap(databaseReference ->
                        Single.<DataSnapshot>create(subscriber ->
                                databaseReference
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (!subscriber.isDisposed()) {
                                                    subscriber.onSuccess(dataSnapshot);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                subscriber.onError(databaseError.toException());
                                            }
                                        })))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DataSnapshot> observeValues(String path) {
        return observeValues(path, true);
    }

    public static Observable<DataSnapshot> observeValues(String path, boolean receiveInitialValues) {
        return getRef(path)
                .flatMapObservable(databaseReference ->
                        Observable.<DataSnapshot>create(subscriber -> {
                            boolean[] initial = new boolean[]{true};
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (initial[0]) {
                                        initial[0] = false;
                                        if (receiveInitialValues) {
                                            subscriber.onNext(dataSnapshot);
                                        }
                                    } else {
                                        subscriber.onNext(dataSnapshot);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    subscriber.onError(databaseError.toException());
                                }
                            };
                            subscriber.setDisposable(Disposables.fromAction(() ->
                                    databaseReference.removeEventListener(valueEventListener)));
                            databaseReference.addValueEventListener(valueEventListener);
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable setValues(Map<String, Object> values) {
        return getRootRef()
                .flatMapCompletable(databaseReference ->
                        Completable.fromAction(() -> databaseReference.updateChildren(values)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable deleteValues(String... paths) {
        return Observable.fromArray(paths)
                .toMap(s -> s, s -> null)
                .flatMapCompletable(RxFireDroidDatabase::setValues);
    }

    public static Completable setValue(String ref, Object value) {
        return Observable.just(value)
                .toMap(toKey -> ref, toValue -> value)
                .flatMapCompletable(RxFireDroidDatabase::setValues);
    }

}
