/*38920: Fix dropdown navigation template

(cherry picked from commit 2d0e52de47d499016a0926dd4cca538fd6301128)

Change-Id:I2a06cc7a747ab681776f9f56a45c0675f9ead88b*/
//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl
//Synthetic comment -- index f2549ad..cb0665e 100644

//Synthetic comment -- @@ -1,9 +1,9 @@
package ${packageName};

<#if minApi < 14>import android.annotation.TargetApi;</#if>
import android.app.ActionBar;
import android.os.Bundle;
<#if minApi < 14>import android.content.Context;
import android.os.Build;</#if>
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//Synthetic comment -- @@ -43,7 +43,7 @@
actionBar.setListNavigationCallbacks(
// Specify a SpinnerAdapter to populate the dropdown list.
new ArrayAdapter<String>(
                        <#if minApi gte 14>actionBar.getThemedContext()<#else>getActionBarThemedContextCompat()</#if>,
android.R.layout.simple_list_item_1,
android.R.id.text1,
new String[] {
//Synthetic comment -- @@ -54,7 +54,7 @@
this);
}

    <#if minApi < 14>
/**
* Backward-compatible version of {@link ActionBar#getThemedContext()} that
* simply returns the {@link android.app.Activity} if







