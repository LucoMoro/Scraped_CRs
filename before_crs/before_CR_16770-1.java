/*Increment CTS Version to 2.1_r4

Bug 2923901

Change-Id:I2e42eae1403eeee8e2386394a654f4a2276749a2*/
//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/Version.java b/tools/host/src/com/android/cts/Version.java
//Synthetic comment -- index 4bdb3ed..257cf8a 100644

//Synthetic comment -- @@ -18,12 +18,12 @@

public class Version {
// The CTS version string
    private static final String version = "2.1_r3";
    
private Version() {
// no instances allowed
}
    
public static String asString() {
return version;
}







