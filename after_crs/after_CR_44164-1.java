/*Add a test for ResourceBundle.Control.getCandidateLocales.

Bug:http://code.google.com/p/android/issues/detail?id=16368Change-Id:I44d2f66f80fb79a6bf6c869ccf9b0e95402b296c*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/ResourceBundleTest.java b/luni/src/test/java/tests/api/java/util/ResourceBundleTest.java
//Synthetic comment -- index f1fd484..587a549 100644

//Synthetic comment -- @@ -33,6 +33,12 @@

public class ResourceBundleTest extends junit.framework.TestCase {

    public void test_getCandidateLocales() throws Exception {
        ResourceBundle.Control c = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT);
        assertEquals("[en_US, en, ]", c.getCandidateLocales("base", Locale.US).toString());
        assertEquals("[de_CH, de, ]", c.getCandidateLocales("base", new Locale("de", "CH")).toString());
    }

/**
* java.util.ResourceBundle#getBundle(java.lang.String,
*        java.util.Locale)
//Synthetic comment -- @@ -64,15 +70,19 @@
assertEquals("Wrong bundle de_FR_var 2", "parentValue4", bundle.getString("parent4")
);

try {
            ResourceBundle.getBundle(null, Locale.US);
fail("NullPointerException expected");
} catch (NullPointerException ee) {
//expected
}
        try {
            ResourceBundle.getBundle("blah", (Locale) null);
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
}

/**







