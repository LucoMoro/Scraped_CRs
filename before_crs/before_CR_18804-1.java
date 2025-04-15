/*Fix AccountManagerTest#testHasFeatureWithCallback

Bug 3097462

Mark testHasFeatureWithCallback as private, since its not a test.
CTS was picking it up as a test and trying to run it but failing
because that method takes a parameter.

Change-Id:Ie90ae033748ef90ff293bd73f962d3087a2975c6*/
//Synthetic comment -- diff --git a/tests/tests/accounts/src/android/accounts/cts/AccountManagerTest.java b/tests/tests/accounts/src/android/accounts/cts/AccountManagerTest.java
//Synthetic comment -- index 1a857ea..e72e4db 100644

//Synthetic comment -- @@ -1171,14 +1171,14 @@
public void testHasFeature()
throws IOException, AuthenticatorException, OperationCanceledException {

        testHasFeature(null /* handler */);
        testHasFeature(new Handler());

        testHasFeatureWithCallback(null /* handler */);
        testHasFeatureWithCallback(new Handler());
}

    public void testHasFeature(Handler handler)
throws IOException, AuthenticatorException, OperationCanceledException {
Bundle resultBundle = addAccount(am,
ACCOUNT_TYPE,
//Synthetic comment -- @@ -1269,7 +1269,7 @@
}
}

    public void testHasFeatureWithCallback(Handler handler)
throws IOException, AuthenticatorException, OperationCanceledException {
Bundle resultBundle = addAccount(am,
ACCOUNT_TYPE,







