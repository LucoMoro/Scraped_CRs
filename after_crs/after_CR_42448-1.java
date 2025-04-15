/*36987: TaskHelper can't parse the tools revision number "21 rc3"

Change-Id:I4286930a2d36d21d9eb60a1c96c70276b77b9d4a*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/TaskHelper.java b/anttasks/src/com/android/ant/TaskHelper.java
//Synthetic comment -- index 24aeb8b..fe154cf 100644

//Synthetic comment -- @@ -93,6 +93,11 @@

String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
if (value != null) {
                value = value.trim();
                int space = value.indexOf(' ');
                if (space != -1) {
                    value = value.substring(0, space);
                }
return new DeweyDecimal(value);
}
} catch (FileNotFoundException e) {







