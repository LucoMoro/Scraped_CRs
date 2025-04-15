/*ADT: use annotations from sdklib

The removes Nullable and VisibleForTesting from ADT
and replaces them by their new versions from sdklib.

Change-Id:I2784e9d044a29a70dcd6258bb45e553246dfd477*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java
deleted file mode 100755
//Synthetic comment -- index ca0f374..0000000

//Synthetic comment -- @@ -1,38 +0,0 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a parameter or field can be null.
 * <p/>
 * When decorating a method call parameter, this denotes the parameter can
 * legitimately be null and the method will gracefully deal with it. Typically used
 * on optional parameters.
 * <p/>
 * When decorating a method, this denotes the method might legitimately return null.
 * <p/>
 * This is a marker annotation and it has no specific attributes.
 *
 * @deprecated TODO use the version moved into sdklib instead
 */
@Deprecated
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java
deleted file mode 100755
//Synthetic comment -- index 9e26bdf..0000000

//Synthetic comment -- @@ -1,53 +0,0 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes that the class, method or field has its visibility relaxed so
 * that unit tests can access it.
 * <p/>
 * The <code>visibility</code> argument can be used to specific what the original
 * visibility should have been if it had not been made public or package-private for testing.
 * The default is to consider the element private.
 *
 * @deprecated TODO use the version moved into sdklib instead
 */
@Deprecated
@Retention(RetentionPolicy.SOURCE)
public @interface VisibleForTesting {
    /**
     * Intended visibility if the element had not been made public or package-private for
     * testing.
     */
    enum Visibility {
        /** The element should be considered protected. */
        PROTECTED,
        /** The element should be considered package-private. */
        PACKAGE,
        /** The element should be considered private. */
        PRIVATE
    }

    /**
     * Intended visibility if the element had not been made public or package-private for testing.
     * If not specified, one should assume the element originally intended to be private.
     */
    Visibility visibility() default Visibility.PRIVATE;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java
//Synthetic comment -- index 44372ef..5ec12a0 100755

//Synthetic comment -- @@ -17,7 +17,7 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import com.android.ide.eclipse.adt.annotations.Nullable;

import groovy.lang.Closure;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java
//Synthetic comment -- index 942ad85..dbfa67b 100755

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import com.android.ide.eclipse.adt.annotations.Nullable;

import groovy.lang.Closure;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 5327d7c..7a74ccc 100755

//Synthetic comment -- @@ -18,8 +18,6 @@

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.editors.layout.gscripts.DropFeedback;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IClientRulesEngine;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement;
//Synthetic comment -- @@ -38,6 +36,8 @@
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;







