/*Rename template file from _ prefix

One of the template files ended up not getting packaged,
probably because it started with a "_".

Change-Id:Ic2f15d968cca156704146bdc4ec501d1359a7620*/




//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl
//Synthetic comment -- index cb2d894..0d217b2 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">

@Override
public boolean onNavigationItemSelected(int position, long id) {








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/SimpleActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/SimpleActivity.java.ftl
//Synthetic comment -- index eb46d9c..83d099f 100644

//Synthetic comment -- @@ -21,5 +21,5 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">
}








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/TabsActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/TabsActivity.java.ftl
//Synthetic comment -- index 0811492..8d5c2f5 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
    <#include "include_onOptionsItemSelected.java.ftl">

@Override
public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/TabsAndPagerActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/TabsAndPagerActivity.java.ftl
//Synthetic comment -- index 850483f..960ca2f 100644

//Synthetic comment -- @@ -82,7 +82,7 @@
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








