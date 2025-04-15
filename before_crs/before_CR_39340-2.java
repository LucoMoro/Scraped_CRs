/*Modify testcases testCpuAbi for x86 devices for K2110 of Lenovo.*/
//Synthetic comment -- diff --git a/tests/src/android/os/cts/CpuFeatures.java b/tests/src/android/os/cts/CpuFeatures.java
//Synthetic comment -- index f87db6a..997a7da 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

public static final String ARMEABI = "armeabi";

static {
System.loadLibrary("cts_jni");
}








//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index 89f26f2..e1b848b 100644

//Synthetic comment -- @@ -42,14 +42,37 @@

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
//Synthetic comment -- @@ -59,7 +82,24 @@
assertEquals(message, Build.UNKNOWN, Build.CPU_ABI2);
}
}

/**
* @param message shown when the test fails
* @param property name passed to getprop







