//<Beginning of snippet n. 0>
if (mValues != null && actionBarOptions != null) {
    actionBarOptions.theme = mValues.holoDark
        ? ActionBarIconGenerator.Theme.HOLO_DARK
        : ActionBarIconGenerator.Theme.HOLO_LIGHT;
    actionBarOptions.sourceIsClipart = (mValues.sourceType == ActionBarIconGenerator.CLIPART);
    
    options = actionBarOptions;
}
//<End of snippet n. 0>