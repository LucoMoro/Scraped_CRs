//<Beginning of snippet n. 0>


content.replace(selStart, selEnd,
String.valueOf(current).toUpperCase());
removeTimeouts(content);

return true;
}
content.replace(selStart, selEnd,
String.valueOf(current).toLowerCase());
removeTimeouts(content);

return true;
}

content.replace(selStart, selEnd, val, ix, ix + 1);
removeTimeouts(content);

return true;
}
}

removeTimeouts(content);

// Set up the callback so we can remove the timeout if the
// cursor moves.

//<End of snippet n. 0>