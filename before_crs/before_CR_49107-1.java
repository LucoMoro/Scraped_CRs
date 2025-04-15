/*Handle theme resources in ResourceRepository.hasResourceItem

This adds the same URL rewriting logic as is already used
in parseResource in the same class

Change-Id:Ifb71111030e4fd19b50f2b3213d486bef8b14f64*/
//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 542409d..40e37dc 100755

//Synthetic comment -- @@ -242,6 +242,35 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(@NonNull String url) {
assert url.startsWith("@") || url.startsWith("?") : url;

ensureInitialized();







