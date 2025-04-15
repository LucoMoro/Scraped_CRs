/*Fix traceview dump format

Change-Id:I1d40f161c8eb9ecd1f2853ace5f9858012f810deSigned-off-by: Lijuan Xiao <lijuan.xiao@intel.com>*/
//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index b49d75e..9bd6882 100644

//Synthetic comment -- @@ -668,7 +668,7 @@
System.out.print("\nMethod Stats\n");
System.out.print("Excl Cpu  Incl Cpu  Excl Real Incl Real    Calls  Method\n");
for (MethodData md : mSortedMethods) {
            System.out.format("%9d %9d %9s  %s\n",
md.getElapsedExclusiveCpuTime(), md.getElapsedInclusiveCpuTime(),
md.getElapsedExclusiveRealTime(), md.getElapsedInclusiveRealTime(),
md.getCalls(), md.getProfileName());







