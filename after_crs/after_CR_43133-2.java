/*gltrace: Allow ability to save images

Add a toolbar item that allows saving images displayed in the view.
This allows saving of frame buffer image at the point of eglSwap,
glDraw, and the texture image.

Change-Id:Ie1df75e41fc12981c2b7f5919caeb39b68812cff*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/FrameSummaryViewPage.java
//Synthetic comment -- index 64e9ac1..e2c20ce 100644

//Synthetic comment -- @@ -75,6 +75,9 @@
private StatsLabelProvider mStatsLabelProvider;
private StatsTableComparator mStatsTableComparator;

    private FitToCanvasAction mFitToCanvasAction;
    private SaveImageAction mSaveImageAction;

private static final String[] STATS_TABLE_PROPERTIES = {
"Function",
"Count",
//Synthetic comment -- @@ -108,8 +111,12 @@

mSash.setWeights(new int[] {70, 30});

        mFitToCanvasAction = new FitToCanvasAction(true, mImageCanvas);
        mSaveImageAction = new SaveImageAction(mImageCanvas);

IToolBarManager toolbarManager = getSite().getActionBars().getToolBarManager();
        toolbarManager.add(mFitToCanvasAction);
        toolbarManager.add(mSaveImageAction);
}

private void createFrameStatisticsPart(Composite parent) {
//Synthetic comment -- @@ -280,6 +287,9 @@
@Override
public void run() {
mImageCanvas.setImage(image);

                    mFitToCanvasAction.setEnabled(image != null);
                    mSaveImageAction.setEnabled(image != null);
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/SaveImageAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/SaveImageAction.java
new file mode 100644
//Synthetic comment -- index 0000000..6cc8b69

//Synthetic comment -- @@ -0,0 +1,59 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.ide.eclipse.gltrace.views;

import com.android.ide.eclipse.gltrace.widgets.ImageCanvas;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import java.io.File;

public class SaveImageAction extends Action {
    private static String sLastUsedPath;

    private ImageCanvas mImageCanvas;

    public SaveImageAction(ImageCanvas canvas) {
        super("Save Image",
                PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_ETOOL_SAVEAS_EDIT));
        setToolTipText("Save Image");
        mImageCanvas = canvas;
    }

    @Override
    public void run() {
        FileDialog fd = new FileDialog(mImageCanvas.getShell(), SWT.SAVE);
        fd.setFilterExtensions(new String[] { "*.png" });
        if (sLastUsedPath != null) {
            fd.setFilterPath(sLastUsedPath);
        }

        String path = fd.open();
        if (path == null) {
            return;
        }

        File f = new File(path);
        sLastUsedPath = f.getParent();
        mImageCanvas.exportImageTo(f);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/GlDrawCallDetailProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/GlDrawCallDetailProvider.java
//Synthetic comment -- index c3cabb0..44a05ff 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.gltrace.model.GLCall;
import com.android.ide.eclipse.gltrace.model.GLTrace;
import com.android.ide.eclipse.gltrace.views.FitToCanvasAction;
import com.android.ide.eclipse.gltrace.views.SaveImageAction;
import com.android.ide.eclipse.gltrace.widgets.ImageCanvas;

import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -27,12 +28,13 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.util.Arrays;
import java.util.List;

public class GlDrawCallDetailProvider implements ICallDetailProvider {
private ImageCanvas mImageCanvas;
private FitToCanvasAction mFitToCanvasAction;
    private SaveImageAction mSaveImageAction;
private List<IContributionItem> mToolBarItems;

@Override
//Synthetic comment -- @@ -47,8 +49,11 @@
mImageCanvas.setFitToCanvas(false);

mFitToCanvasAction = new FitToCanvasAction(false, mImageCanvas);
        mSaveImageAction = new SaveImageAction(mImageCanvas);

        mToolBarItems = Arrays.asList(
                (IContributionItem) new ActionContributionItem(mFitToCanvasAction),
                (IContributionItem) new ActionContributionItem(mSaveImageAction));
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java
//Synthetic comment -- index f29bbef..4dcbb10 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.gltrace.state.GLStringProperty;
import com.android.ide.eclipse.gltrace.state.IGLProperty;
import com.android.ide.eclipse.gltrace.views.FitToCanvasAction;
import com.android.ide.eclipse.gltrace.views.SaveImageAction;
import com.android.ide.eclipse.gltrace.widgets.ImageCanvas;

import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -31,12 +32,13 @@
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.util.Arrays;
import java.util.List;

public class TextureImageDetailsProvider implements IStateDetailProvider {
private ImageCanvas mImageCanvas;
private FitToCanvasAction mFitToCanvasAction;
    private SaveImageAction mSaveImageAction;
private List<IContributionItem> mToolBarItems;

@Override
//Synthetic comment -- @@ -50,8 +52,11 @@
mImageCanvas.setFitToCanvas(false);

mFitToCanvasAction = new FitToCanvasAction(false, mImageCanvas);
        mSaveImageAction = new SaveImageAction(mImageCanvas);

        mToolBarItems = Arrays.asList(
                (IContributionItem) new ActionContributionItem(mFitToCanvasAction),
                (IContributionItem) new ActionContributionItem(mSaveImageAction));
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/widgets/ImageCanvas.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/widgets/ImageCanvas.java
//Synthetic comment -- index 4ff12c3..23baf17 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.gltrace.widgets;

import com.android.ide.eclipse.gltrace.GlTracePlugin;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -23,6 +27,8 @@
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
//Synthetic comment -- @@ -32,6 +38,8 @@
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import java.io.File;

public class ImageCanvas extends Canvas {
private static final int SCROLLBAR_INCREMENT = 20;

//Synthetic comment -- @@ -223,4 +231,20 @@
mImage.dispose();
}
}

    public void exportImageTo(File file) {
        if (mImage == null || file == null) {
            return;
        }

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.data = new ImageData[] { mImage.getImageData() };

        try {
            imageLoader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
        } catch (Exception e) {
            ErrorDialog.openError(getShell(), "Save Image", "Error saving image",
                    new Status(Status.ERROR, GlTracePlugin.PLUGIN_ID, e.toString()));
        }
    }
}







