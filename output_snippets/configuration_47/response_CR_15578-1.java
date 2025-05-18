//<Beginning of snippet n. 0>
Object lock = new Object();
if (locale != null && locale.length >= 2) {
    synchronized (lock) {
        sb.append(configName);
        sb.append(SEP);
        sb.append(((LanguageQualifier) locale[0]).getValue());
        sb.append(SEP_LOCALE);
        sb.append(((RegionQualifier) locale[1]).getValue());
        sb.append(SEP);
        sb.append(theme);
    }
} else {
    synchronized (lock) {
        sb.append(configName);
        sb.append(SEP);
        sb.append("default_language");
        sb.append(SEP_LOCALE);
        sb.append("default_region");
        sb.append(SEP);
        sb.append(theme);
    }
}
//<End of snippet n. 0>