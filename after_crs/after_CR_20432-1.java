/*ProjectResources.getSourceFiles can now be configured to only return the best match.

Change-Id:Ia66902604aaf3dc72152c06893da9f6c49c21661*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 260da86..3c5d3c5 100644

//Synthetic comment -- @@ -369,17 +369,33 @@

/**
* Returns the list of source files for a given resource.
     * Optionally, if a given {@link FolderConfiguration} is given, then only the best
     * match for this config is returned.
*
* @param type the type of the resource.
* @param name the name of the resource.
     * @param referenceConfig an optional config for which only the best match will be returned.
*
* @return a list of files generating this resource or null if it was not found.
*/
    public List<ResourceFile> getSourceFiles(ResourceType type, String name,
            FolderConfiguration referenceConfig) {

ProjectResourceItem[] resources = getResources(type);

for (ProjectResourceItem item : resources) {
if (name.equals(item.getName())) {
                if (referenceConfig != null) {
                    Resource match = findMatchingConfiguredResource(item.getSourceFileList(),
                            referenceConfig);
                    if (match instanceof ResourceFile) {
                        ArrayList<ResourceFile> list = new ArrayList<ResourceFile>();
                        list.add((ResourceFile) match);
                        return list;
                    }

                    return null;
                }
return item.getSourceFileList();
}
}







