//<Beginning of snippet n. 0>
content.replace(selStart, selEnd, String.valueOf(current).toUpperCase());
content.replace(selStart, selEnd, String.valueOf(current).toLowerCase());
content.replace(selStart, selEnd, val, ix, ix + 1);
removeTimeouts(content);
return true;
//<End of snippet n. 0>