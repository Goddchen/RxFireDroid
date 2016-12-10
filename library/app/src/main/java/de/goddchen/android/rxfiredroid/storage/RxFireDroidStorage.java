package de.goddchen.android.rxfiredroid.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Goddchen on 10.12.2016.
 */

@SuppressWarnings("unused")
public class RxFireDroidStorage {

    public static Single<StorageReference> getReference() {
        return Single.fromCallable(() -> FirebaseStorage.getInstance().getReference())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<StorageReference> getReference(String location) {
        return Single.fromCallable(() -> FirebaseStorage.getInstance().getReference(location))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<StorageReference> getReferenceFromUrl(String url) {
        return Single.fromCallable(() -> FirebaseStorage.getInstance().getReferenceFromUrl(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<StorageReference> child(StorageReference ref, String path) {
        return Single.fromCallable(() -> ref.child(path))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable delete(StorageReference ref) {
        return Completable.create(emitter ->
                ref.delete()
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getBucket(StorageReference ref) {
        return Single.fromCallable(ref::getBucket)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<byte[]> getBytes(StorageReference ref, long maxDownloadSizeBytes) {
        return Single.<byte[]>create(emitter ->
                ref.getBytes(maxDownloadSizeBytes)
                        .addOnCompleteListener(task -> emitter.onSuccess(task.getResult()))
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Uri> getDownloadUrl(StorageReference ref) {
        return Single.<Uri>create(emitter ->
                ref.getDownloadUrl()
                        .addOnCompleteListener(task -> emitter.onSuccess(task.getResult()))
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable getFile(StorageReference ref, File outputFile) {
        return Completable.create(emitter ->
                ref.getFile(outputFile)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable getFile(StorageReference ref, Uri destinationUri) {
        return Completable.create(emitter ->
                ref.getFile(destinationUri)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<StorageMetadata> getMetadata(StorageReference ref) {
        return Single.<StorageMetadata>create(emitter ->
                ref.getMetadata()
                        .addOnCompleteListener(task -> emitter.onSuccess(task.getResult()))
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getName(StorageReference ref) {
        return Single.just(ref.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Maybe<StorageReference> getParent(StorageReference ref) {
        return Maybe.fromCallable(ref::getParent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getPath(StorageReference ref) {
        return Single.just(ref.getPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<StorageReference> getRoot(StorageReference ref) {
        return Single.just(ref.getRoot())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<FirebaseStorage> getStorage(StorageReference ref) {
        return Single.just(ref.getStorage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable putBytes(StorageReference ref, byte[] bytes) {
        return Completable.create(emitter ->
                ref.putBytes(bytes)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable putBytes(StorageReference ref, byte[] bytes, StorageMetadata metadata) {
        return Completable.create(emitter ->
                ref.putBytes(bytes, metadata)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable putFile(StorageReference ref, Uri uri) {
        return Completable.create(emitter ->
                ref.putFile(uri)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Completable putFile(StorageReference ref, Uri uri, StorageMetadata metadata) {
        return Completable.create(emitter ->
                ref.putFile(uri, metadata)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable putFile(StorageReference ref, Uri uri, StorageMetadata metadata, Uri existingUploadUri) {
        return Completable.create(emitter ->
                ref.putFile(uri, metadata, existingUploadUri)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable putStream(StorageReference ref, InputStream stream) {
        return Completable.create(emitter ->
                ref.putStream(stream)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable putStream(StorageReference ref, InputStream stream, StorageMetadata metadata) {
        return Completable.create(emitter ->
                ref.putStream(stream, metadata)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable updateMetadata(StorageReference ref, StorageMetadata metadata) {
        return Completable.create(emitter ->
                ref.updateMetadata(metadata)
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
