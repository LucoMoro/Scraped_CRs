/*Add support for data binding in the layout editor.

The Layoutlib_api is changed to allow passing information regarding
adapter content, and querying eclipse to fill the items.

Change-Id:Ie5a047ab9cd0ed7677c13309d95663eae462c3e7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 804156a..de52133 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -336,4 +337,13 @@
constructor.setAccessible(true);
return constructor.newInstance(constructorParameters);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 07eb7ef..c410293 100644

//Synthetic comment -- @@ -1499,7 +1499,7 @@
params.setForceNoDecor();
}

        // FIXME make persistent and only reload when the manifest (or at least resources) chanage.
IFolderWrapper projectFolder = new IFolderWrapper(getProject());
IAbstractFile manifest = AndroidManifest.getManifest(projectFolder);
if (manifest != null) {
//Synthetic comment -- @@ -1527,6 +1527,16 @@
// set the Image Overlay as the image factory.
params.setImageFactory(getCanvasControl().getImageOverlay());

try {
mProjectCallback.setLogger(logger);
return layoutLib.createSession(params);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 94b5ac1..f5c843c 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
//Synthetic comment -- @@ -127,6 +128,10 @@
return null;
}

}

public void testApiDemos() throws IOException, XmlPullParserException {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index ec4cf38..2f79038 100644

//Synthetic comment -- @@ -24,41 +24,41 @@
/** Ability to render at full size, as required by the layout, and unbound by the screen */
UNBOUND_RENDERING,
/** Ability to override the background of the rendering with transparency using
     * {@link SceneParams#setCustomBackgroundColor(int)} */
CUSTOM_BACKGROUND_COLOR,
    /** Ability to call {@link LayoutScene#render()} and {@link LayoutScene#render(long)}. */
RENDER,
/** Ability to ask for a layout only with no rendering through
* {@link SessionParams#setLayoutOnly()}
*/
LAYOUT_ONLY,
/**
     * Ability to control embedded layout parsers through {@link IXmlPullParser#getParser(String)}
*/
EMBEDDED_LAYOUT,
/** Ability to call<br>
     * {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#setProperty(Object, String, String)}<br>
* The method that receives an animation listener can only use it if the
* ANIMATED_VIEW_MANIPULATION, or FULL_ANIMATED_VIEW_MANIPULATION is also supported.
* */
VIEW_MANIPULATION,
/** Ability to play animations with<br>
     * {@link LayoutScene#animate(Object, String, boolean, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
*/
PLAY_ANIMATION,
/**
* Ability to manipulate views with animation, as long as the view does not change parent.
     * {@link LayoutScene#insertChild(Object, IXmlPullParser, int, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
     * {@link LayoutScene#removeChild(Object, com.android.layoutlib.api.LayoutScene.IAnimationListener)}<br>
*/
ANIMATED_VIEW_MANIPULATION,
/**
* Ability to move views (even into a different ViewGroup) with animation.
     * see {@link LayoutScene#moveChild(Object, Object, int, java.util.Map, com.android.layoutlib.api.LayoutScene.IAnimationListener)}
*/
    FULL_ANIMATED_VIEW_MANIPULATION;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java
//Synthetic comment -- index ca60640..f63f16f 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
/** Legacy method, do not call
* @deprecated use {@link #getResourceDensity()} instead.
*/
public Density getDensity() {
return Density.getEnum(mDensity.getDpiValue());
}
//Synthetic comment -- @@ -51,4 +52,35 @@
+ getResourceType() + "/" + getName() + " = " + getValue()
+ " (density:" + mDensity +", framework:" + isFramework() + ")]";
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 0ec214f..112037d 100644

//Synthetic comment -- @@ -73,4 +73,19 @@
* @return an Integer containing the resource Id, or <code>null</code> if not found.
*/
Integer getResourceId(ResourceType type, String name);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java
new file mode 100644
//Synthetic comment -- index 0000000..c8f7c37

//Synthetic comment -- @@ -0,0 +1,85 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java
//Synthetic comment -- index 730d5c1..bb7dab4 100644

//Synthetic comment -- @@ -23,23 +23,19 @@
* Represents an android resource with a name and a string value.
*/
@SuppressWarnings("deprecation")
public class ResourceValue implements IResourceValue {
private final ResourceType mType;
    private final String mName;
private String mValue = null;
    private final boolean mIsFramework;

public ResourceValue(ResourceType type, String name, boolean isFramework) {
mType = type;
        mName = name;
        mIsFramework = isFramework;
}

public ResourceValue(ResourceType type, String name, String value, boolean isFramework) {
mType = type;
        mName = name;
mValue = value;
        mIsFramework = isFramework;
}

public ResourceType getResourceType() {
//Synthetic comment -- @@ -56,13 +52,6 @@
}

/**
     * Returns the name of the resource, as defined in the XML.
     */
    public final String getName() {
        return mName;
    }

    /**
* Returns the value of the resource, as defined in the XML. This can be <code>null</code>
*/
public final String getValue() {
//Synthetic comment -- @@ -70,14 +59,6 @@
}

/**
     * Returns whether the resource is a framework resource (<code>true</code>) or a project
     * resource (<code>false</false>).
     */
    public final boolean isFramework() {
        return mIsFramework;
    }

    /**
* Sets the value of the resource.
* @param value the new value
*/
//Synthetic comment -- @@ -95,9 +76,44 @@

@Override
public String toString() {
        return "ResourceValue [" + mType + "/" + mName + " = " + mValue  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + " (framework:" + mIsFramework + ")]"; //$NON-NLS-1$ //$NON-NLS-2$
}


}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index 826e70d..f50d79b 100644

//Synthetic comment -- @@ -18,6 +18,12 @@

import com.android.resources.Density;

/**
* Rendering parameters for a {@link RenderSession}.
*/
//Synthetic comment -- @@ -46,10 +52,81 @@
}
}


private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;

/**
*
//Synthetic comment -- @@ -99,6 +176,10 @@
super(params);
mLayoutDescription = params.mLayoutDescription;
mRenderingMode = params.mRenderingMode;
}

public ILayoutPullParser getLayoutDescription() {
//Synthetic comment -- @@ -116,4 +197,20 @@
public boolean isLayoutOnly() {
return mLayoutOnly;
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/StyleResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/StyleResourceValue.java
//Synthetic comment -- index 429bd26..9d1e65d 100644

//Synthetic comment -- @@ -75,8 +75,8 @@
* Legacy method.
* @deprecated use {@link #getValue()}
*/
public IResourceValue findItem(String name) {
return mItems.get(name);
}

}







