/*Because the word is different between en_US and en_GB, to detect locale first before set DEFAULT_DISCPRIPTION.

Change-Id:Ia48c09e2b553a89fde115259bbcc5be951552ed7*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/pm/cts/PermissionInfoTest.java b/tests/tests/content/src/android/content/pm/cts/PermissionInfoTest.java
//Synthetic comment -- index 599452b..b97e079 100644

//Synthetic comment -- @@ -22,13 +22,26 @@
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.test.AndroidTestCase;

public class PermissionInfoTest extends AndroidTestCase {
private static final String PERMISSION_NAME = "android.permission.INTERNET";
    private static final String DEFAULT_DISCPRIPTION = "Allows the app to create network sockets "
            + "and use custom network protocols. The browser and other applications provide means "
            + "to send data to the internet, so this permission is not required to send data to "
            + "the internet.";

public void testPermissionInfo() throws NameNotFoundException {
PackageManager pm = getContext().getPackageManager();







