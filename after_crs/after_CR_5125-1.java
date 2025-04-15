/*In removeGpsStatusListener, remove the listener for real.*/




//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index d0e4f49..b780c52 100644

//Synthetic comment -- @@ -470,8 +470,7 @@
for (int i = 0; i < size && l == null; i++) {
Listener test = mListeners.get(i);
if (binder.equals(test.mListener.asBinder())) {
                    l = test;
}
}








