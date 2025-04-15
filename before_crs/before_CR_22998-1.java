/*Fix auto-merge

A change that was cherrypicked from the external master into the
tools-r11 branch was automerged back into the external master in such
a way that a method and a field got duplicated. This CL fixes the
merge.

Change-Id:I5b05c8a9477b4afb669efd1904c1415925d25307*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 6fa1743..89cfe92 100644

//Synthetic comment -- @@ -71,13 +71,6 @@
public static final String VIEW_FRAGMENT = "fragment";    //$NON-NLS-1$

/**
     * The XML name of the special {@code <requestFocus>} layout tag.
     * A synthetic element with that name is created as part of the view descriptors list
     * returned by {@link #getViewDescriptors()}.
     */
    public static final String REQUEST_FOCUS = "requestFocus";//$NON-NLS-1$

    /**
* The XML name of the special {@code <view>} layout tag. This is used to add generic
* views with a class attribute to specify the view.
* <p>
//Synthetic comment -- @@ -492,26 +485,6 @@
return descriptor;
}

     * Creates and return a new {@code <requestFocus>} descriptor.
     * @param knownLayouts  A list of all known layout view descriptors, used to find the
     *   FrameLayout descriptor and extract its layout attributes.
     */
    private ViewElementDescriptor createRequestFocus() {
        String xml_name = REQUEST_FOCUS;

        // Create the include descriptor
        return new ViewElementDescriptor(
                xml_name,  // xml_name
                xml_name, // ui_name
                xml_name, // "class name"; the GLE only treats this as an element tag
                "Requests focus for the parent element or one of its descendants", // tooltip
                null,  // sdk_url
                null,  // attributes
                null,  // layout attributes
                null,  // children
                false  /* mandatory */);
    }

/**
* Creates and returns a new {@code <requestFocus>} descriptor.
*/







