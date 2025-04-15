/*ProjectResources can now be queried for all source file for a given resource.

Change-Id:Iaff7ac33d8cdbf246e873188617d59e7c8689aa3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 08d195b..260da86 100644

//Synthetic comment -- @@ -368,6 +368,26 @@
}

/**
     * Returns the list of source files for a given resource.
     *
     * @param type the type of the resource.
     * @param name the name of the resource.
     *
     * @return a list of files generating this resource or null if it was not found.
     */
    public List<ResourceFile> getSourceFiles(ResourceType type, String name) {
        ProjectResourceItem[] resources = getResources(type);

        for (ProjectResourceItem item : resources) {
            if (name.equals(item.getName())) {
                return item.getSourceFileList();
            }
        }

        return null;
    }

    /**
* Returns the resources values matching a given {@link FolderConfiguration}.
* @param referenceConfig the configuration that each value must match.
*/







