/*Re-enable ADT sample tests.

Change-Id:Ia774a6e0febfc7fb5efac66e42680d27f97b803d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/FuncTests.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/FuncTests.java
//Synthetic comment -- index efa8801..02c9247 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ide.eclipse.tests;

import com.android.ide.eclipse.tests.functests.layoutRendering.ApiDemosRenderingTest;
import com.android.ide.eclipse.tests.functests.sampleProjects.SampleProjectTest;

import junit.framework.TestSuite;

//Synthetic comment -- @@ -38,8 +39,7 @@
public static TestSuite suite() {
TestSuite suite = new TestSuite();

        suite.addTestSuite(SampleProjectTest.class);
suite.addTestSuite(ApiDemosRenderingTest.class);

return suite;







