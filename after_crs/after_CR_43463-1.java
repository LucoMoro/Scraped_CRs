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
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.DefaultPosition;
import com.android.tools.lint.detector.api.Detector;
//Synthetic comment -- @@ -59,9 +60,13 @@
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
//Synthetic comment -- @@ -872,6 +877,125 @@
return Sdk.getCurrent().getTargets();
}

    private boolean mSearchForSuperClasses;

    /**
     * Sets whether this client should search for super types on its own. This
     * is typically not needed when doing a full lint run (because lint will
     * look at all classes and libraries), but is useful during incremental
     * analysis when lint is only looking at a subset of classes. In that case,
     * we want to use Eclipse's data structures for super classes.
     *
     * @param search whether to use a custom Eclipse search for super class
     *            names
     */
    public void setSearchForSuperClasses(boolean search) {
        mSearchForSuperClasses = search;
    }

    /**
     * Whether this lint client is searching for super types. See
     * {@link #setSearchForSuperClasses(boolean)} for details.
     *
     * @return whether the client will search for super types
     */
    public boolean getSearchForSuperClasses() {
        return mSearchForSuperClasses;
    }

    @Override
    @Nullable
    public String getSuperClass(@NonNull Project project, @NonNull String name) {
        if (!mSearchForSuperClasses) {
            // Super type search using the Eclipse index is potentially slow, so
            // only do this when necessary
            return null;
        }

        IProject eclipseProject = getProject(project);
        if (eclipseProject == null) {
            return null;
        }

        try {
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(eclipseProject);
            if (javaProject == null) {
                return null;
            }

            String typeFqcn = ClassContext.getFqcn(name);
            IType type = javaProject.findType(typeFqcn);
            if (type != null) {
                ITypeHierarchy hierarchy = type.newSupertypeHierarchy(new NullProgressMonitor());
                IType superType = hierarchy.getSuperclass(type);
                if (superType != null) {
                    String key = superType.getKey();
                    if (!key.isEmpty()
                            && key.charAt(0) == 'L'
                            && key.charAt(key.length() - 1) == ';') {
                        return key.substring(1, key.length() - 1);
                    } else {
                        String fqcn = superType.getFullyQualifiedName();
                        return ClassContext.getInternalName(fqcn);
                    }
                }
            }
        } catch (JavaModelException e) {
            log(Severity.INFORMATIONAL, e, null);
        } catch (CoreException e) {
            log(Severity.INFORMATIONAL, e, null);
        }

        return null;
    }

    @Override
    @Nullable
    public Boolean isSubclassOf(
            @NonNull Project project,
            @NonNull String name, @NonNull
            String superClassName) {
        if (!mSearchForSuperClasses) {
            // Super type search using the Eclipse index is potentially slow, so
            // only do this when necessary
            return null;
        }

        IProject eclipseProject = getProject(project);
        if (eclipseProject == null) {
            return null;
        }

        try {
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(eclipseProject);
            if (javaProject == null) {
                return null;
            }

            String typeFqcn = ClassContext.getFqcn(name);
            IType type = javaProject.findType(typeFqcn);
            if (type != null) {
                ITypeHierarchy hierarchy = type.newSupertypeHierarchy(new NullProgressMonitor());
                IType[] allSupertypes = hierarchy.getAllSuperclasses(type);
                if (allSupertypes != null) {
                    String target = 'L' + superClassName + ';';
                    for (IType superType : allSupertypes) {
                        if (target.equals(superType.getKey())) {
                            return Boolean.TRUE;
                        }
                    }
                    return Boolean.FALSE;
                }
            }
        } catch (JavaModelException e) {
            log(Severity.INFORMATIONAL, e, null);
        } catch (CoreException e) {
            log(Severity.INFORMATIONAL, e, null);
        }

        return null;
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
                mClient.setSearchForSuperClasses(true);
} else {
EclipseLintClient.clearMarkers(mResources);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index c15b284..edb891b 100644

//Synthetic comment -- @@ -599,4 +599,46 @@

return max;
}

    /**
     * Returns the super class for the given class name, which should be in VM
     * format (e.g. java/lang/Integer, not java.lang.Integer, and using $ rather
     * than . for inner classes). If the super class is not known, returns null.
     * <p>
     * This is typically not necessary, since lint analyzes all the available
     * classes. However, if this lint client is invoking lint in an incremental
     * context (for example, an IDE offering incremental analysis of a single
     * source file), then lint may not see all the classes, and the client can
     * provide its own super class lookup.
     *
     * @param project the project containing the class
     * @param name the fully qualified class name
     * @return the corresponding super class name (in VM format), or null if not
     *         known
     */
    @Nullable
    public String getSuperClass(@NonNull Project project, @NonNull String name) {
        return null;
    }

    /**
     * Checks whether the given name is a subclass of the given super class. If
     * the method does not know, it should return true, and otherwise return
     * {@link Boolean#TRUE} or {@link Boolean#FALSE}.
     * <p>
     * Note that the class names are in internal VM format (java/lang/Integer,
     * not java.lang.Integer, and using $ rather than . for inner classes).
     *
     * @param project the project context to look up the class in
     * @param name the name of the class to be checked
     * @param superClassName the name of the super class to compare to
     * @return true if the class of the given name extends the given super class
     */
    @Nullable
    public Boolean isSubclassOf(
            @NonNull Project project,
            @NonNull String name, @NonNull
            String superClassName) {
        return null;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index a833dae..aad1ad7 100644

//Synthetic comment -- @@ -53,6 +53,7 @@
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
//Synthetic comment -- @@ -930,7 +931,19 @@
throw new IllegalStateException("Only callable during ClassScanner#checkClass");
}
assert name.indexOf('.') == -1 : "Use VM signatures, e.g. java/lang/Integer";

        String superClass = mSuperClassMap.get(name);
        if (superClass == null && mCurrentProject != null) {
            if ("java/lang/Object".equals(name)) {  //$NON-NLS-1$
                return null;
            }
            superClass = mClient.getSuperClass(mCurrentProject, name);
            if (superClass != null) {
                mSuperClassMap.put(name, superClass);
            }
        }

        return superClass;
}

/**
//Synthetic comment -- @@ -947,6 +960,13 @@
return true;
}

        if (mCurrentProject != null) {
            Boolean isSub = mClient.isSubclassOf(mCurrentProject, classNode.name, superClassName);
            if (isSub != null) {
                return isSub.booleanValue();
            }
        }

String className = classNode.name;
while (className != null) {
if (className.equals(superClassName)) {
//Synthetic comment -- @@ -1071,8 +1091,9 @@

if (entries.size() > 0) {
Collections.sort(entries);
                // No superclass info available on individual lint runs, unless
                // the client can provide it
                mSuperClassMap = Maps.newHashMap();
runClassDetectors(Scope.CLASS_FILE, entries, project, main);
}
}
//Synthetic comment -- @@ -1711,6 +1732,19 @@
public int getHighestKnownApiLevel() {
return mDelegate.getHighestKnownApiLevel();
}

        @Override
        @Nullable
        public String getSuperClass(@NonNull Project project, @NonNull String name) {
            return mDelegate.getSuperClass(project, name);
        }

        @Override
        @Nullable
        public Boolean isSubclassOf(@NonNull Project project, @NonNull String name,
                @NonNull String superClassName) {
            return mDelegate.isSubclassOf(project, name, superClassName);
        }
}

/**







