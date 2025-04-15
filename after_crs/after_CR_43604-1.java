/*Never generate R class for libraries inside libraries.

Bad ADT, bad!

Change-Id:Id1e0ee1c0001faf16b5551674d037f009222015b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index 1507a8d..a55e840 100644

//Synthetic comment -- @@ -1109,7 +1109,8 @@
}

// now if the project has libraries, R needs to be created for each libraries
            // unless this is a library.
            if (isLibrary == false && !libRFiles.isEmpty()) {
SymbolLoader symbolValues = new SymbolLoader(new File(outputFolder, "R.txt"));
symbolValues.load();








