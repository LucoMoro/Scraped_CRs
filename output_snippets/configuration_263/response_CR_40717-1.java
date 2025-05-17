//<Beginning of snippet n. 0>
if (actionBarOptions == null) {
    actionBarOptions = new ActionBarOptions();
}
if (mValues == null) {
    mValues = new Values(); // Initialize mValues appropriately
}

if (mValues.holoDark != null) {
    actionBarOptions.theme = mValues.holoDark
        ? ActionBarIconGenerator.Theme.HOLO_DARK
        : ActionBarIconGenerator.Theme.HOLO_LIGHT;
}

if (mValues.sourceType != null) {
    actionBarOptions.sourceIsClipart = (mValues.sourceType == SourceType.CLIPART);
}

options = actionBarOptions;
break;
//<End of snippet n. 0>