/*Fix unit test log usage

There were some test failures because unit tests were relying on
AdtPlugin's logger, which appears to be null during unit test
runs. Use a new test logger instead, which fails the current test if
anyone logs an error message, and dumps warnings to standard error and
prints to standard output.

Change-Id:I8e52fed554d49e98f7d6c8990d41831998f44640*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserManifestTest.java
//Synthetic comment -- index b10f68d..cddd63e 100755

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.resources.platform;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Arrays;
//Synthetic comment -- @@ -36,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath, AdtPlugin.getDefault());
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/resources/platform/AttrsXmlParserTest.java
//Synthetic comment -- index 3e47c35..6b0ddd3 100644

//Synthetic comment -- @@ -18,7 +18,7 @@


import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.tests.AdtTestData;

import java.util.Map;
//Synthetic comment -- @@ -36,7 +36,7 @@
@Override
public void setUp() throws Exception {
mFilePath = AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH); //$NON-NLS-1$
        mParser = new AttrsXmlParser(mFilePath, AdtPlugin.getDefault());
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParserTest.java
//Synthetic comment -- index 42f2455..9d87e2f 100644

//Synthetic comment -- @@ -19,9 +19,9 @@
import com.android.ide.common.resources.platform.AttrsXmlParser;
import com.android.ide.common.resources.platform.ViewClassInfo;
import com.android.ide.common.resources.platform.ViewClassInfo.LayoutParamsInfo;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.AndroidJarLoader.ClassWrapper;
import com.android.ide.eclipse.adt.internal.sdk.IAndroidClassLoader.IClassDescriptor;
import com.android.ide.eclipse.tests.AdtTestData;

import java.lang.reflect.Constructor;
//Synthetic comment -- @@ -62,7 +62,7 @@
super(new MockFrameworkClassLoader(),
new AttrsXmlParser(
AdtTestData.getInstance().getTestFilePath(MOCK_DATA_PATH),
                          AdtPlugin.getDefault()).preload());

mTopViewClass = new ClassWrapper(mock_android.view.View.class);
mTopGroupClass = new ClassWrapper(mock_android.view.ViewGroup.class);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/mock/TestLogger.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/mock/TestLogger.java
new file mode 100644
//Synthetic comment -- index 0000000..78919d4

//Synthetic comment -- @@ -0,0 +1,45 @@







