# RxFireDroid
Rx wrapper for the Firebase Android library. 
This is the first one that works with the RxJava 2!

## Usage
    dependencies {
        compile 'de.goddchen.android:rxfiredroid:0.1'
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
            
## License
MIT
