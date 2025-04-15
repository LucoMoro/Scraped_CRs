/*Handle theme resources in ResourceRepository.hasResourceItem

This adds the same URL rewriting logic as is already used
in parseResource in the same class

Change-Id:Ifb71111030e4fd19b50f2b3213d486bef8b14f64*/
//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 542409d..a3b20ed 100755

//Synthetic comment -- @@ -242,6 +242,35 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(@NonNull String url) {
assert url.startsWith("@") || url.startsWith("?") : url;

ensureInitialized();
//Synthetic comment -- @@ -881,7 +910,7 @@
if (url.startsWith(PREFIX_THEME_REF)) {
String remainder = url.substring(PREFIX_THEME_REF.length());
if (url.startsWith(ATTR_REF_PREFIX)) {
                url = PREFIX_RESOURCE_REF + url.substring(1);
return parseResource(url);
}
int colon = url.indexOf(':');







