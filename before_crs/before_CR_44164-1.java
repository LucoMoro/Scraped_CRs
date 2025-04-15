/*Add a test for ResourceBundle.Control.getCandidateLocales.

Bug:http://code.google.com/p/android/issues/detail?id=16368Change-Id:I44d2f66f80fb79a6bf6c869ccf9b0e95402b296c*/
//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/ResourceBundleTest.java b/luni/src/test/java/tests/api/java/util/ResourceBundleTest.java
//Synthetic comment -- index f1fd484..587a549 100644

//Synthetic comment -- @@ -33,6 +33,12 @@

public class ResourceBundleTest extends junit.framework.TestCase {

/**
* java.util.ResourceBundle#getBundle(java.lang.String,
*        java.util.Locale)
//Synthetic comment -- @@ -64,15 +70,19 @@
assertEquals("Wrong bundle de_FR_var 2", "parentValue4", bundle.getString("parent4")
);

        // Test with a security manager
        Locale.setDefault(new Locale("en", "US"));

try {
            ResourceBundle.getBundle(null, Locale.getDefault());
fail("NullPointerException expected");
} catch (NullPointerException ee) {
//expected
}

try {
ResourceBundle.getBundle("", new Locale("xx", "yy"));
//Synthetic comment -- @@ -80,8 +90,6 @@
} catch (MissingResourceException ee) {
//expected
}

        Locale.setDefault(defLocale);
}

/**







