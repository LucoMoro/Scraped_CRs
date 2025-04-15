/*Add ZoomButton default image

ZoomButton extends ImageButton so it was picking up the
default image, but for ZoomButtons use one of the builtin
zoom images instead.

Change-Id:Iadb4f16cc5e3bc137c25ed7fc3872a0b19bd38d6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ZoomButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ZoomButtonRule.java
new file mode 100644
//Synthetic comment -- index 0000000..ca0413e

//Synthetic comment -- @@ -0,0 +1,33 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;

public class ZoomButtonRule extends BaseViewRule {
    @Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
        super.onCreate(node, parent, insertType);

        if (insertType == InsertType.CREATE) {
            node.setAttribute(ANDROID_URI, ATTR_SRC, "@android:drawable/btn_plus"); //$NON-NLS-1$
        }
    }
}







