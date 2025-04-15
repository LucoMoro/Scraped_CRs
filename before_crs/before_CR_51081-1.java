/*Fix date parsing for time zones without names.

Bug: 8128460
Change-Id:I41a6baee6e22b8af9170e3e569e0599c38b5e707*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DateFormatSymbols.java b/luni/src/main/java/java/text/DateFormatSymbols.java
//Synthetic comment -- index e2a2345..6049fd2 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import libcore.icu.ICU;
import libcore.icu.LocaleData;
import libcore.icu.TimeZones;
//Synthetic comment -- @@ -331,7 +332,25 @@
* </ul>
*/
public String[][] getZoneStrings() {
        return clone2dStringArray(internalZoneStrings());
}

private static String[][] clone2dStringArray(String[][] array) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index b856d7a..e8aea4a 100644

//Synthetic comment -- @@ -1151,8 +1151,7 @@
offset += 3;
}
char sign;
        if (offset < string.length()
                && ((sign = string.charAt(offset)) == '+' || sign == '-')) {
ParsePosition position = new ParsePosition(offset + 1);
Number result = numberFormat.parse(string, position);
if (result == null) {
//Synthetic comment -- @@ -1182,16 +1181,21 @@
calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
return offset;
}
        String[][] zones = formatData.internalZoneStrings();
        for (String[] element : zones) {
            for (int j = TimeZones.LONG_NAME; j < TimeZones.NAME_COUNT; j++) {
                if (string.regionMatches(true, offset, element[j], 0, element[j].length())) {
                    TimeZone zone = TimeZone.getTimeZone(element[TimeZones.OLSON_NAME]);
if (zone == null) {
return -offset - 1;
}
int raw = zone.getRawOffset();
                    if (j == TimeZones.LONG_NAME_DST || j == TimeZones.SHORT_NAME_DST) {
// Not all time zones use a one-hour difference, so we need to query
// the TimeZone. (Australia/Lord_Howe is the usual example of this.)
int dstSavings = zone.getDSTSavings();
//Synthetic comment -- @@ -1206,7 +1210,7 @@
raw += dstSavings;
}
calendar.setTimeZone(new SimpleTimeZone(raw, ""));
                    return offset + element[j].length();
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index 328ab60..187c434 100644

//Synthetic comment -- @@ -184,7 +184,9 @@
// which only gets updated when we update icu4c. http://b/7955614 and http://b/8026776.

// TODO: should we generate these once, in TimeZones.getDisplayName? Revisit when we
        // upgrade to icu4c 50 and rewrite the underlying native code.

int offset = getRawOffset();
if (daylightTime) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/DateFormatSymbolsTest.java b/luni/src/test/java/libcore/java/text/DateFormatSymbolsTest.java
//Synthetic comment -- index 319a76e..4d9c87d 100644

//Synthetic comment -- @@ -123,4 +123,22 @@
assertEquals(Arrays.toString(row), "UTC", row[2]);
assertEquals(Arrays.toString(row), "UTC", row[4]);
}
}







