/*ADT: use annotations from sdklib

The removes Nullable and VisibleForTesting from ADT
and replaces them by their new versions from sdklib.

Change-Id:I2784e9d044a29a70dcd6258bb45e553246dfd477*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java
deleted file mode 100755
//Synthetic comment -- index ca0f374..0000000

//Synthetic comment -- @@ -1,38 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java
deleted file mode 100755
//Synthetic comment -- index 9e26bdf..0000000

//Synthetic comment -- @@ -1,53 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java
//Synthetic comment -- index 44372ef..5ec12a0 100755

//Synthetic comment -- @@ -17,7 +17,7 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import com.android.sdklib.annotations.Nullable;

import groovy.lang.Closure;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java
//Synthetic comment -- index 942ad85..dbfa67b 100755

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import com.android.sdklib.annotations.Nullable;

import groovy.lang.Closure;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 5327d7c..7a74ccc 100755

//Synthetic comment -- @@ -18,8 +18,6 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.editors.layout.gscripts.DropFeedback;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IClientRulesEngine;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement;
//Synthetic comment -- @@ -38,6 +36,8 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;
import com.android.sdklib.annotations.VisibleForTesting.Visibility;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;







