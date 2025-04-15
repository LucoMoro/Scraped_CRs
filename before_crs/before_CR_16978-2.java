/*GLE2: Let scripts display message dialog & input dialog.

Change-Id:I0d07d5f7e672d3ef6b077c5cf24ba5f20fe1dabb*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java
new file mode 100755
//Synthetic comment -- index 0000000..6ea5b36

//Synthetic comment -- @@ -0,0 +1,35 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java
//Synthetic comment -- index a9ff834..44372ef 100755

//Synthetic comment -- @@ -17,6 +17,10 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;



/**
//Synthetic comment -- @@ -51,5 +55,24 @@
*   is fast and will return the same rule instance.
*/
IViewRule loadRule(String fqcn);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java
//Synthetic comment -- index 3674b02..63454a4 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import groovy.lang.Closure;

import java.util.Map;
//Synthetic comment -- @@ -162,6 +164,7 @@
* An optional group id, to place the action in a given sub-menu.
* @null This value can be null.
*/
private final String mGroupId;

/**
//Synthetic comment -- @@ -201,6 +204,7 @@
* Returns the optional id of an existing group or null
* @null This value can be null.
*/
public String getGroupId() {
return mGroupId;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 7790411..cbfe763 100755

//Synthetic comment -- @@ -49,6 +49,10 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
//Synthetic comment -- @@ -69,11 +73,6 @@
import java.util.List;
import java.util.Map;

/* TODO:
 * - create a logger object and pass it around.
 *
 */

/**
* The rule engine manages the groovy rules files and interacts with them.
* There's one {@link RulesEngine} instance per layout editor.
//Synthetic comment -- @@ -814,6 +813,46 @@
public IViewRule loadRule(String fqcn) {
return RulesEngine.this.loadRule(fqcn, fqcn);
}
}

}







