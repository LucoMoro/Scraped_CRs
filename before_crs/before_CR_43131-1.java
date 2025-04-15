/*Improvements to stacktrace handler for constructors

This changeset improves the SourceRevealer for certain types of
stacktraces:

(1) Constructors. If your class initializes fields using code, these
    are logically grouped into the constructor, but the linenumbers do
    not fit the constructor source range. Before this CL, this would
    cause the revealer to show the constructor method rather than the
    field initialization line.

(2) Class initializers. If you clicked on a class initializer method,
    the IDE would throw an NPE. There's now some special handling to
    deal with these.

Change-Id:Ibb3b7dcf13a69018f75fd12648d1f6b5c45625cc*/
//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 1e1d14f..1aa3853 100644

//Synthetic comment -- @@ -957,6 +957,7 @@

// Class Names
public static final String CONSTRUCTOR_NAME = "<init>";                          //$NON-NLS-1$
public static final String FRAGMENT = "android/app/Fragment";                    //$NON-NLS-1$
public static final String FRAGMENT_V4 = "android/support/v4/app/Fragment";      //$NON-NLS-1$
public static final String ANDROID_APP_ACTIVITY = "android/app/Activity";        //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java
//Synthetic comment -- index 85f6992..b1b5390 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt;

import static com.android.SdkConstants.CONSTRUCTOR_NAME;

import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -31,8 +32,10 @@
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
//Synthetic comment -- @@ -138,9 +141,15 @@
// See if the line number looks like it's inside the given method
ISourceRange sourceRange = method.getSourceRange();
IRegion region = AdtUtils.getRegionOfLine(file, lineNumber - 1);
                                if (region != null
                                        && region.getOffset() >= sourceRange.getOffset()
                                        && region.getOffset() < sourceRange.getOffset()
+ sourceRange.getLength()) {
// Yes: use the line number instead
if (perspective != null) {
//Synthetic comment -- @@ -167,6 +176,29 @@
if (fileMatches.size() > 0) {
return revealLineMatch(fileMatches, fileName, lineNumber, perspective);
} else {
return false;
}
}
//Synthetic comment -- @@ -349,6 +381,11 @@
return searchForPattern(fqmn, IJavaSearchConstants.CONSTRUCTOR,
MATCH_IS_METHOD_PREDICATE);
}
return searchForPattern(fqmn, IJavaSearchConstants.METHOD, MATCH_IS_METHOD_PREDICATE);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index ed93b73..55cad2b 100644

//Synthetic comment -- @@ -409,7 +409,7 @@
* Returns the minimum SDK version name (which may not be a numeric string, e.g.
* it could be a codename). It will never be null or empty; if no min sdk version
* was specified in the manifest, the return value will be "1". Use
     * {@link #getCodeName()} instead if you want to look up whether there is a code name.
*
* @return the minimum SDK version
*/







