/*Buffer System.in.

Android apps won't notice this, but command-line tools will. Console was
already doing this.

Bug: 6603218
Change-Id:I0f65f3154f5e3ec5c49a6a2c4c87f30a846bb008*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/System.java b/luni/src/main/java/java/lang/System.java
//Synthetic comment -- index c65ebbb..f452186 100644

//Synthetic comment -- @@ -34,6 +34,7 @@

import dalvik.system.VMRuntime;
import dalvik.system.VMStack;
import java.io.BufferedInputStream;
import java.io.Console;
import java.io.FileDescriptor;
import java.io.FileInputStream;
//Synthetic comment -- @@ -83,10 +84,9 @@
private static Properties systemProperties;

static {
err = new PrintStream(new FileOutputStream(FileDescriptor.err));
out = new PrintStream(new FileOutputStream(FileDescriptor.out));
        in = new BufferedInputStream(new FileInputStream(FileDescriptor.in));
lineSeparator = System.getProperty("line.separator");
}








