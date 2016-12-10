# RxFireDroid
Rx wrapper for the Firebase Android library. 

This is the first one that works with the RxJava 2!

[![Build Status](https://travis-ci.org/Goddchen/RxFireDroid.svg?branch=master)](https://travis-ci.org/Goddchen/RxFireDroid)

## Usage
    dependencies {
        compile 'de.goddchen.android:rxfiredroid:0.2'
    }
    
## Samples
### Database
#### Single Read
    RxFireDroidDatabase.getValues("users")
        .subscribe(dataSnapshot -> { /* do something... */});
#### Observe
    RxFireDroidDatabase.observeValues("users")
        .subscribe(dataSnapshot -> { /* do something... */});
#### Delete
    RxFireDroidDatabase.deleteValues("user/123")
        .subscribe();
#### Set
    RxFireDroidDatabase.setValue("user/123/name", "Goddchen")
        .subscribe();
### Auth
#### Login (email, password)
    RxFireDroidAuth.signIn("test@test.com", "password123")
        .subscribe(
            firebaseUser -> { /* do something... */},
            throwable -> { /* handle error */});
### Remote Config
#### Read
    RxFireDroidRemoteConfig.getBoolean("awesome-feature")
        .subscribe(
            enabled -> { /* enable awesome feature */ },
            throwable -> { /* Log error */ });
### Storage
#### Read
    RxFireDroidStorage.getFile(ref, outFile)
        .doOnError(throwable -> { /* Log error */ })
        .subscribe();

    RxFireDroidStorage.getDownloadUrl(ref)
        .subscribe(
            uri -> { /* Handle download url */ },
            throwable -> { /* Log error */});
#### Write
    RxFireDroidStorage.putBytes(ref, bytes)
        .doOnError(throwable -> { /* Log error */ })
        .subscribe();

## License
MIT
