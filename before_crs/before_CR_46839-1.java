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
* <h4>Sample Code</h4>
* <p>This code:
* <pre>
//Synthetic comment -- @@ -300,10 +303,6 @@

/**
* Formats the specified date using the rules of this date format.
     *
     * @param date
     *            the date to format.
     * @return the formatted string.
*/
public final String format(Date date) {
return format(date, new StringBuffer(), new FieldPosition(0))
//Synthetic comment -- @@ -328,8 +327,7 @@
*            of the alignment field in the formatted text.
* @return the string buffer.
*/
    public abstract StringBuffer format(Date date, StringBuffer buffer,
            FieldPosition field);

/**
* Returns an array of locales for which custom {@code DateFormat} instances
//Synthetic comment -- @@ -342,8 +340,6 @@

/**
* Returns the calendar used by this {@code DateFormat}.
     *
     * @return the calendar used by this date format.
*/
public Calendar getCalendar() {
return calendar;
//Synthetic comment -- @@ -352,8 +348,7 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates in
* the DEFAULT style for the default locale.
     *
     * @return the {@code DateFormat} instance for the default style and locale.
*/
public static final DateFormat getDateInstance() {
return getDateInstance(DEFAULT);
//Synthetic comment -- @@ -365,11 +360,8 @@
* See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
     * @return the {@code DateFormat} instance for {@code style} and the default
     *         locale.
* @throws IllegalArgumentException
     *             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
     *             DEFAULT.
*/
public static final DateFormat getDateInstance(int style) {
checkDateStyle(style);
//Synthetic comment -- @@ -379,7 +371,6 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates in
* the specified style for the specified locale.
     *
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param locale
//Synthetic comment -- @@ -387,8 +378,6 @@
* @throws IllegalArgumentException
*             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
*             DEFAULT.
     * @return the {@code DateFormat} instance for {@code style} and
     *         {@code locale}.
*/
public static final DateFormat getDateInstance(int style, Locale locale) {
checkDateStyle(style);
//Synthetic comment -- @@ -398,23 +387,20 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates
* and time values in the DEFAULT style for the default locale.
     *
     * @return the {@code DateFormat} instance for the default style and locale.
*/
public static final DateFormat getDateTimeInstance() {
return getDateTimeInstance(DEFAULT, DEFAULT);
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing of both
     * dates and time values in the manner appropriate for the user's default locale.
* See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
* @param dateStyle
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param timeStyle
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
     * @return the {@code DateFormat} instance for {@code dateStyle},
     *         {@code timeStyle} and the default locale.
* @throws IllegalArgumentException
*             if {@code dateStyle} or {@code timeStyle} is not one of
*             SHORT, MEDIUM, LONG, FULL, or DEFAULT.
//Synthetic comment -- @@ -426,8 +412,8 @@
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing dates
     * and time values in the specified styles for the specified locale.
*
* @param dateStyle
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
//Synthetic comment -- @@ -435,8 +421,6 @@
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param locale
*            the locale.
     * @return the {@code DateFormat} instance for {@code dateStyle},
     *         {@code timeStyle} and {@code locale}.
* @throws IllegalArgumentException
*             if {@code dateStyle} or {@code timeStyle} is not one of
*             SHORT, MEDIUM, LONG, FULL, or DEFAULT.
//Synthetic comment -- @@ -452,9 +436,7 @@
/**
* Returns a {@code DateFormat} instance for formatting and parsing dates
* and times in the SHORT style for the default locale.
     *
     * @return the {@code DateFormat} instance for the SHORT style and default
     *         locale.
*/
public static final DateFormat getInstance() {
return getDateTimeInstance(SHORT, SHORT);
//Synthetic comment -- @@ -462,31 +444,30 @@

/**
* Returns the {@code NumberFormat} used by this {@code DateFormat}.
     *
     * @return the {@code NumberFormat} used by this date format.
*/
public NumberFormat getNumberFormat() {
return numberFormat;
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing time
     * values in the DEFAULT style for the default locale.
     *
     * @return the {@code DateFormat} instance for the default style and locale.
*/
public static final DateFormat getTimeInstance() {
return getTimeInstance(DEFAULT);
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing time
     * values in the specified style for the user's default locale.
* See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
     * @return the {@code DateFormat} instance for {@code style} and the default
     *         locale.
* @throws IllegalArgumentException
*             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
*             DEFAULT.
//Synthetic comment -- @@ -497,9 +478,10 @@
}

/**
     * Returns a {@code DateFormat} instance for formatting and parsing time
     * values in the specified style for the specified locale.
     *
* @param style
*            one of SHORT, MEDIUM, LONG, FULL, or DEFAULT.
* @param locale
//Synthetic comment -- @@ -507,8 +489,6 @@
* @throws IllegalArgumentException
*             if {@code style} is not one of SHORT, MEDIUM, LONG, FULL, or
*             DEFAULT.
     * @return the {@code DateFormat} instance for {@code style} and
     *         {@code locale}.
*/
public static final DateFormat getTimeInstance(int style, Locale locale) {
checkTimeStyle(style);
//Synthetic comment -- @@ -516,9 +496,7 @@
}

/**
     * Returns the time zone of this date format's calendar.
     *
     * @return the time zone of the calendar used by this date format.
*/
public TimeZone getTimeZone() {
return calendar.getTimeZone();
//Synthetic comment -- @@ -534,9 +512,7 @@
}

/**
     * Indicates whether the calendar used by this date format is lenient.
     *
     * @return {@code true} if the calendar is lenient; {@code false} otherwise.
*/
public boolean isLenient() {
return calendar.isLenient();
//Synthetic comment -- @@ -790,8 +766,6 @@

/**
* Returns the Calendar field that this field represents.
         *
         * @return the calendar field.
*/
public int getCalendarField() {
return calendarField;
//Synthetic comment -- @@ -801,10 +775,6 @@
* Returns the {@code DateFormat.Field} instance for the given calendar
* field.
*
         * @param calendarField
         *            a calendar field constant.
         * @return the {@code DateFormat.Field} corresponding to
         *         {@code calendarField}.
* @throws IllegalArgumentException
*             if {@code calendarField} is negative or greater than the
*             field count of {@code Calendar}.








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index 7c3187c..18e511e 100644

//Synthetic comment -- @@ -40,8 +40,10 @@
* <h4>Time Pattern Syntax</h4>
* <p>You can supply a pattern describing what strings are produced/accepted, but almost all
* callers should use {@link DateFormat#getDateInstance}, {@link DateFormat#getDateTimeInstance},
 * or {@link DateFormat#getTimeInstance} to get a ready-made instance suitable for the user's
 * locale.
*
* <p>The main reason you'd create an instance this class directly is because you need to
* format/parse a specific machine-readable format, in which case you almost certainly want







