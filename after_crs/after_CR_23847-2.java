/*NX test should not fail on ARMv5 hardware.http://code.google.com/p/android/issues/detail?id=17328NX is only required on hardware that supports it.  ARMv5 does
not support NX, so don't even try running the test if we detect
that condition.

Change-Id:I259bb224c10da16fdadcfef531fb36c9e8e6da37*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoExecutePermissionTest.java b/tests/tests/os/src/android/os/cts/NoExecutePermissionTest.java
similarity index 80%
rename from tests/tests/permission/src/android/permission/cts/NoExecutePermissionTest.java
rename to tests/tests/os/src/android/os/cts/NoExecutePermissionTest.java
//Synthetic comment -- index 5c0a4c3..fbde684 100644

//Synthetic comment -- @@ -14,7 +14,9 @@
* limitations under the License.
*/

package android.os.cts;

import android.os.cts.CpuFeatures;

import java.io.File;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -29,6 +31,10 @@
public class NoExecutePermissionTest extends TestCase {

public void testNoExecutePermission() throws FileNotFoundException {
        if (!cpuHasNxSupport()) {
            return;
        }

String heapPermissions = null;
String stackPermissions = null;

//Synthetic comment -- @@ -62,4 +68,16 @@
assertEquals("NX (No Execute) not enabled for heap", "rw-p", heapPermissions);
assertEquals("NX (No Execute) not enabled for stack", "rw-p", stackPermissions);
}

    private static boolean cpuHasNxSupport() {
        if (CpuFeatures.isArmCpu() && !CpuFeatures.isArm7Compatible()) {
            // ARM processors before v7 do not have NX support.
            // http://code.google.com/p/android/issues/detail?id=17328
            return false;
        }

        // TODO: handle other processors.  For now, assume those processors
        // have NX support.
        return true;
    }
}







