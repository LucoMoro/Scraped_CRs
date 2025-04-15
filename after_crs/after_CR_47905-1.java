/*Clear loaders array after they are destroyed.

See changeIa577caecbacb226a3ce525a01a66283efb6ba754for details.

Change-Id:I9f07eeceaa3829f71008e6f6a38ab849095bd69cSigned-off-by: Roman Mazur <mazur.roman@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/app/LoaderManager.java b/core/java/android/app/LoaderManager.java
//Synthetic comment -- index fd0f0bf..267555a 100644

//Synthetic comment -- @@ -833,6 +833,7 @@
for (int i = mLoaders.size()-1; i >= 0; i--) {
mLoaders.valueAt(i).destroy();
}
            mLoaders.clear();
}

if (DEBUG) Log.v(TAG, "Destroying Inactive in " + this);







