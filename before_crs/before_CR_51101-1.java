/*AndroidKeyStore: add key wrapping test

Change-Id:Ib21ab37d22689dd87f014eaa1f7919a575367cdd*/
//Synthetic comment -- diff --git a/keystore/tests/src/android/security/AndroidKeyStoreTest.java b/keystore/tests/src/android/security/AndroidKeyStoreTest.java
//Synthetic comment -- index 056e681..c376f3d 100644

//Synthetic comment -- @@ -51,6 +51,9 @@
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

public class AndroidKeyStoreTest extends AndroidTestCase {
//Synthetic comment -- @@ -1545,4 +1548,49 @@
} catch (UnsupportedOperationException success) {
}
}
}







