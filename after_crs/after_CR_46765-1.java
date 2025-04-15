/*Rethrow an exception with stack trace information.

When a service throws one of the following RuntimeExceptions:
 * SecurityException
 * BadParcelableException
 * IllegalArgumentException
 * NullPointerException
 * IllegalStateException
It can be difficult to tell where the Exception occurs
from the client side. The Parcel only conveys the type
of Exception but does not include the stack trace information.

This change modifies the Parcel to convey the stack trace information
when it is an eng build or a userdebug build.
The 'cause' section in the client side exception contains
the server side exception with the stack trace information.
When the client side exception is output, the 'cause' section
is also output at the same time.

Change-Id:I9449b9ea02825361dd51b57ad250b9d45d913aef*/




//Synthetic comment -- diff --git a/core/java/android/os/Parcel.java b/core/java/android/os/Parcel.java
//Synthetic comment -- index 788ab74..b74db1d 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -1305,18 +1306,7 @@
* @see #readException
*/
public final void writeException(Exception e) {
        int code = getExceptionCode(e);
writeInt(code);
StrictMode.clearGatheredViolations();
if (code == 0) {
//Synthetic comment -- @@ -1326,6 +1316,22 @@
throw new RuntimeException(e);
}
writeString(e.getMessage());

        if (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug")) {
            // Write stack trace and cause when eng or userdebug build.
            // The Caused-by section of the client side exception
            // contains the server side exception information.
            // Finally they can be output at logs.
            // They are useful when server error is analyzed.

            // Write stack trace
            writeStackTraceInternal(e.getStackTrace());

            // Write cause
            Set<Throwable> causeSet = new HashSet<Throwable>();
            causeSet.add(e);
            writeCauseInternal(e.getCause(), causeSet);
        }
}

/**
//Synthetic comment -- @@ -1420,17 +1426,40 @@
* @param msg exception message
*/
public final void readException(int code, String msg) {
        RuntimeException runtimeException = null;
switch (code) {
case EX_SECURITY:
                runtimeException = new SecurityException(msg);
                break;
case EX_BAD_PARCELABLE:
                runtimeException = new BadParcelableException(msg);
                break;
case EX_ILLEGAL_ARGUMENT:
                runtimeException = new IllegalArgumentException(msg);
                break;
case EX_NULL_POINTER:
                runtimeException = new NullPointerException(msg);
                break;
case EX_ILLEGAL_STATE:
                runtimeException = new IllegalStateException(msg);
                break;
        }

        if (runtimeException != null) {
            if (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug")) {
                // Read stack trace and cause when eng or userdebug build.
                // The exception information generated on server side
                // is contained by the cause of client side exception.
                // They are useful when server error is analyzed.

                // Read cause
                Throwable cause = readCauseInternal(code, msg);
                if (cause != null) {
                    runtimeException.initCause(cause);
                }
            }

            throw runtimeException;
}
throw new RuntimeException("Unknown exception code: " + code
+ " msg " + msg);
//Synthetic comment -- @@ -2269,4 +2298,108 @@
N--;
}
}

    private static int getExceptionCode(Throwable throwable) {
        if (throwable instanceof SecurityException) {
            return EX_SECURITY;
        } else if (throwable instanceof BadParcelableException) {
            return EX_BAD_PARCELABLE;
        } else if (throwable instanceof IllegalArgumentException) {
            return EX_ILLEGAL_ARGUMENT;
        } else if (throwable instanceof NullPointerException) {
            return EX_NULL_POINTER;
        } else if (throwable instanceof IllegalStateException) {
            return EX_ILLEGAL_STATE;
        }

        return 0;
    }

    private final void writeStackTraceInternal(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements == null) {
            writeInt(-1);
            return;
        }
        writeInt(stackTraceElements.length);
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            writeString(stackTraceElement.getClassName());
            writeString(stackTraceElement.getMethodName());
            writeString(stackTraceElement.getFileName());
            writeInt(stackTraceElement.getLineNumber());
        }
    }

    private final void writeCauseInternal(Throwable cause, Set<Throwable> causeSet) {
        int code = getExceptionCode(cause);

        if (code != 0 && !causeSet.contains(cause)) {
            causeSet.add(cause);

            writeInt(code);
            writeString(cause.getMessage());

            // Write stack trace
            writeStackTraceInternal(cause.getStackTrace());

            // Write cause
            writeCauseInternal(cause.getCause(), causeSet);
        } else {
            // 0 means the exception code for no exception.
            writeInt(0);
        }
    }

    private final Throwable readCauseInternal(int code, String msg) {
        Throwable result = null;
        switch (code) {
            case EX_SECURITY:
                result = new SecurityException(msg);
                break;
            case EX_BAD_PARCELABLE:
                result = new BadParcelableException(msg);
                break;
            case EX_ILLEGAL_ARGUMENT:
                result = new IllegalArgumentException(msg);
                break;
            case EX_NULL_POINTER:
                result = new NullPointerException(msg);
                break;
            case EX_ILLEGAL_STATE:
                result = new IllegalStateException(msg);
                break;
            default:
                return null;
        }

        // Read stack trace
        int N = readInt();
        if (0 <= N) {
            StackTraceElement[] stackTraceElements = new StackTraceElement[N];
            for (int i = 0; i < N; i++) {
                String className = readString();
                String methodName = readString();
                String fileName = readString();
                int lineNumber = readInt();
                stackTraceElements[i] = new StackTraceElement(
                    className,
                    methodName,
                    fileName,
                    lineNumber
                );
            }
            result.setStackTrace(stackTraceElements);
        }

        // Read cause
        int childCode = readExceptionCode();
        if (childCode != 0) {
            String childMsg = readString();
            Throwable cause = readCauseInternal(childCode, childMsg);
            if (cause != null) {
                result.initCause(cause);
            }
        }

        return result;
    }
}







