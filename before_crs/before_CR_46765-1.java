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
import java.util.List;
import java.util.Map;
import java.util.Set;
//Synthetic comment -- @@ -1305,18 +1306,7 @@
* @see #readException
*/
public final void writeException(Exception e) {
        int code = 0;
        if (e instanceof SecurityException) {
            code = EX_SECURITY;
        } else if (e instanceof BadParcelableException) {
            code = EX_BAD_PARCELABLE;
        } else if (e instanceof IllegalArgumentException) {
            code = EX_ILLEGAL_ARGUMENT;
        } else if (e instanceof NullPointerException) {
            code = EX_NULL_POINTER;
        } else if (e instanceof IllegalStateException) {
            code = EX_ILLEGAL_STATE;
        }
writeInt(code);
StrictMode.clearGatheredViolations();
if (code == 0) {
//Synthetic comment -- @@ -1326,6 +1316,22 @@
throw new RuntimeException(e);
}
writeString(e.getMessage());
}

/**
//Synthetic comment -- @@ -1420,17 +1426,40 @@
* @param msg exception message
*/
public final void readException(int code, String msg) {
switch (code) {
case EX_SECURITY:
                throw new SecurityException(msg);
case EX_BAD_PARCELABLE:
                throw new BadParcelableException(msg);
case EX_ILLEGAL_ARGUMENT:
                throw new IllegalArgumentException(msg);
case EX_NULL_POINTER:
                throw new NullPointerException(msg);
case EX_ILLEGAL_STATE:
                throw new IllegalStateException(msg);
}
throw new RuntimeException("Unknown exception code: " + code
+ " msg " + msg);
//Synthetic comment -- @@ -2269,4 +2298,108 @@
N--;
}
}
}







