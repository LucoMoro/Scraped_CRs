/*Fix Eclair ARMEABI Test

The ro.product.cpu.abi2 property for non-ARMEABI_V7 devices was
incorrectly being checked for "unknown." The value of "unknown"
comes from the Froyo version of this test which is what the
Build.CPU_ABI2 constant was if the property was not set. Fix the
Eclair version by using getprop to make sure the property is
not set at all.

Change-Id:Ieaa22fbbc06b9349b4de0fcbb5fef55207c4728c*/
//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index c9cd54d..f98661c 100644

//Synthetic comment -- @@ -21,11 +21,18 @@
import android.os.Build;
import android.os.SystemProperties;

import junit.framework.TestCase;

@TestTargetClass(Build.class)
public class BuildTest extends TestCase {

/** Tests that check the values of {@link Build#CPU_ABI} and the ABI2 system property. */
public void testCpuAbi() throws Exception {
if (CpuFeatures.isArmCpu()) {
//Synthetic comment -- @@ -33,23 +40,73 @@
}
}

    private void assertArmCpuAbiConstants() {
if (CpuFeatures.isArm7Compatible()) {
            String message = String.format("CPU is ARM v7 compatible, so Build.CPU_ABI must be %s"
                    + " and Build.CPU_ABI2 must be %s.", CpuFeatures.ARMEABI_V7,
                            CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI_V7, Build.CPU_ABI);
assertEquals(message, CpuFeatures.ARMEABI, getCpuAbi2());
} else {
            String message = String.format("CPU is not ARM v7 compatible, so Build.CPU_ABI must "
                    + "be %s and Build.CPU_ABI2 must be 'unknown'.", CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
            assertEquals(message, "unknown", getCpuAbi2());
}
}

private String getCpuAbi2() {
// The property will be replaced by a SDK constant Build.CPU_ABI_2 in future releases.
        return SystemProperties.get("ro.product.cpu.abi2");
}
}







