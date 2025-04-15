/*Fix issue with referenced Java Project.

Also improve slightly DX error reporting.

Change-Id:I2f84f74efb9695db732ac188bd37e62bfdb533c5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 088c9ef..002ac88 100644

//Synthetic comment -- @@ -365,6 +365,7 @@
String[] outputs = new String[1 + projectOutputs.length];

outputs[0] = outputFolder.getLocation().toOSString();
        System.arraycopy(projectOutputs, 0, outputs, 1, projectOutputs.length);

return outputs;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java
//Synthetic comment -- index d9b8499..471b828 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
//Synthetic comment -- @@ -147,19 +146,24 @@
}

return -1;
        } catch (Exception e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }

            String msg = t.getMessage();
            if (msg == null) {
                msg = String.format("%s. Check the Eclipse log for stack trace.",
                        t.getClass().getName());
            }

throw new CoreException(createErrorStatus(
                    String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, msg), t));
}
}

    private static IStatus createErrorStatus(String message, Throwable e) {
AdtPlugin.log(e, message);
AdtPlugin.printErrorToConsole(Messages.DexWrapper_Dex_Loader, message);








