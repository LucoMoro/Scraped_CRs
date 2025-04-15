/*Manual date settings should have valid year in 1980-2037

On operator request, limit the value range for year to
1980-2037. This is implemented on other devices. One
reason behind this is to not become dangerously close
to the end of time as unix knows it.

Change-Id:Id6bcd4d2e9268d744c23a73db5e6926f5d43b207*/
//Synthetic comment -- diff --git a/src/com/android/settings/DateTimeSettings.java b/src/com/android/settings/DateTimeSettings.java
//Synthetic comment -- index 5b38651..0339f68 100644

//Synthetic comment -- @@ -156,6 +156,8 @@
public void onDateSet(DatePicker view, int year, int month, int day) {
Calendar c = Calendar.getInstance();

c.set(Calendar.YEAR, year);
c.set(Calendar.MONTH, month);
c.set(Calendar.DAY_OF_MONTH, day);







