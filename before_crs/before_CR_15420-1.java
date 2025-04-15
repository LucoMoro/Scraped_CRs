/*Activity/provider used is restricted to the application's Package, but can be spread across other packages as well.
A better way to construct the activity name should be by getting the substring from the fully qualified classname
itself and not using the PackageInfo.
Credit:Ritu Srivastava

Change-Id:I9ff5a27d828d5d3836bb55101cb947f670ea5941*/
//Synthetic comment -- diff --git a/apps/Development/src/com/android/development/PackageSummary.java b/apps/Development/src/com/android/development/PackageSummary.java
//Synthetic comment -- index 58ab0f7..d621d4e 100644

//Synthetic comment -- @@ -251,7 +251,7 @@
private final static void setItemText(Button item, PackageInfo pi,
String className)
{
        item.setText(className.substring(pi.packageName.length()+1));
}

private final class ActivityOnClick implements View.OnClickListener







