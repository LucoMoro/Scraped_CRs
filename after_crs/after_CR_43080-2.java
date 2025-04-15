/*Manifest Editor: class selector for <application backupAgent>.

<application android:backupAgent> needs a class selector
that accepts classes deriving from BackupAgent.

Change-Id:Id2e72e85967bc31a67d1eff2c342bcf7acc2cb3c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index ff555cc..8407073 100644

//Synthetic comment -- @@ -306,6 +306,12 @@
if (name.contains("ime") || name.startsWith("Ime")) {
name = name.replaceAll("(?<=^| )[iI]me(?=$| )", "IME");
}
        if (name.contains("vm") || name.startsWith("Vm")) {
            name = name.replaceAll("(?<=^| )[vV]m(?=$| )", "VM");
        }
        if (name.contains("ui") || name.startsWith("Ui")) {
            name = name.replaceAll("(?<=^| )[uU]i(?=$| )", "UI");
        }

return name;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index ba7894b..3429e43 100644

//Synthetic comment -- @@ -197,14 +197,18 @@
overrides.put("action,category,uses-permission/" + ANDROID_NAME_ATTR,       //$NON-NLS-1$
ListAttributeDescriptor.CREATOR);

        overrideClassName(overrides, "application",                                    //$NON-NLS-1$
                                     SdkConstants.CLASS_APPLICATION,
                                     false /*mandatory*/);
        overrideClassName(overrides, "application/backupAgent",                        //$NON-NLS-1$
                                     "android.app.backup.BackupAgent",                 //$NON-NLS-1$
                                     false /*mandatory*/);
        overrideClassName(overrides, "activity", SdkConstants.CLASS_ACTIVITY);         //$NON-NLS-1$
        overrideClassName(overrides, "receiver", SdkConstants.CLASS_BROADCASTRECEIVER);//$NON-NLS-1$
        overrideClassName(overrides, "service",  SdkConstants.CLASS_SERVICE);          //$NON-NLS-1$
        overrideClassName(overrides, "provider", SdkConstants.CLASS_CONTENTPROVIDER);  //$NON-NLS-1$
        overrideClassName(overrides, "instrumentation",
                                                 SdkConstants.CLASS_INSTRUMENTATION);  //$NON-NLS-1$

// -- list element nodes already created --
// These elements are referenced by already opened editors, so we want to update them
//Synthetic comment -- @@ -235,18 +239,52 @@
SdkConstants.ANDROID_NS_NAME, SdkConstants.ANDROID_URI);
insertAttribute(MANIFEST_ELEMENT, xmlns);

        /*
         *
         *
         */
assert sanityCheck(manifestMap, MANIFEST_ELEMENT);
}

/**
     * Sets up a mandatory attribute override using a ClassAttributeDescriptor
* with the specified class name.
     *
     * @param overrides The current map of overrides.
     * @param elementName The element name to override, e.g. "application".
     *  If this name does NOT have a slash (/), the ANDROID_NAME_ATTR attribute will be overriden.
     *  Otherwise, if it contains a (/) the format is "element/attribute", for example
     *  "application/name" vs "application/backupAgent".
     * @param className The fully qualified name of the base class of the attribute.
*/
private static void overrideClassName(
Map<String, ITextAttributeCreator> overrides,
String elementName,
final String className) {
        overrideClassName(overrides, elementName, className, true);
    }

    /**
     * Sets up an attribute override using a ClassAttributeDescriptor
     * with the specified class name.
     *
     * @param overrides The current map of overrides.
     * @param elementName The element name to override, e.g. "application".
     *  If this name does NOT have a slash (/), the ANDROID_NAME_ATTR attribute will be overriden.
     *  Otherwise, if it contains a (/) the format is "element/attribute", for example
     *  "application/name" vs "application/backupAgent".
     * @param className The fully qualified name of the base class of the attribute.
     * @param mandatory True if this attribute is mandatory, false if optional.
     */
    private static void overrideClassName(
            Map<String, ITextAttributeCreator> overrides,
            String elementName,
            final String className,
            final boolean mandatory) {
        if (elementName.indexOf('/') == -1) {
            elementName = elementName + '/' + ANDROID_NAME_ATTR;
        }
        overrides.put(elementName,
new ITextAttributeCreator() {
@Override
public TextAttributeDescriptor create(String xmlName, String nsUri,
//Synthetic comment -- @@ -262,7 +300,7 @@
xmlName,
nsUri,
attrInfo,
                            mandatory,
true /*defaultToProjectOnly*/);
} else if (SdkConstants.CLASS_BROADCASTRECEIVER.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -271,7 +309,7 @@
xmlName,
nsUri,
attrInfo,
                            mandatory,
true /*defaultToProjectOnly*/);
} else if (SdkConstants.CLASS_INSTRUMENTATION.equals(className)) {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -280,7 +318,7 @@
xmlName,
nsUri,
attrInfo,
                            mandatory,
false /*defaultToProjectOnly*/);
} else {
return new ClassAttributeDescriptor(
//Synthetic comment -- @@ -288,7 +326,7 @@
xmlName,
nsUri,
attrInfo,
                            mandatory);
}
}
});








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/ApplicationAttributeDescriptor.java
deleted file mode 100644
//Synthetic comment -- index 4f41ac2..0000000

//Synthetic comment -- @@ -1,62 +0,0 @@







