/*ADT: Extract AttrsXmlParser in com.android.ide.common

This is a pure-refactoring CL that moves AttrsXmlParser
into an ide.common.platformData package. In a next CL, the
parser should be cleanup to remove some references to
external classes (e.g. adtplugin is only used for logging so
it will become an ILog reference.)

The goal of the platformData package is to allow other IDEs
to parse the manifest schema. An utility class would be provided
here that would then be used by AndroidTargetParser. The rest of the
data parsing (widgets, resources, etc.) is a non-goal. Maybe later.

Change-Id:I4fb8eb5d168b75ef8bfab57d0b2883aea85b6167*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttributeInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/AttributeInfo.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttributeInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/AttributeInfo.java
//Synthetic comment -- index 6beaa5f..efb96b2 100755

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.common.api.IAttributeInfo;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/AttrsXmlParser.java
similarity index 99%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParser.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/AttrsXmlParser.java
//Synthetic comment -- index 9b6675e..c49c5f5 100644

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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/DeclareStyleableInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/DeclareStyleableInfo.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/DeclareStyleableInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/DeclareStyleableInfo.java
//Synthetic comment -- index 5013cf0..67044ca 100644

//Synthetic comment -- @@ -14,7 +14,8 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;











//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ViewClassInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/ViewClassInfo.java
similarity index 98%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ViewClassInfo.java
rename to eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/platformData/ViewClassInfo.java
//Synthetic comment -- index 893964a..e377ca5 100644

//Synthetic comment -- @@ -14,7 +14,8 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.resources;


/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index 422776a..63ef27c 100644

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
//Synthetic comment -- index 1f102af..3b1c02a 100644

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
//Synthetic comment -- index d290b05..b3693c1 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index 6dc49a1..2f84ab5 100644

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
//Synthetic comment -- index 5f5af7c..6986640 100755

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
//Synthetic comment -- index 1de2906..a3e757c 100644

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
//Synthetic comment -- index a4763f7..9c7ffd4 100644

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
//Synthetic comment -- index 2eb2bf8..5c6828c 100644

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
//Synthetic comment -- index 121dd3f..b04f6cf 100644

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
//Synthetic comment -- index 0dcfb17..c5bfae7 100644

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
//Synthetic comment -- index 05b1769..b5701de 100644

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
//Synthetic comment -- index 41f5a35..8b1b52d 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.AttrsXmlParser;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.sdklib.SdkConstants;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParserTest.java
//Synthetic comment -- index f857126..5228b1c 100644

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









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserManifestTest.java
//Synthetic comment -- index 955b2d0..aae898d 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.resources;

import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Arrays;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/AttrsXmlParserTest.java
//Synthetic comment -- index ae4837e..d5dffaa 100644

//Synthetic comment -- @@ -18,6 +18,10 @@


import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Map;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index 166b63d..3b94b34 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.internal.resources.AttrsXmlParser;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo;
import com.android.ide.eclipse.adt.internal.resources.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.internal.sdk.AndroidJarLoader.ClassWrapper;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.ide.eclipse.tests.AdtTestData;







