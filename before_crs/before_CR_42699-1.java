/*35959: ADT Layout Editor should fill id field for Fragments

Fragment tags should include an ID, as described inhttp://developer.android.com/guide/components/fragments.htmlChange-Id:I23001a6082db879443b289ef6fc3218b1525627a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index d53eaeb..e6f296a 100644

//Synthetic comment -- @@ -35,7 +35,6 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.REQUEST_FOCUS;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;
import static com.android.utils.XmlUtils.ANDROID_URI;
//Synthetic comment -- @@ -783,7 +782,6 @@
// By default, layouts do not need ids.
String tag = descriptor.getXmlLocalName();
if (tag.endsWith("Layout")  //$NON-NLS-1$
                || tag.equals(VIEW_FRAGMENT)
|| tag.equals(VIEW_INCLUDE)
|| tag.equals(VIEW_MERGE)
|| tag.equals(SPACE)







