//<Beginning of snippet n. 0>
if (locale != null && locale.length >= 2) {
    if (locale[0] instanceof LanguageQualifier && locale[1] instanceof RegionQualifier) {
        sb.append(configName);
        sb.append(SEP);
        sb.append(((LanguageQualifier) locale[0]).getValue());
        sb.append(SEP_LOCALE);
        sb.append(((RegionQualifier) locale[1]).getValue());
    } else {
        System.err.println("Invalid locale types, using default values.");
        appendDefaultValues(sb, configName);
    }
} else {
    System.err.println("Locale is null or does not have enough elements, using default values.");
    appendDefaultValues(sb, configName);
}
sb.append(SEP);
if (theme != null) {
    sb.append(theme);
}

private void appendDefaultValues(StringBuilder sb, String configName) {
    sb.append(configName);
    sb.append(SEP);
    sb.append("defaultLanguage"); // Default value
    sb.append(SEP_LOCALE);
    sb.append("defaultRegion"); // Default value
}
//<End of snippet n. 0>