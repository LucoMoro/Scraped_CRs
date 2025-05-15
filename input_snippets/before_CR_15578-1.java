
//<Beginning of snippet n. 0>


sb.append(configName);
sb.append(SEP);
if (locale != null) {
                    sb.append(((LanguageQualifier) locale[0]).getValue());
                    sb.append(SEP_LOCALE);
                    sb.append(((RegionQualifier) locale[1]).getValue());
}
sb.append(SEP);
sb.append(theme);

//<End of snippet n. 0>








