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

import junit.framework.TestCase;

import java.util.regex.Pattern;

@TestTargetClass(Build.class)
public class BuildTest extends TestCase {

/** Tests that check the values of {@link Build#CPU_ABI} and {@link Build#CPU_ABI2}. */
    public void testCpuAbi() {
if (CpuFeatures.isArmCpu()) {
assertArmCpuAbiConstants();
}
}

    private void assertArmCpuAbiConstants() {
if (CpuFeatures.isArm7Compatible()) {
            String message = String.format("CPU is ARM v7 compatible, so Build.CPU_ABI must be %s"
                    + " and Build.CPU_ABI2 must be %s.", CpuFeatures.ARMEABI_V7,
                            CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI_V7, Build.CPU_ABI);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI2);
} else {
            String message = String.format("CPU is not ARM v7 compatible, so Build.CPU_ABI must "
                    + "be %s and Build.CPU_ABI2 must be 'unknown'.", CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
            assertEquals(message, "unknown", Build.CPU_ABI2);
}
}








