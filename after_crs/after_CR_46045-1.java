/*39153: lint shows project folder instead of project name

Change-Id:I415c42e19573bf733de672625a1200e62204ec77*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 18d72db..f6b18e0 100644

//Synthetic comment -- @@ -287,6 +287,19 @@
return null;
}

    @Override
    @NonNull
    public String getProjectName(@NonNull Project project) {
        // Initialize the lint project's name to the name of the Eclipse project,
        // which might differ from the directory name
        IProject eclipseProject = getProject(project);
        if (eclipseProject != null) {
            return eclipseProject.getName();
        }

        return super.getProjectName(project);
    }

@NonNull
@Override
public Configuration getConfiguration(@NonNull Project project) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 2a68ddf..461e64f 100644

//Synthetic comment -- @@ -568,12 +568,33 @@
return project;
}

        project = createProject(dir, referenceDir);
mDirToProject.put(canonicalDir, project);
return project;
}

    /**
     * Create a project for the given directory
     * @param dir the root directory of the project
     * @param referenceDir See {@link Project#getReferenceDir()}.
     * @return a new project
     */
    @NonNull
    protected Project createProject(@NonNull File dir, @NonNull File referenceDir) {
        return Project.create(this, dir, referenceDir);
    }

    /**
     * Returns the name of the given project
     *
     * @param project the project to look up
     * @return the name of the project
     */
    @NonNull
    public String getProjectName(@NonNull Project project) {
        return project.getDir().getName();
    }

private IAndroidTarget[] mTargets;

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index fc9487d..463562b 100644

//Synthetic comment -- @@ -1763,6 +1763,12 @@
@NonNull String superClassName) {
return mDelegate.isSubclassOf(project, name, superClassName);
}

        @Override
        @NonNull
        public String getProjectName(@NonNull Project project) {
            return mDelegate.getProjectName(project);
        }
}

/**








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index eb41807..eb04231 100644

//Synthetic comment -- @@ -617,14 +617,23 @@
@NonNull
public String getName() {
if (mName == null) {
            mName = mClient.getProjectName(this);
}

return mName;
}

/**
     * Sets the name of the project
     *
     * @param name the name of the project, never null
     */
    public void setName(@NonNull String name) {
        assert !name.isEmpty();
        mName = name;
    }

    /**
* Sets whether lint should report issues in this project. See
* {@link #getReportIssues()} for a full description of what that means.
*







