/*Avoid creating suppressedExceptions ArrayList for all Throwables

Suppressed exceptions are a Java 7 feature and therefore unlikely to be
being used. Throwable is the parent of all exceptions, so allocating
memory here is expensive. Make Throwable use Collections empty list when
no suppressed exceptions are present and then create a list when asked
to add a suppressed exception.

(cherry-pick of 24d2b0060b13d953cb5a9f10d7d2198ec3385f85.)

Change-Id:I66e4a755be66493b33c9562a8275c006b601b28e*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Throwable.java b/luni/src/main/java/java/lang/Throwable.java
//Synthetic comment -- index b561832..e88d6ce 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import libcore.util.EmptyArray;

//Synthetic comment -- @@ -63,7 +64,7 @@
* Throwables suppressed by this throwable. Null when suppressed exceptions
* are disabled.
*/
    private List<Throwable> suppressedExceptions = Collections.emptyList();

/**
* An intermediate representation of the stack trace.  This field may
//Synthetic comment -- @@ -418,6 +419,11 @@
throw new NullPointerException("suppressed == null");
}
if (suppressedExceptions != null) {
            // suppressed exceptions are enabled
            if (suppressedExceptions.isEmpty()) {
                // ensure we have somewhere to place suppressed exceptions
                suppressedExceptions = new ArrayList<Throwable>(1);
            }
suppressedExceptions.add(throwable);
}
}
//Synthetic comment -- @@ -429,7 +435,7 @@
* @hide 1.7
*/
public final Throwable[] getSuppressed() {
        return (suppressedExceptions != null && !suppressedExceptions.isEmpty())
? suppressedExceptions.toArray(new Throwable[suppressedExceptions.size()])
: EmptyArray.THROWABLE;
}







