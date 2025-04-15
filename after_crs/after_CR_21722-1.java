/*Avoid repeating manifest element names.

In the manifest form tree, avoid repeating element names
in the tree labels. E.g. "MyActivity (Activity)" can just
be "MyActivity".

Change-Id:I8de481aabcbc33e830e18c528cb10a0b2a14138a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java
//Synthetic comment -- index 0b78a22..a6862e7 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
/**
* Computes a short string describing the UI node suitable for tree views.
* Uses the element's attribute "android:name" if present, or the "android:label" one
     * followed by the element's name if not repeated.
*
* @return A short string describing the UI node suitable for tree views.
*/
//Synthetic comment -- @@ -68,12 +68,13 @@
manifestDescriptors = target.getManifestDescriptors();
}

        String name = getDescriptor().getUiName();

if (manifestDescriptors != null &&
getXmlNode() != null &&
getXmlNode() instanceof Element &&
getXmlNode().hasAttributes()) {

// Application and Manifest nodes have a special treatment: they are unique nodes
// so we don't bother trying to differentiate their strings and we fall back to
// just using the UI name below.
//Synthetic comment -- @@ -90,12 +91,18 @@
AndroidManifestDescriptors.ANDROID_LABEL_ATTR);
}
if (attr != null && attr.length() > 0) {
                    // If the ui name is repeated in the attribute value, don't use it.
                    // Typical case is to avoid ".pkg.MyActivity (Activity)".
                    if (attr.indexOf(name) >= 0) {
                        return attr;
                    } else {
                        return String.format("%1$s (%2$s)", attr, name);
                    }
}
}
}

        return String.format("%1$s", name);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 1a66cdb..cfcc0aa 100644

//Synthetic comment -- @@ -211,17 +211,24 @@
/**
* Computes a short string describing the UI node suitable for tree views.
* Uses the element's attribute "android:name" if present, or the "android:label" one
     * followed by the element's name if not repeated.
*
* @return A short string describing the UI node suitable for tree views.
*/
public String getShortDescription() {
        String name = mDescriptor.getUiName();
String attr = getDescAttribute();
if (attr != null) {
            // If the ui name is repeated in the attribute value, don't use it.
            // Typical case is to avoid ".pkg.MyActivity (Activity)".
            if (attr.indexOf(name) >= 0) {
                return attr;
            } else {
                return String.format("%1$s (%2$s)", attr, name);
            }
}

        return name;
}

/** Returns the key attribute that can be used to describe this node, or null */







