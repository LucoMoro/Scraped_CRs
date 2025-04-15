/*Added a feature to the ApkBuilder for setting a custom KeyStore path.*/




//Synthetic comment -- diff --git a/tools/apkbuilder/src/com/android/apkbuilder/ApkBuilder.java b/tools/apkbuilder/src/com/android/apkbuilder/ApkBuilder.java
//Synthetic comment -- index 40abff1..e7a338a 100644

//Synthetic comment -- @@ -66,6 +66,7 @@
private boolean mSignedPackage = true;
/** the optional type of the debug keystore. If <code>null</code>, the default */
private String mStoreType = null;
    private String mKeyStore = null;

/**
* @param args
//Synthetic comment -- @@ -149,6 +150,11 @@
if (index == args.length) printUsageAndQuit();

mStoreType  = args[index++];
                } else if ("-keystore".equals(argument)) {
                    // quick check on the next argument.
                    if (index == args.length) printUsageAndQuit();

                    mKeyStore  = args[index++];
} else {
printAndExit("Unknown argument: " + argument);
}
//Synthetic comment -- @@ -339,12 +345,15 @@
SignedJarBuilder builder;

if (mSignedPackage) {
                if (mKeyStore == null) {
                        mKeyStore = DebugKeyProvider.getDefaultKeyStoreOsPath();
                }

System.err.println(String.format("Using keystore: %s",
                                mKeyStore));

DebugKeyProvider keyProvider = new DebugKeyProvider(
                        mKeyStore /* Custom KeyStore */,
mStoreType, null /* IKeyGenOutput */);
PrivateKey key = keyProvider.getDebugKey();
X509Certificate certificate = (X509Certificate)keyProvider.getCertificate();
//Synthetic comment -- @@ -437,6 +446,7 @@
System.err.println("    -v      Verbose.");
System.err.println("    -u      Creates an unsigned package.");
System.err.println("    -storetype Forces the KeyStore type. If ommited the default is used.");
        System.err.println("    -keystore Uses a custom keystore (no password).");
System.err.println("");
System.err.println("    -z      Followed by the path to a zip archive.");
System.err.println("            Adds the content of the application package.");







