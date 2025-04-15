/*Rename template file from _ prefix

One of the template files ended up not getting packaged,
probably because it started with a "_".

Also, add @Override to some methods overriding a superclass
method (onCreate(Bundle)) and fix an NPE.

(cherry picked from commit 8624e045922e68ccca262a25e44ad979447c17a9)

Change-Id:I4155d21a98c4f133ba3cf97f7b9b5042c5c54bc8*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceFolder.java b/ide_common/src/com/android/ide/common/resources/ResourceFolder.java
//Synthetic comment -- index d0d91c7..d6464c8 100644

//Synthetic comment -- @@ -251,7 +251,7 @@
* @param name the name of the file.
*/
public boolean hasFile(String name) {
        if (mNames != null && mNames.containsKey(name)) {
return true;
}









//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl
//Synthetic comment -- index cb2d894..a7bbff0 100644

//Synthetic comment -- @@ -21,6 +21,7 @@

private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});
//Synthetic comment -- @@ -67,7 +68,7 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">

@Override
public boolean onNavigationItemSelected(int position, long id) {








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/SimpleActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/SimpleActivity.java.ftl
//Synthetic comment -- index eb46d9c..70cc3f7 100644

//Synthetic comment -- @@ -8,6 +8,7 @@

public class ${activityClass} extends Activity {

    @Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});
//Synthetic comment -- @@ -21,5 +22,5 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">
}








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/TabsActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/TabsActivity.java.ftl
//Synthetic comment -- index 0811492..ba5c26c 100644

//Synthetic comment -- @@ -20,6 +20,7 @@

private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});
//Synthetic comment -- @@ -56,7 +57,7 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">

@Override
public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/TabsAndPagerActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/TabsAndPagerActivity.java.ftl
//Synthetic comment -- index 850483f..58f6452 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
*/
ViewPager mViewPager;

    @Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});
//Synthetic comment -- @@ -82,7 +83,7 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">

<#if navType?contains("tabs")>
@Override








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/_onOptionsItemSelected.java.ftl b/templates/activities/BlankActivity/root/src/app_package/include_onOptionsItemSelected.java.ftl
similarity index 100%
rename from templates/activities/BlankActivity/root/src/app_package/_onOptionsItemSelected.java.ftl
rename to templates/activities/BlankActivity/root/src/app_package/include_onOptionsItemSelected.java.ftl









//Synthetic comment -- diff --git a/templates/activities/MasterDetailFlow/root/src/app_package/ContentListActivity.java.ftl b/templates/activities/MasterDetailFlow/root/src/app_package/ContentListActivity.java.ftl
//Synthetic comment -- index d75c5c3..9edf5c8 100644

//Synthetic comment -- @@ -11,6 +11,7 @@

private boolean mTwoPane;

    @Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_${collection_name});







