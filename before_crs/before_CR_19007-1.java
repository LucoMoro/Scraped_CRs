/*Fix PDT. Extension class of toolsLocator is now ToolsLocator

Change-Id:Ic2b65e07755339fc277392e4acdfe1e880a83a3d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.pdt/src/com/android/ide/eclipse/pdt/internal/AdbLocator.java b/eclipse/plugins/com.android.ide.eclipse.pdt/src/com/android/ide/eclipse/pdt/internal/ToolsLocator.java
similarity index 95%
rename from eclipse/plugins/com.android.ide.eclipse.pdt/src/com/android/ide/eclipse/pdt/internal/AdbLocator.java
rename to eclipse/plugins/com.android.ide.eclipse.pdt/src/com/android/ide/eclipse/pdt/internal/ToolsLocator.java
//Synthetic comment -- index 4576ded..2b0b243 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
/**
* Implementation of the com.android.ide.ddms.adbLocator extension point.
*/
public class AdbLocator implements IToolsLocator {

public String getAdbLocation() {
return PdtPlugin.getAdbLocation();







