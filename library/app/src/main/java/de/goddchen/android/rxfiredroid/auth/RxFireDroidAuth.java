package de.goddchen.android.rxfiredroid.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by goddc on 03.12.2016.
 */

@SuppressWarnings("unused")
public class RxFireDroidAuth {

    public static Single<FirebaseUser> signIn(String email, String password) {
        return Single.<FirebaseUser>create(emitter ->
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                emitter.onError(task.getException());
                            } else {
                                emitter.onSuccess(task.getResult().getUser());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseUser> signIn() {
        return Single.<FirebaseUser>create(emitter ->
                FirebaseAuth.getInstance().signInAnonymously()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                emitter.onError(task.getException());
                            } else {
                                emitter.onSuccess(task.getResult().getUser());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseUser> signIn(AuthCredential authCredential) {
        return Single.<FirebaseUser>create(emitter ->
                FirebaseAuth.getInstance().signInWithCredential(authCredential)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                emitter.onError(task.getException());
                            } else {
                                emitter.onSuccess(task.getResult().getUser());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseUser> signIn(String customToken) {
        return Single.<FirebaseUser>create(emitter ->
                FirebaseAuth.getInstance().signInWithCustomToken(customToken)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                emitter.onError(task.getException());
                            } else {
                                emitter.onSuccess(task.getResult().getUser());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable signOut() {
        return Completable.fromAction(() -> FirebaseAuth.getInstance().signOut())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Maybe<FirebaseUser> getCurrentUser() {
        return Maybe.fromCallable(() -> FirebaseAuth.getInstance().getCurrentUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseUser> createUser(String email, String password) {
        return Single.<FirebaseUser>create(emitter ->
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                emitter.onError(task.getException());
                            } else {
                                emitter.onSuccess(task.getResult().getUser());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable confirmPasswordReset(String code, String newPassword) {
        return Completable.create(emitter ->
                FirebaseAuth.getInstance().confirmPasswordReset(code, newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onComplete();
                            } else {
                                emitter.onError(task.getException());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable sendPasswordResetEmail(String email) {
        return Completable.create(emitter ->
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onComplete();
                            } else {
                                emitter.onError(task.getException());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> verifyPasswordResetCode(String code) {
        return Single.<String>create(emitter ->
                FirebaseAuth.getInstance().verifyPasswordResetCode(code)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onSuccess(task.getResult());
                            } else {
                                emitter.onError(task.getException());
                            }
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<FirebaseAuth> observeAuthStateChanges() {
        return Observable.<FirebaseAuth>create(emitter -> {
            FirebaseAuth.AuthStateListener authStateListener = emitter::onNext;
            FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
            emitter.setDisposable(Disposables.fromAction(() ->
                    FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)));
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable deleteUser(FirebaseUser user) {
        return Completable.create(emitter -> user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        emitter.onComplete();
                    } else {
                        emitter.onError(task
                                .getException());
                    }
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
