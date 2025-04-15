/*Issue 1597:  	 browser crash possibly related to invalid SSL certificate*/
//Synthetic comment -- diff --git a/core/java/android/net/http/SslCertificate.java b/core/java/android/net/http/SslCertificate.java
//Synthetic comment -- index 46b2bee..2214405 100644

//Synthetic comment -- @@ -196,26 +196,31 @@
*/
public DName(String dName) {
if (dName != null) {
                X509Name x509Name = new X509Name(mDName = dName);

                Vector val = x509Name.getValues();
                Vector oid = x509Name.getOIDs();

                for (int i = 0; i < oid.size(); i++) {
                    if (oid.elementAt(i).equals(X509Name.CN)) {
                        mCName = (String) val.elementAt(i);
                        continue;
}

                    if (oid.elementAt(i).equals(X509Name.O)) {
                        mOName = (String) val.elementAt(i);
                        continue;
                    }

                    if (oid.elementAt(i).equals(X509Name.OU)) {
                        mUName = (String) val.elementAt(i);
                        continue;
                    }
}
}
}








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/net/http/SslCertificateTest.java b/core/tests/coretests/src/android/net/http/SslCertificateTest.java
new file mode 100644
//Synthetic comment -- index 0000000..147816b

//Synthetic comment -- @@ -0,0 +1,55 @@







