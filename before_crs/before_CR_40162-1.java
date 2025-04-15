/*Don't cache partial sets of date fields in Calendar.

This was only useful when modifying calendars within a single day.
Not worth the complexity, and possibly a source of bugs.

(cherry-pick of 0170a5c2cd87d90e11513a45f29ebafee6a1701c.)

Bug: 2435103
Change-Id:Ib6d68aed29f424ac464fc389e7ae26909667b9b9*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Calendar.java b/luni/src/main/java/java/util/Calendar.java
//Synthetic comment -- index bef6e26..4cabd4a 100644

//Synthetic comment -- @@ -877,8 +877,7 @@
return getTimeInMillis() == cal.getTimeInMillis()
&& isLenient() == cal.isLenient()
&& getFirstDayOfWeek() == cal.getFirstDayOfWeek()
                && getMinimalDaysInFirstWeek() == cal
                        .getMinimalDaysInFirstWeek()
&& getTimeZone().equals(cal.getTimeZone());
}

//Synthetic comment -- @@ -903,11 +902,8 @@
}

/**
     * Gets the maximum value of the specified field for the current date.
     *
     * @param field
     *            the field.
     * @return the maximum value of the specified field.
*/
public int getActualMaximum(int field) {
int value, next;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/GregorianCalendar.java b/luni/src/main/java/java/util/GregorianCalendar.java
//Synthetic comment -- index 4d93aef..9ff9ccc 100644

//Synthetic comment -- @@ -219,14 +219,6 @@
private static int[] leastMaximums = new int[] { 1, 292269054, 11, 50, 3,
28, 355, 7, 3, 1, 11, 23, 59, 59, 999, 50400000, 1200000 };

    private boolean isCached;

    private int[] cachedFields = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    private long nextMidnightMillis = 0L;

    private long lastMidnightMillis = 0L;

private int currentYearSkew = 10;

private int lastYearSkew = 0;
//Synthetic comment -- @@ -365,8 +357,6 @@
throw new IllegalArgumentException();
}

        isCached = false;

if (field == ERA) {
complete();
if (fields[ERA] == AD) {
//Synthetic comment -- @@ -468,19 +458,8 @@
complete();
}

    /**
     * Creates new instance of {@code GregorianCalendar} with the same properties.
     *
     * @return a shallow copy of this {@code GregorianCalendar}.
     */
    @Override
    public Object clone() {
        GregorianCalendar thisClone = (GregorianCalendar) super.clone();
        thisClone.cachedFields = cachedFields.clone();
        return thisClone;
    }

    private final void fullFieldsCalc(long timeVal, int millis, int zoneOffset) {
long days = timeVal / 86400000;

if (millis < 0) {
//Synthetic comment -- @@ -583,31 +562,6 @@
}
}

    private final void cachedFieldsCheckAndGet(long timeVal,
            long newTimeMillis, long newTimeMillisAdjusted, int millis,
            int zoneOffset) {
        int dstOffset = fields[DST_OFFSET];
        if (!isCached
                || newTimeMillis >= nextMidnightMillis
                || newTimeMillis <= lastMidnightMillis
                || cachedFields[4] != zoneOffset
                || (dstOffset == 0 && (newTimeMillisAdjusted >= nextMidnightMillis))
                || (dstOffset != 0 && (newTimeMillisAdjusted <= lastMidnightMillis))) {
            fullFieldsCalc(timeVal, millis, zoneOffset);
            isCached = false;
        } else {
            fields[YEAR] = cachedFields[0];
            fields[MONTH] = cachedFields[1];
            fields[DATE] = cachedFields[2];
            fields[DAY_OF_WEEK] = cachedFields[3];
            fields[ERA] = cachedFields[5];
            fields[WEEK_OF_YEAR] = cachedFields[6];
            fields[WEEK_OF_MONTH] = cachedFields[7];
            fields[DAY_OF_YEAR] = cachedFields[8];
            fields[DAY_OF_WEEK_IN_MONTH] = cachedFields[9];
        }
    }

@Override
protected void computeFields() {
TimeZone timeZone = getTimeZone();
//Synthetic comment -- @@ -616,99 +570,11 @@
fields[DST_OFFSET] = dstOffset;
fields[ZONE_OFFSET] = zoneOffset;

        int millis = (int) (time % 86400000);
        int savedMillis = millis;
        // compute without a change in daylight saving time
        int offset = zoneOffset + dstOffset;
        long newTime = time + offset;

        if (time > 0L && newTime < 0L && offset > 0) {
            newTime = 0x7fffffffffffffffL;
        } else if (time < 0L && newTime > 0L && offset < 0) {
            newTime = 0x8000000000000000L;
        }

        // FIXME: I don't think this caching ever really gets used, because it requires that the
        // time zone doesn't use daylight savings (ever). So unless you're somewhere like Taiwan...
        if (isCached) {
            if (millis < 0) {
                millis += 86400000;
            }

            // Cannot add ZONE_OFFSET to time as it might overflow
            millis += zoneOffset;
            millis += dstOffset;

            if (millis < 0) {
                millis += 86400000;
            } else if (millis >= 86400000) {
                millis -= 86400000;
            }

            fields[MILLISECOND] = (millis % 1000);
            millis /= 1000;
            fields[SECOND] = (millis % 60);
            millis /= 60;
            fields[MINUTE] = (millis % 60);
            millis /= 60;
            fields[HOUR_OF_DAY] = (millis % 24);
            millis /= 24;
            fields[AM_PM] = fields[HOUR_OF_DAY] > 11 ? 1 : 0;
            fields[HOUR] = fields[HOUR_OF_DAY] % 12;

            // FIXME: this has to be wrong; useDaylightTime doesn't mean what they think it means!
            long newTimeAdjusted = newTime;
            if (timeZone.useDaylightTime()) {
                int dstSavings = timeZone.getDSTSavings();
                newTimeAdjusted += (dstOffset == 0) ? dstSavings : -dstSavings;
            }

            if (newTime > 0L && newTimeAdjusted < 0L && dstOffset == 0) {
                newTimeAdjusted = 0x7fffffffffffffffL;
            } else if (newTime < 0L && newTimeAdjusted > 0L && dstOffset != 0) {
                newTimeAdjusted = 0x8000000000000000L;
            }

            cachedFieldsCheckAndGet(time, newTime, newTimeAdjusted,
                    savedMillis, zoneOffset);
        } else {
            fullFieldsCalc(time, savedMillis, zoneOffset);
        }

for (int i = 0; i < FIELD_COUNT; i++) {
isSet[i] = true;
}

        // Caching
        if (!isCached
                && newTime != 0x7fffffffffffffffL
                && newTime != 0x8000000000000000L
                && (!timeZone.useDaylightTime() || timeZone instanceof SimpleTimeZone)) {
            int cacheMillis = 0;

            cachedFields[0] = fields[YEAR];
            cachedFields[1] = fields[MONTH];
            cachedFields[2] = fields[DATE];
            cachedFields[3] = fields[DAY_OF_WEEK];
            cachedFields[4] = zoneOffset;
            cachedFields[5] = fields[ERA];
            cachedFields[6] = fields[WEEK_OF_YEAR];
            cachedFields[7] = fields[WEEK_OF_MONTH];
            cachedFields[8] = fields[DAY_OF_YEAR];
            cachedFields[9] = fields[DAY_OF_WEEK_IN_MONTH];

            cacheMillis += (23 - fields[HOUR_OF_DAY]) * 60 * 60 * 1000;
            cacheMillis += (59 - fields[MINUTE]) * 60 * 1000;
            cacheMillis += (59 - fields[SECOND]) * 1000;
            nextMidnightMillis = newTime + cacheMillis;

            cacheMillis = fields[HOUR_OF_DAY] * 60 * 60 * 1000;
            cacheMillis += fields[MINUTE] * 60 * 1000;
            cacheMillis += fields[SECOND] * 1000;
            lastMidnightMillis = newTime - cacheMillis;

            isCached = true;
        }
}

@Override
//Synthetic comment -- @@ -939,19 +805,17 @@
return (int) days + 1;
}

    private long daysFromBaseYear(int iyear) {
        long year = iyear;

if (year >= 1970) {
long days = (year - 1970) * 365 + ((year - 1969) / 4);
if (year > changeYear) {
days -= ((year - 1901) / 100) - ((year - 1601) / 400);
} else {
                if(year == changeYear){
days += currentYearSkew;
                }else if(year == changeYear -1){
days += lastYearSkew;
                }else{
days += julianSkew;
}
}
//Synthetic comment -- @@ -995,21 +859,10 @@
}

/**
     * Compares the specified {@code Object} to this {@code GregorianCalendar} and returns whether
     * they are equal. To be equal, the {@code Object} must be an instance of {@code GregorianCalendar} and
     * have the same properties.
     *
     * @param object
     *            the {@code Object} to compare with this {@code GregorianCalendar}.
     * @return {@code true} if {@code object} is equal to this
     *         {@code GregorianCalendar}, {@code false} otherwise.
     * @throws IllegalArgumentException
     *                if the time is not set and the time cannot be computed
     *                from the current field values.
     * @see #hashCode
*/
    @Override
    public boolean equals(Object object) {
if (!(object instanceof GregorianCalendar)) {
return false;
}
//Synthetic comment -- @@ -1020,28 +873,12 @@
&& gregorianCutover == ((GregorianCalendar) object).gregorianCutover;
}

    /**
     * Gets the maximum value of the specified field for the current date. For
     * example, the maximum number of days in the current month.
     *
     * @param field
     *            the field.
     * @return the maximum value of the specified field.
     */
    @Override
    public int getActualMaximum(int field) {
int value;
if ((value = maximums[field]) == leastMaximums[field]) {
return value;
}

        switch (field) {
            case WEEK_OF_YEAR:
            case WEEK_OF_MONTH:
                isCached = false;
                break;
        }

complete();
long orgTime = time;
int result = 0;
//Synthetic comment -- @@ -1216,32 +1053,16 @@
month++;
}
int dayOfWeek = mod7(dayCount - 3) + 1;
        int offset = timeZone.getOffset(AD, year, month, date, dayOfWeek,
                millis);
        return offset;
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
return super.hashCode()
+ ((int) (gregorianCutover >>> 32) ^ (int) gregorianCutover);
}

/**
     * Returns whether the specified year is a leap year.
     *
     * @param year
     *            the year.
     * @return {@code true} if the specified year is a leap year, {@code false}
     *         otherwise.
*/
public boolean isLeapYear(int year) {
if (year > changeYear) {
//Synthetic comment -- @@ -1294,8 +1115,6 @@
throw new IllegalArgumentException();
}

        isCached = false;

complete();
int days, day, mod, maxWeeks, newWeek;
int max = -1;
//Synthetic comment -- @@ -1410,9 +1229,6 @@

/**
* Sets the gregorian change date of this calendar.
     *
     * @param date
     *            a {@code Date} which represents the gregorian change date.
*/
public void setGregorianChange(Date date) {
gregorianCutover = date.getTime();
//Synthetic comment -- @@ -1424,7 +1240,6 @@
}
julianSkew = ((changeYear - 2000) / 400) + julianError()
- ((changeYear - 2000) / 100);
        isCached = false;
int dayOfYear = cal.get(DAY_OF_YEAR);
if (dayOfYear < julianSkew) {
currentYearSkew = dayOfYear-1;
//Synthetic comment -- @@ -1433,30 +1248,14 @@
lastYearSkew = 0;
currentYearSkew = julianSkew;
}
        isCached = false;
}

private void writeObject(ObjectOutputStream stream) throws IOException {
stream.defaultWriteObject();
}

    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {

stream.defaultReadObject();
setGregorianChange(new Date(gregorianCutover));
        isCached = false;
    }

    @Override
    public void setFirstDayOfWeek(int value) {
        super.setFirstDayOfWeek(value);
        isCached = false;
    }

    @Override
    public void setMinimalDaysInFirstWeek(int value) {
        super.setMinimalDaysInFirstWeek(value);
        isCached = false;
}
}







