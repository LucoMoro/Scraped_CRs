/*Explain the current 12/24-hour situation with DateFormat.

Until we have a way to access the user's setting from libcore,
we can't fix this here. We might want to add more API to
android.text.format.DateFormat in the meantime.

Bug: 7259195 (amongst others)
Change-Id:Iea18cc7fa4412b79a1acd8730f28ded5125e6129*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DateFormat.java b/luni/src/main/java/java/text/DateFormat.java
//Synthetic comment -- index 8441b59..116bdd5 100644

//Synthetic comment -- @@ -32,6 +32,9 @@
* <p>This class provides factories for obtaining instances configured for a specific locale.
* The most common subclass is {@link SimpleDateFormat}.
*
 * <p>Use {@link android.text.format.DateFormat#getTimeFormat} to take the user's preference for
 * the 12- or 24-hour clock into account.
 *
* <h4>Sample Code</h4>
* <p>This code:
* <pre>
//Synthetic comment -- @@ -300,10 +303,6 @@

/**
* Formats the specified date using the rules of this date format.
*/
public final String format(Date date) {
return format(date, new StringBuffer(), new FieldPosition(0))
//Synthetic comment -- @@ -328,8 +327,7 @@
*            of the alignment field in the formatted text.
* @return the string buffer.
*/
    public abstract StringBuffer format(Date date, StringBuffer buffer, FieldPosition field);

/**
* Returns an array of locales for which custom {@code DateFormat} instances
//Synthetic comment -- @@ -342,8 +340,6 @@

/**
* Returns the calendar used by this {@code DateFormat}.
*/
public Calendar getCalendar() {
return calendar;
//Synthetic comment -- @@ -352,8 +348,7 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates in
* the DEFAULT style for the default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
*/
public static final DateFormat getDateInstance() {
return getDateInstance(DEFAULT);
//Synthetic comment -- @@ -365,11 +360,8 @@
* See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @throws IllegalArgumentException
     *             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
*/
public static final DateFormat getDateInstance(int style) {
checkDateStyle(style);
//Synthetic comment -- @@ -379,7 +371,6 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates in
* the specified style for the specified locale.
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param locale
//Synthetic comment -- @@ -387,8 +378,6 @@
* @throws IllegalArgumentException
*             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
*             DEFAULT.
*/
public static final DateFormat getDateInstance(int style, Locale locale) {
checkDateStyle(style);
//Synthetic comment -- @@ -398,23 +387,20 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates
* and time values in the DEFAULT style for the default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
*/
public static final DateFormat getDateTimeInstance() {
return getDateTimeInstance(DEFAULT, DEFAULT);
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing
     * dates and times in the manner appropriate for the user's default locale.
* See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
* @param dateStyle
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param timeStyle
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @throws IllegalArgumentException
*             if {@code dateStyle} or {@code timeStyle} is not one of
*             SHORT, MEDIUM, LONG, FULL, or DEFAULT.
//Synthetic comment -- @@ -426,8 +412,8 @@
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing
     * dates and times in the specified styles for the specified locale.
*
* @param dateStyle
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
//Synthetic comment -- @@ -435,8 +421,6 @@
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param locale
*            the locale.
* @throws IllegalArgumentException
*             if {@code dateStyle} or {@code timeStyle} is not one of
*             SHORT, MEDIUM, LONG, FULL, or DEFAULT.
//Synthetic comment -- @@ -452,9 +436,7 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates
* and times in the SHORT style for the default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
*/
public static final DateFormat getInstance() {
return getDateTimeInstance(SHORT, SHORT);
//Synthetic comment -- @@ -462,31 +444,30 @@

/**
* Returns the {@code NumberFormat} used by this {@code DateFormat}.
*/
public NumberFormat getNumberFormat() {
return numberFormat;
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing times
     * in the DEFAULT style for the default locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     * See {@link android.text.format.DateFormat#getTimeFormat} for an equivalent method
     * that takes the user's preference for the 12- or 24-hour clock into account.
*/
public static final DateFormat getTimeInstance() {
return getTimeInstance(DEFAULT);
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing times
     * in the specified style for the user's default locale.
* See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     * See {@link android.text.format.DateFormat#getTimeFormat} for an equivalent method
     * that takes the user's preference for the 12- or 24-hour clock into account.
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @throws IllegalArgumentException
*             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
*             DEFAULT.
//Synthetic comment -- @@ -497,9 +478,10 @@
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing times
     * in the specified style for the specified locale.
     * See {@link android.text.format.DateFormat#getTimeFormat} for an equivalent method
     * that takes the user's preference for the 12- or 24-hour clock into account.
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param locale
//Synthetic comment -- @@ -507,8 +489,6 @@
* @throws IllegalArgumentException
*             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
*             DEFAULT.
*/
public static final DateFormat getTimeInstance(int style, Locale locale) {
checkTimeStyle(style);
//Synthetic comment -- @@ -516,9 +496,7 @@
}

/**
     * Returns the {@code TimeZone} used by this date format's {@code Calendar}.
*/
public TimeZone getTimeZone() {
return calendar.getTimeZone();
//Synthetic comment -- @@ -534,9 +512,7 @@
}

/**
     * Returns {@code true} if this instance's calendar is lenient; {@code false} otherwise.
*/
public boolean isLenient() {
return calendar.isLenient();
//Synthetic comment -- @@ -790,8 +766,6 @@

/**
* Returns the Calendar field that this field represents.
*/
public int getCalendarField() {
return calendarField;
//Synthetic comment -- @@ -801,10 +775,6 @@
* Returns the {@code DateFormat.Field} instance for the given calendar
* field.
*
* @throws IllegalArgumentException
*             if {@code calendarField} is negative or greater than the
*             field count of {@code Calendar}.








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index 7c3187c..18e511e 100644

//Synthetic comment -- @@ -40,8 +40,10 @@
* <h4>Time Pattern Syntax</h4>
* <p>You can supply a pattern describing what strings are produced/accepted, but almost all
* callers should use {@link DateFormat#getDateInstance}, {@link DateFormat#getDateTimeInstance},
 * or {@link android.text.format.DateFormat#getTimeFormat} to get a ready-made instance suitable
 * for the user's locale. (Note that {@code java.text.DateFormat.getDateTimeInstance} doesn't
 * respect the user's preference for the 12- or 24-hour clock, but
 * {@code android.text.format.DateFormat.getTimeFormat} does.)
*
* <p>The main reason you'd create an instance this class directly is because you need to
* format/parse a specific machine-readable format, in which case you almost certainly want







