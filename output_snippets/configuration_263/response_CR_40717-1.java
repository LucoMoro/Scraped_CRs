//<Beginning of snippet n. 0>
if (mValues != null) {
    actionBarOptions.theme = mValues.holoDark
        ? ActionBarIconGenerator.Theme.HOLO_DARK
        : ActionBarIconGenerator.Theme.HOLO_LIGHT;
    actionBarOptions.sourceIsClipart = (mValues.sourceType != null && mValues.sourceType == CLIPART);
    options = actionBarOptions;
}
//<End of snippet n. 0>