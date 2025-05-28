//<Beginning of snippet n. 0>
ActionBarOptions options = new ActionBarOptions();
Values mValues = new Values(); // Assuming this class is defined elsewhere and properly initialized
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

ActionBarOptions actionBarOptions = options; // Declaration and initialization before use
options = actionBarOptions;
//<End of snippet n. 0>