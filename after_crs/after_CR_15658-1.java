/*Documentation Bug (Issue 9557)

There was an issue in the Time.toMillis example.
Also removed some whitespaces so the file
applies to the Android coding Styleguide

Change-Id:Ic1383e506b30ff181c6e14a12f675b52e5c8ccbc*/




//Synthetic comment -- diff --git a/core/java/android/text/format/Time.java b/core/java/android/text/format/Time.java
//Synthetic comment -- index 8eae111..c05a8fe 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
private static final String Y_M_D_T_H_M_S_000 = "%Y-%m-%dT%H:%M:%S.000";
private static final String Y_M_D_T_H_M_S_000_Z = "%Y-%m-%dT%H:%M:%S.000Z";
private static final String Y_M_D = "%Y-%m-%d";

public static final String TIMEZONE_UTC = "UTC";

/**
//Synthetic comment -- @@ -170,11 +170,11 @@
public Time() {
this(TimeZone.getDefault().getID());
}

/**
* A copy constructor.  Construct a Time object by copying the given
* Time object.  No normalization occurs.
     *
* @param other
*/
public Time(Time other) {
//Synthetic comment -- @@ -185,17 +185,17 @@
* Ensures the values in each field are in range. For example if the
* current value of this calendar is March 32, normalize() will convert it
* to April 1. It also fills in weekDay, yearDay, isDst and gmtoff.
     *
* <p>
* If "ignoreDst" is true, then this method sets the "isDst" field to -1
* (the "unknown" value) before normalizing.  It then computes the
* correct value for "isDst".
     *
* <p>
* See {@link #toMillis(boolean)} for more information about when to
* use <tt>true</tt> or <tt>false</tt> for "ignoreDst".
     *
     * @return the UTC milliseconds since the epoch
*/
native public long normalize(boolean ignoreDst);

//Synthetic comment -- @@ -379,13 +379,13 @@
* Parses a date-time string in either the RFC 2445 format or an abbreviated
* format that does not include the "time" field.  For example, all of the
* following strings are valid:
     *
* <ul>
*   <li>"20081013T160000Z"</li>
*   <li>"20081013T160000"</li>
*   <li>"20081013"</li>
* </ul>
     *
* Returns whether or not the time is in UTC (ends with Z).  If the string
* ends with "Z" then the timezone is set to UTC.  If the date-time string
* included only a date and no time field, then the <code>allDay</code>
//Synthetic comment -- @@ -396,10 +396,10 @@
* <code>yearDay</code>, and <code>gmtoff</code> are always set to zero,
* and the field <code>isDst</code> is set to -1 (unknown).  To set those
* fields, call {@link #normalize(boolean)} after parsing.
     *
* To parse a date-time string and convert it to UTC milliseconds, do
* something like this:
     *
* <pre>
*   Time time = new Time();
*   String date = "20081013T160000Z";
//Synthetic comment -- @@ -428,25 +428,25 @@
* Parse a time in RFC 3339 format.  This method also parses simple dates
* (that is, strings that contain no time or time offset).  For example,
* all of the following strings are valid:
     *
* <ul>
*   <li>"2008-10-13T16:00:00.000Z"</li>
*   <li>"2008-10-13T16:00:00.000+07:00"</li>
*   <li>"2008-10-13T16:00:00.000-07:00"</li>
*   <li>"2008-10-13"</li>
* </ul>
     *
* <p>
* If the string contains a time and time offset, then the time offset will
* be used to convert the time value to UTC.
* </p>
     *
* <p>
* If the given string contains just a date (with no time field), then
* the {@link #allDay} field is set to true and the {@link #hour},
* {@link #minute}, and  {@link #second} fields are set to zero.
* </p>
     *
* <p>
* Returns true if the resulting time value is in UTC time.
* </p>
//Synthetic comment -- @@ -462,7 +462,7 @@
}
return false;
}

native private boolean nativeParse3339(String s);

/**
//Synthetic comment -- @@ -484,13 +484,13 @@
* <em>not</em> change any of the fields in this Time object.  If you want
* to normalize the fields in this Time object and also get the milliseconds
* then use {@link #normalize(boolean)}.
     *
* <p>
* If "ignoreDst" is false, then this method uses the current setting of the
* "isDst" field and will adjust the returned time if the "isDst" field is
* wrong for the given time.  See the sample code below for an example of
* this.
     *
* <p>
* If "ignoreDst" is true, then this method ignores the current setting of
* the "isDst" field in this Time object and will instead figure out the
//Synthetic comment -- @@ -499,27 +499,27 @@
* correct value of the "isDst" field is when the time is inherently
* ambiguous because it falls in the hour that is repeated when switching
* from Daylight-Saving Time to Standard Time.
     *
* <p>
* Here is an example where <tt>toMillis(true)</tt> adjusts the time,
* assuming that DST changes at 2am on Sunday, Nov 4, 2007.
     *
* <pre>
* Time time = new Time();
     * time.set(4, 10, 2007);  // set the date to Nov 4, 2007, 12am
* time.normalize();       // this sets isDst = 1
* time.monthDay += 1;     // changes the date to Nov 5, 2007, 12am
* millis = time.toMillis(false);   // millis is Nov 4, 2007, 11pm
* millis = time.toMillis(true);    // millis is Nov 5, 2007, 12am
* </pre>
     *
* <p>
* To avoid this problem, use <tt>toMillis(true)</tt>
* after adding or subtracting days or explicitly setting the "monthDay"
* field.  On the other hand, if you are adding
* or subtracting hours or minutes, then you should use
* <tt>toMillis(false)</tt>.
     *
* <p>
* You should also use <tt>toMillis(false)</tt> if you want
* to read back the same milliseconds that you set with {@link #set(long)}
//Synthetic comment -- @@ -531,14 +531,14 @@
* Sets the fields in this Time object given the UTC milliseconds.  After
* this method returns, all the fields are normalized.
* This also sets the "isDst" field to the correct value.
     *
* @param millis the time in UTC milliseconds since the epoch.
*/
native public void set(long millis);

/**
* Format according to RFC 2445 DATETIME type.
     *
* <p>
* The same as format("%Y%m%dT%H%M%S").
*/
//Synthetic comment -- @@ -584,7 +584,7 @@
* Sets the date from the given fields.  Also sets allDay to true.
* Sets weekDay, yearDay and gmtoff to 0, and isDst to -1.
* Call {@link #normalize(boolean)} if you need those.
     *
* @param monthDay the day of the month (in the range [1,31])
* @param month the zero-based month number (in the range [0,11])
* @param year the year
//Synthetic comment -- @@ -606,7 +606,7 @@
/**
* Returns true if the time represented by this Time object occurs before
* the given time.
     *
* @param that a given Time object to compare against
* @return true if this time is less than the given time
*/
//Synthetic comment -- @@ -618,7 +618,7 @@
/**
* Returns true if the time represented by this Time object occurs after
* the given time.
     *
* @param that a given Time object to compare against
* @return true if this time is greater than the given time
*/
//Synthetic comment -- @@ -632,12 +632,12 @@
* closest Thursday yearDay.
*/
private static final int[] sThursdayOffset = { -3, 3, 2, 1, 0, -1, -2 };

/**
* Computes the week number according to ISO 8601.  The current Time
* object must already be normalized because this method uses the
* yearDay and weekDay fields.
     *
* <p>
* In IS0 8601, weeks start on Monday.
* The first week of the year (week 1) is defined by ISO 8601 as the
//Synthetic comment -- @@ -645,12 +645,12 @@
* Or equivalently, the week containing January 4.  Or equivalently,
* the week with the year's first Thursday in it.
* </p>
     *
* <p>
* The week number can be calculated by counting Thursdays.  Week N
* contains the Nth Thursday of the year.
* </p>
     *
* @return the ISO week number.
*/
public int getWeekNumber() {
//Synthetic comment -- @@ -661,7 +661,7 @@
if (closestThursday >= 0 && closestThursday <= 364) {
return closestThursday / 7 + 1;
}

// The week crosses a year boundary.
Time temp = new Time(this);
temp.monthDay += sThursdayOffset[weekDay];
//Synthetic comment -- @@ -670,7 +670,7 @@
}

/**
     * Return a string in the RFC 3339 format.
* <p>
* If allDay is true, expresses the time as Y-M-D</p>
* <p>
//Synthetic comment -- @@ -691,13 +691,13 @@
int offset = (int)Math.abs(gmtoff);
int minutes = (offset % 3600) / 60;
int hours = offset / 3600;

return String.format("%s%s%02d:%02d", base, sign, hours, minutes);
}
}

/**
     * Returns true if the day of the given time is the epoch on the Julian Calendar
* (January 1, 1970 on the Gregorian calendar).
*
* @param time the time to test
//Synthetic comment -- @@ -707,7 +707,7 @@
long millis = time.toMillis(true);
return getJulianDay(millis, 0) == EPOCH_JULIAN_DAY;
}

/**
* Computes the Julian day number, given the UTC milliseconds
* and the offset (in seconds) from UTC.  The Julian day for a given
//Synthetic comment -- @@ -716,10 +716,10 @@
* what timezone is being used.  The Julian day is useful for testing
* if two events occur on the same day and for determining the relative
* time of an event from the present ("yesterday", "3 days ago", etc.).
     *
* <p>
* Use {@link #toMillis(boolean)} to get the milliseconds.
     *
* @param millis the time in UTC milliseconds
* @param gmtoff the offset from UTC in seconds
* @return the Julian day
//Synthetic comment -- @@ -729,7 +729,7 @@
long julianDay = (millis + offsetMillis) / DateUtils.DAY_IN_MILLIS;
return (int) julianDay + EPOCH_JULIAN_DAY;
}

/**
* <p>Sets the time from the given Julian day number, which must be based on
* the same timezone that is set in this Time object.  The "gmtoff" field
//Synthetic comment -- @@ -738,7 +738,7 @@
* After this method returns all the fields will be normalized and the time
* will be set to 12am at the beginning of the given Julian day.
* </p>
     *
* <p>
* The only exception to this is if 12am does not exist for that day because
* of daylight saving time.  For example, Cairo, Eqypt moves time ahead one
//Synthetic comment -- @@ -746,7 +746,7 @@
* also change daylight saving time at 12am.  In those cases, the time
* will be set to 1am.
* </p>
     *
* @param julianDay the Julian day in the timezone for this Time object
* @return the UTC milliseconds for the beginning of the Julian day
*/
//Synthetic comment -- @@ -756,13 +756,13 @@
// the day.
long millis = (julianDay - EPOCH_JULIAN_DAY) * DateUtils.DAY_IN_MILLIS;
set(millis);

// Figure out how close we are to the requested Julian day.
// We can't be off by more than a day.
int approximateDay = getJulianDay(millis, gmtoff);
int diff = julianDay - approximateDay;
monthDay += diff;

// Set the time to 12am and re-normalize.
hour = 0;
minute = 0;







