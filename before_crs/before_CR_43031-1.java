/*Add support for MNC=00

This adds support for operators with MNC (Mobile Network Code) zero
to add customized resources. For example, it makes it possible to
add a folder called "/res/values-mnc00/" in an application. This will
cause resources in that folder to be used when MNC is zero.
(There is a total of 14 countries that have an operator with MNC
zero.)

Without this fix, the resource framework gets confused, because MNC 0
is normally used when the MNC is undefined (not set).

Bug: 7170488
Change-Id:Ie209ab04e5c24bfad8509af5e0e0e948fad22796*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/MccTable.java b/src/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index cb33521..c431c57 100644

//Synthetic comment -- @@ -198,6 +198,8 @@
}
if (mnc != 0) {
config.mnc = mnc;
}
ActivityManagerNative.getDefault().updateConfiguration(config);
} catch (RemoteException e) {







