package de.goddchen.android.rxfiredroid.database;

import com.google.firebase.database.ChildEventListener;
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

    public static Single<DataSnapshot> getValues(DatabaseReference ref) {
        return getValues((Query) ref);
    }

    public static Single<DataSnapshot> getValues(String pathFormat, String... args) {
        return getValues(String.format(pathFormat, (Object[]) args));
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
        return observeValues((Query) ref, receiveInitialValues);
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

    public static Observable<DataSnapshot> observeValues(String pathFormat, String... args) {
        return observeValues(String.format(pathFormat, (Object[]) args));
    }

    public static Observable<DataSnapshot> observeValues(String path) {
        return observeValues(path, true);
    }

    public static Observable<DataSnapshot> observeValues(String pathFormat, boolean receiveInitialValues, String... args) {
        return observeValues(String.format(pathFormat, (Object[]) args), receiveInitialValues);
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

    public static Completable setValue(String pathFormat, Object value, String... args) {
        return setValue(String.format(pathFormat, (Object[]) args), value);
    }

    public static Completable setValue(String ref, Object value) {
        return Observable.just(value)
                .toMap(toKey -> ref, toValue -> value)
                .flatMapCompletable(RxFireDroidDatabase::setValues);
    }

    public static Observable<ChildEvent> observeChildren(String pathFormat, String... args) {
        return observeChildren(String.format(pathFormat, (Object[]) args));
    }

    public static Observable<ChildEvent> observeChildren(String path) {
        return getRef(path)
                .flatMapObservable(RxFireDroidDatabase::observeChildren);
    }

    public static Observable<ChildEvent> observeChildren(DatabaseReference databaseReference) {
        return observeChildren((Query) databaseReference);
    }

    public static Observable<ChildEvent> observeChildren(Query query) {
        return Observable.<ChildEvent>create(emitter -> {
            final ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    emitter.onNext(new ChildEvent(ChildEvent.TYPE.ADDED, dataSnapshot, s));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    emitter.onNext(new ChildEvent(ChildEvent.TYPE.CHANGED, dataSnapshot, s));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    emitter.onNext(new ChildEvent(ChildEvent.TYPE.REMOVED, dataSnapshot, null));
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    emitter.onNext(new ChildEvent(ChildEvent.TYPE.MOVED, dataSnapshot, s));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    emitter.onError(databaseError.toException());
                }
            };
            emitter.setDisposable(Disposables.fromAction(() ->
                    query.removeEventListener(childEventListener)));
            query.addChildEventListener(childEventListener);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static class ChildEvent {
        public TYPE type;
        public DataSnapshot dataSnapshot;
        public String previousChildKey;

        public ChildEvent(TYPE type, DataSnapshot dataSnapshot, String previousChildKey) {
            this.type = type;
            this.dataSnapshot = dataSnapshot;
            this.previousChildKey = previousChildKey;
        }

        public enum TYPE {
            ADDED,
            CHANGED,
            REMOVED,
            MOVED
        }
    }

}
