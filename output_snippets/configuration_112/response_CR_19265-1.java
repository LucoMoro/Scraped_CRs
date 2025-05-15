//<Beginning of snippet n. 0>
long time = exif.getGpsDateTime();
if (time == -1) {
    time = exif.getDateTime(); // Assuming getDateTime() retrieves the original datetime
}
values.put(Images.Media.DATE_TAKEN, time);
int orientation = exif.getAttributeInt(
//<End of snippet n. 0>