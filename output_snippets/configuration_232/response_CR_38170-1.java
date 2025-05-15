//<Beginning of snippet n. 0>
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    getMenuInflater().inflate(R.menu.${menuName}, menu);
    return true;
}

@Override
public boolean onNavigationItemSelected(int position, long id) {
    // Navigation logic here
    return true;
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    getMenuInflater().inflate(R.menu.${menuName}, menu);
    return true;
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
@Override
public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    // Logic for tab unselected
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    getMenuInflater().inflate(R.menu.${menuName}, menu);
    return true;
}

<#if navType?contains("tabs")>
@Override
public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    // Logic for tab unselected
}
</#if>
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
similarity index 100%
rename from templates/activities/BlankActivity/root/src/app_package/_onOptionsItemSelected.java.ftl
rename to templates/activities/BlankActivity/root/src/app_package/include_onOptionsItemSelected.java.ftl
//<End of snippet n. 4>