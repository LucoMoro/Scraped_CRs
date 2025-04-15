/*GPS handler have some delay and will remove listener after request location update,
and it will cause passive provider got additional one fix. The test will met the failure by "passive gps location updated too fast: 1000ms < 1490ms".
We add 2 seconds delay between each Test Cycle to fix timing issue.

Change-Id:I76fc7313e4e9c58ed2d6382f09d01a93863b58d4*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/GpsTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/GpsTestActivity.java
//Synthetic comment -- index 31b5854..d429771 100644

//Synthetic comment -- @@ -155,6 +155,12 @@
@Override
public void pass() {
log("OK!\n", Color.GREEN);
mLocationVerifier = null;
nextTest();
}







