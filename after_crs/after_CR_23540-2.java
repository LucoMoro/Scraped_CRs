/*Handle fragment tags in included contexts

This changeset fixes the support for rendering user-chosen layouts in
<fragment> tags in a couple of scenarios:
- where the fragment is part of a layout that "surrounds" the current
  layout (e.g. "show included in")
- where the fragment is part of a layout that is included from the
  edited layout

In both cases, the fragment is read from a layout that is parsed with
a plain pull parser rather than the full XML node model used for
edited layouts, so to fix this the ContextPullParser now looks
specially for <fragment> tags and handles these specially.

In the second scenario, the parser is constructed by layoutlib, so we
have to use the project callback to anticipate requested parsers and
supply our own fragment-capable pull parsers instead.

Change-Id:I8aba253ac0c1a0d5dad7bc6efb9def5f20c75144*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 7c40097..bd1ac81 100644

//Synthetic comment -- @@ -20,28 +20,45 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;

import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
import com.android.sdklib.SdkConstants;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;

/**
 * Modified {@link KXmlParser} that adds the methods of {@link ILayoutPullParser}, and
 * performs other layout-specific parser behavior like translating fragment tags into
 * include tags.
* <p/>
* It will return a given parser when queried for one through
 * {@link ILayoutPullParser#getParser(String)} for a given name.
*
*/
public class ContextPullParser extends KXmlParser implements ILayoutPullParser {
    private static final String COMMENT_PREFIX = "<!--"; //$NON-NLS-1$
    private static final String COMMENT_SUFFIX = "-->"; //$NON-NLS-1$
    /** The callback to request parsers from */
private final IProjectCallback mProjectCallback;
    /** The {@link File} for the layout currently being parsed */
    private File mFile;
    /** The layout to be shown for the current {@code <fragment>} tag. Usually null. */
    private String mFragmentLayout = null;

    public ContextPullParser(IProjectCallback projectCallback, File file) {
super();
mProjectCallback = projectCallback;
        mFile = file;
}

// --- Layout lib API methods
//Synthetic comment -- @@ -62,7 +79,28 @@
// --- KXMLParser override

@Override
    public String getName() {
        String name = super.getName();

        // At designtime, replace fragments with includes.
        if (name.equals(VIEW_FRAGMENT)) {
            mFragmentLayout = findFragmentLayout();
            if (mFragmentLayout != null) {
                return VIEW_INCLUDE;
            }
        } else {
            mFragmentLayout = null;
        }

        return name;
    }

    @Override
public String getAttributeValue(String namespace, String localName) {
        if (localName.equals(ATTR_LAYOUT) && mFragmentLayout != null) {
            return mFragmentLayout;
        }

String value = super.getAttributeValue(namespace, localName);

// on the fly convert match_parent to fill_parent for compatibility with older
//Synthetic comment -- @@ -76,4 +114,58 @@

return value;
}

    /**
     * This method determines whether the {@code <fragment>} tag in the current parsing
     * context has been configured with a layout to render at designtime. If so,
     * it returns the resource name of the layout, and if not, returns null.
     */
    private String findFragmentLayout() {
        try {
            if (!isEmptyElementTag()) {
                // We need to look inside the <fragment> tag to see
                // if it contains a comment which indicates a fragment
                // to be rendered.
                String file = AdtPlugin.readFile(mFile);

                int line = getLineNumber() - 1;
                int column = getColumnNumber() - 1;
                int offset = 0;
                int currentLine = 0;
                int length = file.length();
                while (currentLine < line && offset < length) {
                    int next = file.indexOf('\n', offset);
                    if (next == -1) {
                        break;
                    }

                    currentLine++;
                    offset = next + 1;
                }
                if (currentLine == line) {
                    offset += column;
                    if (offset < length) {
                        offset = file.indexOf('<', offset);
                        if (offset != -1 && file.startsWith(COMMENT_PREFIX, offset)) {
                            // The fragment tag contains a comment
                            int end = file.indexOf(COMMENT_SUFFIX, offset);
                            if (end != -1) {
                                String commentText = file.substring(
                                        offset + COMMENT_PREFIX.length(), end);
                                String l = LayoutMetadata.getProperty(KEY_FRAGMENT_LAYOUT,
                                        commentText);
                                if (l != null) {
                                    return l;
                                }
                            }
                        }
                    }
                }
            }
        } catch (XmlPullParserException e) {
            AdtPlugin.log(e, null);
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index c3698fd..e601e99 100644

//Synthetic comment -- @@ -34,6 +34,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.Result;
import com.android.ide.common.rendering.legacy.LegacyCallback;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata;
//Synthetic comment -- @@ -47,7 +48,12 @@
import com.android.util.Pair;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
//Synthetic comment -- @@ -58,7 +64,7 @@
/**
* Loader for Android Project class in order to use them in the layout editor.
* <p/>This implements {@link IProjectCallback} for the old and new API through
 * {@link LegacyCallback}
*/
public final class ProjectCallback extends LegacyCallback {
private final HashMap<String, Class<?>> mLoadedClasses = new HashMap<String, Class<?>>();
//Synthetic comment -- @@ -75,7 +81,7 @@

private String mLayoutName;
private ILayoutPullParser mLayoutEmbeddedParser;
    private ResourceResolver mResourceResolver;

/**
* Creates a new {@link ProjectCallback} to be used with the layout lib.
//Synthetic comment -- @@ -396,15 +402,46 @@
}

public ILayoutPullParser getParser(String layoutName) {
        // Try to compute the ResourceValue for this layout since layoutlib
        // must be an older version which doesn't pass the value:
        if (mResourceResolver != null) {
            ResourceValue value = mResourceResolver.getProjectResource(ResourceType.LAYOUT,
                    layoutName);
            if (value != null) {
                return getParser(value);
            }
        }

        return getParser(layoutName, null);
    }

    public ILayoutPullParser getParser(ResourceValue layoutResource) {
        return getParser(layoutResource.getName(),
                new File(layoutResource.getValue()));
    }

    private ILayoutPullParser getParser(String layoutName, File xml) {
if (layoutName.equals(mLayoutName)) {
return mLayoutEmbeddedParser;
}

        // For included layouts, create a ContextPullParser such that we get the
        // layout editor behavior in included layouts as well - which for example
        // replaces <fragment> tags with <include>.
        if (xml != null && xml.isFile()) {
            ContextPullParser parser = new ContextPullParser(this, xml);
            try {
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                parser.setInput(new FileInputStream(xml), "UTF-8"); //$NON-NLS-1$
                return parser;
            } catch (XmlPullParserException e) {
                AdtPlugin.log(e, null);
            } catch (FileNotFoundException e) {
                // Shouldn't happen since we check isFile() above
            }
        }

        return null;
}

public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie,
//Synthetic comment -- @@ -543,4 +580,13 @@

return binding;
}

    /**
     * Sets the {@link ResourceResolver} to be used when looking up resources
     *
     * @param resolver the resolver to use
     */
    public void setResourceResolver(ResourceResolver resolver) {
        mResourceResolver = resolver;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 41017a0..f610000 100644

//Synthetic comment -- @@ -220,16 +220,10 @@
private Map<ResourceType, Map<String, ResourceValue>> mConfiguredProjectRes;
private ProjectCallback mProjectCallback;
private boolean mNeedsRecompute = false;
private TargetListener mTargetListener;
private ConfigListener mConfigListener;
private ResourceResolver mResourceResolver;
private ReloadListener mReloadListener;
private int mMinSdkVersion;
private int mTargetSdkVersion;
private LayoutActionBar mActionBar;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index d2c4985..906a2ec 100644

//Synthetic comment -- @@ -98,22 +98,7 @@
Node comment = findComment(node);
if (comment != null) {
String text = comment.getNodeValue();
                return getProperty(name, text);
}

return null;
//Synthetic comment -- @@ -125,6 +110,33 @@
}

/**
     * Returns the given property specified in the given XML comment
     *
     * @param name the name of the property to look up
     * @param text the comment text for an XML node
     * @return the value stored with the given node and name, or null
     */
    public static String getProperty(String name, String text) {
        assert text.startsWith(COMMENT_PROLOGUE);
        String valuesString = text.substring(COMMENT_PROLOGUE.length());
        String[] values = valuesString.split(","); //$NON-NLS-1$
        if (values.length == 1) {
            valuesString = values[0].trim();
            if (valuesString.indexOf('\n') != -1) {
                values = valuesString.split("\n"); //$NON-NLS-1$
            }
        }
        String target = name + '=';
        for (int j = 0; j < values.length; j++) {
            String value = values[j].trim();
            if (value.startsWith(target)) {
                return value.substring(target.length()).trim();
            }
        }
        return null;
    }

    /**
* Sets the given property of the given DOM node to a given value, or if null clears
* the property.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 36cea84..a03d038 100644

//Synthetic comment -- @@ -320,7 +320,7 @@
// as it's what IXmlPullParser.getParser(String) will receive.
String queryLayoutName = mEditor.getLayoutResourceName();
mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
                        topParser = new ContextPullParser(mProjectCallback, layoutFile);
topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
topParser.setInput(new FileInputStream(layoutFile), "UTF-8"); //$NON-NLS-1$
} catch (XmlPullParserException e) {
//Synthetic comment -- @@ -376,6 +376,7 @@

try {
mProjectCallback.setLogger(mLogger);
            mProjectCallback.setResourceResolver(mResourceResolver);
return mLayoutLib.createSession(params);
} catch (RuntimeException t) {
// Exceptions from the bridge
//Synthetic comment -- @@ -383,6 +384,7 @@
throw t;
} finally {
mProjectCallback.setLogger(null);
            mProjectCallback.setResourceResolver(null);
}
}

//Synthetic comment -- @@ -482,6 +484,7 @@
RenderSession session = null;
try {
mProjectCallback.setLogger(mLogger);
            mProjectCallback.setResourceResolver(mResourceResolver);
session = mLayoutLib.createSession(params);
if (session.getResult().isSuccess()) {
assert session.getRootViews().size() == 1;
//Synthetic comment -- @@ -506,6 +509,7 @@
throw t;
} finally {
mProjectCallback.setLogger(null);
            mProjectCallback.setResourceResolver(null);
if (session != null) {
session.dispose();
}







