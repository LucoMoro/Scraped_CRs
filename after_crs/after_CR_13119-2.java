/*Add null checking to prevent unexpected NullPointerException.

getAuthToken() could return null bundle.

Change-Id:If00aea0278424e9d15b0bdf4fa6685bcd6bd0229*/




//Synthetic comment -- diff --git a/core/java/android/accounts/AccountManager.java b/core/java/android/accounts/AccountManager.java
//Synthetic comment -- index 9765496..997eabb 100644

//Synthetic comment -- @@ -450,6 +450,7 @@
throws OperationCanceledException, IOException, AuthenticatorException {
Bundle bundle = getAuthToken(account, authTokenType, notifyAuthFailure, null /* callback */,
null /* handler */).getResult();
        if ( bundle == null ) return null;
return bundle.getString(KEY_AUTHTOKEN);
}








