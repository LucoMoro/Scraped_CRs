/*Possible NullPointerException in restorePanelState

curFeatureId is just an index in a cycle and can not be used for getting an object. May get null that will cause NullPointerException. Solution is to use key to get an object from icicles.

Change-Id:I61d5c6bd1c3226a993f60efa74872995ba0e42ac*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PhoneWindow.java b/policy/src/com/android/internal/policy/impl/PhoneWindow.java
//Synthetic comment -- index f1fe43b7..2864d22 100644

//Synthetic comment -- @@ -1653,7 +1653,9 @@
*/
private void restorePanelState(SparseArray<Parcelable> icicles) {
PanelFeatureState st;
        int curFeatureId;
        for (int i = icicles.size() - 1; i >= 0; i--) {
            curFeatureId = icicles.keyAt(i);
st = getPanelState(curFeatureId, false /* required */);
if (st == null) {
// The panel must not have been required, and is currently not around, skip it







