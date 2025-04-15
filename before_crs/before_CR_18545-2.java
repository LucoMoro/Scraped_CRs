/*Add layout unit tests

Add layout unit tests, and some infrastructure for testing.  Also fix
some formatting errors (>100 column lines) in the previous commit.

Change-Id:I3eabf30998ab7deb84df57e4d0c10cf57ee399d5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 6d640a1..e2d64eb 100644

//Synthetic comment -- @@ -54,7 +54,11 @@
});
}

    void drawFeedback(IGraphics gc, INode targetNode, IDragElement[] elements, DropFeedback feedback) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayout.java
//Synthetic comment -- index 9d8e0c2..13b335e 100644

//Synthetic comment -- @@ -355,7 +355,8 @@
if (value != null && value.length() > 0) {
newNode.setAttribute(uri, name, value);

                if (uri.equals(ANDROID_URI) && name.equals(ATTR_ID) && oldId != null && !oldId.equals(value)) {
newId = value;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseView.java
//Synthetic comment -- index 950ea2b..9bd82a7 100644

//Synthetic comment -- @@ -143,7 +143,10 @@

IMenuCallback onChange = new IMenuCallback() {

            public void action(final MenuAction action, final String valueId, final Boolean newValue) {
String fullActionId = action.getId();
boolean isProp = fullActionId.startsWith("@prop@");
final String actionId = isProp ? fullActionId.substring(6) : fullActionId;
//Synthetic comment -- @@ -460,7 +463,11 @@
// ignore
}

    public void onDropped(INode targetNode, IDragElement[] elements, DropFeedback feedback, Point p) {
// ignore
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index f3efb64..16b477b 100755

//Synthetic comment -- @@ -51,7 +51,11 @@
});
}

    void drawFeedback(IGraphics gc, INode targetNode, IDragElement[] elements, DropFeedback feedback) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 7a960b1..3e5bdcf 100755

//Synthetic comment -- @@ -1152,7 +1152,6 @@
int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);

        // test, remove me
if (e.button == 3) {
// Right click button is used to display a context menu.
// If there's an existing selection and the click is anywhere in this selection








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2f4e22f

//Synthetic comment -- @@ -0,0 +1,73 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbstractLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbstractLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..96cdbb6

//Synthetic comment -- @@ -0,0 +1,180 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutTest.java
new file mode 100644
//Synthetic comment -- index 0000000..dad1420

//Synthetic comment -- @@ -0,0 +1,240 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseViewTest.java
new file mode 100644
//Synthetic comment -- index 0000000..c5e40c2

//Synthetic comment -- @@ -0,0 +1,43 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/FrameLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/FrameLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..2adf4f0

//Synthetic comment -- @@ -0,0 +1,63 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..8b7a644

//Synthetic comment -- @@ -0,0 +1,308 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..cc47df1

//Synthetic comment -- @@ -0,0 +1,165 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttribute.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttribute.java
new file mode 100644
//Synthetic comment -- index 0000000..fc59ba8

//Synthetic comment -- @@ -0,0 +1,54 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestAttributeInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..a864764

//Synthetic comment -- @@ -0,0 +1,56 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestColor.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestColor.java
new file mode 100644
//Synthetic comment -- index 0000000..449ad5e

//Synthetic comment -- @@ -0,0 +1,36 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java
new file mode 100644
//Synthetic comment -- index 0000000..b113ced

//Synthetic comment -- @@ -0,0 +1,142 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestGraphics.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestGraphics.java
new file mode 100644
//Synthetic comment -- index 0000000..b82f309

//Synthetic comment -- @@ -0,0 +1,148 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
new file mode 100644
//Synthetic comment -- index 0000000..21250de

//Synthetic comment -- @@ -0,0 +1,164 @@
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/ZoomControlsRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/ZoomControlsRuleTest.java
new file mode 100644
//Synthetic comment -- index 0000000..ee08633

//Synthetic comment -- @@ -0,0 +1,50 @@







