/*Manifest Editor: class selector for <application backupAgent>.

<application android:backupAgent> needs a class selector
that accepts classes deriving from BackupAgent.

Change-Id:Id2e72e85967bc31a67d1eff2c342bcf7acc2cb3c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index ba7894b..ef95ad1 100644

//Synthetic comment -- @@ -197,14 +197,17 @@
overrides.put("action,category,uses-permission/" + ANDROID_NAME_ATTR,       //$NON-NLS-1$
ListAttributeDescriptor.CREATOR);

        overrides.put("application/" + ANDROID_NAME_ATTR,                           //$NON-NLS-1$
                                            ApplicationAttributeDescriptor.CREATOR);

overrideClassName(overrides, "activity", SdkConstants.CLASS_ACTIVITY);           //$NON-NLS-1$
overrideClassName(overrides, "receiver", SdkConstants.CLASS_BROADCASTRECEIVER);  //$NON-NLS-1$
overrideClassName(overrides, "service",  SdkConstants.CLASS_SERVICE);            //$NON-NLS-1$
overrideClassName(overrides, "provider", SdkConstants.CLASS_CONTENTPROVIDER);    //$NON-NLS-1$
        overrideClassName(overrides, "instrumentation", SdkConstants.CLASS_INSTRUMENTATION);    //$NON-NLS-1$

// -- list element nodes already created --
// These elements are referenced by already opened editors, so we want to update them
//Synthetic comment -- @@ -235,18 +238,52 @@
SdkConstants.ANDROID_NS_NAME, SdkConstants.ANDROID_URI);
insertAttribute(MANIFEST_ELEMENT, xmlns);

assert sanityCheck(manifestMap, MANIFEST_ELEMENT);
}

/**
     * Sets up an attribute override for ANDROID_NAME_ATTR using a ClassAttributeDescriptor
* with the specified class name.
*/
private static void overrideClassName(
Map<String, ITextAttributeCreator> overrides,
String elementName,
final String className) {
        overrides.put(elementName + "/" + ANDROID_NAME_ATTR,
new ITextAttributeCreator() {
@Override
public TextAttributeDescriptor create(String xmlName, String nsUri,
//Synthetic comment -- @@ -262,7 +299,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */,
true /*defaultToProjectOnly*/);
} else if (SdkConstants.CLASS_BROADCASTRECEIVER.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -271,7 +308,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */,
true /*defaultToProjectOnly*/);
} else if (SdkConstants.CLASS_INSTRUMENTATION.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -280,7 +317,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */,
false /*defaultToProjectOnly*/);
} else {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -288,7 +325,7 @@
xmlName,
nsUri,
attrInfo,
                            true /*mandatory */);
}
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java
//Synthetic comment -- index 4f41ac2..7d9155a 100644

//Synthetic comment -- @@ -31,11 +31,13 @@
* <p/>
* Used by the override for application/name in {@link AndroidManifestDescriptors}.
*/
public class ApplicationAttributeDescriptor extends TextAttributeDescriptor {

/**
* Used by {@link DescriptorsUtils} to create instances of this descriptor.
*/
public static final ITextAttributeCreator CREATOR = new ITextAttributeCreator() {
@Override
public TextAttributeDescriptor create(String xmlLocalName,







