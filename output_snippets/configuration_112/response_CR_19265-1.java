//<Beginning of snippet n. 0>
long time = exif.getGpsDateTime();
if (time != -1) {
    values.put(Images.Media.DATE_TAKEN, time);
} else {
    String originalDateTime = exif.getOriginalDateTime();
    if (originalDateTime != null) {
        values.put(Images.Media.DATE_TAKEN, originalDateTime);
    }
}
int orientation = exif.getAttributeInt(
//<End of snippet n. 0>