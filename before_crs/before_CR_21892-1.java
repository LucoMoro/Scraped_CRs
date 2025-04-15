/*On ImageView or ImageButton drop, ask user for drawable

Similar to the <include> tag handling, when you drop an image a
resource chooser inputting @drawable resources pops up and lets you
pick the drawable to be shown in the image. If you cancel, then the
default image will be set instead.

Change-Id:Id09801a877acbeb437f518cefe60062ac92e7e7c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java
//Synthetic comment -- index fe2e346..a65a0df 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import com.android.ide.common.api.InsertType;

/**
 * An {@link IViewRule} for android.widget.ImageButtonRule.
*/
public class ImageButtonRule extends BaseViewRule {

//Synthetic comment -- @@ -32,6 +32,17 @@
public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {
node.setAttribute(ANDROID_URI, ATTR_SRC, getSampleImageSrc());
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java
//Synthetic comment -- index d0f4da7..7ebf13d 100644

//Synthetic comment -- @@ -32,6 +32,17 @@
public void onCreate(INode node, INode parent, InsertType insertType) {
super.onCreate(node, parent, insertType);

if (insertType.isCreate()) {
node.setAttribute(ANDROID_URI, ATTR_SRC, getSampleImageSrc());
}







