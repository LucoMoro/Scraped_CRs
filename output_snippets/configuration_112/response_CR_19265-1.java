//<Beginning of snippet n. 0>

long time = exif.getGpsDateTime();
long originalDateTime = exif.getOriginalDateTime();

if (time > 0) {
    values.put(Images.Media.DATE_TAKEN, time);
} else if (originalDateTime > 0) {
    values.put(Images.Media.DATE_TAKEN, originalDateTime);
} else {
    // Logging or error handling can be added here for traceability
    // Assuming a method logError for logging purposes
    logError("Both GPS date time and original date time are invalid.");
}

int orientation = exif.getAttributeInt(

//<End of snippet n. 0>