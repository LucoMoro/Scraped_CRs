//<Beginning of snippet n. 0>

sb.append(configName);
sb.append(SEP);
if (locale != null && locale.length >= 2) {
    sb.append(((LanguageQualifier) locale[0]).getValue());
    sb.append(SEP_LOCALE);
    sb.append(((RegionQualifier) locale[1]).getValue());
} else {
    if (locale == null) {
        System.err.println("Locale is null. Using default values.");
    } else if (locale.length < 2) {
        System.err.println("Locale has insufficient elements. Using fallback values.");
    }
    sb.append("en"); // Fallback for LanguageQualifier
    sb.append(SEP_LOCALE);
    sb.append("US"); // Fallback for RegionQualifier
}
sb.append(SEP);
sb.append(theme);

//<End of snippet n. 0>