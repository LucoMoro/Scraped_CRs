/*Move Pair<,> from ADT to SdkLib.utils

Change-Id:I5c37e4b4dd83bc239ed242709ede70982f14705f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 559c3bf..f0005cc 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.Map;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index eeb0478..69548c3 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.IDragElement.IDragAttribute;

import java.util.Arrays;
import java.util.HashMap;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index 46b3bb7..ef0be13 100755

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.Map;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 15d18fc..7137e0a 100755

//Synthetic comment -- @@ -49,6 +49,7 @@
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.api.INode.IAttribute;

import java.util.ArrayList;
import java.util.Collections;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 18f0002..869c0e8 100755

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.layout.Pair;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -30,6 +29,7 @@
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.sdklib.annotations.VisibleForTesting;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 54b2cb3..02ff692 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
import static com.android.sdklib.xml.AndroidManifest.NODE_SERVICE;

import com.android.ide.common.layout.Pair;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
//Synthetic comment -- @@ -50,6 +49,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.annotations.VisibleForTesting;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.Augmentations;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutRuleTest.java
//Synthetic comment -- index 7255f66..8fac515 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.BaseLayoutRule.AttributeFilter;

import java.util.Arrays;
import java.util.HashMap;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java
//Synthetic comment -- index f04538a..8ccbb25 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.List;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/Pair.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/util/Pair.java
similarity index 94%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/Pair.java
rename to sdkmanager/libs/sdklib/src/com/android/sdklib/util/Pair.java
//Synthetic comment -- index e959321..e07381c 100644

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

package com.android.ide.common.layout;

/**
* A Pair class is simply a 2-tuple for use in this package. We might want to







