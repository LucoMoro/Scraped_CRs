/*NativeCryptoTest: try to get lib paths from BaseDexClassLoader

Change-Id:I463c3b3fb4ac0920cad938641e93d82734da8276*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java
//Synthetic comment -- index fb351e8..282543a 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package org.apache.harmony.xnet.provider.jsse;

import dalvik.system.BaseDexClassLoader;

import java.io.FileDescriptor;
import java.io.IOException;
import java.math.BigInteger;
//Synthetic comment -- @@ -2017,7 +2019,14 @@
NativeCrypto.ENGINE_load_dynamic();
int dynEngine = NativeCrypto.ENGINE_by_id("dynamic");
try {
            ClassLoader loader = NativeCryptoTest.class.getClassLoader();

            final String libraryPaths;
            if (loader instanceof BaseDexClassLoader) {
                libraryPaths = ((BaseDexClassLoader) loader).getLdLibraryPath();
            } else {
                libraryPaths = System.getProperty("java.library.path");
            }
assertNotNull(libraryPaths);

String[] libraryPathArray = libraryPaths.split(":");







