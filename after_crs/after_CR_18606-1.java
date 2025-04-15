/*Tell dalvikvm where to find dalvik-cache.

This makes vm-tests work on production devices.

Bug 3130080

Change-Id:Ia5faa3cd2d648c19dcad5285548c076b322056d2*/




//Synthetic comment -- diff --git a/tools/vm-tests/src/util/build/DeviceUtil.java.template b/tools/vm-tests/src/util/build/DeviceUtil.java.template
//Synthetic comment -- index ee2995c..ffb5b8b 100644

//Synthetic comment -- @@ -94,7 +94,9 @@
}

public static void adbExec(String classpath, String mainclass) {
        DeviceUtil.digestCommand(new String[] {"adb", "shell", "mkdir", 
               "/data/local/tmp/dalvik-cache"}, null);
        DeviceUtil.digestCommand(new String[] {"adb", "shell", "ANDROID_DATA=/data/local/tmp", 
"dalvikvm", "-Xint:portable", "-Xmx512M",
"-Xss32K", "-Djava.io.tmpdir=/data/local/tmp",
"-classpath", classpath, mainclass,







