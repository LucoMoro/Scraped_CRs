/*Only create R class for libraries when the project is not a library.

Change-Id:Ia3359fd389aee6fc15796e1e6834cdd4864ecb09*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/AndroidBuilder.java b/builder/src/main/java/com/android/builder/AndroidBuilder.java
//Synthetic comment -- index a9feb95..fb91ca6 100644

//Synthetic comment -- @@ -596,12 +596,13 @@
// library specific options
if (mVariant.getType() == VariantConfiguration.Type.LIBRARY) {
command.add("--non-constant-id");
        } else {
            // only create the R class from library dependencies if this is not a library itself.
            String extraPackages = mVariant.getLibraryPackages();
            if (extraPackages != null) {
                command.add("--extra-packages");
                command.add(extraPackages);
            }
}

// AAPT options







