/*Avoid repeating manifest element names.

In the manifest form tree, avoid repeating element names
in the tree labels. E.g. "MyActivity (Activity)" can just
be "MyActivity".

Change-Id:I8de481aabcbc33e830e18c528cb10a0b2a14138a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestElementNode.java
//Synthetic comment -- index 0b78a22..a4f701f 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
/**
* Computes a short string describing the UI node suitable for tree views.
* Uses the element's attribute "android:name" if present, or the "android:label" one
     * followed by the element's name.
*
* @return A short string describing the UI node suitable for tree views.
*/
//Synthetic comment -- @@ -68,12 +68,13 @@
manifestDescriptors = target.getManifestDescriptors();
}

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
                    return String.format("%1$s (%2$s)", attr, getDescriptor().getUiName());
}
}
}

        return String.format("%1$s", getDescriptor().getUiName());
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 1a66cdb..b969d30 100644

//Synthetic comment -- @@ -211,17 +211,24 @@
/**
* Computes a short string describing the UI node suitable for tree views.
* Uses the element's attribute "android:name" if present, or the "android:label" one
     * followed by the element's name.
*
* @return A short string describing the UI node suitable for tree views.
*/
public String getShortDescription() {
String attr = getDescAttribute();
if (attr != null) {
            return String.format("%1$s (%2$s)", attr, mDescriptor.getUiName());
}

        return mDescriptor.getUiName();
}

/** Returns the key attribute that can be used to describe this node, or null */







