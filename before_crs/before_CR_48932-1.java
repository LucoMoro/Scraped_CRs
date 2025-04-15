/*Improve the Calendar documentation.

Bug:http://code.google.com/p/android/issues/detail?id=42059Change-Id:I08112426fcd8a2389e4068d2c6f9c3a583ec95d5*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Calendar.java b/luni/src/main/java/java/util/Calendar.java
//Synthetic comment -- index 81d01fb..aeddc18 100644

//Synthetic comment -- @@ -712,12 +712,7 @@
}

/**
     * Constructs a {@code Calendar} instance using the specified {@code TimeZone} and {@code Locale}.
     *
     * @param timezone
     *            the timezone.
     * @param locale
     *            the locale.
*/
protected Calendar(TimeZone timezone, Locale locale) {
this(timezone);
//Synthetic comment -- @@ -728,7 +723,7 @@


/**
     * Adds the specified amount to a {@code Calendar} field.
*
* @param field
*            the {@code Calendar} field to modify.
//Synthetic comment -- @@ -741,8 +736,8 @@
public abstract void add(int field, int value);

/**
     * Returns whether the {@code Date} specified by this {@code Calendar} instance is after the {@code Date}
     * specified by the parameter. The comparison is not dependent on the time
* zones of the {@code Calendar}.
*
* @param calendar
//Synthetic comment -- @@ -760,8 +755,8 @@
}

/**
     * Returns whether the {@code Date} specified by this {@code Calendar} instance is before the
     * {@code Date} specified by the parameter. The comparison is not dependent on the
* time zones of the {@code Calendar}.
*
* @param calendar
//Synthetic comment -- @@ -791,10 +786,7 @@
}

/**
     * Clears the specified field to zero and sets the isSet flag to {@code false}.
     *
     * @param field
     *            the field to clear.
*/
public final void clear(int field) {
fields[field] = 0;
//Synthetic comment -- @@ -803,11 +795,7 @@
}

/**
     * Returns a new {@code Calendar} with the same properties.
     *
     * @return a shallow copy of this {@code Calendar}.
     *
     * @see java.lang.Cloneable
*/
@Override
public Object clone() {
//Synthetic comment -- @@ -856,13 +844,11 @@
protected abstract void computeTime();

/**
     * Compares the specified object to this {@code Calendar} and returns whether they are
* equal. The object must be an instance of {@code Calendar} and have the same
* properties.
*
     * @param object
     *            the object to compare with this object.
     * @return {@code true} if the specified object is equal to this {@code Calendar}, {@code false}
*         otherwise.
*/
@Override
//Synthetic comment -- @@ -882,13 +868,9 @@
}

/**
     * Gets the value of the specified field after computing the field values by
* calling {@code complete()} first.
*
     * @param field
     *            the field to get.
     * @return the value of the specified field.
     *
* @throws IllegalArgumentException
*                if the fields are not set, the time is not set, and the
*                time cannot be computed from the current field values.
//Synthetic comment -- @@ -902,7 +884,7 @@
}

/**
     * Returns the maximum value of the specified field for the current date.
* For example, the maximum number of days in the current month.
*/
public int getActualMaximum(int field) {
//Synthetic comment -- @@ -924,11 +906,7 @@
}

/**
     * Gets the minimum value of the specified field for the current date.
     *
     * @param field
     *            the field.
     * @return the minimum value of the specified field.
*/
public int getActualMinimum(int field) {
int value, next;
//Synthetic comment -- @@ -958,31 +936,22 @@
}

/**
     * Gets the first day of the week for this {@code Calendar}.
     *
     * @return the first day of the week.
*/
public int getFirstDayOfWeek() {
return firstDayOfWeek;
}

/**
     * Gets the greatest minimum value of the specified field. This is the
* biggest value that {@code getActualMinimum} can return for any possible
* time.
     *
     * @param field
     *            the field.
     * @return the greatest minimum value of the specified field.
*/
public abstract int getGreatestMinimum(int field);

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale}.
     *
     * @return a {@code Calendar} subclass instance set to the current date and time in
     *         the default {@code Timezone}.
*/
public static synchronized Calendar getInstance() {
return new GregorianCalendar();
//Synthetic comment -- @@ -990,11 +959,7 @@

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * specified {@code Locale}.
     *
     * @param locale
     *            the locale to use.
     * @return a {@code Calendar} subclass instance set to the current date and time.
*/
public static synchronized Calendar getInstance(Locale locale) {
return new GregorianCalendar(locale);
//Synthetic comment -- @@ -1002,12 +967,7 @@

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale}, using the specified {@code TimeZone}.
     *
     * @param timezone
     *            the {@code TimeZone} to use.
     * @return a {@code Calendar} subclass instance set to the current date and time in
     *         the specified timezone.
*/
public static synchronized Calendar getInstance(TimeZone timezone) {
return new GregorianCalendar(timezone);
//Synthetic comment -- @@ -1015,63 +975,40 @@

/**
* Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * specified {@code Locale}.
     *
     * @param timezone
     *            the {@code TimeZone} to use.
     * @param locale
     *            the {@code Locale} to use.
     * @return a {@code Calendar} subclass instance set to the current date and time in
     *         the specified timezone.
*/
public static synchronized Calendar getInstance(TimeZone timezone, Locale locale) {
return new GregorianCalendar(timezone, locale);
}

/**
     * Gets the smallest maximum value of the specified field. This is the
* smallest value that {@code getActualMaximum()} can return for any
* possible time.
     *
     * @param field
     *            the field number.
     * @return the smallest maximum value of the specified field.
*/
public abstract int getLeastMaximum(int field);

/**
     * Gets the greatest maximum value of the specified field. This returns the
     * biggest value that {@code get} can return for the specified field.
     *
     * @param field
     *            the field.
     * @return the greatest maximum value of the specified field.
*/
public abstract int getMaximum(int field);

/**
     * Gets the minimal days in the first week of the year.
     *
     * @return the minimal days in the first week of the year.
*/
public int getMinimalDaysInFirstWeek() {
return minimalDaysInFirstWeek;
}

/**
     * Gets the smallest minimum value of the specified field. this returns the
     * smallest value thet {@code get} can return for the specified field.
     *
     * @param field
     *            the field number.
     * @return the smallest minimum value of the specified field.
*/
public abstract int getMinimum(int field);

/**
     * Gets the time of this {@code Calendar} as a {@code Date} object.
     *
     * @return a new {@code Date} initialized to the time of this {@code Calendar}.
*
* @throws IllegalArgumentException
*                if the time is not set and the time cannot be computed
//Synthetic comment -- @@ -1082,9 +1019,8 @@
}

/**
     * Computes the time from the fields if required and returns the time.
     *
     * @return the time of this {@code Calendar}.
*
* @throws IllegalArgumentException
*                if the time is not set and the time cannot be computed
//Synthetic comment -- @@ -1099,22 +1035,12 @@
}

/**
     * Gets the timezone of this {@code Calendar}.
     *
     * @return the {@code TimeZone} used by this {@code Calendar}.
*/
public TimeZone getTimeZone() {
return zone;
}

    /**
     * Returns an integer hash code for the receiver. Objects which are equal
     * return the same value for this method.
     *
     * @return the receiver's hash.
     *
     * @see #equals
     */
@Override
public int hashCode() {
return (isLenient() ? 1237 : 1231) + getFirstDayOfWeek()
//Synthetic comment -- @@ -1122,28 +1048,22 @@
}

/**
     * Gets the value of the specified field without recomputing.
     *
     * @param field
     *            the field.
     * @return the value of the specified field.
*/
protected final int internalGet(int field) {
return fields[field];
}

/**
     * Returns if this {@code Calendar} accepts field values which are outside the valid
* range for the field.
     *
     * @return {@code true} if this {@code Calendar} is lenient, {@code false} otherwise.
*/
public boolean isLenient() {
return lenient;
}

/**
     * Returns whether the specified field is set. Note that the interpretation of "is set" is
* somewhat technical. In particular, it does <i>not</i> mean that the field's value is up
* to date. If you want to know whether a field contains an up-to-date value, you must also
* check {@code areFieldsSet}, making this method somewhat useless unless you're a subclass,
//Synthetic comment -- @@ -1153,25 +1073,16 @@
* of the {@code clear} methods. Thus "set" does not mean "valid". You probably want to call
* {@code get} -- which will update fields as necessary -- rather than try to make use of
* this method.
     *
     * @param field
     *            a {@code Calendar} field number.
     * @return {@code true} if the specified field is set, {@code false} otherwise.
*/
public final boolean isSet(int field) {
return isSet[field];
}

/**
     * Adds the specified amount to the specified field and wraps the value of
* the field when it goes beyond the maximum or minimum value for the
* current date. Other fields will be adjusted as required to maintain a
* consistent date.
     *
     * @param field
     *            the field to roll.
     * @param value
     *            the amount to add.
*/
public void roll(int field, int value) {
boolean increment = value >= 0;
//Synthetic comment -- @@ -1182,25 +1093,15 @@
}

/**
     * Increment or decrement the specified field and wrap the value of the
* field when it goes beyond the maximum or minimum value for the current
* date. Other fields will be adjusted as required to maintain a consistent
* date.
     *
     * @param field
     *            the number indicating the field to roll.
     * @param increment
     *            {@code true} to increment the field, {@code false} to decrement.
*/
public abstract void roll(int field, boolean increment);

/**
     * Sets a field to the specified value.
     *
     * @param field
     *            the code indicating the {@code Calendar} field to modify.
     * @param value
     *            the value.
*/
public void set(int field, int value) {
fields[field] = value;
//Synthetic comment -- @@ -1218,15 +1119,9 @@
}

/**
     * Sets the year, month and day of the month fields. Other fields are not
     * changed.
     *
     * @param year
     *            the year.
     * @param month
     *            the month.
     * @param day
     *            the day of the month.
*/
public final void set(int year, int month, int day) {
set(YEAR, year);
//Synthetic comment -- @@ -1235,66 +1130,37 @@
}

/**
     * Sets the year, month, day of the month, hour of day and minute fields.
     * Other fields are not changed.
     *
     * @param year
     *            the year.
     * @param month
     *            the month.
     * @param day
     *            the day of the month.
     * @param hourOfDay
     *            the hour of day.
     * @param minute
     *            the minute.
*/
    public final void set(int year, int month, int day, int hourOfDay,
            int minute) {
set(year, month, day);
set(HOUR_OF_DAY, hourOfDay);
set(MINUTE, minute);
}

/**
     * Sets the year, month, day of the month, hour of day, minute and second
     * fields. Other fields are not changed.
     *
     * @param year
     *            the year.
     * @param month
     *            the month.
     * @param day
     *            the day of the month.
     * @param hourOfDay
     *            the hour of day.
     * @param minute
     *            the minute.
     * @param second
     *            the second.
*/
    public final void set(int year, int month, int day, int hourOfDay,
            int minute, int second) {
set(year, month, day, hourOfDay, minute);
set(SECOND, second);
}

/**
* Sets the first day of the week for this {@code Calendar}.
     *
     * @param value
     *            a {@code Calendar} day of the week.
*/
public void setFirstDayOfWeek(int value) {
firstDayOfWeek = value;
}

/**
     * Sets this {@code Calendar} to accept field values which are outside the valid
* range for the field.
     *
     * @param value
     *            a boolean value.
*/
public void setLenient(boolean value) {
lenient = value;
//Synthetic comment -- @@ -1302,9 +1168,6 @@

/**
* Sets the minimal days in the first week of the year.
     *
     * @param value
     *            the minimal days in the first week of the year.
*/
public void setMinimalDaysInFirstWeek(int value) {
minimalDaysInFirstWeek = value;
//Synthetic comment -- @@ -1312,19 +1175,14 @@

/**
* Sets the time of this {@code Calendar}.
     *
     * @param date
     *            a {@code Date} object.
*/
public final void setTime(Date date) {
setTimeInMillis(date.getTime());
}

/**
     * Sets the time of this {@code Calendar}.
     *
     * @param milliseconds
     *            the time as the number of milliseconds since Jan. 1, 1970.
*/
public void setTimeInMillis(long milliseconds) {
if (!isTimeSet || !areFieldsSet || time != milliseconds) {
//Synthetic comment -- @@ -1337,9 +1195,6 @@

/**
* Sets the {@code TimeZone} used by this Calendar.
     *
     * @param timezone
     *            a {@code TimeZone}.
*/
public void setTimeZone(TimeZone timezone) {
zone = timezone;
//Synthetic comment -- @@ -1347,7 +1202,7 @@
}

/**
     * Returns the string representation of this {@code Calendar}.
*/
@Override
public String toString() {
//Synthetic comment -- @@ -1373,11 +1228,9 @@
}

/**
     * Compares the times of the two {@code Calendar}, which represent the milliseconds
     * from the January 1, 1970 00:00:00.000 GMT (Gregorian).
*
     * @param anotherCalendar
     *            another calendar that this one is compared with.
* @return 0 if the times of the two {@code Calendar}s are equal, -1 if the time of
*         this {@code Calendar} is before the other one, 1 if the time of this
*         {@code Calendar} is after the other one.







