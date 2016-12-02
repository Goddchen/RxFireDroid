package de.goddchen.android.rxfiredroid.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Goddchen on 09.12.2015.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RxFireDroidDatabase {

    public static DatabaseReference getRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getRef(String path) {
        return getRootRef().child(path.startsWith("/") ? path.substring(1) : path);
    }

    public static DatabaseReference getRef(String pathFormat, String... args) {
        return getRootRef().child(String.format(Locale.US, pathFormat, (Object[]) args));
    }

    public static String escapeKey(String key) {
        return key
                .replaceAll("\\.", ",")
                .replaceAll("\\+", ",");
    }

    public static String toLowerCase(String key) {
        if (key == null) {
            return null;
        } else {
            return key.toLowerCase(Locale.US);
        }
    }

    public static Observable<DataSnapshot> getValues(String ref) {
        return Observable.<DataSnapshot>create(subscriber ->
                RxFireDroidDatabase.getRef(ref)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!subscriber.isDisposed()) {
                                    subscriber.onNext(dataSnapshot);
                                }
                                subscriber.onComplete();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }))
                .subscribeOn(Schedulers.io());
    }

    public static Observable<DataSnapshot> observeValues(String path) {
        return observeValues(path, true);
    }

    public static Observable<DataSnapshot> observeValues(String path, boolean receiveInitialValues) {
        return Observable.<DataSnapshot>create(subscriber -> {
            boolean[] initial = new boolean[]{true};
            DatabaseReference ref = RxFireDroidDatabase.getRef(path);
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
                    ref.removeEventListener(valueEventListener)));
            ref.addValueEventListener(valueEventListener);
        })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Object> setValues(Map<String, Object> values) {
        return Observable.create(subscriber -> {
            RxFireDroidDatabase.getRootRef().updateChildren(values);
            subscriber.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Object> deleteValues(String... paths) {
        return Observable.fromArray(paths)
                .toMap(s -> s, s -> null)
                .flatMapObservable(RxFireDroidDatabase::setValues);
    }

    public static Observable<Object> setValue(String ref, Object value) {
        return Observable.just(value)
                .toMap(toKey -> ref, toValue -> value)
                .flatMapObservable(RxFireDroidDatabase::setValues);
    }

}
