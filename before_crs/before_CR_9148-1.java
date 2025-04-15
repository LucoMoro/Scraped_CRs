/*Fix issue "Permission Denial: broadcasting Intent ... requires null"

The added if corresponds to a similar check on another Permission Denial: broadcasting some pages
above within the same module.

The problem was spotted with broadcasting a ACTION_NEW_OUTGOING_CALL intent which currently does not
require any permissions. It is suggested to require a new permission MAKE_OUTGOING_CALL for the
broadcast receiver in Phone application.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 361c7f7..d443386 100644

//Synthetic comment -- @@ -10420,20 +10420,23 @@
(ResolveInfo)nextReceiver;

boolean skip = false;
            int perm = checkComponentPermission(info.activityInfo.permission,
                    r.callingPid, r.callingUid,
                    info.activityInfo.exported
                            ? -1 : info.activityInfo.applicationInfo.uid);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "Permission Denial: broadcasting "
                        + r.intent.toString()
                        + " from " + r.callerPackage + " (pid=" + r.callingPid
                        + ", uid=" + r.callingUid + ")"
                        + " requires " + info.activityInfo.permission
                        + " due to receiver " + info.activityInfo.packageName
                        + "/" + info.activityInfo.name);
                skip = true;
            }
if (r.callingUid != Process.SYSTEM_UID &&
r.requiredPermission != null) {
try {







