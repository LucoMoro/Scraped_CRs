/*Fix copyright and clarify silent logger

Fix missing header in previous checkin.

Also remove the silent logger from the render() method, and create the
silent logger in the palette preview drag where it's more obvious why
the rendering is quiet.

Change-Id:I80d2be0c40d00589182988262f1d0f2e64ee4dd4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 87dc966..beaeb50 100644

//Synthetic comment -- @@ -1198,10 +1198,12 @@
* @param transparentBackground If true, the rendering will <b>not</b> paint the
*            normal background requested by the theme, and it will instead paint the
*            background using a fully transparent background color
     * @param logger a logger where rendering errors are reported
* @return the resulting rendered image wrapped in an {@link RenderSession}
*/
public RenderSession render(UiDocumentNode model, int width, int height,
            Set<UiElementNode> explodeNodes, boolean transparentBackground,
            LayoutLog logger) {
if (!ensureFileValid()) {
return null;
}
//Synthetic comment -- @@ -1212,7 +1214,7 @@

IProject iProject = mEditedFile.getProject();
return renderWithBridge(iProject, model, layoutLib, width, height, explodeNodes,
                transparentBackground, logger);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 5b00a70..e5e3a13 100755

//Synthetic comment -- @@ -24,11 +24,12 @@

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -770,9 +771,9 @@
org.eclipse.draw2d.geometry.Rectangle screenBounds = editor.getScreenBounds();
int renderWidth = Math.min(screenBounds.width, MAX_RENDER_WIDTH);
int renderHeight = Math.min(screenBounds.height, MAX_RENDER_HEIGHT);
                LayoutLog silentLogger = new LayoutLog();
session = editor.render(model, renderWidth, renderHeight,
                    null /* explodeNodes */, hasTransparency, silentLogger);
} catch (Throwable t) {
// Previews can fail for a variety of reasons -- let's not bug
// the user with it








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index d7c5af4..bc89b24 100644

//Synthetic comment -- @@ -1,3 +1,18 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;








