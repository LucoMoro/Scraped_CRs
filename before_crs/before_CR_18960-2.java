/*ADT: Extract AttrsXmlParser in com.android.ide.common

This is a pure-refactoring CL that moves AttrsXmlParser
into an ide.common.resources.platform package. In a next CL,
the parser should be cleanup to remove some references to
external classes (e.g. adtplugin is only used for logging so
it will become an ILog reference.)

The goal of the resources.platform package is to allow other IDEs
to parse the manifest schema. An utility class would be provided
here that would then be used by AndroidTargetParser. The rest of the
data parsing (widgets, resources, etc.) is a non-goal. Maybe later.

Change-Id:I4fb8eb5d168b75ef8bfab57d0b2883aea85b6167*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttributeInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttributeInfo.java
//Synthetic comment -- index 6beaa5f..6f58656 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.common.api.IAttributeInfo;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
similarity index 99%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParser.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 9b6675e..85b3c76 100644

//Synthetic comment -- @@ -14,13 +14,13 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo.LayoutParamsInfo;

import org.eclipse.core.runtime.IStatus;
import org.w3c.dom.Document;
//Synthetic comment -- @@ -32,7 +32,6 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/DeclareStyleableInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/DeclareStyleableInfo.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/DeclareStyleableInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/DeclareStyleableInfo.java
//Synthetic comment -- index 5013cf0..123e75e 100644

//Synthetic comment -- @@ -14,7 +14,9 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;











//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ViewClassInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/ViewClassInfo.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ViewClassInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/ViewClassInfo.java
//Synthetic comment -- index 893964a..82a3c34 100644

//Synthetic comment -- @@ -14,7 +14,9 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;


/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 422776a..0e1e8bd 100644

//Synthetic comment -- @@ -17,11 +17,11 @@
package com.android.ide.eclipse.adt.internal.editors.descriptors;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.sdklib.SdkConstants;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ReferenceAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ReferenceAttributeDescriptor.java
//Synthetic comment -- index 1f102af..f291bff 100644

//Synthetic comment -- @@ -18,11 +18,11 @@

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.ui.ResourceValueCellEditor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiResourceAttributeNode;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.sdklib.SdkConstants;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index d290b05..0b1f2b7 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 6dc49a1..500c8ba 100644

//Synthetic comment -- @@ -17,15 +17,15 @@
package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.SeparatorAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo.LayoutParamsInfo;
import com.android.sdklib.SdkConstants;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 5f5af7c..3916eaa 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.Rect;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -32,7 +33,6 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.NamedNodeMap;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index 1de2906..0a7c8c1 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -27,8 +29,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ReferenceAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.IStatus;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/descriptors/MenuDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/descriptors/MenuDescriptors.java
//Synthetic comment -- index a4763f7..76a7765 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.menu.descriptors;

import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo;
import com.android.sdklib.SdkConstants;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java
//Synthetic comment -- index 2eb2bf8..3e4136e 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.resources.descriptors;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.FlagAttributeDescriptor;
//Synthetic comment -- @@ -24,7 +25,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ListAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextValueDescriptor;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;










//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 121dd3f..a78da12 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.uimodel;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -31,7 +32,6 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.IUiUpdateListener.UiUpdateState;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.SdkConstants;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/descriptors/XmlDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/descriptors/XmlDescriptors.java
//Synthetic comment -- index 0dcfb17..87366d8 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.xml.descriptors;

import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
//Synthetic comment -- @@ -24,9 +27,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.SeparatorAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.sdklib.SdkConstants;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index 05b1769..56c047f 100644

//Synthetic comment -- @@ -17,17 +17,17 @@
package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.common.layoutlib.LayoutLibrary;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.xml.descriptors.XmlDescriptors;
import com.android.ide.eclipse.adt.internal.resources.AttrsXmlParser;
import com.android.ide.eclipse.adt.internal.resources.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.sdklib.IAndroidTarget;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParser.java
//Synthetic comment -- index 41f5a35..c1802f6 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.AttrsXmlParser;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.sdklib.SdkConstants;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserManifestTest.java
rename to eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
//Synthetic comment -- index 955b2d0..3033275 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.tests.AdtTestData;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserTest.java
rename to eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
//Synthetic comment -- index ae4837e..39ed330 100644

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;


import com.android.ide.common.api.IAttributeInfo.Format;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index f857126..d74b8be 100644

//Synthetic comment -- @@ -17,12 +17,12 @@
package com.android.ide.eclipse.adt.internal.editors.layout;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.mock.MockXmlNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.AttributeInfo;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.resources.Density;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index 166b63d..d3ebc57 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.internal.resources.AttrsXmlParser;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.internal.sdk.AndroidJarLoader.ClassWrapper;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.ide.eclipse.tests.AdtTestData;







