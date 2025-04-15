/*Handle theme resources in ResourceRepository.hasResourceItem

This adds the same URL rewriting logic as is already used
in parseResource in the same class

Change-Id:I67f362cacdb17ab93affcb89bd88e47a2bbe89ad*/




//Synthetic comment -- diff --git a/sdk_common/src/main/java/com/android/ide/common/resources/ResourceRepository.java b/sdk_common/src/main/java/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 542409d..a3b20ed 100755

//Synthetic comment -- @@ -242,6 +242,35 @@
* @return true if the resource is known
*/
public boolean hasResourceItem(@NonNull String url) {
        // Handle theme references
        if (url.startsWith(PREFIX_THEME_REF)) {
            String remainder = url.substring(PREFIX_THEME_REF.length());
            if (url.startsWith(ATTR_REF_PREFIX)) {
                url = PREFIX_RESOURCE_REF + url.substring(PREFIX_THEME_REF.length());
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
//Synthetic comment -- @@ -881,7 +910,7 @@
if (url.startsWith(PREFIX_THEME_REF)) {
String remainder = url.substring(PREFIX_THEME_REF.length());
if (url.startsWith(ATTR_REF_PREFIX)) {
                url = PREFIX_RESOURCE_REF + url.substring(PREFIX_THEME_REF.length());
return parseResource(url);
}
int colon = url.indexOf(':');







