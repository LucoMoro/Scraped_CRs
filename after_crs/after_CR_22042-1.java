/*Add support for data binding in the layout editor.

The Layoutlib_api is changed to allow passing information regarding
adapter content, and querying eclipse to fill the items.

Change-Id:Ie5a047ab9cd0ed7677c13309d95663eae462c3e7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 804156a..de52133 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -336,4 +337,13 @@
constructor.setAccessible(true);
return constructor.newInstance(constructorParameters);
}

    public String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
            int fullPosition, int typePosition, ResourceReference viewRef, String viewClass) {
        if (viewClass.contains("TextView")) {
            return viewRef.getName() + " " + typePosition;
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 07eb7ef..c410293 100644

//Synthetic comment -- @@ -1499,7 +1499,7 @@
params.setForceNoDecor();
}

        // FIXME make persistent and only reload when the manifest (or at least resources) changes.
IFolderWrapper projectFolder = new IFolderWrapper(getProject());
IAbstractFile manifest = AndroidManifest.getManifest(projectFolder);
if (manifest != null) {
//Synthetic comment -- @@ -1527,6 +1527,16 @@
// set the Image Overlay as the image factory.
params.setImageFactory(getCanvasControl().getImageOverlay());

        // ---------------------------------------
        // Data binding DEBUG
//        AdapterBinding binding = new AdapterBinding();
//        binding.addHeader(new ResourceReference("header", false));
//        binding.addFooter(new ResourceReference("footer", false));
//        binding.addItem(new ListItemReference("listitem", false, 3));
//        binding.addItem(new ListItemReference("separator"));
//        params.addAdapterBinding(new ResourceReference("listview"), binding);
        // ---------------------------------------

try {
mProjectCallback.setLogger(logger);
return layoutLib.createSession(params);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 94b5ac1..f5c843c 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
//Synthetic comment -- @@ -127,6 +128,10 @@
return null;
}

        public String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
                int fullPosition, int typePosition, ResourceReference viewRef, String viewClass) {
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
     * {@link SessionParams#setCustomBackgroundColor(int)} */
CUSTOM_BACKGROUND_COLOR,
    /** Ability to call {@link RenderSession#render()} and {@link RenderSession#render(long)}. */
RENDER,
/** Ability to ask for a layout only with no rendering through
* {@link SessionParams#setLayoutOnly()}
*/
LAYOUT_ONLY,
/**
     * Ability to control embedded layout parsers through {@link ILayoutPullParser#getParser(String)}
*/
EMBEDDED_LAYOUT,
/** Ability to call<br>
     * {@link RenderSession#insertChild(Object, ILayoutPullParser, int, IAnimationListener)}<br>
     * {@link RenderSession#moveChild(Object, Object, int, java.util.Map, IAnimationListener)}<br>
     * {@link RenderSession#setProperty(Object, String, String)}<br>
* The method that receives an animation listener can only use it if the
* ANIMATED_VIEW_MANIPULATION, or FULL_ANIMATED_VIEW_MANIPULATION is also supported.
* */
VIEW_MANIPULATION,
/** Ability to play animations with<br>
     * {@link RenderSession#animate(Object, String, boolean, IAnimationListener)}
*/
PLAY_ANIMATION,
/**
* Ability to manipulate views with animation, as long as the view does not change parent.
     * {@link RenderSession#insertChild(Object, ILayoutPullParser, int, IAnimationListener)}<br>
     * {@link RenderSession#moveChild(Object, Object, int, java.util.Map, IAnimationListener)}<br>
     * {@link RenderSession#removeChild(Object, IAnimationListener)}<br>
*/
ANIMATED_VIEW_MANIPULATION,
/**
* Ability to move views (even into a different ViewGroup) with animation.
     * see {@link RenderSession#moveChild(Object, Object, int, java.util.Map, IAnimationListener)}
*/
    FULL_ANIMATED_VIEW_MANIPULATION,
    ADAPTER_BINDING;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DensityBasedResourceValue.java
//Synthetic comment -- index ca60640..f63f16f 100644

//Synthetic comment -- @@ -41,6 +41,7 @@
/** Legacy method, do not call
* @deprecated use {@link #getResourceDensity()} instead.
*/
    @Deprecated
public Density getDensity() {
return Density.getEnum(mDensity.getDpiValue());
}
//Synthetic comment -- @@ -51,4 +52,35 @@
+ getResourceType() + "/" + getName() + " = " + getValue()
+ " (density:" + mDensity +", framework:" + isFramework() + ")]";
}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mDensity == null) ? 0 : mDensity.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DensityBasedResourceValue other = (DensityBasedResourceValue) obj;
        if (mDensity == null) {
            if (other.mDensity != null)
                return false;
        } else if (!mDensity.equals(other.mDensity))
            return false;
        return true;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 0ec214f..112037d 100644

//Synthetic comment -- @@ -73,4 +73,19 @@
* @return an Integer containing the resource Id, or <code>null</code> if not found.
*/
Integer getResourceId(ResourceType type, String name);

    /**
     * Returns the value of an item used by an adapter.
     * @param adapterView The {@link ResourceReference} for the adapter view info.
     * @param itemRef the {@link ResourceReference} for the layout used by the adapter item.
     * @param fullPosition the position of the item in the full list.
     * @param typePosition the position of the item if only items of the same type are considered.
     *     If there is only one type of items, this is the same as <var>position</var>.
     * @param viewRef The {@link ResourceReference} for the view we're trying to fill.
     * @param viewClass the class name of the view we're trying to fill.
     * @return
     */
    String getAdapterItemValue(ResourceReference adapterView, ResourceReference itemRef,
            int fullPosition, int typePosition,
            ResourceReference viewRef, String viewClass);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceReference.java
new file mode 100644
//Synthetic comment -- index 0000000..c8f7c37

//Synthetic comment -- @@ -0,0 +1,85 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.rendering.api;

/**
 * A resource reference. This contains the String ID of the resource and whether this is a framework
 * reference.
 *
 */
public class ResourceReference {
    private final String mName;
    private final boolean mIsFramework;

    public ResourceReference(String name, boolean isFramework) {
        mName = name;
        mIsFramework = isFramework;
    }

    public ResourceReference(String name) {
        this(name, false /*platformLayout*/);
    }

    /**
     * Returns the name of the resource, as defined in the XML.
     */
    public final String getName() {
        return mName;
    }

    /**
     * Returns whether the resource is a framework resource (<code>true</code>) or a project
     * resource (<code>false</false>).
     */
    public final boolean isFramework() {
        return mIsFramework;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mIsFramework ? 1231 : 1237);
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourceReference other = (ResourceReference) obj;
        if (mIsFramework != other.mIsFramework)
            return false;
        if (mName == null) {
            if (other.mName != null)
                return false;
        } else if (!mName.equals(other.mName))
            return false;
        return true;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java
//Synthetic comment -- index 730d5c1..bb7dab4 100644

//Synthetic comment -- @@ -23,23 +23,19 @@
* Represents an android resource with a name and a string value.
*/
@SuppressWarnings("deprecation")
public class ResourceValue extends ResourceReference implements IResourceValue {
private final ResourceType mType;
private String mValue = null;

public ResourceValue(ResourceType type, String name, boolean isFramework) {
        super(name, isFramework);
mType = type;
}

public ResourceValue(ResourceType type, String name, String value, boolean isFramework) {
        super(name, isFramework);
mType = type;
mValue = value;
}

public ResourceType getResourceType() {
//Synthetic comment -- @@ -56,13 +52,6 @@
}

/**
* Returns the value of the resource, as defined in the XML. This can be <code>null</code>
*/
public final String getValue() {
//Synthetic comment -- @@ -70,14 +59,6 @@
}

/**
* Sets the value of the resource.
* @param value the new value
*/
//Synthetic comment -- @@ -95,9 +76,44 @@

@Override
public String toString() {
        return "ResourceValue [" + mType + "/" + getName() + " = " + mValue  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + " (framework:" + isFramework() + ")]"; //$NON-NLS-1$ //$NON-NLS-2$
}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((mType == null) ? 0 : mType.hashCode());
        result = prime * result + ((mValue == null) ? 0 : mValue.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResourceValue other = (ResourceValue) obj;
        if (mType == null) {
            if (other.mType != null)
                return false;
        } else if (!mType.equals(other.mType))
            return false;
        if (mValue == null) {
            if (other.mValue != null)
                return false;
        } else if (!mValue.equals(other.mValue))
            return false;
        return true;
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index 826e70d..f50d79b 100644

//Synthetic comment -- @@ -18,6 +18,12 @@

import com.android.resources.Density;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Rendering parameters for a {@link RenderSession}.
*/
//Synthetic comment -- @@ -46,10 +52,81 @@
}
}

    /**
     * A ListItemReference. On top of being a {@link ResourceReference}, it contains how many
     * items of this type the data binding should display.
     */
    public static class ListItemReference extends ResourceReference {
        private final int mCount;

        public ListItemReference(String name, boolean platformLayout, int count) {
            super(name, platformLayout);
            mCount = count;
        }

        public ListItemReference(String name, boolean platformLayout) {
            this(name, platformLayout, 1);
        }

        public ListItemReference(String name) {
            this(name, false /*platformLayout*/, 1);
        }

        public int getCount() {
            return mCount;
        }
    }

    /**
     * Describe the content of the dynamic android.widget.Adapter used to fill
     * android.widget.AdapterView
     */
    public static class AdapterBinding {
        private final List<ResourceReference> mHeaders = new ArrayList<ResourceReference>();
        private final List<ListItemReference> mItems = new ArrayList<ListItemReference>();
        private final List<ResourceReference> mFooters = new ArrayList<ResourceReference>();

        public void addHeader(ResourceReference layoutInfo) {
            mHeaders.add(layoutInfo);
        }

        public int getHeaderCount() {
            return mHeaders.size();
        }

        public ResourceReference getHeaderAt(int index) {
            return mHeaders.get(index);
        }

        public void addItem(ListItemReference itemInfo) {
            mItems.add(itemInfo);
        }

        public int getItemCount() {
            return mItems.size();
        }

        public ListItemReference getItemAt(int index) {
            return mItems.get(index);
        }

        public void addFooter(ResourceReference layoutInfo) {
            mFooters.add(layoutInfo);
        }

        public int getFooterCount() {
            return mFooters.size();
        }

        public ResourceReference getFooterAt(int index) {
            return mFooters.get(index);
        }
    }

private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;
private boolean mLayoutOnly = false;
    private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;

/**
*
//Synthetic comment -- @@ -99,6 +176,10 @@
super(params);
mLayoutDescription = params.mLayoutDescription;
mRenderingMode = params.mRenderingMode;
        if (params.mAdapterBindingMap != null) {
            mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>(
                    params.mAdapterBindingMap);
        }
}

public ILayoutPullParser getLayoutDescription() {
//Synthetic comment -- @@ -116,4 +197,20 @@
public boolean isLayoutOnly() {
return mLayoutOnly;
}

    public void addAdapterBinding(ResourceReference reference, AdapterBinding data) {
        if (mAdapterBindingMap == null) {
            mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>();
        }

        mAdapterBindingMap.put(reference, data);
    }

    public Map<ResourceReference, AdapterBinding> getAdapterBindings() {
        if (mAdapterBindingMap == null) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(mAdapterBindingMap);
    }
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/StyleResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/StyleResourceValue.java
//Synthetic comment -- index 429bd26..9d1e65d 100644

//Synthetic comment -- @@ -75,8 +75,8 @@
* Legacy method.
* @deprecated use {@link #getValue()}
*/
    @Deprecated
public IResourceValue findItem(String name) {
return mItems.get(name);
}
}







