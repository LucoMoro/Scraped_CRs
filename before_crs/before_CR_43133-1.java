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

private static final String[] STATS_TABLE_PROPERTIES = {
"Function",
"Count",
//Synthetic comment -- @@ -108,8 +111,12 @@

mSash.setWeights(new int[] {70, 30});

IToolBarManager toolbarManager = getSite().getActionBars().getToolBarManager();
        toolbarManager.add(new FitToCanvasAction(true, mImageCanvas));
}

private void createFrameStatisticsPart(Composite parent) {
//Synthetic comment -- @@ -280,6 +287,9 @@
@Override
public void run() {
mImageCanvas.setImage(image);
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/SaveImageAction.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/SaveImageAction.java
new file mode 100644
//Synthetic comment -- index 0000000..ba3d498

//Synthetic comment -- @@ -0,0 +1,58 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/GlDrawCallDetailProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/GlDrawCallDetailProvider.java
//Synthetic comment -- index c3cabb0..44a05ff 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.gltrace.model.GLCall;
import com.android.ide.eclipse.gltrace.model.GLTrace;
import com.android.ide.eclipse.gltrace.views.FitToCanvasAction;
import com.android.ide.eclipse.gltrace.widgets.ImageCanvas;

import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -27,12 +28,13 @@
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.util.Collections;
import java.util.List;

public class GlDrawCallDetailProvider implements ICallDetailProvider {
private ImageCanvas mImageCanvas;
private FitToCanvasAction mFitToCanvasAction;
private List<IContributionItem> mToolBarItems;

@Override
//Synthetic comment -- @@ -47,8 +49,11 @@
mImageCanvas.setFitToCanvas(false);

mFitToCanvasAction = new FitToCanvasAction(false, mImageCanvas);
        mToolBarItems = Collections.singletonList(
                (IContributionItem) new ActionContributionItem(mFitToCanvasAction));
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/views/detail/TextureImageDetailsProvider.java
//Synthetic comment -- index f29bbef..4dcbb10 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.ide.eclipse.gltrace.state.GLStringProperty;
import com.android.ide.eclipse.gltrace.state.IGLProperty;
import com.android.ide.eclipse.gltrace.views.FitToCanvasAction;
import com.android.ide.eclipse.gltrace.widgets.ImageCanvas;

import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -31,12 +32,13 @@
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.util.Collections;
import java.util.List;

public class TextureImageDetailsProvider implements IStateDetailProvider {
private ImageCanvas mImageCanvas;
private FitToCanvasAction mFitToCanvasAction;
private List<IContributionItem> mToolBarItems;

@Override
//Synthetic comment -- @@ -50,8 +52,11 @@
mImageCanvas.setFitToCanvas(false);

mFitToCanvasAction = new FitToCanvasAction(false, mImageCanvas);
        mToolBarItems = Collections.singletonList(
                (IContributionItem) new ActionContributionItem(mFitToCanvasAction));
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/widgets/ImageCanvas.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/widgets/ImageCanvas.java
//Synthetic comment -- index 4ff12c3..6df5b5e 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.gltrace.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -23,6 +27,8 @@
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
//Synthetic comment -- @@ -223,4 +229,20 @@
mImage.dispose();
}
}
}







