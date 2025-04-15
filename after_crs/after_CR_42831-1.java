/*Don't use an empty main manifest when merging library manifests.

Change-Id:I45733f5769a8e6d5f392030f28f117b47999c985*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/AndroidBuilder.java b/builder/src/main/java/com/android/builder/AndroidBuilder.java
//Synthetic comment -- index f083dba..65153e3 100644

//Synthetic comment -- @@ -361,13 +361,9 @@
throw new RuntimeException();
}
} else {
// recursively merge all manifests starting with the leaves and up toward the
// root (the app)
                    mergeLibraryManifests(mainLocation, config.getDirectLibraries(),
new File(outManifestLocation));
}
}







