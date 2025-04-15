/*Improve Maven defaults in lint

Change-Id:I3c7c13437403fb1392343af079afab9434695566*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index ec0ab7f..f168d7a 100644

//Synthetic comment -- @@ -423,6 +423,45 @@
}
}

            if (classes.size() == 0) {
                File folder = new File(projectDir, CLASS_FOLDER);
                if (folder.exists()) {
                    classes.add(folder);
                } else {
                    // Maven checks
                    folder = new File(projectDir,
                            "target" + File.separator + "classes"); //$NON-NLS-1$ //$NON-NLS-2$
                    if (folder.exists()) {
                        classes.add(folder);

                        // If it's maven, also correct the source path, "src" works but
                        // it's in a more specific subfolder
                        if (sources.size() == 0) {
                            File src = new File(projectDir,
                                    "src" + File.separator     //$NON-NLS-1$
                                    + "main" + File.separator  //$NON-NLS-1$
                                    + "java");                 //$NON-NLS-1$
                            if (src.exists()) {
                                sources.add(src);
                            } else {
                                src = new File(projectDir, SRC_FOLDER);
                                if (src.exists()) {
                                    sources.add(src);
                                }
                            }

                            File gen = new File(projectDir,
                                    "target" + File.separator                  //$NON-NLS-1$
                                    + "generated-sources" + File.separator     //$NON-NLS-1$
                                    + "r");                                    //$NON-NLS-1$
                            if (gen.exists()) {
                                sources.add(gen);
                            }
                        }
                    }
                }
            }

// Fallback, in case there is no Eclipse project metadata here
if (sources.size() == 0) {
File src = new File(projectDir, SRC_FOLDER);
//Synthetic comment -- @@ -434,19 +473,6 @@
sources.add(gen);
}
}

info = new ClassPathInfo(sources, classes, libraries);
mProjectInfo.put(project, info);







