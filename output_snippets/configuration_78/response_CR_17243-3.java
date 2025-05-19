//<Beginning of snippet n. 0>


import android.os.Build;
import android.os.SystemProperties;

import junit.framework.TestCase;

@TestTargetClass(Build.class)
public class BuildTest extends TestCase {

/** Tests that check the values of {@link Build#CPU_ABI} and the ABI2 system property. */
public void testCpuAbi() throws Exception {
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
assertEquals(message, CpuFeatures.ARMEABI, getCpuAbi2());
} else {
            String message = String.format("CPU is not ARM v7 compatible, so Build.CPU_ABI must "
                    + "be %s and Build.CPU_ABI2 must be absent.", CpuFeatures.ARMEABI);
assertEquals(message, CpuFeatures.ARMEABI, Build.CPU_ABI);
            assertNull(message, getCpuAbi2());
}
}

private String getCpuAbi2() {
        String abi2Value = SystemProperties.get("ro.product.cpu.abi2");
        return abi2Value == null || abi2Value.isEmpty() ? null : abi2Value;
}
}

//<End of snippet n. 0>