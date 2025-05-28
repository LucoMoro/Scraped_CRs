
//<Beginning of snippet n. 0>


}

ThreadGroup group = Thread.currentThread().getThreadGroup();
        SamplingProfiler.ThreadSet threadSet = SamplingProfiler.newThreadGroupTheadSet(group);
samplingProfiler = new SamplingProfiler(samplingProfilerDepth, threadSet);
samplingProfiler.start(samplingProfilerMilliseconds);
startMillis = System.currentTimeMillis();

//<End of snippet n. 0>








