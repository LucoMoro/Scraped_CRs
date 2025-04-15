/*fix calendar crash when deleting events in agenda view

calendar is stopped and JAVACRASH occurs after deleting some events in Calendar's Agenda view

Change-Id:I9da477a6ba6ac68327d5f64f44c76618d612a248Author: Huaqiang Chen <huaqiangx.chen@intel.com>
Signed-off-by: b566 <b566@borqs.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 48926*/




//Synthetic comment -- diff --git a/src/com/android/calendar/AsyncQueryServiceHelper.java b/src/com/android/calendar/AsyncQueryServiceHelper.java
//Synthetic comment -- index 36ee581..615a779 100644

//Synthetic comment -- @@ -312,7 +312,14 @@
break;

case Operation.EVENT_ARG_DELETE:
                    try {
                        args.result = resolver.delete(args.uri, args.selection, args.selectionArgs);
                    } catch (IllegalArgumentException e) {
                        Log.w(TAG, "Delete failed.");
                        Log.w(TAG, e.toString());
                        args.result = 0;
                    }

break;

case Operation.EVENT_ARG_BATCH:







