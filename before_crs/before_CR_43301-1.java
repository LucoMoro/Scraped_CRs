/*Fix LocaleData.toString once and for all.

Change-Id:I207a3226470557ac26caba165ef35f5df6859273*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/LocaleData.java b/luni/src/main/java/libcore/icu/LocaleData.java
//Synthetic comment -- index 97d5d30..8ec2294 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

/**
* Passes locale-specific from ICU native code to Java.
//Synthetic comment -- @@ -129,44 +130,7 @@
}

@Override public String toString() {
        return "LocaleData[" +
                "firstDayOfWeek=" + firstDayOfWeek + "," +
                "minimalDaysInFirstWeek=" + minimalDaysInFirstWeek + "," +
                "amPm=" + Arrays.toString(amPm) + "," +
                "eras=" + Arrays.toString(eras) + "," +
                "longMonthNames=" + Arrays.toString(longMonthNames) + "," +
                "shortMonthNames=" + Arrays.toString(shortMonthNames) + "," +
                "longStandAloneMonthNames=" + Arrays.toString(longStandAloneMonthNames) + "," +
                "shortStandAloneMonthNames=" + Arrays.toString(shortStandAloneMonthNames) + "," +
                "longWeekdayNames=" + Arrays.toString(longWeekdayNames) + "," +
                "shortWeekdayNames=" + Arrays.toString(shortWeekdayNames) + "," +
                "longStandAloneWeekdayNames=" + Arrays.toString(longStandAloneWeekdayNames) + "," +
                "shortStandAloneWeekdayNames=" + Arrays.toString(shortStandAloneWeekdayNames) + "," +
                "fullTimeFormat=" + fullTimeFormat + "," +
                "longTimeFormat=" + longTimeFormat + "," +
                "mediumTimeFormat=" + mediumTimeFormat + "," +
                "shortTimeFormat=" + shortTimeFormat + "," +
                "fullDateFormat=" + fullDateFormat + "," +
                "longDateFormat=" + longDateFormat + "," +
                "mediumDateFormat=" + mediumDateFormat + "," +
                "shortDateFormat=" + shortDateFormat + "," +
                "zeroDigit=" + zeroDigit + "," +
                "decimalSeparator=" + decimalSeparator + "," +
                "groupingSeparator=" + groupingSeparator + "," +
                "patternSeparator=" + patternSeparator + "," +
                "percent=" + percent + "," +
                "perMill=" + perMill + "," +
                "monetarySeparator=" + monetarySeparator + "," +
                "minusSign=" + minusSign + "," +
                "exponentSeparator=" + exponentSeparator + "," +
                "infinity=" + infinity + "," +
                "NaN=" + NaN + "," +
                "currencySymbol=" + currencySymbol + "," +
                "internationalCurrencySymbol=" + internationalCurrencySymbol + "," +
                "numberPattern=" + numberPattern + "," +
                "integerPattern=" + integerPattern + "," +
                "currencyPattern=" + currencyPattern + "," +
                "percentPattern=" + percentPattern + "]";
}

public String getDateFormat(int style) {








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/Objects.java b/luni/src/main/java/libcore/util/Objects.java
//Synthetic comment -- index 7817316..573b973 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package libcore.util;

public final class Objects {
private Objects() {}

//Synthetic comment -- @@ -29,4 +33,64 @@
public static int hashCode(Object o) {
return (o == null) ? 0 : o.hashCode();
}
}







