/*Reject trace files created with newer versions.

Change-Id:I01e4c1553d2a6ff48913f6ff3614d0b3859c55d4*/




//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index 075485d..cc9a529 100644

//Synthetic comment -- @@ -449,6 +449,12 @@
case PARSE_VERSION:
mVersionNumber = Integer.decode(line);
mode = PARSE_OPTIONS;
                if (mVersionNumber > 2) {
                    throw new UnsupportedOperationException(
                            "Unsupported trace version number " + mVersionNumber
                            + "!  Please use a newer version of TraceView "
                            + "to read this file.");
                }
break;
case PARSE_THREADS:
parseThread(line);







