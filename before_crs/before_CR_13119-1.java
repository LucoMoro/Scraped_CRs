/*getAuthToken() could return null bundle. Add null checking to prevent unexpected NullPointerException.*/
//Synthetic comment -- diff --git a/core/java/android/accounts/AccountManager.java b/core/java/android/accounts/AccountManager.java
//Synthetic comment -- index 9765496..997eabb 100644

//Synthetic comment -- @@ -450,6 +450,7 @@
throws OperationCanceledException, IOException, AuthenticatorException {
Bundle bundle = getAuthToken(account, authTokenType, notifyAuthFailure, null /* callback */,
null /* handler */).getResult();
return bundle.getString(KEY_AUTHTOKEN);
}








