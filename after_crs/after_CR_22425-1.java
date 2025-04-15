/*Move getParser(name) into IProjectCallback.

Change-Id:I316f03a9903e90eac0cb8059469c1de5b679dac5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index d8911e4..7c40097 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.layoutlib.api.IXmlPullParser;
import com.android.sdklib.SdkConstants;

import org.kxml2.io.KXmlParser;
//Synthetic comment -- @@ -35,23 +37,22 @@
*/
public class ContextPullParser extends KXmlParser implements ILayoutPullParser {

    private final IProjectCallback mProjectCallback;

    public ContextPullParser(IProjectCallback projectCallback) {
super();
        mProjectCallback = projectCallback;
}

// --- Layout lib API methods

    /**
     * this is deprecated but must still be implemented for older layout libraries.
     * @deprecated use {@link IProjectCallback#getParser(String)}.
     */
    @Deprecated
public ILayoutPullParser getParser(String layoutName) {
        return mProjectCallback.getParser(layoutName);
}

public Object getViewCookie() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 1a924a9..8d3eaf9 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.DataBindingItem;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.ResourceReference;
//Synthetic comment -- @@ -66,6 +67,10 @@
private LayoutLog mLogger;
private LayoutLibrary mLayoutLib;

    private String mLayoutName;
    private ILayoutPullParser mLayoutEmbeddedParser;


/**
* Creates a new {@link ProjectCallback} to be used with the layout lib.
*
//Synthetic comment -- @@ -350,6 +355,19 @@
return constructor.newInstance(constructorParameters);
}

    public void setLayoutParser(String layoutName, ILayoutPullParser layoutParser) {
        mLayoutName = layoutName;
        mLayoutEmbeddedParser = layoutParser;
    }

    public ILayoutPullParser getParser(String layoutName) {
        if (layoutName.equals(mLayoutName)) {
            return mLayoutEmbeddedParser;
        }

        return null;
    }

public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
ResourceReference itemRef,
int fullPosition, int typePosition, int fullChildPosition, int typeChildPosition,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 6592fa2..337ad5c 100644

//Synthetic comment -- @@ -1433,9 +1433,11 @@
ILayoutPullParser topParser = modelParser;

// Code to support editing included layout
        // first reset the layout parser just in case.
        mProjectCallback.setLayoutParser(null, null);

if (includeWithin != null) {
            // Outer layout name:
String contextLayoutName = includeWithin.getName();

// Find the layout file.
//Synthetic comment -- @@ -1448,7 +1450,8 @@
// Get the name of the layout actually being edited, without the extension
// as it's what IXmlPullParser.getParser(String) will receive.
String queryLayoutName = getLayoutResourceName();
                        mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
                        topParser = new ContextPullParser(mProjectCallback);
topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
topParser.setInput(new FileReader(layoutFile));
} catch (XmlPullParserException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 10625bb..dce46fe 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
* operations on this set.
*/
public class ViewHierarchy {
    private static final boolean DUMP_INFO = true;

private LayoutCanvas mCanvas;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index a043e6d..8b71c5a 100644

//Synthetic comment -- @@ -129,6 +129,10 @@
return null;
}

        public ILayoutPullParser getParser(String layoutName) {
            return null;
        }

public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
ResourceReference itemRef, int fullPosition, int typePosition,
int fullChildPosition, int typeChildPosition,








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ILayoutPullParser.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ILayoutPullParser.java
//Synthetic comment -- index 4b033d9..574f9bb 100644

//Synthetic comment -- @@ -38,8 +38,9 @@
* @param layoutName the name of the layout.
* @return returns a custom parser or null if no custom parsers are needed.
*
     * @deprecated use {@link IProjectCallback#getParser(String)} instead
*/
    @Deprecated
ILayoutPullParser getParser(String layoutName);
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 7368e50..b91b598 100644

//Synthetic comment -- @@ -94,6 +94,13 @@
Integer getResourceId(ResourceType type, String name);

/**
     * Returns a custom parser for the layout of the given name.
     * @param layoutName the name of the layout.
     * @return returns a custom parser or null if no custom parsers are needed.
     */
    ILayoutPullParser getParser(String layoutName);

    /**
* Returns the value of an item used by an adapter.
* @param adapterView The {@link ResourceReference} for the adapter view info.
* @param adapterCookie the view cookie for this particular view.







