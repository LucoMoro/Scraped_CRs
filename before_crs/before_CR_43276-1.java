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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLListProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLListProperty.java
//Synthetic comment -- index 6573c77..0692593 100644

//Synthetic comment -- @@ -166,4 +166,14 @@
public int size() {
return mList.size();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/GLSparseArrayProperty.java
//Synthetic comment -- index 6bf4e96..3f9984f 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

import java.util.ArrayList;
import java.util.List;

public class GLSparseArrayProperty implements IGLProperty {
private final GLStateType mType;
//Synthetic comment -- @@ -149,4 +150,17 @@
throw new UnsupportedOperationException(
"Values cannot be obtained for composite properties."); //$NON-NLS-1$
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
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/StatePrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/StatePrettyPrinter.java
new file mode 100644
//Synthetic comment -- index 0000000..c898e64

//Synthetic comment -- @@ -0,0 +1,61 @@







