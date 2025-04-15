/*Work around initialization bugs

This bug fixes some bugs related to resource initialization.  Note
that these are probably not fixing the root cause, but they are making
the symptoms less severe. I happened to run into a scenario where I
triggered the problem (the editor came up blank, see issuehttp://code.google.com/p/android/issues/detail?id=21935for example),
and I verified that reopening the editor after these fixes worked.
How the resource repository came to be empty to begin with is not
clear.

In addition to issue 21935 this workaround may also help with issuehttp://code.google.com/p/android/issues/detail?id=17522Change-Id:I0166e8c58c790888d53c46ca03348a0d8edc75b0*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index edae923..bfa942d 100644

//Synthetic comment -- @@ -155,7 +155,14 @@
*/
public ProjectResources getProjectResources(IProject project) {
synchronized (mMap) {
            ProjectResources resources = mMap.get(project);

            if (resources == null) {
                resources = new ProjectResources(project);
                mMap.put(project, resources);
            }

            return resources;
}
}









//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 87d729d..3040dc0 100644

//Synthetic comment -- @@ -355,7 +355,17 @@
* @return the {@link ResourceFolder} or null if it was not found.
*/
public ResourceFolder getResourceFolder(IAbstractFolder folder) {
        Collection<List<ResourceFolder>> values = mFolderMap.values();

        if (values.isEmpty()) { // This shouldn't be necessary, but has been observed
            try {
                loadResources(folder.getParentFolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (List<ResourceFolder> list : values) {
for (ResourceFolder resFolder : list) {
IAbstractFolder wrapper = resFolder.getFolder();
if (wrapper.equals(folder)) {







