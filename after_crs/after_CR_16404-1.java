/*ADT: Fix visibility of method so that Eclipse tests can access it.

Also added an annotation to indicate why it has been made
public.

SDK Bug: 2906164

Change-Id:I9490467a72989392f838a8c8e7137e929721358e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java
new file mode 100755
//Synthetic comment -- index 0000000..379371d

//Synthetic comment -- @@ -0,0 +1,51 @@
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
 * When running the AdtTest Plugin in a headless Eclipse configuration,
 * AdtTest <em>cannot</em> access package-private members of the ADT plugin
 * so we need to relax the visibility of certain classes or methods.
 * <p/>
 * The <code>visibility</code> argument can be used to specific what the original
 * visibility should have been if it had not been made public for testing.
 * The default is to consider the element private.
 */
@Retention(RetentionPolicy.CLASS)
public @interface VisibleForTesting {
    /** Intended visibility if the element had not been made public for testing. */
    enum Visibility {
        /** The element should be considered protected. */
        PROTECTED,
        /** The element should be considered package-private. */
        PACKAGE,
        /** The element should be considered private. */
        PRIVATE
    }

    /**
     * Intended visibility if the element had not been made public for testing.
     * If not specified, one should assume the element originally intented to be private.
     */
    Visibility visibility() default Visibility.PRIVATE;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleAttribute.java
//Synthetic comment -- index 8dc81a6..0ebe7ac 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.annotations.VisibleForTesting.Visibility;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement.IDragAttribute;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode.IAttribute;

//Synthetic comment -- @@ -85,7 +87,8 @@
private static final Pattern REGEXP =
Pattern.compile("[^@]*@([^:]+):([^=]*)=([^\n]*)\n*");       //$NON-NLS-1$

    @VisibleForTesting(visibility=Visibility.PACKAGE)
    public static SimpleAttribute parseString(String value) {
Matcher m = REGEXP.matcher(value);
if (m.matches()) {
return new SimpleAttribute(m.group(2), m.group(1), m.group(3));







