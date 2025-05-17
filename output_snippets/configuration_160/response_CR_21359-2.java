//<Beginning of snippet n. 0>
content.replace(selStart, selEnd,
String.valueOf(current).toUpperCase());
content.replace(selStart, selEnd,
String.valueOf(current).toLowerCase());
content.replace(selStart, selEnd, val, ix, ix + 1);

return true;
// Set up the callback so we can remove the timeout if the
// cursor moves.
//<End of snippet n. 0>