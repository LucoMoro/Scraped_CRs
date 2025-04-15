/*Small mask calculation optimization

Get rid of loop to calculate bitmask where 2^n-1 will suffice.

Change-Id:I1d77a5c3753207663531efb407f6114ccabb1220*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/RawImage.java b/ddms/libs/ddmlib/src/com/android/ddmlib/RawImage.java
//Synthetic comment -- index 3ec6148..adb0cc9 100644

//Synthetic comment -- @@ -216,12 +216,7 @@
* @param length
* @return
*/
    private static int getMask(int length) {
        return (1 << length) - 1;
}
}







