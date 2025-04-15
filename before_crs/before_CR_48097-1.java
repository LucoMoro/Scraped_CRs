/*Telephony: Fix issue with fdn deletion

Deletion of FDN contact with name having the "=" character
fails.

If there is a contact with "=" as part of the name, then split
will result in 3strings instead of 2(key and value).

For eg: Contact name: "="

Result with current code: [string.split("=")]
string[0] = tag
string[1] = '
string[2] = '.

Expected result:
string[0] = tag (key)
string[1] = '=' (value)

If split function with 2 arguments variation is used,
this issue will be solved. Example: string.split("=", 2)

Change-Id:I6c2d6152f9b034cae9739cd96c2fe799784dc5beAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 10805*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccProvider.java b/src/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index 77bfde2..6f2c4ed 100644

//Synthetic comment -- @@ -207,12 +207,7 @@
String param = tokens[n];
if (DBG) log("parsing '" + param + "'");

            String[] pair = param.split("=");

            if (pair.length != 2) {
                Rlog.e(TAG, "resolve: bad whereClause parameter: " + param);
                continue;
            }

String key = pair[0].trim();
String val = pair[1].trim();







