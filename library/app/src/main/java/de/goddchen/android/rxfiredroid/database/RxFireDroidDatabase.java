package de.goddchen.android.rxfiredroid.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    public static Single<DatabaseReference> getRef(String pathFormat, Object... args) {
        return getRootRef()
                .map(databaseReference ->
                        databaseReference.child(
                                String.format(Locale.US, pathFormat, args)));
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

    public static Single<DataSnapshot> getValues(DatabaseReference ref) {
        return Single.<DataSnapshot>create(emitter ->
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<DataSnapshot> getValues(String pathFormat, Object... args) {
        return getValues(String.format(pathFormat, args));
    }

    public static Single<DataSnapshot> getValues(String ref) {
        return getRef(ref)
                .flatMap(RxFireDroidDatabase::getValues);
    }

    public static Single<DataSnapshot> getValues(Query query) {
        return Single.<DataSnapshot>create(emitter ->
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        emitter.onError(databaseError.toException());
                    }
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DataSnapshot> observeValues(DatabaseReference ref, boolean receiveInitialValues) {
        return Observable.<DataSnapshot>create(subscriber -> {
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
                    ref.removeEventListener(valueEventListener)));
            ref.addValueEventListener(valueEventListener);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DataSnapshot> observeValues(Query query, boolean receiveInitialValues) {
        return Observable.<DataSnapshot>create(subscriber -> {
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
                    query.removeEventListener(valueEventListener)));
            query.addValueEventListener(valueEventListener);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DataSnapshot> observeValues(Query query) {
        return observeValues(query, true);
    }

    public static Observable<DataSnapshot> observeValues(String pathFormat, Object... args) {
        return observeValues(String.format(pathFormat, args));
    }

    public static Observable<DataSnapshot> observeValues(String path) {
        return observeValues(path, true);
    }

    public static Observable<DataSnapshot> observeValues(String pathFormat, boolean receiveInitialValues, Object... args) {
        return observeValues(String.format(pathFormat, args), receiveInitialValues);
    }

    public static Observable<DataSnapshot> observeValues(String path, boolean receiveInitialValues) {
        return getRef(path)
                .flatMapObservable(databaseReference ->
                        observeValues(databaseReference, receiveInitialValues));
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

    public static Completable setValue(String pathFormat, Object value, Object... args) {
        return setValue(String.format(pathFormat, args), value);
    }

    public static Completable setValue(String ref, Object value) {
        return Observable.just(value)
                .toMap(toKey -> ref, toValue -> value)
                .flatMapCompletable(RxFireDroidDatabase::setValues);
    }

}
