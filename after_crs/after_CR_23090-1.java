/*List project themes, if any, before framework themes

This changeset moves the project-themes in the combobox up above the
framework themes, since
(a) it's likely that the user wants to frequently choose these, and
(b) the list of project themes is usually much smaller than the
    framework list, so it brings these items up to the visible part of
    the combo

Change-Id:Idb989d388f5cb1f5cc6a302a6c5796db69e78c8e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 1ef6f80..525ddf3 100644

//Synthetic comment -- @@ -1523,40 +1523,8 @@
}
}

// now get the themes and languages from the project.
            int projectThemeCount = 0;
ResourceRepository projectRes = mListener.getProjectResources();
// in cases where the opened file is not linked to a project, this could be null.
if (projectRes != null) {
//Synthetic comment -- @@ -1578,12 +1546,6 @@
}
}

Collections.sort(themes);

for (String theme : themes) {
//Synthetic comment -- @@ -1592,6 +1554,44 @@
}
}
}
                projectThemeCount = themes.size();
                themes.clear();
            }

            // get the themes, and languages from the Framework.
            if (frameworkRes != null) {
                // get the configured resources for the framework
                Map<ResourceType, Map<String, ResourceValue>> frameworResources =
                    frameworkRes.getConfiguredResources(getCurrentConfig());

                if (frameworResources != null) {
                    // get the styles.
                    Map<String, ResourceValue> styles = frameworResources.get(ResourceType.STYLE);


                    // collect the themes out of all the styles.
                    for (ResourceValue value : styles.values()) {
                        String name = value.getName();
                        if (name.startsWith("Theme.") || name.equals("Theme")) {
                            themes.add(value.getName());
                        }
                    }

                    // sort them and add them to the combo
                    Collections.sort(themes);

                    if (projectThemeCount > 0 && themes.size() > 0) {
                        mThemeCombo.add(THEME_SEPARATOR);
                        mIsProjectTheme.add(Boolean.FALSE);
                    }

                    for (String theme : themes) {
                        mThemeCombo.add(theme);
                        mIsProjectTheme.add(Boolean.FALSE);
                    }

                    themes.clear();
                }
}

// try to reselect the previous theme.







