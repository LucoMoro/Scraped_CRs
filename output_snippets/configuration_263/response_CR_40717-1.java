//<Beginning of snippet n. 0>
Values mValues = new Values(); // Assuming this class is defined elsewhere and properly initialized
ActionBarOptions options = new ActionBarOptions();
final String CLIPART = "clipart"; // Assuming this is the correct type and string definition

if (mValues != null) {
    if (mValues.holoDark != null) {
        options.theme = mValues.holoDark
            ? ActionBarIconGenerator.Theme.HOLO_DARK
            : ActionBarIconGenerator.Theme.HOLO_LIGHT;
    }

    if (mValues.sourceType != null) {
        options.sourceIsClipart = mValues.sourceType.equals(CLIPART);
    }
}
//<End of snippet n. 0>