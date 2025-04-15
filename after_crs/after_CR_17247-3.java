/*Improve Froyo ARMEABI Test

Unlike the Eclair port of the test, there wasn't anything
wrong with the Froyo version, because the Build.CPU_ABI2
returns Build.UNKNOWN when the property is not set. However,
the test couldn't make a distinction between the property
not being set and a property being set with the value "unknown."

Therefore, port the same enhancements done for the Eclair test
and improve the messaging.

Change-Id:Ie0edfc390065da0d6c75cc6490eb9d319ad8de91*/




//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index 086397a..ada90ee 100644

//Synthetic comment -- @@ -20,32 +20,89 @@

import android.os.Build;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import junit.framework.TestCase;

@TestTargetClass(Build.class)
public class BuildTest extends TestCase {

    private static final String RO_PRODUCT_CPU_ABI = "ro.product.cpu.abi";

    private static final String RO_PRODUCT_CPU_ABI2 = "ro.product.cpu.abi2";

/** Tests that check the values of {@link Build#CPU_ABI} and {@link Build#CPU_ABI2}. */
    public void testCpuAbi() throws IOException {
if (CpuFeatures.isArmCpu()) {
assertArmCpuAbiConstants();
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
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI2);
} else {
            String message = "CPU is not ARM v7 compatible. "
                    + RO_PRODUCT_CPU_ABI  + " must be set to " + CpuFeatures.ARMEABI + " and "
                    + RO_PRODUCT_CPU_ABI2 + " must not be set.";
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI);
            assertNoPropertySet(message, RO_PRODUCT_CPU_ABI2);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
            assertEquals(message, Build.UNKNOWN, Build.CPU_ABI2);
        }
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








