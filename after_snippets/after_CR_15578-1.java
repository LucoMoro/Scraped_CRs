
//<Beginning of snippet n. 0>


sb.append(configName);
sb.append(SEP);
if (locale != null) {
                    if (locale[0] != null && locale[1] != null) {
                        // locale[0]/[1] can be null sometimes when starting Eclipse
                        sb.append(((LanguageQualifier) locale[0]).getValue());
                        sb.append(SEP_LOCALE);
                        sb.append(((RegionQualifier) locale[1]).getValue());
                    }
}
sb.append(SEP);
sb.append(theme);

//<End of snippet n. 0>








