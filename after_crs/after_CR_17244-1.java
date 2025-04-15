/*There is no @note javadoc tag.

Change-Id:I4bb4e0be518b01f2a81d870ff4ec9da81555afbd*/




//Synthetic comment -- diff --git a/drm/java/android/drm/DrmManagerClient.java b/drm/java/android/drm/DrmManagerClient.java
//Synthetic comment -- index 9ee597c..7ec70da 100644

//Synthetic comment -- @@ -213,12 +213,12 @@
* Save DRM rights to specified rights path
* and make association with content path.
*
     * <p class="note">In case of OMA or WM-DRM, rightsPath and contentPath could be null.</p>
     *
* @param drmRights DrmRights to be saved
* @param rightsPath File path where rights to be saved
* @param contentPath File path where content was saved
* @throws IOException if failed to save rights information in the given path
*/
public void saveRights(
DrmRights drmRights, String rightsPath, String contentPath) throws IOException {
//Synthetic comment -- @@ -256,9 +256,9 @@
* @param path Path of the content to be handled
* @param mimeType Mimetype of the object to be handled
* @return
     *        true - if the given mimeType or path can be handled.
     *        false - cannot be handled.  false will be returned in case
     *        the state is uninitialized
*/
public boolean canHandle(String path, String mimeType) {
if ((null == path || path.equals("")) && (null == mimeType || mimeType.equals(""))) {







