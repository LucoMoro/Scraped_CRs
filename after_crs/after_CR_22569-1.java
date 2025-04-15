/*Make Go To Declaration work for <fragment> names.

Change-Id:Ib2e8d8b93cdb52717719296dec0cbcf9780802ff*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index f88e7ab..92b6b4f 100644

//Synthetic comment -- @@ -32,6 +32,9 @@
/** The element name in a <code>&lt;view class="..."&gt;</code> element. */
public static final String VIEW = "view";                           //$NON-NLS-1$

    /** The element name in a <code>&lt;fragment android:name="..."&gt;</code> element. */
    public static final String FRAGMENT = "fragment";                   //$NON-NLS-1$

/** The attribute name in a <code>&lt;view class="..."&gt;</code> element. */
public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$
//Synthetic comment -- @@ -99,6 +102,7 @@

public static final String ATTR_LAYOUT_Y = "layout_y";                      //$NON-NLS-1$
public static final String ATTR_LAYOUT_X = "layout_x";                      //$NON-NLS-1$
    public static final String ATTR_NAME = "name";                              //$NON-NLS-1$

public static final String VALUE_WRAP_CONTENT = "wrap_content";             //$NON-NLS-1$
public static final String VALUE_FILL_PARENT = "fill_parent";               //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 83e9bc4..c30b78a 100644

//Synthetic comment -- @@ -18,7 +18,9 @@

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ON_CLICK;
import static com.android.ide.common.layout.LayoutConstants.FRAGMENT;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VIEW;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
//Synthetic comment -- @@ -321,20 +323,25 @@
return false;
}

    /** Returns true if this represents a style attribute */
private static boolean isStyleAttribute(XmlContext context) {
String tag = context.getElement().getTagName();
return STYLE_ELEMENT.equals(tag);
}

    /**
     * Returns true if this represents a {@code <view class="foo.bar.Baz">} class
     * attribute, or a {@code <fragment android:name="foo.bar.Baz">} class attribute
     */
private static boolean isClassAttribute(XmlContext context) {
Attr attribute = context.getAttribute();
if (attribute == null) {
return false;
}
String tag = context.getElement().getTagName();
        String attributeName = attribute.getLocalName();
        return ATTR_CLASS.equals(attributeName) && (VIEW.equals(tag) || FRAGMENT.equals(tag))
                || ATTR_NAME.equals(attributeName) && FRAGMENT.equals(tag);
}

/** Returns true if this represents an onClick attribute specifying a method handler */







