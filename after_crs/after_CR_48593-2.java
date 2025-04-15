/*Use most specific attributes for distinguished name display

Bug: 7894348
Bug:http://code.google.com/p/android/issues/detail?id=41662Change-Id:I8048a53b8a9a17b384f12b6a4f40071bb3dd3e04*/




//Synthetic comment -- diff --git a/core/java/android/net/http/SslCertificate.java b/core/java/android/net/http/SslCertificate.java
//Synthetic comment -- index fe6d4eb..5b60c0d 100644

//Synthetic comment -- @@ -334,9 +334,11 @@

/**
* A distinguished name helper class: a 3-tuple of:
     * <ul>
     *   <li>the most specific common name (CN)</li>
     *   <li>the most specific organization (O)</li>
     *   <li>the most specific organizational unit (OU)</li>
     * <ul>
*/
public class DName {
/**
//Synthetic comment -- @@ -360,8 +362,15 @@
private String mUName;

/**
         * Creates a new {@code DName} from a string. The attributes
         * are assumed to come in most significant to least
         * significant order which is true of human readable values
         * returned by methods such as {@code X500Principal.getName()}.
         * Be aware that the underlying sources of distinguished names
         * such as instances of {@code X509Certificate} are encoded in
         * least significant to most significant order, so make sure
         * the value passed here has the expected ordering of
         * attributes.
*/
public DName(String dName) {
if (dName != null) {
//Synthetic comment -- @@ -374,18 +383,24 @@

for (int i = 0; i < oid.size(); i++) {
if (oid.elementAt(i).equals(X509Name.CN)) {
                            if (mCName == null) {
                                mCName = (String) val.elementAt(i);
                            }
continue;
}

if (oid.elementAt(i).equals(X509Name.O)) {
                            if (mOName == null) {
                                mOName = (String) val.elementAt(i);
                                continue;
                            }
}

if (oid.elementAt(i).equals(X509Name.OU)) {
                            if (mUName == null) {
                                mUName = (String) val.elementAt(i);
                                continue;
                            }
}
}
} catch (IllegalArgumentException ex) {
//Synthetic comment -- @@ -402,21 +417,21 @@
}

/**
         * @return The most specific Common-name (CN) component of this name
*/
public String getCName() {
return mCName != null ? mCName : "";
}

/**
         * @return The most specific Organization (O) component of this name
*/
public String getOName() {
return mOName != null ? mOName : "";
}

/**
         * @return The most specific Organizational Unit (OU) component of this name
*/
public String getUName() {
return mUName != null ? mUName : "";







