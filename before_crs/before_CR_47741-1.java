/*Support layout aliases properly

Change-Id:I3c1dac7809a225118b69f1557a42051e96011198*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java
//Synthetic comment -- index defaca6..98a1fab 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.SdkConstants.XMLNS_ANDROID;
import static com.android.SdkConstants.XMLNS_URI;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
//Synthetic comment -- @@ -343,7 +344,7 @@
}

private void perform() {
            Pair<ResourceType,String> resource = ResourceHelper.parseResource(mResource);
ResourceType type = resource.getFirst();
String name = resource.getSecond();
String value = ""; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java
//Synthetic comment -- index 23846a3..0e46255 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.ATTR_REF_PREFIX;
import static com.android.SdkConstants.CLASS_ACTIVITY;
import static com.android.SdkConstants.EXT_XML;
import static com.android.SdkConstants.FD_DOCS;
//Synthetic comment -- @@ -36,13 +35,13 @@
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.PREFIX_THEME_REF;
import static com.android.SdkConstants.RESOURCE_CLZ_ATTR;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.TAG_RESOURCES;
import static com.android.SdkConstants.TAG_STYLE;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.SdkConstants.VIEW;
import static com.android.SdkConstants.VIEW_FRAGMENT;
import static com.android.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -1154,41 +1153,6 @@
}

/**
     * Parse a resource reference or a theme reference and return the individual
     * parts
     *
     * @param url the url to parse
     * @return a pair which represents the resource type and name
     */
    public static Pair<ResourceType,String> parseResource(String url) {
        if (url.startsWith(PREFIX_THEME_REF)) {
            String remainder = url.substring(PREFIX_THEME_REF.length());
            if (url.startsWith(ATTR_REF_PREFIX)) {
                url = PREFIX_RESOURCE_REF + url.substring(1);
                return ResourceHelper.parseResource(url);
            }
            int colon = url.indexOf(':');
            if (colon != -1) {
                // Convert from ?android:progressBarStyleBig to ?android:attr/progressBarStyleBig
                if (remainder.indexOf('/', colon) == -1) {
                    remainder = remainder.substring(0, colon) + RESOURCE_CLZ_ATTR + '/'
                            + remainder.substring(colon);
                }
                url = PREFIX_RESOURCE_REF + remainder;
                return ResourceHelper.parseResource(url);
            } else {
                int slash = url.indexOf('/');
                if (slash == -1) {
                    url = PREFIX_RESOURCE_REF + RESOURCE_CLZ_ATTR + '/' + remainder;
                    return ResourceHelper.parseResource(url);
                }
            }
        }

        return ResourceHelper.parseResource(url);
    }

    /**
* Computes hyperlinks to resource definitions for resource urls (e.g.
* {@code @android:string/ok} or {@code @layout/foo}. May create multiple links.
* @param range TBD








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationChooser.java
//Synthetic comment -- index a81ddc8..c11268c 100644

//Synthetic comment -- @@ -62,7 +62,6 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -2072,7 +2071,7 @@
*/
public boolean isBestMatchFor(IFile file, FolderConfiguration config) {
ResourceFile match = mResources.getMatchingFile(mEditedFile.getName(),
                ResourceFolderType.LAYOUT, config);
if (match != null) {
return match.getFile().equals(mEditedFile);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationMatcher.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationMatcher.java
//Synthetic comment -- index 096fa6f..5dfcdb8 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceFolderType;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenSize;
import com.android.resources.UiMode;
//Synthetic comment -- @@ -145,7 +145,7 @@
*/
public boolean isCurrentFileBestMatchFor(FolderConfiguration config) {
ResourceFile match = mResources.getMatchingFile(mEditedFile.getName(),
                ResourceFolderType.LAYOUT, config);

if (match != null) {
return match.getFile().equals(mEditedFile);
//Synthetic comment -- @@ -687,7 +687,7 @@
}
String name = editedFile.getName();
FolderConfiguration config = chooser.getConfiguration().getFullConfig();
        ResourceFile match = resources.getMatchingFile(name, ResourceFolderType.LAYOUT, config);

if (match != null) {
// In Eclipse, the match's file is always an instance of IFileWrapper








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 496173a..07baaeb 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.Density;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -384,7 +383,7 @@
ProjectResources resources = chooser.getResources();
if (resources != null) {
ResourceFile best = resources.getMatchingFile(editedFile.getName(),
                            ResourceFolderType.LAYOUT, config);
if (best != null) {
IAbstractFile file = best.getFile();
if (file instanceof IFileWrapper) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java
//Synthetic comment -- index 8d52114..4ca7837 100644

//Synthetic comment -- @@ -24,8 +24,8 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.Hyperlinks;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.resources.ResourceType;
import com.android.utils.Pair;
//Synthetic comment -- @@ -173,7 +173,7 @@
return null;
}

                    return Hyperlinks.parseResource(url);
}
}
} catch (BadLocationException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 9695318..978980b 100644

//Synthetic comment -- @@ -34,6 +34,7 @@

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
//Synthetic comment -- @@ -180,39 +181,6 @@
}

/**
     * Return the resource type of the given url, and the resource name
     *
     * @param url the resource url to be parsed
     * @return a pair of the resource type and the resource name
     */
    public static Pair<ResourceType,String> parseResource(String url) {
        if (!url.startsWith(PREFIX_RESOURCE_REF)) {
            return null;
        }
        int typeEnd = url.indexOf('/', 1);
        if (typeEnd == -1) {
            return null;
        }
        int nameBegin = typeEnd + 1;

        // Skip @ and @+
        int typeBegin = url.startsWith("@+") ? 2 : 1; //$NON-NLS-1$

        int colon = url.lastIndexOf(':', typeEnd);
        if (colon != -1) {
            typeBegin = colon + 1;
        }
        String typeName = url.substring(typeBegin, typeEnd);
        ResourceType type = ResourceType.getEnum(typeName);
        if (type == null) {
            return null;
        }
        String name = url.substring(nameBegin);

        return Pair.of(type, name);
    }

    /**
* Is this a resource that can be defined in any file within the "values" folder?
* <p>
* Some resource types can be defined <b>both</b> as a separate XML file as well
//Synthetic comment -- @@ -281,7 +249,7 @@
return false;
}

        Pair<ResourceType,String> parsed = parseResource(resource);
if (parsed != null) {
ResourceType type = parsed.getFirst();
String name = parsed.getSecond();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java
//Synthetic comment -- index d3459b8..b7793bb 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.internal.editors.Hyperlinks;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.resources.ResourceType;
import com.android.utils.Pair;
//Synthetic comment -- @@ -366,7 +366,7 @@
if (resource instanceof String) {
String url = (String) resource;
assert url.startsWith("@") : resource;
            Pair<ResourceType, String> pair = Hyperlinks.parseResource(url);
assertNotNull(url, pair);
ResourceType type = pair.getFirst();
String currentName = pair.getSecond();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java
//Synthetic comment -- index f90f437..7ca3fc0 100644

//Synthetic comment -- @@ -16,10 +16,12 @@

package com.android.ide.eclipse.adt.internal.resources;

import static com.android.resources.ResourceType.DIMEN;
import static com.android.resources.ResourceType.LAYOUT;

import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -124,15 +126,26 @@
}

public void testParseResource() {
        assertNull(ResourceHelper.parseResource(""));
        assertNull(ResourceHelper.parseResource("not_a_resource"));

        assertEquals(LAYOUT, ResourceHelper.parseResource("@layout/foo").getFirst());
        assertEquals(DIMEN, ResourceHelper.parseResource("@dimen/foo").getFirst());
        assertEquals(DIMEN, ResourceHelper.parseResource("@android:dimen/foo").getFirst());
        assertEquals("foo", ResourceHelper.parseResource("@layout/foo").getSecond());
        assertEquals("foo", ResourceHelper.parseResource("@dimen/foo").getSecond());
        assertEquals("foo", ResourceHelper.parseResource("@android:dimen/foo").getSecond());
}










//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 02c61d1..542409d 100755

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.common.resources;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -30,7 +35,9 @@
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -510,39 +517,126 @@
}

/**
     * Returns the {@link ResourceFile} matching the given name, {@link ResourceFolderType} and
     * configuration.
     * <p/>This only works with files generating one resource named after the file (for instance,
     * layouts, bitmap based drawable, xml, anims).
* @return the matching file or <code>null</code> if no match was found.
*/
@Nullable
    public ResourceFile getMatchingFile(@NonNull String name, @NonNull ResourceFolderType type,
@NonNull FolderConfiguration config) {
        ensureInitialized();

        // get the folders for the given type
        List<ResourceFolder> folders = mFolderMap.get(type);

        // look for folders containing a file with the given name.
        ArrayList<ResourceFolder> matchingFolders = new ArrayList<ResourceFolder>(folders.size());

        // remove the folders that do not have a file with the given name.
        for (int i = 0 ; i < folders.size(); i++) {
            ResourceFolder folder = folders.get(i);

            if (folder.hasFile(name) == true) {
                matchingFolders.add(folder);
}
}

        // from those, get the folder with a config matching the given reference configuration.
        Configurable match = config.findMatchingConfigurable(matchingFolders);

        // do we have a matching folder?
        if (match instanceof ResourceFolder) {
            // get the ResourceFile from the filename
            return ((ResourceFolder)match).getFile(name);
}

return null;
//Synthetic comment -- @@ -775,5 +869,63 @@

return null;
}
}








