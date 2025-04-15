/*Support layout aliases properly

Change-Id:I3c1dac7809a225118b69f1557a42051e96011198*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java
//Synthetic comment -- index defaca6..98a1fab 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.SdkConstants.XMLNS_ANDROID;
import static com.android.SdkConstants.XMLNS_URI;

import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
//Synthetic comment -- @@ -343,7 +344,7 @@
}

private void perform() {
            Pair<ResourceType,String> resource = ResourceRepository.parseResource(mResource);
ResourceType type = resource.getFirst();
String name = resource.getSecond();
String value = ""; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java
//Synthetic comment -- index 23846a3..0e46255 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.CLASS_ACTIVITY;
import static com.android.SdkConstants.EXT_XML;
import static com.android.SdkConstants.FD_DOCS;
//Synthetic comment -- @@ -36,13 +35,13 @@
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.PREFIX_THEME_REF;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.TAG_RESOURCES;
import static com.android.SdkConstants.TAG_STYLE;
import static com.android.SdkConstants.TOOLS_URI;
import static com.android.SdkConstants.VIEW;
import static com.android.SdkConstants.VIEW_FRAGMENT;
import static com.android.ide.common.resources.ResourceRepository.parseResource;
import static com.android.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -1154,41 +1153,6 @@
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
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -2072,7 +2071,7 @@
*/
public boolean isBestMatchFor(IFile file, FolderConfiguration config) {
ResourceFile match = mResources.getMatchingFile(mEditedFile.getName(),
                ResourceType.LAYOUT, config);
if (match != null) {
return match.getFile().equals(mEditedFile);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationMatcher.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationMatcher.java
//Synthetic comment -- index 096fa6f..5dfcdb8 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.resources.Density;
import com.android.resources.NightMode;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.resources.ScreenSize;
import com.android.resources.UiMode;
//Synthetic comment -- @@ -145,7 +145,7 @@
*/
public boolean isCurrentFileBestMatchFor(FolderConfiguration config) {
ResourceFile match = mResources.getMatchingFile(mEditedFile.getName(),
                ResourceType.LAYOUT, config);

if (match != null) {
return match.getFile().equals(mEditedFile);
//Synthetic comment -- @@ -687,7 +687,7 @@
}
String name = editedFile.getName();
FolderConfiguration config = chooser.getConfiguration().getFullConfig();
        ResourceFile match = resources.getMatchingFile(name, ResourceType.LAYOUT, config);

if (match != null) {
// In Eclipse, the match's file is always an instance of IFileWrapper








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreview.java
//Synthetic comment -- index 496173a..07baaeb 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.Density;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -384,7 +383,7 @@
ProjectResources resources = chooser.getResources();
if (resources != null) {
ResourceFile best = resources.getMatchingFile(editedFile.getName(),
                            ResourceType.LAYOUT, config);
if (best != null) {
IAbstractFile file = best.getFile();
if (file instanceof IFileWrapper) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceXmlTextAction.java
//Synthetic comment -- index 8d52114..4ca7837 100644

//Synthetic comment -- @@ -24,8 +24,8 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.resources.ResourceType;
import com.android.utils.Pair;
//Synthetic comment -- @@ -173,7 +173,7 @@
return null;
}

                    return ResourceRepository.parseResource(url);
}
}
} catch (BadLocationException e) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 9695318..978980b 100644

//Synthetic comment -- @@ -34,6 +34,7 @@

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
//Synthetic comment -- @@ -180,39 +181,6 @@
}

/**
* Is this a resource that can be defined in any file within the "values" folder?
* <p>
* Some resource types can be defined <b>both</b> as a separate XML file as well
//Synthetic comment -- @@ -281,7 +249,7 @@
return false;
}

        Pair<ResourceType,String> parsed = ResourceRepository.parseResource(resource);
if (parsed != null) {
ResourceType type = parsed.getFirst();
String name = parsed.getSecond();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java
//Synthetic comment -- index d3459b8..b7793bb 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.annotations.NonNull;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.resources.ResourceType;
import com.android.utils.Pair;
//Synthetic comment -- @@ -366,7 +366,7 @@
if (resource instanceof String) {
String url = (String) resource;
assert url.startsWith("@") : resource;
            Pair<ResourceType, String> pair = ResourceRepository.parseResource(url);
assertNotNull(url, pair);
ResourceType type = pair.getFirst();
String currentName = pair.getSecond();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java
//Synthetic comment -- index f90f437..7ca3fc0 100644

//Synthetic comment -- @@ -16,10 +16,12 @@

package com.android.ide.eclipse.adt.internal.resources;

import static com.android.resources.ResourceType.ATTR;
import static com.android.resources.ResourceType.DIMEN;
import static com.android.resources.ResourceType.LAYOUT;

import com.android.ide.common.resources.ResourceDeltaKind;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -124,15 +126,26 @@
}

public void testParseResource() {
        // This isn't the right place for this test, but sdk_common doesn't have a proper
        // test setup yet
        assertNull(ResourceRepository.parseResource(""));
        assertNull(ResourceRepository.parseResource("not_a_resource"));

        assertEquals(LAYOUT, ResourceRepository.parseResource("@layout/foo").getFirst());
        assertEquals(DIMEN, ResourceRepository.parseResource("@dimen/foo").getFirst());
        assertEquals(DIMEN, ResourceRepository.parseResource("@android:dimen/foo").getFirst());
        assertEquals("foo", ResourceRepository.parseResource("@layout/foo").getSecond());
        assertEquals("foo", ResourceRepository.parseResource("@dimen/foo").getSecond());
        assertEquals("foo", ResourceRepository.parseResource("@android:dimen/foo").getSecond());

        assertEquals(ATTR, ResourceRepository.parseResource("?attr/foo").getFirst());
        assertEquals("foo", ResourceRepository.parseResource("?attr/foo").getSecond());

        assertEquals(ATTR, ResourceRepository.parseResource("?foo").getFirst());
        assertEquals("foo", ResourceRepository.parseResource("?foo").getSecond());

        assertEquals(ATTR, ResourceRepository.parseResource("?android:foo").getFirst());
        assertEquals("foo", ResourceRepository.parseResource("?android:foo").getSecond());
}










//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 02c61d1..542409d 100755

//Synthetic comment -- @@ -16,6 +16,11 @@

package com.android.ide.common.resources;

import static com.android.SdkConstants.ATTR_REF_PREFIX;
import static com.android.SdkConstants.PREFIX_RESOURCE_REF;
import static com.android.SdkConstants.PREFIX_THEME_REF;
import static com.android.SdkConstants.RESOURCE_CLZ_ATTR;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -30,7 +35,9 @@
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -510,39 +517,126 @@
}

/**
     * Returns the {@link ResourceFile} matching the given name,
     * {@link ResourceFolderType} and configuration.
     * <p/>
     * This only works with files generating one resource named after the file
     * (for instance, layouts, bitmap based drawable, xml, anims).
     *
     * @param name the resource name or file name
     * @param type the folder type search for
     * @param config the folder configuration to match for
* @return the matching file or <code>null</code> if no match was found.
*/
@Nullable
    public ResourceFile getMatchingFile(
            @NonNull String name,
            @NonNull ResourceFolderType type,
@NonNull FolderConfiguration config) {
        List<ResourceType> types = FolderTypeRelationship.getRelatedResourceTypes(type);
        for (ResourceType t : types) {
            if (t == ResourceType.ID) {
                continue;
            }
            ResourceFile match = getMatchingFile(name, type, config);
            if (match != null) {
                return match;
}
}

        return null;
    }

    /**
     * Returns the {@link ResourceFile} matching the given name,
     * {@link ResourceType} and configuration.
     * <p/>
     * This only works with files generating one resource named after the file
     * (for instance, layouts, bitmap based drawable, xml, anims).
     *
     * @param name the resource name or file name
     * @param type the folder type search for
     * @param config the folder configuration to match for
     * @return the matching file or <code>null</code> if no match was found.
     */
    @Nullable
    public ResourceFile getMatchingFile(
            @NonNull String name,
            @NonNull ResourceType type,
            @NonNull FolderConfiguration config) {
        ensureInitialized();

        String resourceName = name;
        int dot = resourceName.indexOf('.');
        if (dot != -1) {
            resourceName = resourceName.substring(0, dot);
        }

        Map<String, ResourceItem> items = mResourceMap.get(type);
        if (items != null) {
            ResourceItem item = items.get(resourceName);
            if (item != null) {
                List<ResourceFile> files = item.getSourceFileList();
                if (files != null) {
                    if (files.size() > 1) {
                        ResourceValue value = item.getResourceValue(type, config,
                                isFrameworkRepository());
                        if (value != null) {
                            String v = value.getValue();
                            if (v != null) {
                                Pair<ResourceType, String> pair = parseResource(v);
                                if (pair != null) {
                                    return getMatchingFile(pair.getSecond(), pair.getFirst(),
                                            config);
                                } else {
                                    // Looks like the resource value is pointing to a file
                                    // It's most likely one of the source files for this
                                    // resource item, so check those first
                                    for (ResourceFile f : files) {
                                        if (v.equals(f.getFile().getOsLocation())) {
                                            // Found the file
                                            return f;
                                        }
                                    }

                                    // No; look up the resource file from the full path
                                    File file = new File(v);
                                    if (file.exists()) {
                                        ResourceFile f = findResourceFile(file);
                                        if (f != null) {
                                            return f;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (files.size() == 1) {
                        // Single file: see if it matches
                        ResourceFile matchingFile = files.get(0);
                        if (matchingFile.getFolder().getConfiguration().isMatchFor(config)) {
                            return matchingFile;
                        }
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    private ResourceFile findResourceFile(@NonNull File file) {
        // Look up the right resource file for this path
        String parentName = file.getParentFile().getName();
        IAbstractFolder folder = getResFolder().getFolder(parentName);
        if (folder != null) {
            ResourceFolder resourceFolder = getResourceFolder(folder);
            if (resourceFolder != null) {
                ResourceFile resourceFile = resourceFolder.getFile(file.getName());
                if (resourceFile != null) {
                    return resourceFile;
                }
            }
}

return null;
//Synthetic comment -- @@ -775,5 +869,63 @@

return null;
}

    /**
     * Return the resource type of the given url, and the resource name
     *
     * @param url the resource url to be parsed
     * @return a pair of the resource type and the resource name
     */
    public static Pair<ResourceType,String> parseResource(String url) {
        // Handle theme references
        if (url.startsWith(PREFIX_THEME_REF)) {
            String remainder = url.substring(PREFIX_THEME_REF.length());
            if (url.startsWith(ATTR_REF_PREFIX)) {
                url = PREFIX_RESOURCE_REF + url.substring(1);
                return parseResource(url);
            }
            int colon = url.indexOf(':');
            if (colon != -1) {
                // Convert from ?android:progressBarStyleBig to ?android:attr/progressBarStyleBig
                if (remainder.indexOf('/', colon) == -1) {
                    remainder = remainder.substring(0, colon) + RESOURCE_CLZ_ATTR + '/'
                            + remainder.substring(colon);
                }
                url = PREFIX_RESOURCE_REF + remainder;
                return parseResource(url);
            } else {
                int slash = url.indexOf('/');
                if (slash == -1) {
                    url = PREFIX_RESOURCE_REF + RESOURCE_CLZ_ATTR + '/' + remainder;
                    return parseResource(url);
                }
            }
        }

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
}








