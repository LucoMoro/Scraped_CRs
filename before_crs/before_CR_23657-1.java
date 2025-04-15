/*Remove testUnknownSourcesByDefaultTest

This was expected to be removed by merging in the changes
from Froyo. However, since the change originated in GB,
the cherry-picking into Froyo and removing it in Froyo
ended up as a no-op when merging the removal back into GB.
Thus, remove this again!

Change-Id:I95ff719a1ce6b276e00d33e3286bd49c2255759d*/
//Synthetic comment -- diff --git a/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java b/tests/tests/provider/src/android/provider/cts/Settings_SecureTest.java
//Synthetic comment -- index c325abf..718b9d2 100644

//Synthetic comment -- @@ -179,9 +179,4 @@
assertNotNull(uri);
assertEquals(Uri.withAppendedPath(Secure.CONTENT_URI, name), uri);
}

    public void testUnknownSourcesOffByDefault() throws SettingNotFoundException {
        assertEquals("Device should not ship with 'Unknown Sources' enabled by default.",
                0, Secure.getInt(cr, Settings.Secure.INSTALL_NON_MARKET_APPS));
    }
}







