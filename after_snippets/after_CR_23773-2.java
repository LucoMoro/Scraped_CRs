
//<Beginning of snippet n. 0>


String value = attributes.getValue(ATTR_VALUE);

try {
                    // Integer.decode/parseInt can't deal with hex value > 0x7FFFFFFF so we
                    // use Long.decode instead.
mCurrentDeclareStyleable.addValue(mCurrentAttribute,
                            name, (int)(long)Long.decode(value));
} catch (NumberFormatException e) {
// pass, we'll just ignore this value
}

//<End of snippet n. 0>








