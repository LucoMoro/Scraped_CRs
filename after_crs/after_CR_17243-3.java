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
//Synthetic comment -- @@ -33,23 +40,73 @@
}
}

    private void assertArmCpuAbiConstants() throws IOException {
if (CpuFeatures.isArm7Compatible()) {
            String message = "CPU is ARM v7 compatible, so "
                    + RO_PRODUCT_CPU_ABI  + " must be set to " + CpuFeatures.ARMEABI_V7 + " and "
                    + RO_PRODUCT_CPU_ABI2 + " must be set to " + CpuFeatures.ARMEABI;
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI_V7);
            assertProperty(message, RO_PRODUCT_CPU_ABI2, CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI_V7, Build.CPU_ABI);
assertEquals(message, CpuFeatures.ARMEABI, getCpuAbi2());
} else {
            String message = "CPU is not ARM v7 compatible. "
                    + RO_PRODUCT_CPU_ABI  + " must be set to " + CpuFeatures.ARMEABI + " and "
                    + RO_PRODUCT_CPU_ABI2 + " must not be set.";
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI);
            assertNoPropertySet(message, RO_PRODUCT_CPU_ABI2);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
}
}

private String getCpuAbi2() {
// The property will be replaced by a SDK constant Build.CPU_ABI_2 in future releases.
        return SystemProperties.get(RO_PRODUCT_CPU_ABI2, null);
    }

    /**
     * @param message shown when the test fails
     * @param property name passed to getprop
     * @param expected value of the property
     */
    private void assertProperty(String message, String property, String expected)
            throws IOException {
        Process process = new ProcessBuilder("getprop", property).start();
        Scanner scanner = null;
        try {
            scanner = new Scanner(process.getInputStream());
            String line = scanner.nextLine();
            assertEquals(message + " Value found: " + line , expected, line);
            assertFalse(scanner.hasNext());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    /**
     * Check that a property is not set by scanning through the list of properties returned by
     * getprop, since calling getprop on an property set to "" and on a non-existent property
     * yields the same output.
     *
     * @param message shown when the test fails
     * @param property name passed to getprop
     */
    private void assertNoPropertySet(String message, String property) throws IOException {
        Process process = new ProcessBuilder("getprop").start();
        Scanner scanner = null;
        try {
            scanner = new Scanner(process.getInputStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                assertFalse(message + "Property found: " + line,
                        line.startsWith("[" + property + "]"));
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
}
}







