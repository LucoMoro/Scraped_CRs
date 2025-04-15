/*Modify testcases testCpuAbi for x86 devices.*/




//Synthetic comment -- diff --git a/tests/src/android/os/cts/CpuFeatures.java b/tests/src/android/os/cts/CpuFeatures.java
//Synthetic comment -- index f87db6a..997a7da 100644

//Synthetic comment -- @@ -22,6 +22,8 @@

public static final String ARMEABI = "armeabi";

    public static final  String ARCH_X86 = "x86";
    
static {
System.loadLibrary("cts_jni");
}








//Synthetic comment -- diff --git a/tests/tests/os/src/android/os/cts/BuildTest.java b/tests/tests/os/src/android/os/cts/BuildTest.java
//Synthetic comment -- index 89f26f2..e1b848b 100644

//Synthetic comment -- @@ -42,14 +42,37 @@

private void assertArmCpuAbiConstants() throws IOException {
if (CpuFeatures.isArm7Compatible()) {
            String cpuAbi = getProperty(RO_PRODUCT_CPU_ABI);
            String cpuAbi2 = getProperty(RO_PRODUCT_CPU_ABI2); 
            //if CPU_ABI is armv7, CPU_ABI2 is either of {armeabi, NULL}
            if (cpuAbi.equals(CpuFeatures.ARMEABI_V7)) {
                String message = "CPU is ARM v7 compatible, so "
+ RO_PRODUCT_CPU_ABI  + " must be set to " + CpuFeatures.ARMEABI_V7 + " and "
                    + RO_PRODUCT_CPU_ABI2 + " must be set to " + CpuFeatures.ARMEABI + " or NULL";
            	assertEquals(message, CpuFeatures.ARMEABI_V7, Build.CPU_ABI);
		if (cpuAbi2.equals(CpuFeatures.ARMEABI)){
            	    assertEquals(message, cpuAbi2, Build.CPU_ABI2);
                } else {
                    assertNoPropertySet(message, RO_PRODUCT_CPU_ABI2); 
                    assertEquals(message, Build.UNKNOWN, Build.CPU_ABI2);                    
                }
            }
            //if CPU_ABI is x86, then CPU_ABI2 is either of {armeabi, armv7, NULL} 
            else if (cpuAbi.equals(CpuFeatures.ARCH_X86)) {
                String message = "CPU is x86 but ARM v7 compatible, so "
                    + RO_PRODUCT_CPU_ABI  + " must be set to " + CpuFeatures.ARCH_X86 + " and "
                    + RO_PRODUCT_CPU_ABI2 + " must be set to " + CpuFeatures.ARMEABI + " or "
                    + CpuFeatures.ARMEABI_V7 + " or NULL";
            	assertEquals(message, CpuFeatures.ARCH_X86, Build.CPU_ABI);
                if (cpuAbi2.equals(CpuFeatures.ARMEABI_V7) || cpuAbi2.equals(CpuFeatures.ARMEABI))
                    assertEquals(message, cpuAbi2, Build.CPU_ABI2);
                else {
                    assertNoPropertySet(message, RO_PRODUCT_CPU_ABI2); 
                    assertEquals(message, Build.UNKNOWN, Build.CPU_ABI2);                    
                }
            } 
        }
        else {
String message = "CPU is not ARM v7 compatible. "
+ RO_PRODUCT_CPU_ABI  + " must be set to " + CpuFeatures.ARMEABI + " and "
+ RO_PRODUCT_CPU_ABI2 + " must not be set.";
//Synthetic comment -- @@ -59,7 +82,24 @@
assertEquals(message, Build.UNKNOWN, Build.CPU_ABI2);
}
}
    /**
     * @param property name passed to getprop
     */
    private String getProperty(String property)
            throws IOException {
        Process process = new ProcessBuilder("getprop", property).start();
        Scanner scanner = null;
        String line = "";
        try {
            scanner = new Scanner(process.getInputStream());
            line = scanner.nextLine();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return line;
    }
/**
* @param message shown when the test fails
* @param property name passed to getprop







