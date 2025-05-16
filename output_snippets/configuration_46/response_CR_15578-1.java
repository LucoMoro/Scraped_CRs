//<Beginning of snippet n. 0>
sb.append(configName);
sb.append(SEP);
if (locale != null && locale.length >= 2) {
    sb.append(((LanguageQualifier) locale[0]).getValue());
    sb.append(SEP_LOCALE);
    sb.append(((RegionQualifier) locale[1]).getValue());
} else {
    sb.append("default_language");
    sb.append(SEP_LOCALE);
    sb.append("default_region");
}
sb.append(SEP);
sb.append(theme);
//<End of snippet n. 0>