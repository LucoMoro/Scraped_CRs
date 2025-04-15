/*37733: TargetAPI doesn't fails to report unsupported API

Make incremental lint in source files use the Eclipse context
to find super classes.

Change-Id:I31af7a146c259875857026bc7721294205482a91*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index b3303b3..ab850e1 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.tools.lint.client.api.IJavaParser;
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.DefaultPosition;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -59,9 +60,13 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
//Synthetic comment -- @@ -872,6 +877,125 @@
return Sdk.getCurrent().getTargets();
}

private static class LazyLocation extends Location implements Location.Handle {
private final IStructuredDocument mDocument;
private final IndexedRegion mRegion;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java
//Synthetic comment -- index 2851c47..95e9f18 100644

//Synthetic comment -- @@ -144,6 +144,7 @@
marker.delete();
}
}
} else {
EclipseLintClient.clearMarkers(mResources);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index c15b284..2e21c06 100644

//Synthetic comment -- @@ -599,4 +599,46 @@

return max;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index a833dae..aad1ad7 100644

//Synthetic comment -- @@ -53,6 +53,7 @@
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
//Synthetic comment -- @@ -930,7 +931,19 @@
throw new IllegalStateException("Only callable during ClassScanner#checkClass");
}
assert name.indexOf('.') == -1 : "Use VM signatures, e.g. java/lang/Integer";
        return mSuperClassMap.get(name);
}

/**
//Synthetic comment -- @@ -947,6 +960,13 @@
return true;
}

String className = classNode.name;
while (className != null) {
if (className.equals(superClassName)) {
//Synthetic comment -- @@ -1071,8 +1091,9 @@

if (entries.size() > 0) {
Collections.sort(entries);
                // No superclass info available on individual lint runs
                mSuperClassMap = Collections.emptyMap();
runClassDetectors(Scope.CLASS_FILE, entries, project, main);
}
}
//Synthetic comment -- @@ -1711,6 +1732,19 @@
public int getHighestKnownApiLevel() {
return mDelegate.getHighestKnownApiLevel();
}
}

/**







