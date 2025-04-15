/*Fix typos from "beed" to "been"

Bug:http://code.google.com/p/android/issues/detail?id=38366Change-Id:I44c647843e66a57bcbeabad41ec3142faaa1848e*/




//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java b/luni/src/main/java/javax/net/ssl/DistinguishedNameParser.java
//Synthetic comment -- index fa8ed1b..abe11a0 100644

//Synthetic comment -- @@ -196,7 +196,7 @@
case '+':
case ',':
case ';':
                // separator char has been found
return new String(chars, beg, end - beg);
case '\\':
// escaped char
//Synthetic comment -- @@ -216,7 +216,7 @@
}
if (pos == length || chars[pos] == ',' || chars[pos] == '+'
|| chars[pos] == ';') {
                    // separator char or the end of DN has been found
return new String(chars, beg, cur - beg);
}
break;








//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/SSLPeerUnverifiedException.java b/luni/src/main/java/javax/net/ssl/SSLPeerUnverifiedException.java
//Synthetic comment -- index bb5bd64..cdd467a 100644

//Synthetic comment -- @@ -18,7 +18,7 @@
package javax.net.ssl;

/**
 * The exception that is thrown when the identity of a peer has not been
* verified.
*/
public class SSLPeerUnverifiedException extends SSLException {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/DNParser.java b/luni/src/main/java/org/apache/harmony/security/x509/DNParser.java
//Synthetic comment -- index 980ab05..0d5da91 100644

//Synthetic comment -- @@ -227,7 +227,7 @@
case '+':
case ',':
case ';':
                // separator char has been found
return new String(chars, beg, end - beg);
case '\\':
// escaped char
//Synthetic comment -- @@ -247,7 +247,7 @@
}
if (pos == chars.length || chars[pos] == ',' || chars[pos] == '+'
|| chars[pos] == ';') {
                    // separator char or the end of DN has been found
return new String(chars, beg, cur - beg);
}
break;







