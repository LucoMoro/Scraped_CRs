/*Allow application to query whether any permissions are displayable

The package installer application hides the AppSecurityPermissions
widget if the package being installed doesn't request any permissions.
However if the package is requesting only signature-level permissions,
the widget is displayed, even though it won't list them.

This results in the display showing "Allow this application to: No
permissions required". Ideally this would be invisible.

So this patch adds a method which the app can use to query whether any
of the permissions in the list are displayable, rather than just
testing for an empty list.

Change-Id:Ia47e40b43b14f7edf06a80b794aaa6db3284772e*/
//Synthetic comment -- diff --git a/core/java/android/widget/AppSecurityPermissions.java b/core/java/android/widget/AppSecurityPermissions.java
//Synthetic comment -- index d3aa42f..0bddcbe 100755

//Synthetic comment -- @@ -203,6 +203,14 @@
return mPermsList.size();
}

public View getPermissionsView() {

mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);







