/*Fix Eclair ARMEABI Test

The ro.product.cpu.abi2 property for non-ARMEABI_V7 devices was
incorrectly being checked for "unknown." The value of "unknown"
comes from the Froyo version of this test which is what the
Build.CPU_ABI2 constant was if the property was not set. Fix the
Eclair version by using getprop to make sure the property is
not set at all.

Change-Id:Ieaa22fbbc06b9349b4de0fcbb5fef55207c4728c*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index c9cd54d..e6f323e 100644

//Synthetic comment -- @@ -21,11 +21,18 @@
import android.os.Build;
import android.os.SystemProperties;

import java.io.IOException;
import java.util.Scanner;

import junit.framework.TestCase;

@TestTargetClass(Build.class)
public class BuildTest extends TestCase {

    private static final String RO_PRODUCT_CPU_ABI = "ro.product.cpu.abi";

    private static final String RO_PRODUCT_CPU_ABI2 = "ro.product.cpu.abi2";

/** Tests that check the values of {@link Build#CPU_ABI} and the ABI2 system property. */
public void testCpuAbi() throws Exception {
if (CpuFeatures.isArmCpu()) {
//Synthetic comment -- @@ -33,23 +40,52 @@
}
}

    private void assertArmCpuAbiConstants() throws IOException {
if (CpuFeatures.isArm7Compatible()) {
            String message = String.format("CPU is ARM v7 compatible. "
                    + "Build.CPU_ABI and %s must be %s, and %s must be set to %s.",
                    RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI_V7,
                    RO_PRODUCT_CPU_ABI2, CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI_V7, Build.CPU_ABI);
assertEquals(message, CpuFeatures.ARMEABI, getCpuAbi2());
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI_V7);
            assertProperty(message, RO_PRODUCT_CPU_ABI2, CpuFeatures.ARMEABI);
} else {
            String message = String.format("CPU is not ARM v7 compatible. "
                    + "Build.CPU_ABI and %s must be %s, and %s must not be set.",
                    RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI, RO_PRODUCT_CPU_ABI2);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI);
            assertProperty(message, RO_PRODUCT_CPU_ABI2, null);
}
}

private String getCpuAbi2() {
// The property will be replaced by a SDK constant Build.CPU_ABI_2 in future releases.
        return SystemProperties.get(RO_PRODUCT_CPU_ABI2);
    }

    /**
     * @param message shown when the test fails
     * @param property name passed to getprop
     * @param expected value of the property or null if the property should not be set
     */
    private void assertProperty(String message, String property, String expected)
            throws IOException {
        Process process = new ProcessBuilder("getprop", property).start();
        Scanner scanner = null;
        try {
            scanner = new Scanner(process.getInputStream());
            if (expected != null) {
                assertEquals(message, expected, scanner.next());
            } else {
                // getprop should return nothing for a property not set.
            }
            assertFalse(scanner.hasNext());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
}
}







