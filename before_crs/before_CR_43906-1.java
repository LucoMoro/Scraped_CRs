/*Fix tests on Android builder.

Change-Id:I570f1d60f339bc94094550ac410d0624956ed14d*/
//Synthetic comment -- diff --git a/builder/src/test/java/com/android/builder/VariantConfigurationTest.java b/builder/src/test/java/com/android/builder/VariantConfigurationTest.java
//Synthetic comment -- index c1ee5a5..2e0e889 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.builder;

import junit.framework.TestCase;

import java.io.File;
//Synthetic comment -- @@ -37,6 +38,11 @@
public String getPackage(File manifestFile) {
return mPackageName;
}
}

@Override







