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
//Synthetic comment -- index 086397a..0c750f3 100644

//Synthetic comment -- @@ -20,32 +20,69 @@

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
            String message = String.format("CPU is ARM v7 compatible. "
                    + "Build.CPU_ABI and %s must be %s, and Build.CPU_ABI2 and %s must be %s.",
                    RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI_V7,
                    RO_PRODUCT_CPU_ABI2, CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI_V7, Build.CPU_ABI);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI2);
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI_V7);
            assertProperty(message, RO_PRODUCT_CPU_ABI2, CpuFeatures.ARMEABI);
} else {
            String message = String.format("CPU is not ARM v7 compatible. "
                    + "Build.CPU_ABI and %s must be %s, and Build.CPU_ABI2 must be 'unknown' "
                    + "with %s not set.", RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI,
                    RO_PRODUCT_CPU_ABI2);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
            assertEquals(message, Build.UNKNOWN, Build.CPU_ABI2);
            assertProperty(message, RO_PRODUCT_CPU_ABI, CpuFeatures.ARMEABI);
            assertProperty(message, RO_PRODUCT_CPU_ABI2, null);
        }
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








