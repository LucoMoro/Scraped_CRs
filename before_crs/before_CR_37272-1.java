/*Fix bug in manifest metadata handler

The ManifestInfo class keeps a cache of the manifest metadata, such as
the package name, minSdkVersion, etc.

However, the code which cached the minSdkVersion and targetSdkVersion
fields was incorrectly placed inside an unrelated if-block, which
meant that in some cases (in particular, manifests specifying a
default theme) these fields would not get set, and the class would
return an incorrect value of 1 for minSdkVersion, regardless of the
actual value specified.

Change-Id:I32423cad45c9f6f775b7334ee3fd9bd183497abd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 8479b0d..eacac77 100644

//Synthetic comment -- @@ -226,7 +226,6 @@
}

NodeList applications = root.getElementsByTagName(AndroidManifest.NODE_APPLICATION);
            String defaultTheme = null;
if (applications.getLength() > 0) {
assert applications.getLength() == 1;
Element application = (Element) applications.item(0);
//Synthetic comment -- @@ -237,23 +236,22 @@
mApplicationLabel = application.getAttributeNS(NS_RESOURCES, ATTRIBUTE_LABEL);
}

                defaultTheme = application.getAttributeNS(NS_RESOURCES, ATTRIBUTE_THEME);
}

// Look up target SDK
            if (defaultTheme == null || defaultTheme.length() == 0) {
                // From manifest theme documentation:
                // "If that attribute is also not set, the default system theme is used."

                NodeList usesSdks = root.getElementsByTagName(NODE_USES_SDK);
                if (usesSdks.getLength() > 0) {
                    Element usesSdk = (Element) usesSdks.item(0);
                    mMinSdk = getApiVersion(usesSdk, ATTRIBUTE_MIN_SDK_VERSION, 1);
                    mTargetSdk = getApiVersion(usesSdk, ATTRIBUTE_TARGET_SDK_VERSION, mMinSdk);
                }
            } else {
                mManifestTheme = defaultTheme;
}
} catch (SAXException e) {
AdtPlugin.log(e, "Malformed manifest");
} catch (Exception e) {







