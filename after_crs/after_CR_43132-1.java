/*gltrace: Better progress indicator

Use the percent of file parsed as an indicator rather than having
an indeterminate progress bar.

Change-Id:I7a904e007c6ec195cb0e0bb2de7483e3b585489b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceFileParserTask.java
//Synthetic comment -- index c3ea122..f525657 100644

//Synthetic comment -- @@ -155,7 +155,15 @@
@Override
public void run(IProgressMonitor monitor) throws InvocationTargetException,
InterruptedException {
        long fileLength;
        try {
            fileLength = mFile.length();
        } catch (IOException e1) {
            fileLength = 0;
        }

        monitor.beginTask("Parsing OpenGL Trace File",
                fileLength > 0 ? 100 : IProgressMonitor.UNKNOWN);

List<GLFrame> glFrames = null;

//Synthetic comment -- @@ -163,6 +171,7 @@
GLMessage msg = null;
int msgCount = 0;
long filePointer = mFile.getFilePointer();
            int percentParsed = 0;

// counters that maintain some statistics about the trace messages
long minTraceStartTime = Long.MAX_VALUE;
//Synthetic comment -- @@ -180,6 +189,12 @@
if (monitor.isCanceled()) {
throw new InterruptedException();
}

                if (fileLength > 0) {
                    int percentParsedNow = (int)((filePointer * 100) / fileLength);
                    monitor.worked(percentParsedNow - percentParsed);
                    percentParsed = percentParsedNow;
                }
}

if (mGLContextIds.size() > 1) {







