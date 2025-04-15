/*Refactor the manifest parser test in the correct package.

And don't make it use the Parser Helper that's eclipse specific.

Change-Id:Ie472d7d6c4847e3fae660873cca7d71e801a2a34*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index 2330020..f95edfa 100644

//Synthetic comment -- @@ -427,7 +427,6 @@
AndroidManifest.ATTRIBUTE_LARGESCREENS, true /*hasNamespace*/));
}


/**
* Searches through the attributes list for a particular one and returns its value.
* @param attributes the attribute list to search through








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/project/AndroidManifestParserTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
similarity index 91%
rename from eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/project/AndroidManifestParserTest.java
rename to sdkmanager/libs/sdklib/tests/com/android/sdklib/xml/AndroidManifestParserTest.java
//Synthetic comment -- index c52fbb2..e3b2be9 100644

//Synthetic comment -- @@ -14,16 +14,15 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.project;

import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.tests.AdtTestData;
import com.android.sdklib.xml.ManifestData;

import junit.framework.TestCase;

/**
 * Tests for {@link AndroidManifestHelper}
*/
public class AndroidManifestParserTest extends TestCase {
private ManifestData mManifestTestApp;
//Synthetic comment -- @@ -47,11 +46,11 @@
super.setUp();

String testFilePath = AdtTestData.getInstance().getTestFilePath(TESTAPP_XML);
        mManifestTestApp = AndroidManifestHelper.parseForData(testFilePath);
assertNotNull(mManifestTestApp);

testFilePath = AdtTestData.getInstance().getTestFilePath(INSTRUMENTATION_XML);
        mManifestInstrumentation = AndroidManifestHelper.parseForData(testFilePath);
assertNotNull(mManifestInstrumentation);
}








