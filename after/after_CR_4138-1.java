Avoid using the 'sample' syntax to refer to Phone Application, in the core.
This adds a build dependency on the PhoneApp, and the core build fails on
configurations that do not have Phone Application.

Build Error:
frameworks/base/core/java/android/widget/TwoLineListItem.java:31: error 2: Error reading file for include "" packages/apps/Phone/res/layout/dialer_list_item.xml




diff --git a/core/java/android/widget/TwoLineListItem.java b/core/java/android/widget/TwoLineListItem.java
index 77ea645..9a72980 100644

@@ -36,8 +36,8 @@
* that can be displayed when a TwoLineListItem has focus. Android supplies a 
* {@link android.R.layout#two_line_list_item standard layout resource for TwoLineListView} 
* (which does not include a selected item icon), but you can design your own custom XML
 * layout for this object as shown in the Phone Application.
 * (packages/apps/Phone/res/layout/dialer_list_item.xml)
* 
* @attr ref android.R.styleable#TwoLineListItem_mode
*/







