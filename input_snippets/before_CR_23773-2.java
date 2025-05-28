
//<Beginning of snippet n. 0>


String value = attributes.getValue(ATTR_VALUE);

try {
mCurrentDeclareStyleable.addValue(mCurrentAttribute,
                            name, Integer.decode(value));
} catch (NumberFormatException e) {
// pass, we'll just ignore this value
}

//<End of snippet n. 0>








