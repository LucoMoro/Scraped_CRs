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
                && getMinimalDaysInFirstWeek() == cal.getMinimalDaysInFirstWeek()
&& getTimeZone().equals(cal.getTimeZone());
}

//Synthetic comment -- @@ -903,11 +902,8 @@
}

/**
     * Returns the maximum value of the specified field for the current date.
     * For example, the maximum number of days in the current month.
*/
public int getActualMaximum(int field) {
int value, next;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/GregorianCalendar.java b/luni/src/main/java/java/util/GregorianCalendar.java
//Synthetic comment -- index 4d93aef..9ff9ccc 100644

//Synthetic comment -- @@ -219,14 +219,6 @@
private static int[] leastMaximums = new int[] { 1, 292269054, 11, 50, 3,
28, 355, 7, 3, 1, 11, 23, 59, 59, 999, 50400000, 1200000 };

private int currentYearSkew = 10;

private int lastYearSkew = 0;
//Synthetic comment -- @@ -365,8 +357,6 @@
throw new IllegalArgumentException();
}

if (field == ERA) {
complete();
if (fields[ERA] == AD) {
//Synthetic comment -- @@ -468,19 +458,8 @@
complete();
}

    private void fullFieldsCalc(long timeVal, int zoneOffset) {
        int millis = (int) (time % 86400000);
long days = timeVal / 86400000;

if (millis < 0) {
//Synthetic comment -- @@ -583,31 +562,6 @@
}
}

@Override
protected void computeFields() {
TimeZone timeZone = getTimeZone();
//Synthetic comment -- @@ -616,99 +570,11 @@
fields[DST_OFFSET] = dstOffset;
fields[ZONE_OFFSET] = zoneOffset;

        fullFieldsCalc(time, zoneOffset);

for (int i = 0; i < FIELD_COUNT; i++) {
isSet[i] = true;
}
}

@Override
//Synthetic comment -- @@ -939,19 +805,17 @@
return (int) days + 1;
}

    private long daysFromBaseYear(long year) {
if (year >= 1970) {
long days = (year - 1970) * 365 + ((year - 1969) / 4);
if (year > changeYear) {
days -= ((year - 1901) / 100) - ((year - 1601) / 400);
} else {
                if (year == changeYear) {
days += currentYearSkew;
                } else if (year == changeYear - 1) {
days += lastYearSkew;
                } else {
days += julianSkew;
}
}
//Synthetic comment -- @@ -995,21 +859,10 @@
}

/**
     * Returns true if {@code object} is a GregorianCalendar with the same
     * properties.
*/
    @Override public boolean equals(Object object) {
if (!(object instanceof GregorianCalendar)) {
return false;
}
//Synthetic comment -- @@ -1020,28 +873,12 @@
&& gregorianCutover == ((GregorianCalendar) object).gregorianCutover;
}

    @Override public int getActualMaximum(int field) {
int value;
if ((value = maximums[field]) == leastMaximums[field]) {
return value;
}

complete();
long orgTime = time;
int result = 0;
//Synthetic comment -- @@ -1216,32 +1053,16 @@
month++;
}
int dayOfWeek = mod7(dayCount - 3) + 1;
        return timeZone.getOffset(AD, year, month, date, dayOfWeek, millis);
}

    @Override public int hashCode() {
return super.hashCode()
+ ((int) (gregorianCutover >>> 32) ^ (int) gregorianCutover);
}

/**
     * Returns true if {@code year} is a leap year.
*/
public boolean isLeapYear(int year) {
if (year > changeYear) {
//Synthetic comment -- @@ -1294,8 +1115,6 @@
throw new IllegalArgumentException();
}

complete();
int days, day, mod, maxWeeks, newWeek;
int max = -1;
//Synthetic comment -- @@ -1410,9 +1229,6 @@

/**
* Sets the gregorian change date of this calendar.
*/
public void setGregorianChange(Date date) {
gregorianCutover = date.getTime();
//Synthetic comment -- @@ -1424,7 +1240,6 @@
}
julianSkew = ((changeYear - 2000) / 400) + julianError()
- ((changeYear - 2000) / 100);
int dayOfYear = cal.get(DAY_OF_YEAR);
if (dayOfYear < julianSkew) {
currentYearSkew = dayOfYear-1;
//Synthetic comment -- @@ -1433,30 +1248,14 @@
lastYearSkew = 0;
currentYearSkew = julianSkew;
}
}

private void writeObject(ObjectOutputStream stream) throws IOException {
stream.defaultWriteObject();
}

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();
setGregorianChange(new Date(gregorianCutover));
}
}







