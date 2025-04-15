/*Misc clean up.

Change-Id:I18bc1d9567ebd91e67f0ca54d6bc420880af3953*/
//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/AndroidBuilder.java b/builder/src/main/java/com/android/builder/AndroidBuilder.java
//Synthetic comment -- index 67a02d1..6499a31 100644

//Synthetic comment -- @@ -626,7 +626,7 @@
// (since that R class was already created).
String appPackageName = packageOverride;
if (appPackageName == null) {
                appPackageName = VariantConfiguration.sManifestParser.getPackage(manifestFile);
}

// list of all the symbol loaders per package names.
//Synthetic comment -- @@ -637,8 +637,7 @@
// if the library has no resource, this file won't exist.
if (rFile.isFile()) {

                    String packageName = VariantConfiguration.sManifestParser.getPackage(
                            lib.getManifest());
if (appPackageName.equals(packageName)) {
// ignore libraries that have the same package name as the app
continue;








//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/VariantConfiguration.java b/builder/src/main/java/com/android/builder/VariantConfiguration.java
//Synthetic comment -- index d16ef99..24105f0 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
*/
public class VariantConfiguration {

    final static ManifestParser sManifestParser = new DefaultManifestParser();

private final ProductFlavor mDefaultConfig;
private final SourceProvider mDefaultSourceProvider;
//Synthetic comment -- @@ -73,6 +73,16 @@
}

/**
* Creates the configuration with the base source sets.
*
* This creates a config with a {@link Type#DEFAULT} type.







