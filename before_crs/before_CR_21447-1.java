/*Move ResourceFolderType into common.

Moved some constants from sdklib (which is not a dependency
of common but instead depends on common) into common.

Change-Id:I6fdfbad4e77813a9f2a2ca9ea0d740692d8bce5b*/
//Synthetic comment -- diff --git a/common/src/com/android/AndroidConstants.java b/common/src/com/android/AndroidConstants.java
new file mode 100644
//Synthetic comment -- index 0000000..dfe137a

//Synthetic comment -- @@ -0,0 +1,50 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java b/common/src/com/android/resources/ResourceFolderType.java
similarity index 69%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolderType.java
rename to common/src/com/android/resources/ResourceFolderType.java
//Synthetic comment -- index 91aec28..3a5b64d 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2007 The Android Open Source Project
*
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.eclipse.org/org/documents/epl-v10.php
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -14,26 +14,25 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.sdklib.SdkConstants;

/**
* Enum representing a type of resource folder.
*/
public enum ResourceFolderType {
    ANIM(SdkConstants.FD_ANIM),
    ANIMATOR(SdkConstants.FD_ANIMATOR),
    COLOR(SdkConstants.FD_COLOR),
    DRAWABLE(SdkConstants.FD_DRAWABLE),
    INTERPOLATOR(SdkConstants.FD_INTERPOLATOR),
    LAYOUT(SdkConstants.FD_LAYOUT),
    MENU(SdkConstants.FD_MENU),
    MIPMAP(SdkConstants.FD_MIPMAP),
    RAW(SdkConstants.FD_RAW),
    VALUES(SdkConstants.FD_VALUES),
    XML(SdkConstants.FD_XML);

private final String mName;

//Synthetic comment -- @@ -71,7 +70,7 @@
*/
public static ResourceFolderType getFolderType(String folderName) {
// split the name of the folder in segments.
        String[] folderSegments = folderName.split(FolderConfiguration.QUALIFIER_SEP);

// get the enum for the resource type.
return getTypeByName(folderSegments[0]);








//Synthetic comment -- diff --git a/common/src/com/android/resources/ResourceType.java b/common/src/com/android/resources/ResourceType.java
//Synthetic comment -- index a4d3aa2..e9d4d53 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2007 The Android Open Source Project
*
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.eclipse.org/org/documents/epl-v10.php
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index 0b14358..e7c5157 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt;

import com.android.ide.eclipse.adt.internal.build.builders.PostCompilerBuilder;
import com.android.ide.eclipse.adt.internal.build.builders.PreCompilerBuilder;
import com.android.ide.eclipse.adt.internal.build.builders.ResourceManagerBuilder;
//Synthetic comment -- @@ -144,7 +145,7 @@
public final static String WS_ASSETS = WS_SEP + SdkConstants.FD_ASSETS;

/** Absolute path of the layout folder, e.g. "/res/layout".<br> This is a workspace path. */
    public final static String WS_LAYOUTS = WS_RESOURCES + WS_SEP + SdkConstants.FD_LAYOUT;

/** Leaf of the javaDoc folder. Does not start with a separator. */
public final static String WS_JAVADOC_FOLDER_LEAF = SdkConstants.FD_DOCS + "/" + //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 28244b9..eb89db1 100644

//Synthetic comment -- @@ -36,7 +36,6 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
//Synthetic comment -- @@ -44,6 +43,7 @@
import com.android.ide.eclipse.adt.internal.ui.EclipseUiHelper;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.io.StreamException;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkstats.SdkStatsService;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java
//Synthetic comment -- index e1a4fa0..f13e201 100644

//Synthetic comment -- @@ -16,14 +16,15 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.build.builders.BaseBuilder;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -140,7 +141,7 @@
IFolder genFolder = getGenFolder();

IFolder rawFolder = project.getFolder(
                new Path(SdkConstants.FD_RES).append(SdkConstants.FD_RAW));

int depIndex;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/MatchingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/MatchingStrategy.java
//Synthetic comment -- index c6c5261..077400f 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index ea6cf9c..d82774d 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.LayoutDevice;
//Synthetic comment -- @@ -43,6 +42,7 @@
import com.android.resources.Density;
import com.android.resources.DockMode;
import com.android.resources.NightMode;
import com.android.resources.ResourceType;
import com.android.resources.ScreenOrientation;
import com.android.sdklib.AndroidVersion;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LayoutCreatorDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/LayoutCreatorDialog.java
//Synthetic comment -- index 978f114..ace26fb 100644

//Synthetic comment -- @@ -19,10 +19,10 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.ConfigurationState;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.Dialog;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index a1a653f..88711e2 100644

//Synthetic comment -- @@ -34,8 +34,8 @@
import com.android.ide.common.rendering.api.SessionParams.RenderingMode;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.IPageImageProvider;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.ContextPullParser;
//Synthetic comment -- @@ -59,7 +59,6 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.ScreenSizeQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
//Synthetic comment -- @@ -69,6 +68,7 @@
import com.android.io.IAbstractFile;
import com.android.io.StreamException;
import com.android.resources.Density;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 080afce..c69c402 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_LAYOUT;

import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
//Synthetic comment -- @@ -666,7 +666,7 @@
//     /res/layout/foo.xml => "foo"
//     /res/layout-land/foo.xml => "-land/foo"

        if (FD_LAYOUT.equals(folderName)) {
// Normal case -- keep just the basename
return name;
} else {
//Synthetic comment -- @@ -913,7 +913,7 @@
public IFile getFile() {
String reference = mId;
if (!reference.contains(WS_SEP)) {
                reference = SdkConstants.FD_LAYOUT + WS_SEP + reference;
}

String projectPath = SdkConstants.FD_RESOURCES + WS_SEP + reference + '.' + EXT_XML;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index 58ae8ff..77431c45 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.sdklib.SdkConstants.FD_ANIMATOR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;

import com.android.ide.common.rendering.api.Capability;
//Synthetic comment -- @@ -228,7 +228,7 @@
LayoutEditor editor = mCanvas.getLayoutEditor();
IWorkbenchWindow workbenchWindow = editor.getEditorSite().getWorkbenchWindow();
IWorkbench workbench = workbenchWindow.getWorkbench();
            String animationDir = FD_RESOURCES + WS_SEP + FD_ANIMATOR;
Pair<IProject, String> pair = Pair.of(editor.getProject(), animationDir);
IStructuredSelection selection = new StructuredSelection(pair);
wizard.init(workbench, selection);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 1456b47..efd95e2 100644

//Synthetic comment -- @@ -46,7 +46,6 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
//Synthetic comment -- @@ -55,6 +54,7 @@
import com.android.io.FileWrapper;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 2fbb4a3..3a8348d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -297,7 +298,7 @@
IResource resource = layoutMembers[j];
if (resource instanceof IFolder
&& resource.exists()
                        && resource.getName().startsWith(SdkConstants.FD_LAYOUT)) {
IFolder layoutFolder = (IFolder) resource;
IResource[] members = layoutFolder.members();
for (int i = 0; i < members.length; i++) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 48da579..735c595 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -232,7 +233,7 @@
private void addLayoutChanges(IProject project, String className) {
try {
IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);
            IFolder layoutFolder = resFolder.getFolder(SdkConstants.FD_LAYOUT);
IResource[] members = layoutFolder.members();
for (int i = 0; i < members.length; i++) {
IResource member = members[i];








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 53c941a..b80125a 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -193,7 +194,7 @@
private void addLayoutChanges(IProject project, String className) {
try {
IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);
            IFolder layoutFolder = resFolder.getFolder(SdkConstants.FD_LAYOUT);
IResource[] members = layoutFolder.members();
for (int i = 0; i < members.length; i++) {
IResource member = members[i];








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index 1c5c56c..413abcf 100644

//Synthetic comment -- @@ -17,11 +17,12 @@
package com.android.ide.eclipse.adt.internal.refactorings.extractstring;


import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.resources.IFolder;
//Synthetic comment -- @@ -576,7 +577,7 @@
wsFolderPath = wsFolderPath.substring(0, pos);
}

                String[] folderSegments = wsFolderPath.split(FolderConfiguration.QUALIFIER_SEP);

if (folderSegments.length > 0) {
String folderName = folderSegments[0];








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 48a0544..955a0b2 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index fd8ad9e..f373ccc 100644

//Synthetic comment -- @@ -18,13 +18,13 @@

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.FolderTypeRelationship;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResourceItem;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index b759e6e..259ed35 100644

//Synthetic comment -- @@ -16,7 +16,8 @@

package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;


/**
//Synthetic comment -- @@ -24,7 +25,6 @@
* value which means that the property is not set.
*/
public final class FolderConfiguration implements Comparable<FolderConfiguration> {
    public final static String QUALIFIER_SEP = "-"; //$NON-NLS-1$

private final ResourceQualifier[] mQualifiers = new ResourceQualifier[INDEX_COUNT];

//Synthetic comment -- @@ -404,7 +404,7 @@
if (qualifier != null) {
String segment = qualifier.getFolderSegment();
if (segment != null && segment.length() > 0) {
                    result.append(QUALIFIER_SEP);
result.append(segment);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationship.java
//Synthetic comment -- index 168f7fb..b0f1389 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.resources.ResourceType;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index dc68995..3de0f4a 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFolder;
import com.android.resources.ResourceType;
import com.android.util.Pair;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceFolder.java
//Synthetic comment -- index ac744c8..270fc3e 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index 9293f33..5e5f78d 100644

//Synthetic comment -- @@ -16,8 +16,9 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
//Synthetic comment -- @@ -29,6 +30,7 @@
import com.android.io.IAbstractFile;
import com.android.io.IAbstractFolder;
import com.android.io.IAbstractResource;
import com.android.resources.ResourceType;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -579,7 +581,7 @@
*/
private ResourceFolder processFolder(IAbstractFolder folder, ProjectResources project) {
// split the name of the folder in segments.
        String[] folderSegments = folder.getName().split(FolderConfiguration.QUALIFIER_SEP);

// get the enum for the resource type.
ResourceFolderType type = ResourceFolderType.getTypeByName(folderSegments[0]);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index ad18ceb..ccf4301 100644

//Synthetic comment -- @@ -16,9 +16,10 @@

package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.sdklib.SdkConstants.FD_DATA;
import static com.android.sdklib.SdkConstants.FD_RES;
import static com.android.sdklib.SdkConstants.FD_VALUES;
import static java.io.File.separator;

import com.android.ide.common.rendering.LayoutLibrary;
//Synthetic comment -- @@ -405,7 +406,7 @@
* returns the result as a map from resource type to a list of names
*/
private Map<ResourceType, Collection<String>> readPublicAttributeLists() {
        String relative = FD_DATA + separator + FD_RES + separator + FD_VALUES + separator +
"public.xml"; //$NON-NLS-1$
File file = new File(mTarget.getLocation(), relative);
if (file.isFile()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index e8ca524..d98d674 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.ide.eclipse.adt.internal.resources.configurations.CountryCodeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.DockModeQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
//Synthetic comment -- @@ -509,7 +510,7 @@
*/
public boolean setConfiguration(String folderName) {
// split the name of the folder in segments.
        String[] folderSegments = folderName.split(FolderConfiguration.QUALIFIER_SEP);

return setConfiguration(folderSegments);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 2c0ac44..87d1b75 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.ide.eclipse.adt.internal.ui;

import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors.NAME_ATTR;
import static com.android.sdklib.SdkConstants.FD_RESOURCES;
import static com.android.sdklib.SdkConstants.FD_VALUES;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -251,7 +251,7 @@
// Find "dimens.xml" file in res/values/ (or corresponding name for other
// value types)
String fileName = type.getName() + 's';
        String projectPath = FD_RESOURCES + WS_SEP + FD_VALUES + WS_SEP + fileName + '.' + EXT_XML;
IResource member = mProject.findMember(projectPath);
if (member != null) {
if (member instanceof IFile) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectWizard.java
//Synthetic comment -- index ebccf25..b02d1a8 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -140,17 +141,20 @@
private static final String ASSETS_DIRECTORY =
SdkConstants.FD_ASSETS + AdtConstants.WS_SEP;
private static final String DRAWABLE_DIRECTORY =
        SdkConstants.FD_DRAWABLE + AdtConstants.WS_SEP;
private static final String DRAWABLE_HDPI_DIRECTORY =
        SdkConstants.FD_DRAWABLE + "-" + Density.HIGH.getResourceValue() + AdtConstants.WS_SEP;   //$NON-NLS-1$
private static final String DRAWABLE_MDPI_DIRECTORY =
        SdkConstants.FD_DRAWABLE + "-" + Density.MEDIUM.getResourceValue() + AdtConstants.WS_SEP; //$NON-NLS-1$
private static final String DRAWABLE_LDPI_DIRECTORY =
        SdkConstants.FD_DRAWABLE + "-" + Density.LOW.getResourceValue() + AdtConstants.WS_SEP;    //$NON-NLS-1$
private static final String LAYOUT_DIRECTORY =
        SdkConstants.FD_LAYOUT + AdtConstants.WS_SEP;
private static final String VALUES_DIRECTORY =
        SdkConstants.FD_VALUES + AdtConstants.WS_SEP;
private static final String GEN_SRC_DIRECTORY =
SdkConstants.FD_GEN_SOURCES + AdtConstants.WS_SEP;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index 1cf41fd..8829d76 100644

//Synthetic comment -- @@ -17,8 +17,9 @@

package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
//Synthetic comment -- @@ -30,13 +31,13 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.ResourceQualifier;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.TargetChangeListener;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.ConfigurationState;
import com.android.ide.eclipse.adt.internal.ui.ConfigurationSelector.SelectorMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.util.Pair;
//Synthetic comment -- @@ -1023,7 +1024,7 @@
wsFolderPath = wsFolderPath.substring(0, pos);
}

            String[] folderSegments = wsFolderPath.split(FolderConfiguration.QUALIFIER_SEP);

if (folderSegments.length > 0) {
String folderName = folderSegments[0];
//Synthetic comment -- @@ -1044,7 +1045,7 @@
// For now, treat a selection of /res/animator as /res/anim/,
// though we need to handle this better
// TODO: Properly support ANIMATOR templates!
                if (!selected && folderName.equals(SdkConstants.FD_ANIMATOR)) {
for (TypeInfo type : sTypes) {
if (type.getResFolderType() == ResourceFolderType.ANIM) {
matches.add(type);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index afb6dcc..5131126 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.tests.functests.layoutRendering;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
//Synthetic comment -- @@ -168,7 +169,7 @@
}

// look for the layout folder
        File layoutFolder = new File(resFolder, SdkConstants.FD_LAYOUT);
if (layoutFolder.isDirectory() == false) {
fail("Sample project has no layout folder!");
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index 59c1897..2c53bea 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFile;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.SingleResourceFile;
import com.android.ide.eclipse.adt.io.IFileWrapper;
//Synthetic comment -- @@ -34,6 +33,7 @@
import com.android.resources.Navigation;
import com.android.resources.NavigationState;
import com.android.resources.NightMode;
import com.android.resources.ScreenOrientation;
import com.android.resources.TouchScreen;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java
//Synthetic comment -- index 9c2a0c7..2c7c75c 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.Collections;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationShipTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/manager/FolderTypeRelationShipTest.java
//Synthetic comment -- index f1ce9d9..24ac601 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.resources.ResourceType;

import junit.framework.TestCase;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index 37265b1..99af0db 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib;

import java.io.File;

/**
//Synthetic comment -- @@ -180,28 +182,6 @@
public final static String FD_APK_NATIVE_LIBS = "lib"; //$NON-NLS-1$
/** Default output folder name, i.e. "bin" */
public final static String FD_OUTPUT = "bin"; //$NON-NLS-1$
    /** Default anim resource folder name, i.e. "anim" */
    public final static String FD_ANIM = "anim"; //$NON-NLS-1$
    /** Default animator resource folder name, i.e. "animator" */
    public final static String FD_ANIMATOR = "animator"; //$NON-NLS-1$
    /** Default color resource folder name, i.e. "color" */
    public final static String FD_COLOR = "color"; //$NON-NLS-1$
    /** Default drawable resource folder name, i.e. "drawable" */
    public final static String FD_DRAWABLE = "drawable"; //$NON-NLS-1$
    /** Default interpolator resource folder name, i.e. "interpolator" */
    public final static String FD_INTERPOLATOR = "interpolator"; //$NON-NLS-1$
    /** Default layout resource folder name, i.e. "layout" */
    public final static String FD_LAYOUT = "layout"; //$NON-NLS-1$
    /** Default menu resource folder name, i.e. "menu" */
    public final static String FD_MENU = "menu"; //$NON-NLS-1$
    /** Default menu resource folder name, i.e. "mipmap" */
    public final static String FD_MIPMAP = "mipmap"; //$NON-NLS-1$
    /** Default values resource folder name, i.e. "values" */
    public final static String FD_VALUES = "values"; //$NON-NLS-1$
    /** Default xml resource folder name, i.e. "xml" */
    public final static String FD_XML = "xml"; //$NON-NLS-1$
    /** Default raw resource folder name, i.e. "raw" */
    public final static String FD_RAW = "raw"; //$NON-NLS-1$
/** proguard output folder for mapping, etc.. files */
public final static String FD_PROGUARD = "proguard"; //$NON-NLS-1$

//Synthetic comment -- @@ -336,11 +316,13 @@

/** Path of the attrs.xml file relative to a platform folder. */
public final static String OS_PLATFORM_ATTRS_XML =
            OS_PLATFORM_RESOURCES_FOLDER + FD_VALUES + File.separator + FN_ATTRS_XML;

/** Path of the attrs_manifest.xml file relative to a platform folder. */
public final static String OS_PLATFORM_ATTRS_MANIFEST_XML =
            OS_PLATFORM_RESOURCES_FOLDER + FD_VALUES + File.separator + FN_ATTRS_MANIFEST_XML;

/** Path of the layoutlib.jar file relative to a platform folder. */
public final static String OS_PLATFORM_LAYOUTLIB_JAR =








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 16dd8eb..7840b91 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdklib.internal.project;

import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -325,11 +326,11 @@

if (isTestProject == false) {
/* Make res files only for non test projects */
                File valueFolder = createDirs(resourceFolder, SdkConstants.FD_VALUES);
installTargetTemplate("strings.template", new File(valueFolder, "strings.xml"),
keywords, target);

                File layoutFolder = createDirs(resourceFolder, SdkConstants.FD_LAYOUT);
installTargetTemplate("layout.template", new File(layoutFolder, "main.xml"),
keywords, target);








