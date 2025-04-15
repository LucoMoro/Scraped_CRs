/*Clean up.

Change-Id:Ib2a0ce531b6683618ff000387cef036cebd5f9b5*/
//Synthetic comment -- diff --git a/testapps/basic/AllTests.java b/testapps/basic/AllTests.java
deleted file mode 100644
//Synthetic comment -- index b19ddfc..0000000

//Synthetic comment -- @@ -1,15 +0,0 @@
package com.android.tests.basic;

import junit.framework.Test;
import junit.framework.TestSuite;

import android.test.suitebuilder.TestSuiteBuilder;

public class AllTests extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class)
                .includeAllPackagesUnderHere()
                .build();
    }
}







