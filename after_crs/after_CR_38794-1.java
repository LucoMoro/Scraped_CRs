/*Fix editor startup exception for new configurations

If you use the wizard to create a new layout in a new configuration
folder (for example, pick a brand new language qualifier on the second
page of the wizard), then the editor comes up blank.

The reason this happens is that during editor initialization, it
attempts to look up the ResourceFolder, and gets back null which it
then proceeds to dereference.

This fix works around this by simply looking up the configuration
folder directly rather than from the ResourceFolder, since that's all
it's needed for at this point.

This probably fixeshttp://code.google.com/p/android/issues/detail?id=24578(cherry picked from commit a6a51d22457f4ae6a43db992c4712b3a8bfd0a77)

Change-Id:I4b74ae18224b693bc3c88e75b91e6f2d0bac74a4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 2e1434f..4ebc805 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.AndroidConstants.RES_QUALIFIER_SEP;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
//Synthetic comment -- @@ -725,9 +726,14 @@
mResources = ResourceManager.getInstance().getProjectResources(iProject);
}
if (mEditedConfig == null) {
                        IFolder parent = (IFolder) mEditedFile.getParent();
                        ResourceFolder resFolder = mResources.getResourceFolder(parent);
                        if (resFolder != null) {
                            mEditedConfig = resFolder.getConfiguration();
                        } else {
                            mEditedConfig = FolderConfiguration.getConfig(
                                    parent.getName().split(RES_QUALIFIER_SEP));
                        }
}

targetData = Sdk.getCurrent().getTargetData(mProjectTarget);







