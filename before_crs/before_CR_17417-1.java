/*Make sure unmatched autenticator/account-types doesn't crash Contacts.

If the Contacts-app didn't find a valid authenticator for a specific
account-type, it would previously throw an exception and crash.
Now the account-type is silently ignored.

Installing an app with unmatching authenticator/accounts like, e.g:

<account-authenticator
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:accountType="com.abc.def"
    android:icon="@drawable/icon"
    android:smallIcon="@drawable/icon"
    android:label="@string/label"
/>

<sync-adapter
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:contentAuthority="com.android.contacts"
    android:accountType="com.abc.ghi"
    android:supportsUploading="true"
/>

would otherwise crash Contacts.

Change-Id:Idefa3266e1624246de97d1eae6612747b52f8c60*/
//Synthetic comment -- diff --git a/src/com/android/contacts/model/Sources.java b/src/com/android/contacts/model/Sources.java
//Synthetic comment -- index be3f17d..68e7754 100644

//Synthetic comment -- @@ -203,6 +203,10 @@
// adapter, using the authenticator to find general resources.
final String accountType = sync.accountType;
final AuthenticatorDescription auth = findAuthenticator(auths, accountType);

ContactsSource source;
if (GoogleSource.ACCOUNT_TYPE.equals(accountType)) {
//Synthetic comment -- @@ -239,7 +243,7 @@
return auth;
}
}
        throw new IllegalStateException("Couldn't find authenticator for specific account type");
}

/**







