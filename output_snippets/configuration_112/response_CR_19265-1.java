//<Beginning of snippet n. 0>

long time = exif.getGpsDateTime();
if (time != -1) {
    values.put(Images.Media.DATE_TAKEN, time);
} else {
    long originalDateTime = exif.getOriginalDateTime(); // Assuming this method exists to get original datetime
    values.put(Images.Media.DATE_TAKEN, originalDateTime);
}

int orientation = exif.getAttributeInt(

//<End of snippet n. 0>