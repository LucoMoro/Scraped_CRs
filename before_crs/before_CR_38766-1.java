/*33985: Persistent errors in Java files

This CL fixes issue 33985. There reason these error markers
were sticky, is that the IssueRegistry.PARSER_ERROR error
type is special, and was not handled properly by the
Issue.isAdequate(Scope) method which is supposed to return
whether the given issue can be fully analyzed in the given
context.

In addition, I also disabled the Java parser generating
PARSER_ERROR issues when running in Eclipse, since Eclipse
itself will already provide these errors, so all this
achieves is creating "multiple annotations on this line"
conflicts when you hover over the icon.

(cherry picked from commit afb7b2c7b91831381e5f1f096738654dcd1bb82d)

Change-Id:Ie1208a715aedb7a66ea7f992f31cd76e9e205241*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index e7c81c5..4e8f686 100644

//Synthetic comment -- @@ -59,7 +59,6 @@
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
//Synthetic comment -- @@ -944,24 +943,10 @@
try {
unit = mParser.parse(sourceUnit, compilationResult);
} catch (AbortCompilation e) {

                    String message;
                    Location location;
                    if (e.problem != null) {
                        CategorizedProblem problem = e.problem;
                        message = problem.getMessage();
                        location = Location.create(context.file,
                                new DefaultPosition(problem.getSourceLineNumber() - 1, -1,
                                        problem.getSourceStart()),
                                new DefaultPosition(problem.getSourceLineNumber() - 1, -1,
                                        problem.getSourceEnd()));
                    } else {
                        location = Location.create(context.file);
                        message = e.getCause() != null ? e.getCause().getLocalizedMessage() :
                            e.getLocalizedMessage();
                    }

                    context.report(IssueRegistry.PARSER_ERROR, location, message, null);
return null;
}
if (unit == null) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index faccdaf..1ac15e2 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.Configuration;
import com.google.common.annotations.Beta;

import java.util.ArrayList;
//Synthetic comment -- @@ -355,6 +356,10 @@
}
}

return false;
}








