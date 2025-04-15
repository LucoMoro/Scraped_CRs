/*Template unit test fix

When iterating over all the possible API versions,
include the max version too, not just all versions
smaller than it.

Change-Id:I8f7bfa18dafff445bc2f9080ed6275355342a6c5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandlerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandlerTest.java
//Synthetic comment -- index 2f43cf0..ece9b0f 100644

//Synthetic comment -- @@ -336,7 +336,7 @@
continue;
}

                for (int targetSdk = minSdk; targetSdk < HIGHEST_KNOWN_API; targetSdk++) {
if (!isInterestingApiLevel(targetSdk)) {
continue;
}
//Synthetic comment -- @@ -447,7 +447,7 @@
continue;
}

                for (int targetSdk = minSdk; targetSdk < HIGHEST_KNOWN_API; targetSdk++) {
if (!isInterestingApiLevel(targetSdk)) {
continue;
}







