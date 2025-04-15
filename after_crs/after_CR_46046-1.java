/*Copy changes to java.io.tmpdir to $TMPDIR.

This seems like the best way out to me. It means no new bionic API, it means
we work on glibc, it means native code now behaves the same as managed code,
and it means native code no longer ends up rewriting tmpfile(3) badly itself
because the built-in C library functions don't work (because they're hard-coded
to try /tmp and then give up).

There's currently a CTS test that explicitly checks that $TMPDIR isn't set,
so we'll have to disarm that.

Bug: 6938580
Bug: 7189929
Change-Id:If412ecda35f57eaa0b0a781b8dd36c851295cb4f*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/System.java b/luni/src/main/java/java/lang/System.java
//Synthetic comment -- index 62845d6..5fec60e 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
import java.util.Properties;
import java.util.Set;
import libcore.icu.ICU;
import libcore.io.ErrnoException;
import libcore.io.Libcore;
import libcore.io.StructUtsname;
import libcore.util.ZoneInfoDB;
//Synthetic comment -- @@ -444,6 +445,16 @@
if (prop.isEmpty()) {
throw new IllegalArgumentException();
}

        // Keep the C library $TMPDIR in sync with the Java java.io.tmpdir.
        if (prop.equals("java.io.tmpdir")) {
            try {
                Libcore.os.setenv("TMPDIR", value, true);
            } catch (ErrnoException unexpected) {
                throw new AssertionError(unexpected);
            }
        }

return (String) getProperties().setProperty(prop, value);
}








