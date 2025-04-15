/*Fix a potential case where aok signing is incomplete.

Change-Id:I53416ed62f55422f450a2d9dc5e61b53b9bcb7f0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/build/SignedJarBuilder.java
//Synthetic comment -- index e676520..5044b45 100644

//Synthetic comment -- @@ -262,11 +262,15 @@
mOutputJar.putNextEntry(new JarEntry("META-INF/CERT.SF"));
SignatureOutputStream out = new SignatureOutputStream(mOutputJar, signature);
writeSignatureFile(out);
            out.close();

// CERT.*
mOutputJar.putNextEntry(new JarEntry("META-INF/CERT." + mKey.getAlgorithm()));
writeSignatureBlock(signature, mCertificate, mKey);
}

mOutputJar.close();







