/*Reference android.R in template rather than importing it

Change-Id:I84ffc48d8c57ac3f46ab28b62be4785135f2878d*/




//Synthetic comment -- diff --git a/templates/activities/MasterDetailFlow/root/src/app_package/ContentListActivity.java.ftl b/templates/activities/MasterDetailFlow/root/src/app_package/ContentListActivity.java.ftl
//Synthetic comment -- index 9edf5c8..4456815 100644

//Synthetic comment -- @@ -3,8 +3,10 @@
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
<#if parentActivityClass != "">
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
</#if>

public class ${CollectionName}Activity extends FragmentActivity
implements ${CollectionName}Fragment.Callbacks {








//Synthetic comment -- diff --git a/templates/activities/MasterDetailFlow/root/src/app_package/ContentListFragment.java.ftl b/templates/activities/MasterDetailFlow/root/src/app_package/ContentListFragment.java.ftl
//Synthetic comment -- index 6b4b9a0..7de2868 100644

//Synthetic comment -- @@ -2,7 +2,6 @@

import ${packageName}.dummy.DummyContent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
//Synthetic comment -- @@ -35,8 +34,8 @@
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
DummyContent.ITEMS));
}








