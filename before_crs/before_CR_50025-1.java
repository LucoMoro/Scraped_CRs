/*Track a typo fix in hidden libcore API.

Change-Id:I6fa3c2ef7f4d73bbf6557560e46c7c11bff74a32*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/SamplingProfilerIntegration.java b/core/java/com/android/internal/os/SamplingProfilerIntegration.java
//Synthetic comment -- index df0fcd9..6429aa4 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
}

ThreadGroup group = Thread.currentThread().getThreadGroup();
        SamplingProfiler.ThreadSet threadSet = SamplingProfiler.newThreadGroupTheadSet(group);
samplingProfiler = new SamplingProfiler(samplingProfilerDepth, threadSet);
samplingProfiler.start(samplingProfilerMilliseconds);
startMillis = System.currentTimeMillis();







