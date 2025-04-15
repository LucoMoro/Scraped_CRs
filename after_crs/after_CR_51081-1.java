/*Fix date parsing for time zones without names.

Bug: 8128460
Change-Id:I41a6baee6e22b8af9170e3e569e0599c38b5e707*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DateFormatSymbols.java b/luni/src/main/java/java/text/DateFormatSymbols.java
//Synthetic comment -- index e2a2345..6049fd2 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import libcore.icu.ICU;
import libcore.icu.LocaleData;
import libcore.icu.TimeZones;
//Synthetic comment -- @@ -331,7 +332,25 @@
* </ul>
*/
public String[][] getZoneStrings() {
        String[][] result = clone2dStringArray(internalZoneStrings());
        // If icu4c doesn't have a name, our array contains a null. TimeZone.getDisplayName
        // knows how to format GMT offsets (and, unlike icu4c, has accurate data). http://b/8128460.
        for (String[] zone : result) {
            String id = zone[0];
            if (zone[1] == null) {
                zone[1] = TimeZone.getTimeZone(id).getDisplayName(false, TimeZone.LONG, locale);
            }
            if (zone[2] == null) {
                zone[2] = TimeZone.getTimeZone(id).getDisplayName(false, TimeZone.SHORT, locale);
            }
            if (zone[3] == null) {
                zone[3] = TimeZone.getTimeZone(id).getDisplayName(true, TimeZone.LONG, locale);
            }
            if (zone[4] == null) {
                zone[4] = TimeZone.getTimeZone(id).getDisplayName(true, TimeZone.LONG, locale);
            }
        }
        return result;
}

private static String[][] clone2dStringArray(String[][] array) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index b856d7a..e8aea4a 100644

//Synthetic comment -- @@ -1151,8 +1151,7 @@
offset += 3;
}
char sign;
        if (offset < string.length() && ((sign = string.charAt(offset)) == '+' || sign == '-')) {
ParsePosition position = new ParsePosition(offset + 1);
Number result = numberFormat.parse(string, position);
if (result == null) {
//Synthetic comment -- @@ -1182,16 +1181,21 @@
calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
return offset;
}
        for (String[] row : formatData.internalZoneStrings()) {
            for (int i = TimeZones.LONG_NAME; i < TimeZones.NAME_COUNT; ++i) {
                if (row[i] == null) {
                    // If icu4c doesn't have a name, our array contains a null. Normally we'd
                    // work out the correct GMT offset, but we already handled parsing GMT offsets
                    // above, so we can just ignore these cases. http://b/8128460.
                    continue;
                }
                if (string.regionMatches(true, offset, row[i], 0, row[i].length())) {
                    TimeZone zone = TimeZone.getTimeZone(row[TimeZones.OLSON_NAME]);
if (zone == null) {
return -offset - 1;
}
int raw = zone.getRawOffset();
                    if (i == TimeZones.LONG_NAME_DST || i == TimeZones.SHORT_NAME_DST) {
// Not all time zones use a one-hour difference, so we need to query
// the TimeZone. (Australia/Lord_Howe is the usual example of this.)
int dstSavings = zone.getDSTSavings();
//Synthetic comment -- @@ -1206,7 +1210,7 @@
raw += dstSavings;
}
calendar.setTimeZone(new SimpleTimeZone(raw, ""));
                    return offset + row[i].length();
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index 328ab60..187c434 100644

//Synthetic comment -- @@ -184,7 +184,9 @@
// which only gets updated when we update icu4c. http://b/7955614 and http://b/8026776.

// TODO: should we generate these once, in TimeZones.getDisplayName? Revisit when we
        // upgrade to icu4c 50 and rewrite the underlying native code. See also the
        // "element[j] != null" check in SimpleDateFormat.parseTimeZone, and the extra work in
        // DateFormatSymbols.getZoneStrings.

int offset = getRawOffset();
if (daylightTime) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/DateFormatSymbolsTest.java b/luni/src/test/java/libcore/java/text/DateFormatSymbolsTest.java
//Synthetic comment -- index 319a76e..4d9c87d 100644

//Synthetic comment -- @@ -123,4 +123,22 @@
assertEquals(Arrays.toString(row), "UTC", row[2]);
assertEquals(Arrays.toString(row), "UTC", row[4]);
}

    // http://b/8128460
    // If icu4c doesn't actually have a name, we arrange to return null from native code rather
    // that use icu4c's probably-out-of-date time zone transition data.
    // getZoneStrings has to paper over this.
    public void test_getZoneStrings_no_nulls() throws Exception {
        String[][] array = DateFormatSymbols.getInstance(Locale.US).getZoneStrings();
        int failCount = 0;
        for (String[] row : array) {
            for (String element : row) {
                if (element == null) {
                    System.err.println(Arrays.toString(row));
                    ++failCount;
                }
            }
          }
          assertEquals(0, failCount);
    }
}







