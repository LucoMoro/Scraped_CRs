//<Beginning of snippet n. 0>


getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
<#include "onOptionsItemSelected.java.ftl">

@Override
public boolean onNavigationItemSelected(int position, long id) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
<#include "onOptionsItemSelected.java.ftl">
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
<#include "onOptionsItemSelected.java.ftl">

@Override
public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


getMenuInflater().inflate(R.menu.${menuName}, menu);
return true;
}
<#include "onOptionsItemSelected.java.ftl">

<#if navType?contains("tabs")>
@Override

//<End of snippet n. 3>










//<Beginning of snippet n. 4>

similarity index 100%
rename from templates/activities/BlankActivity/root/src/app_package/_onOptionsItemSelected.java.ftl
rename to templates/activities/BlankActivity/root/src/app_package/include_onOptionsItemSelected.java.ftl


//<End of snippet n. 4>