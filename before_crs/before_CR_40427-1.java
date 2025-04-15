/*Improve Maven defaults in lint

Change-Id:I3c7c13437403fb1392343af079afab9434695566*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index ec0ab7f..f168d7a 100644

//Synthetic comment -- @@ -423,6 +423,45 @@
}
}

// Fallback, in case there is no Eclipse project metadata here
if (sources.size() == 0) {
File src = new File(projectDir, SRC_FOLDER);
//Synthetic comment -- @@ -434,19 +473,6 @@
sources.add(gen);
}
}
            if (classes.size() == 0) {
                File folder = new File(projectDir, CLASS_FOLDER);
                if (folder.exists()) {
                    classes.add(folder);
                } else {
                    // Maven perhaps?
                    folder = new File(projectDir,
                            "target" + File.separator + "classes"); //$NON-NLS-1$ //$NON-NLS-2$
                    if (folder.exists()) {
                        classes.add(folder);
                    }
                }
            }

info = new ClassPathInfo(sources, classes, libraries);
mProjectInfo.put(project, info);







