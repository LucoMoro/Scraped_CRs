/*cherry pick: Fix issue with referenced Java Project.

Also improve slightly DX error reporting.

This is cherry-picked from master.

Change-Id:Iaaca9625575d87a607b21bf81636315820d76c2c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 5047e11..7af2b9d 100644

//Synthetic comment -- @@ -360,6 +360,7 @@
String[] outputs = new String[1 + projectOutputs.length];

outputs[0] = outputFolder.getLocation().toOSString();

return outputs;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/DexWrapper.java
//Synthetic comment -- index d9b8499..471b828 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
//Synthetic comment -- @@ -147,19 +146,24 @@
}

return -1;
        } catch (IllegalAccessException e) {
throw new CoreException(createErrorStatus(
                    String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, e.getMessage()), e));
        } catch (InstantiationException e) {
            throw new CoreException(createErrorStatus(
                    String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, e.getMessage()), e));
        } catch (InvocationTargetException e) {
            throw new CoreException(createErrorStatus(
                    String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, e.getMessage()), e));
}
}

    private static IStatus createErrorStatus(String message, Exception e) {
AdtPlugin.log(e, message);
AdtPlugin.printErrorToConsole(Messages.DexWrapper_Dex_Loader, message);








