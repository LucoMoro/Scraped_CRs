/*Remove more cruft.

Unused imports and bogus comments.

(cherry-pick of 9af8c0318fac8bf03ee145da01b0c38a503791fc.)

Change-Id:I2bddb32028b71964407e86c4dbef5516673c27eb*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectInputStream.java b/luni/src/main/java/java/io/ObjectInputStream.java
//Synthetic comment -- index 449204f..0d75a44 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/namespace/QName.java b/luni/src/main/java/javax/xml/namespace/QName.java
//Synthetic comment -- index f748b64..a82487f 100644

//Synthetic comment -- @@ -22,8 +22,6 @@
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.xml.XMLConstants;

/**








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ClientHandshakeImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ClientHandshakeImpl.java
//Synthetic comment -- index da8a1c3..83f86ae 100644

//Synthetic comment -- @@ -18,14 +18,12 @@
package org.apache.harmony.xnet.provider.jsse;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/Logger.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/Logger.java
//Synthetic comment -- index a2688e2..2fbbef7 100644

//Synthetic comment -- @@ -19,8 +19,6 @@

import java.io.PrintStream;
import java.util.Locale;
import libcore.util.EmptyArray;

/**








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSessionImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSessionImpl.java
//Synthetic comment -- index e194a38..5895eec 100644

//Synthetic comment -- @@ -380,9 +380,7 @@
/**
* Returns the object which is bound to the the input parameter name.
* This name is a sort of link to the data of the SSL session's application
     * layer, if any exists.
*
* @param name the name of the binding to find.
* @return the value bound to that name, or null if the binding does not
//Synthetic comment -- @@ -398,9 +396,7 @@

/**
* Returns an array with the names (sort of links) of all the data
     * objects of the application layer bound into the SSL session.
*
* @return a non-null (possibly empty) array of names of the data objects
*         bound to this SSL session.
//Synthetic comment -- @@ -413,9 +409,7 @@
* A link (name) with the specified value object of the SSL session's
* application layer data is created or replaced. If the new (or existing)
* value object implements the <code>SSLSessionBindingListener</code>
     * interface, that object will be notified in due course.
*
* @param name the name of the link (no null are
*            accepted!)
//Synthetic comment -- @@ -446,10 +440,6 @@
* <p>If the value object implements the <code>SSLSessionBindingListener</code>
* interface, the object will receive a <code>valueUnbound</code> notification.
*
* @param name the name of the link (no null are
*            accepted!)
* @throws <code>IllegalArgumentException</code> if the argument is null.








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ServerHandshakeImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/ServerHandshakeImpl.java
//Synthetic comment -- index ff7fb3d..fa8d291 100644

//Synthetic comment -- @@ -19,7 +19,6 @@

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;







