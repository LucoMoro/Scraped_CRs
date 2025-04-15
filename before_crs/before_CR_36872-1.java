/*Fix interface method lookup in the API Detector

The api-versions.xml file is being updated in a separate CL to pick up
some metadata which was missing, such as some enumeration constants.
As part of the update, it's also removing the duplication of all the
interface methods in the classes that implement those methods.

This changeset updates the API Lookup code to handle this
correctly. It will now properly visit the interface hierarchy, not
just the superclass hierarchy, when computing the full set of methods
and fields inherited into a class, as well as when it determines the
introduced-in API level for each method.

Change-Id:If19697b9812eaa072536057daa5cffe3589a0c75*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java
//Synthetic comment -- index 8c35b74..9b18c03 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.tools.lint.checks;

import com.android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -38,8 +38,8 @@
private final String mName;
private final int mSince;

    private final List<Pair<String, Integer>> mSuperClasses = new ArrayList<Pair<String, Integer>>();
    private final List<Pair<String, Integer>> mInterfaces = new ArrayList<Pair<String, Integer>>();

private final Map<String, Integer> mFields = new HashMap<String, Integer>();
private final Map<String, Integer> mMethods = new HashMap<String, Integer>();
//Synthetic comment -- @@ -161,6 +161,20 @@
}
}

return min;
}

//Synthetic comment -- @@ -232,6 +246,7 @@
for (String method : mMethods.keySet()) {
set.add(method);
}
for (Pair<String, Integer> superClass : mSuperClasses) {
ApiClass clz = info.getClass(superClass.getFirst());
assert clz != null : superClass.getSecond();
//Synthetic comment -- @@ -239,6 +254,15 @@
clz.addAllMethods(info, set);
}
}
}

/**
//Synthetic comment -- @@ -259,6 +283,7 @@
for (String field : mFields.keySet()) {
set.add(field);
}
for (Pair<String, Integer> superClass : mSuperClasses) {
ApiClass clz = info.getClass(superClass.getFirst());
assert clz != null : superClass.getSecond();
//Synthetic comment -- @@ -266,6 +291,14 @@
clz.addAllFields(info, set);
}
}
    }

}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index 3163a6c..dd5585b 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
/** Relative path to the api-versions.xml database file within the Lint installation */
private static final String XML_FILE_PATH = "platform-tools/api/api-versions.xml"; //$NON-NLS-1$
private static final String FILE_HEADER = "API database used by Android lint\000";
    private static final int BINARY_FORMAT_VERSION = 2;
private static final boolean DEBUG_FORCE_REGENERATE_BINARY = false;
private static final boolean DEBUG_SEARCH = false;
private static final boolean WRITE_STATS = false;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 044778f..1b9c3ef 100644

//Synthetic comment -- @@ -62,6 +62,15 @@
assertEquals(9, mDb.getCallVersion("java/nio/Buffer", "array", "()"));
}

@Override
protected Detector getDetector() {
fail("This is not used in the ApiDatabase test");







