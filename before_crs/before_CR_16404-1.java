/*ADT: Fix visibility of method so that Eclipse tests can access it.

Also added an annotation to indicate why it has been made
public.

SDK Bug: 2906164

Change-Id:I9490467a72989392f838a8c8e7137e929721358e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/VisibleForTesting.java
new file mode 100755
//Synthetic comment -- index 0000000..379371d

//Synthetic comment -- @@ -0,0 +1,51 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SimpleAttribute.java
//Synthetic comment -- index 8dc81a6..0ebe7ac 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement.IDragAttribute;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode.IAttribute;

//Synthetic comment -- @@ -85,7 +87,8 @@
private static final Pattern REGEXP =
Pattern.compile("[^@]*@([^:]+):([^=]*)=([^\n]*)\n*");       //$NON-NLS-1$

    static SimpleAttribute parseString(String value) {
Matcher m = REGEXP.matcher(value);
if (m.matches()) {
return new SimpleAttribute(m.group(2), m.group(1), m.group(3));







