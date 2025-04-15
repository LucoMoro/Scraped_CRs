/*Add a new getParser method to IProjectCallback.

This is used to make the implementation easier with access to the
XML file path in the ResourceValue.

Change-Id:Iedbb194bdf4b3d17841be1d50c384b62615bae8f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index a20cc6c..c3698fd 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -402,6 +403,10 @@
return null;
}

    public ILayoutPullParser getParser(ResourceValue layoutResource) {
        return getParser(layoutResource.getName());
    }

public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
ResourceReference itemRef,
int fullPosition, int typePosition, int fullChildPosition, int typeChildPosition,








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index d5a1829..026907e 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
*/
public abstract class Bridge {

    public final static int API_CURRENT = 7;

/**
* Returns the API level of the layout library.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index b91b598..d35ca35 100644

//Synthetic comment -- @@ -97,10 +97,20 @@
* Returns a custom parser for the layout of the given name.
* @param layoutName the name of the layout.
* @return returns a custom parser or null if no custom parsers are needed.
     * @deprecated This is replaced by {@link #getParser(ResourceValue)} but older version
     * of the layoutlib (before API7) will still call this method.
*/
    @Deprecated
ILayoutPullParser getParser(String layoutName);

/**
     * Returns a custom parser for a given layout.
     * @param layoutResource The layout.
     * @return returns a custom parser or null if no custom parsers are needed.
     */
    ILayoutPullParser getParser(ResourceValue layoutResource);

    /**
* Returns the value of an item used by an adapter.
* @param adapterView The {@link ResourceReference} for the adapter view info.
* @param adapterCookie the view cookie for this particular view.







