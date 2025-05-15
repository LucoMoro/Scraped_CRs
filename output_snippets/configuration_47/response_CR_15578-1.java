//<Beginning of snippet n. 0>
sb.append(configName);
sb.append(SEP);
if (locale != null && locale.length >= 2) {
    sb.append(((LanguageQualifier) locale[0]).getValue());
    sb.append(SEP_LOCALE);
    sb.append(((RegionQualifier) locale[1]).getValue());
} else {
    // Handle null or insufficient locale case, e.g., using default values or logging
    sb.append("defaultLanguage"); // replace with actual default if applicable
    sb.append(SEP_LOCALE);
    sb.append("defaultRegion"); // replace with actual default if applicable
}
sb.append(SEP);
sb.append(theme);
//<End of snippet n. 0>