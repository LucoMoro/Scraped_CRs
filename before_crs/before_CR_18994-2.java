/*Remove non-api references from AttrsXmlParser.

Change-Id:Ic139e6f942e835dda4b7ef0303556aef014a60d3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index deacd7a..bd79e29 100644

//Synthetic comment -- @@ -19,8 +19,6 @@
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.log.ILogger;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -44,6 +42,8 @@
*/
public final class AttrsXmlParser {

private Document mDocument;
private String mOsAttrsXmlPath;

//Synthetic comment -- @@ -187,7 +187,7 @@
}

/**
     * Returns a list of all <code>decleare-styleable</code> found in the XML file.
*/
public Map<String, DeclareStyleableInfo> getDeclareStyleableList() {
return Collections.unmodifiableMap(mStyleMap);
//Synthetic comment -- @@ -291,8 +291,7 @@
if (key.startsWith(name) && !key.equals(name)) {
// We found a child which name starts with the full name of the
// parent. Simplify the children name.
                    String newName = AndroidManifestDescriptors.ANDROID_MANIFEST_STYLEABLE +
                        key.substring(name.length());

DeclareStyleableInfo newStyle =
new DeclareStyleableInfo(newName, mStyleMap.get(key));
//Synthetic comment -- @@ -560,8 +559,7 @@
* Parses the javadoc comment.
* Only keeps the first sentence.
* <p/>
     * This does not remove nor simplify links and references. Such a transformation
     * is done later at "display" time in {@link DescriptorsUtils#formatTooltip(String)} and co.
*/
private String parseJavadoc(String comment) {
if (comment == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index 0a7c8c1..7c69e0b 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.common.resources.platform.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -53,7 +54,8 @@
public final class AndroidManifestDescriptors implements IDescriptorProvider {

private static final String MANIFEST_NODE_NAME = "manifest";                //$NON-NLS-1$
    public static final String ANDROID_MANIFEST_STYLEABLE = "AndroidManifest";  //$NON-NLS-1$

// Public attributes names, attributes descriptors and elements descriptors








