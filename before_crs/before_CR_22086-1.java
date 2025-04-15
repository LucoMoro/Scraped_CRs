/*Update test to track layoutlib change

Change-Id:Ie4ccd0d05a85ebbb03fefb9ba1e31dfbde1a89be*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index d336b35..ed0d67b 100644

//Synthetic comment -- @@ -18,9 +18,8 @@

import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.MergeCookie;
import com.android.ide.common.rendering.api.SessionParams;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.SessionParams.AdapterItemReference;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
//Synthetic comment -- @@ -676,7 +675,7 @@
UiViewElementNode childNode = createNode(rootNode, "ListView", false);
/*UiViewElementNode grandChildNode =*/ createNode(childNode, "LinearLayout", false);
/*UiViewElementNode greatGrandChildNode =*/ createNode(childNode, "TextView", false);
        AdapterItemReference adapterItem = new SessionParams.AdapterItemReference("foo");

ViewInfo root = new ViewInfo("FrameLayout", rootNode, 0, 50, 320, 480);
ViewInfo child = new ViewInfo("ListView", childNode, 0, 0, 320, 430);







