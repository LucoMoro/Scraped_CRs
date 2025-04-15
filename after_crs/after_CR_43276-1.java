/*wip: add support for exporting current gl state

Change-Id:I7140f8e71167dc4a8fd8481782f29822844e28c7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLAbstractAtomicProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLAbstractAtomicProperty.java
//Synthetic comment -- index d8d9b99..cb96312 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace.state;


/**
* Abstract implementation of {@link IGLProperty}. This provides the basics that can be
* used by leaf level properties.
//Synthetic comment -- @@ -56,4 +57,9 @@
return null;
}
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, getStringValue());
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLBooleanProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLBooleanProperty.java
//Synthetic comment -- index c193d83..22d8d47 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace.state;


/** Properties that hold boolean values. */
public final class GLBooleanProperty extends GLAbstractAtomicProperty {
private final Boolean mDefaultValue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLCompositeProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLCompositeProperty.java
//Synthetic comment -- index 421b7e4..ff8b83a 100644

//Synthetic comment -- @@ -123,4 +123,14 @@
throw new UnsupportedOperationException(
"Values cannot be obtained for composite properties."); //$NON-NLS-1$
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, null);
        pp.incrementIndentLevel();
        for (IGLProperty p : mPropertiesMap.values()) {
            p.prettyPrint(pp);
        }
        pp.decrementIndentLevel();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLListProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLListProperty.java
//Synthetic comment -- index 6573c77..0692593 100644

//Synthetic comment -- @@ -166,4 +166,14 @@
public int size() {
return mList.size();
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, null);
        pp.incrementIndentLevel();
        for (IGLProperty p : mList) {
            p.prettyPrint(pp);
        }
        pp.decrementIndentLevel();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java
//Synthetic comment -- index 6bf4e96..3f9984f 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GLSparseArrayProperty implements IGLProperty {
private final GLStateType mType;
//Synthetic comment -- @@ -149,4 +150,17 @@
throw new UnsupportedOperationException(
"Values cannot be obtained for composite properties."); //$NON-NLS-1$
}

    @Override
    public void prettyPrint(StatePrettyPrinter pp) {
        pp.prettyPrint(mType, null);
        pp.incrementIndentLevel();
        for (int i = 0; i < mSparseArray.size(); i++) {
            int key = mSparseArray.keyAt(i);
            pp.prettyPrint(String.format(Locale.US, "Index %d:", key));
            IGLProperty prop = mSparseArray.get(key);
            prop.prettyPrint(pp);
        }
        pp.decrementIndentLevel();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/IGLProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/IGLProperty.java
//Synthetic comment -- index 51a526b..ed87fd2 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace.state;


/**
* The GL state is modeled as a hierarchical set of properties, all of which implement
* this interface.
//Synthetic comment -- @@ -56,4 +57,7 @@

/** Deep clone this property. */
IGLProperty clone();

    /** Pretty print current property value to the given writer. */
    void prettyPrint(StatePrettyPrinter pp);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/StatePrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/StatePrettyPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..c898e64

//Synthetic comment -- @@ -0,0 +1,61 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.gltrace.state;

import com.android.SdkUtils;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

public class StatePrettyPrinter {
    private static final int SPACES_PER_INDENT = 4;
    private static final String LINE_SEPARATOR = SdkUtils.getLineSeparator();

    private StringBuilder mSb = new StringBuilder(1000);
    private int mIndentLevel = 0;

    public void prettyPrint(@NonNull GLStateType name, @Nullable String value) {
        for (int i = 0; i < mIndentLevel * SPACES_PER_INDENT; i++) {
            mSb.append(' ');
        }

        mSb.append(name.toString());

        if (value != null) {
            mSb.append(':');
            mSb.append(value);
        }
        mSb.append(LINE_SEPARATOR);
    }

    public void prettyPrint(@NonNull String s) {
        mSb.append(s);
        mSb.append(LINE_SEPARATOR);
    }

    public void incrementIndentLevel() {
        mIndentLevel++;
    }

    public void decrementIndentLevel() {
        mIndentLevel--;
    }

    @Override
    public String toString() {
        return mSb.toString();
    }
}







