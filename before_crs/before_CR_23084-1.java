/*Move Grid{Data,Layout}Builder to SDK UI Lib. DO NOT MERGE.

(cherry picked from commit 7f7cdef2e2c1741114588f4f8c774fabb9567dde)

Change-Id:I6301730b125aab3c22e0893e9adb46003a4a2315*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/DecorComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/DecorComposite.java
//Synthetic comment -- index 4edb09f..b7cc44e 100755

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/GridDataBuilder.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridDataBuilder.java
similarity index 94%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/GridDataBuilder.java
rename to sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridDataBuilder.java
//Synthetic comment -- index 6c95cc5..2e7367a 100755

//Synthetic comment -- @@ -1,11 +1,11 @@
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
//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.editors.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/GridLayoutBuilder.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java
similarity index 92%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/GridLayoutBuilder.java
rename to sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java
//Synthetic comment -- index 5d51525..fbb31ce 100755

//Synthetic comment -- @@ -1,11 +1,11 @@
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
//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ide.eclipse.adt.internal.editors.ui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
//Synthetic comment -- @@ -41,7 +41,7 @@
*/
static public GridLayoutBuilder create(Composite parent) {
GridLayoutBuilder glh = new GridLayoutBuilder();
        parent.setLayout(glh.mGL);
return glh;
}








