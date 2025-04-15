/*Explain Formatter's %tc better.

Bug reported via email by Roy Yokoyama.

Change-Id:I13617d640df8f4e1000114cbe6b3b738e8405568*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Formatter.java b/luni/src/main/java/java/util/Formatter.java
//Synthetic comment -- index 021da08..6ca8733 100644

//Synthetic comment -- @@ -297,9 +297,9 @@
* </tr>
* </table>
* <p>
 * It's also possible to format dates and times with {@code Formatter}, though you should seriously
 * consider using {@link java.text.SimpleDateFormat} via the factory methods in
 * {@link java.text.DateFormat} instead.
* The facilities offered by {@code Formatter} are low-level and place the burden of localization
* on the developer. Using {@link java.text.DateFormat#getDateInstance},
* {@link java.text.DateFormat#getTimeInstance}, and
//Synthetic comment -- @@ -310,11 +310,8 @@
* which you can get with {@code "%tF"} (2010-01-22), {@code "%tF %tR"} (2010-01-22 13:39),
* {@code "%tF %tT"} (2010-01-22 13:39:15), or {@code "%tF %tT%z"} (2010-01-22 13:39:15-0800).
* <p>
 * As with the other conversions, date/time conversion has an uppercase format. Replacing
 * {@code %t} with {@code %T} will uppercase the field according to the rules of the formatter's
 * locale.
 * <p>
 * This table shows the date/time conversions:
* <table BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
* <tr BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
* <TD COLSPAN=4><B>Date/time conversions</B>
//Synthetic comment -- @@ -349,7 +346,7 @@
* </tr>
* <tr>
* <td width="5%">{@code tc}</td>
 * <td width="25%">Locale-preferred date and time representation. (See {@link java.text.DateFormat} for more variations.)</td>
* <td width="30%">{@code format("%tc", cal);}</td>
* <td width="30%">{@code Tue Apr 01 16:19:17 CEST 2008}</td>
* </tr>
//Synthetic comment -- @@ -510,6 +507,10 @@
* <td width="30%">{@code CEST}</td>
* </tr>
* </table>
* <p><i>Number localization</i>. Some conversions use localized decimal digits rather than the
* usual ASCII digits. So formatting {@code 123} with {@code %d} will give 123 in English locales
* but &#x0661;&#x0662;&#x0663; in appropriate Arabic locales, for example. This number localization







