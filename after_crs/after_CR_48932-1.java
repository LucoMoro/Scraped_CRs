/*Improve the Calendar documentation.

Bug:http://code.google.com/p/android/issues/detail?id=42059Change-Id:I08112426fcd8a2389e4068d2c6f9c3a583ec95d5*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Calendar.java b/luni/src/main/java/java/util/Calendar.java
//Synthetic comment -- index 81d01fb..aeddc18 100644

//Synthetic comment -- @@ -712,12 +712,7 @@
}

/**
     * Constructs a {@code Calendar} instance using the given {@code TimeZone} and {@code Locale}.
*/
protected Calendar(TimeZone timezone, Locale locale) {
this(timezone);
//Synthetic comment -- @@ -728,7 +723,7 @@


/**
     * Adds the given amount to a {@code Calendar} field.
*
* @param field
*            the {@code Calendar} field to modify.
//Synthetic comment -- @@ -741,8 +736,8 @@
public abstract void add(int field, int value);

/**
     * Returns whether the {@code Date} represented by this {@code Calendar} instance is after the {@code Date}
     * represented by the parameter. The comparison is not dependent on the time
* zones of the {@code Calendar}.
*
* @param calendar
//Synthetic comment -- @@ -760,8 +755,8 @@
}

/**
     * Returns whether the {@code Date} represented by this {@code Calendar} instance is before the
     * {@code Date} represented by the parameter. The comparison is not dependent on the
* time zones of the {@code Calendar}.
*
* @param calendar
//Synthetic comment -- @@ -791,10 +786,7 @@
}

/**
     * Clears the given field to zero and sets the isSet flag to {@code false}.
*/
public final void clear(int field) {
fields[field] = 0;
//Synthetic comment -- @@ -803,11 +795,7 @@
}

/**
     * Returns a shallow copy of this {@code Calendar} with the same properties.
*/
@Override
public Object clone() {
//Synthetic comment -- @@ -856,13 +844,11 @@
protected abstract void computeTime();

/**
     * Compares the given object to this {@code Calendar} and returns whether they are
* equal. The object must be an instance of {@code Calendar} and have the same
* properties.
*
     * @return {@code true} if the given object is equal to this {@code Calendar}, {@code false}
*         otherwise.
*/
@Override
//Synthetic comment -- @@ -882,13 +868,9 @@
}

/**
     * Returns the value of the given field after computing the field values by
* calling {@code complete()} first.
*
* @throws IllegalArgumentException
*                if the fields are not set, the time is not set, and the
*                time cannot be computed from the current field values.
//Synthetic comment -- @@ -902,7 +884,7 @@
}

/**
     * Returns the maximum value of the given field for the current date.
* For example, the maximum number of days in the current month.
*/
public int getActualMaximum(int field) {
//Synthetic comment -- @@ -924,11 +906,7 @@
}

/**
     * Returns the minimum value of the given field for the current date.
*/
public int getActualMinimum(int field) {
int value, next;
//Synthetic comment -- @@ -958,31 +936,22 @@
}

/**
     * Returns the first day of the week for this {@code Calendar}.
*/
public int getFirstDayOfWeek() {
return firstDayOfWeek;
}

/**
     * Returns the greatest minimum value of the given field. This is the
* biggest value that {@code getActualMinimum} can return for any possible
* time.
*/
public abstract int getGreatestMinimum(int field);

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale} and default {@code TimeZone}, set to the current date and time.
*/
public static synchronized Calendar getInstance() {
return new GregorianCalendar();
//Synthetic comment -- @@ -990,11 +959,7 @@

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * given {@code Locale} and default {@code TimeZone}, set to the current date and time.
*/
public static synchronized Calendar getInstance(Locale locale) {
return new GregorianCalendar(locale);
//Synthetic comment -- @@ -1002,12 +967,7 @@

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale} and given {@code TimeZone}, set to the current date and time.
*/
public static synchronized Calendar getInstance(TimeZone timezone) {
return new GregorianCalendar(timezone);
//Synthetic comment -- @@ -1015,63 +975,40 @@

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * given {@code Locale} and given {@code TimeZone}, set to the current date and time.
*/
public static synchronized Calendar getInstance(TimeZone timezone, Locale locale) {
return new GregorianCalendar(timezone, locale);
}

/**
     * Returns the smallest maximum value of the given field. This is the
* smallest value that {@code getActualMaximum()} can return for any
* possible time.
*/
public abstract int getLeastMaximum(int field);

/**
     * Returns the greatest maximum value of the given field. This returns the
     * biggest value that {@code get} can return for the given field.
*/
public abstract int getMaximum(int field);

/**
     * Returns the minimal days in the first week of the year.
*/
public int getMinimalDaysInFirstWeek() {
return minimalDaysInFirstWeek;
}

/**
     * Returns the smallest minimum value of the given field. this returns the
     * smallest value that {@code get} can return for the given field.
*/
public abstract int getMinimum(int field);

/**
     * Returns the time of this {@code Calendar} as a {@code Date} object.
*
* @throws IllegalArgumentException
*                if the time is not set and the time cannot be computed
//Synthetic comment -- @@ -1082,9 +1019,8 @@
}

/**
     * Returns the time represented by this {@code Calendar}, recomputing the time from its
     * fields if necessary.
*
* @throws IllegalArgumentException
*                if the time is not set and the time cannot be computed
//Synthetic comment -- @@ -1099,22 +1035,12 @@
}

/**
     * Returns the time zone used by this {@code Calendar}.
*/
public TimeZone getTimeZone() {
return zone;
}

@Override
public int hashCode() {
return (isLenient() ? 1237 : 1231) + getFirstDayOfWeek()
//Synthetic comment -- @@ -1122,28 +1048,22 @@
}

/**
     * Returns the value of the given field without recomputing.
*/
protected final int internalGet(int field) {
return fields[field];
}

/**
     * Tests whether this {@code Calendar} accepts field values which are outside the valid
* range for the field.
*/
public boolean isLenient() {
return lenient;
}

/**
     * Tests whether the given field is set. Note that the interpretation of "is set" is
* somewhat technical. In particular, it does <i>not</i> mean that the field's value is up
* to date. If you want to know whether a field contains an up-to-date value, you must also
* check {@code areFieldsSet}, making this method somewhat useless unless you're a subclass,
//Synthetic comment -- @@ -1153,25 +1073,16 @@
* of the {@code clear} methods. Thus "set" does not mean "valid". You probably want to call
* {@code get} -- which will update fields as necessary -- rather than try to make use of
* this method.
*/
public final boolean isSet(int field) {
return isSet[field];
}

/**
     * Adds the given amount to the given field and wraps the value of
* the field when it goes beyond the maximum or minimum value for the
* current date. Other fields will be adjusted as required to maintain a
* consistent date.
*/
public void roll(int field, int value) {
boolean increment = value >= 0;
//Synthetic comment -- @@ -1182,25 +1093,15 @@
}

/**
     * Increment or decrement the given field and wrap the value of the
* field when it goes beyond the maximum or minimum value for the current
* date. Other fields will be adjusted as required to maintain a consistent
* date.
*/
public abstract void roll(int field, boolean increment);

/**
     * Sets the given field to the given value.
*/
public void set(int field, int value) {
fields[field] = value;
//Synthetic comment -- @@ -1218,15 +1119,9 @@
}

/**
     * Sets the year, month, and day of the month fields.
     * Other fields are not changed; call {@link #clear} first if this is not desired.
     * The month value is 0-based, so it may be clearer to use a constant like {@code JANUARY}.
*/
public final void set(int year, int month, int day) {
set(YEAR, year);
//Synthetic comment -- @@ -1235,66 +1130,37 @@
}

/**
     * Sets the year, month, day of the month, hour of day, and minute fields.
     * Other fields are not changed; call {@link #clear} first if this is not desired.
     * The month value is 0-based, so it may be clearer to use a constant like {@code JANUARY}.
*/
    public final void set(int year, int month, int day, int hourOfDay, int minute) {
set(year, month, day);
set(HOUR_OF_DAY, hourOfDay);
set(MINUTE, minute);
}

/**
     * Sets the year, month, day of the month, hour of day, minute, and second fields.
     * Other fields are not changed; call {@link #clear} first if this is not desired.
     * The month value is 0-based, so it may be clearer to use a constant like {@code JANUARY}.
*/
    public final void set(int year, int month, int day, int hourOfDay, int minute, int second) {
set(year, month, day, hourOfDay, minute);
set(SECOND, second);
}

/**
* Sets the first day of the week for this {@code Calendar}.
     * The value should be a day of the week such as {@code MONDAY}.
*/
public void setFirstDayOfWeek(int value) {
firstDayOfWeek = value;
}

/**
     * Sets whether this {@code Calendar} accepts field values which are outside the valid
* range for the field.
*/
public void setLenient(boolean value) {
lenient = value;
//Synthetic comment -- @@ -1302,9 +1168,6 @@

/**
* Sets the minimal days in the first week of the year.
*/
public void setMinimalDaysInFirstWeek(int value) {
minimalDaysInFirstWeek = value;
//Synthetic comment -- @@ -1312,19 +1175,14 @@

/**
* Sets the time of this {@code Calendar}.
*/
public final void setTime(Date date) {
setTimeInMillis(date.getTime());
}

/**
     * Sets the time of this {@code Calendar} to the given Unix time. See {@link Date} for more
     * about what this means.
*/
public void setTimeInMillis(long milliseconds) {
if (!isTimeSet || !areFieldsSet || time != milliseconds) {
//Synthetic comment -- @@ -1337,9 +1195,6 @@

/**
* Sets the {@code TimeZone} used by this Calendar.
*/
public void setTimeZone(TimeZone timezone) {
zone = timezone;
//Synthetic comment -- @@ -1347,7 +1202,7 @@
}

/**
     * Returns a string representation of this {@code Calendar}, showing which fields are set.
*/
@Override
public String toString() {
//Synthetic comment -- @@ -1373,11 +1228,9 @@
}

/**
     * Compares the time represented by this {@code Calendar} to that represented by the given
     * {@code Calendar}.
*
* @return 0 if the times of the two {@code Calendar}s are equal, -1 if the time of
*         this {@code Calendar} is before the other one, 1 if the time of this
*         {@code Calendar} is after the other one.







