/*Performance: Activity manager perf unnecessary recalc of oom_adj

If an activity has bound service content providers,
updateLruProcessInternalLocked will be called recursively with
the oomAdj flag set, resulting in several recalculations of oomAdj
with unchanged data. Doing it at the end of the top level call to
updateLruProcessInternalLocked is sufficient.

Change-Id:I95e27011e1d3519f256a9bd756cbb18d43e8db29*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 16cd62b..2053a49 100644

//Synthetic comment -- @@ -1675,7 +1675,7 @@
if (cr.binding != null && cr.binding.service != null
&& cr.binding.service.app != null
&& cr.binding.service.app.lruSeq != mLruSeq) {
                    updateLruProcessInternalLocked(cr.binding.service.app, oomAdj,
updateActivityTime, i+1);
}
}
//Synthetic comment -- @@ -1683,7 +1683,7 @@
if (app.conProviders.size() > 0) {
for (ContentProviderRecord cpr : app.conProviders.keySet()) {
if (cpr.app != null && cpr.app.lruSeq != mLruSeq) {
                    updateLruProcessInternalLocked(cpr.app, oomAdj,
updateActivityTime, i+1);
}
}







