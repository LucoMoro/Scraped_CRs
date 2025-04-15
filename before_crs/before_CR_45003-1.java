/*Move HardwareConfigHelper to sdk_common.

Change-Id:Iae39e23f6c72f5a2e1444c39da1eb1a78894e9f4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 065e327..0dd81b2 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.DrawableParams;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java b/sdk_common/src/com/android/ide/common/rendering/HardwareConfigHelper.java
similarity index 95%
rename from eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/HardwareConfigHelper.java
rename to sdk_common/src/com/android/ide/common/rendering/HardwareConfigHelper.java
//Synthetic comment -- index db1c1f1..79a6171 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
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

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.HardwareConfig;
import com.android.resources.ScreenOrientation;







