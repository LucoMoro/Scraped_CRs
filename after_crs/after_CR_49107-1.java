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
        // Handle theme references
        if (url.startsWith(PREFIX_THEME_REF)) {
            String remainder = url.substring(PREFIX_THEME_REF.length());
            if (url.startsWith(ATTR_REF_PREFIX)) {
                url = PREFIX_RESOURCE_REF + url.substring(1);
                return hasResourceItem(url);
            }
            int colon = url.indexOf(':');
            if (colon != -1) {
                // Convert from ?android:progressBarStyleBig to ?android:attr/progressBarStyleBig
                if (remainder.indexOf('/', colon) == -1) {
                    remainder = remainder.substring(0, colon) + RESOURCE_CLZ_ATTR + '/'
                            + remainder.substring(colon);
                }
                url = PREFIX_RESOURCE_REF + remainder;
                return hasResourceItem(url);
            } else {
                int slash = url.indexOf('/');
                if (slash == -1) {
                    url = PREFIX_RESOURCE_REF + RESOURCE_CLZ_ATTR + '/' + remainder;
                    return hasResourceItem(url);
                }
            }
        }

        if (!url.startsWith(PREFIX_RESOURCE_REF)) {
            return false;
        }

assert url.startsWith("@") || url.startsWith("?") : url;

ensureInitialized();







