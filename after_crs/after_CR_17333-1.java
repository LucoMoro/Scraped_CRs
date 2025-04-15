/*Work around a bug in the jar verifier of Android.

up to Android 1.6, if the signature file of an apk was
a multiple of 1024 the jar verification failed.

This make sure the signing code in ADT/Ant does not
generate such a signature file by adding an extra CRLF
at the end.

Seehttp://b.android.com/830Change-Id:Ia9ec0563d2abfaa6402ca4d19ca27335e9ba57a3*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index 81131bc..d7a5897 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
/** Write to another stream and also feed it to the Signature object. */
private static class SignatureOutputStream extends FilterOutputStream {
private Signature mSignature;
        private int mCount = 0;

public SignatureOutputStream(OutputStream out, Signature sig) {
super(out);
//Synthetic comment -- @@ -75,6 +76,7 @@
throw new IOException("SignatureException: " + e);
}
super.write(b);
            mCount++;
}

@Override
//Synthetic comment -- @@ -85,6 +87,11 @@
throw new IOException("SignatureException: " + e);
}
super.write(b, off, len);
            mCount += len;
        }

        public int size() {
            return mCount;
}
}

//Synthetic comment -- @@ -299,7 +306,7 @@
}

/** Writes a .SF file with a digest to the manifest. */
    private void writeSignatureFile(SignatureOutputStream out)
throws IOException, GeneralSecurityException {
Manifest sf = new Manifest();
Attributes main = sf.getMainAttributes();
//Synthetic comment -- @@ -333,6 +340,15 @@
}

sf.write(out);

        // A bug in the java.util.jar implementation of Android platforms
        // up to version 1.6 will cause a spurious IOException to be thrown
        // if the length of the signature file is a multiple of 1024 bytes.
        // As a workaround, add an extra CRLF in this case.
        if ((out.size() % 1024) == 0) {
            out.write('\r');
            out.write('\n');
        }
}

/** Write the certificate file with a digital signature. */







