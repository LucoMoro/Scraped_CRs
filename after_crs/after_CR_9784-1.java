/*Fix NullPointer exception in function getBrokenAvds()*/




//Synthetic comment -- diff --git a/tools/sdkmanager/libs/sdklib/src/com/android/sdklib/avd/AvdManager.java b/tools/sdkmanager/libs/sdklib/src/com/android/sdklib/avd/AvdManager.java
//Synthetic comment -- index 0cda887..3447bf9 100644

//Synthetic comment -- @@ -338,7 +338,7 @@
synchronized (mAllAvdList) {
if (mBrokenAvdList == null) {
ArrayList<AvdInfo> list = new ArrayList<AvdInfo>();
                for (AvdInfo avd : mAllAvdList) {
if (avd.getStatus() != AvdStatus.OK) {
list.add(avd);
}







