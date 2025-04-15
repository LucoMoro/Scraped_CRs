/*Replace (most) raw types with proper generic types + some tidying

Most of the minefield of raw types have been properly set as well as
removing some unused imports and variables.

I think there's still a lot of work to be done here - classes and variables
beginning with "my" ("myComparator","myData") can't be good and many
other variable names are poorly chosen - I'm happy to tidy up this but
hopefully this is a good start :)

Change-Id:Ib942fc4fef4ee1304b008a9f3d679d58fb16c6e3*/
//Synthetic comment -- diff --git a/src/com/android/settings/ZoneList.java b/src/com/android/settings/ZoneList.java
//Synthetic comment -- index aaaf989..b166204 100644

//Synthetic comment -- @@ -21,19 +21,16 @@
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
//Synthetic comment -- @@ -58,8 +55,6 @@
private static final String XMLTAG_TIMEZONE = "timezone";

private static final int HOURS_1 = 60 * 60000;
    private static final int HOURS_24 = 24 * HOURS_1;
    private static final int HOURS_HALF = HOURS_1 / 2;

private static final int MENU_TIMEZONE = Menu.FIRST+1;
private static final int MENU_ALPHABETICAL = Menu.FIRST;
//Synthetic comment -- @@ -81,7 +76,7 @@

MyComparator comparator = new MyComparator(KEY_OFFSET);

        List<HashMap> timezoneSortedList = getZones();
Collections.sort(timezoneSortedList, comparator);
mTimezoneSortedAdapter = new SimpleAdapter(this,
(List) timezoneSortedList,
//Synthetic comment -- @@ -89,7 +84,8 @@
from,
to);

        List<HashMap> alphabeticalList = new ArrayList<HashMap>(timezoneSortedList);
comparator.setSortingKey(KEY_DISPLAYNAME);
Collections.sort(alphabeticalList, comparator);
mAlphabeticalAdapter = new SimpleAdapter(this,
//Synthetic comment -- @@ -154,8 +150,8 @@
mSortedByTimezone = timezone;
}

    private List<HashMap> getZones() {
        List<HashMap> myData = new ArrayList<HashMap>();
long date = Calendar.getInstance().getTimeInMillis();
try {
XmlResourceParser xrp = getResources().getXml(R.xml.timezones);
//Synthetic comment -- @@ -189,9 +185,9 @@
return myData;
}

    protected void addItem(List<HashMap> myData, String id, String displayName, 
long date) {
        HashMap map = new HashMap();
map.put(KEY_ID, id);
map.put(KEY_DISPLAYNAME, displayName);
TimeZone tz = TimeZone.getTimeZone(id);
//Synthetic comment -- @@ -238,7 +234,7 @@
}

private static class MyComparator implements Comparator<HashMap> {
        private String mSortingKey; 

public MyComparator(String sortingKey) {
mSortingKey = sortingKey;
//Synthetic comment -- @@ -266,7 +262,7 @@
}

private boolean isComparable(Object value) {
            return (value != null) && (value instanceof Comparable); 
}
}









//Synthetic comment -- diff --git a/src/com/android/settings/ZonePicker.java b/src/com/android/settings/ZonePicker.java
//Synthetic comment -- index def5036..492faaa 100644

//Synthetic comment -- @@ -39,8 +39,8 @@
setListAdapter(mFilterAdapter);
}

    protected void addItem(List<Map> data, String name, String zone) {
        HashMap temp = new HashMap();
temp.put("title", name);
temp.put("zone", zone);
data.add(temp);







