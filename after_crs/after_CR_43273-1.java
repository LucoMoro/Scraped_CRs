/*Fix a native memory leak in SimpleDateFormat cloning.

We didn't use the regular finalization idiom in NativeDecimalFormat because
Formatter used to create a lot of these objects, and we wanted to be able to
close them manually rather than clogging up the GC. We've since rewritten
the Formatter code so this is no longer relevant, but we left the finalization
in the wrong class.

Tested with a "while (true) sdf.clone();" loop and ps(1).

Bug:http://code.google.com/p/android/issues/detail?id=37607Change-Id:I0c435edf8c1bced00ebf04bd7187883344e971e7*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DateFormat.java b/luni/src/main/java/java/text/DateFormat.java
//Synthetic comment -- index 4055d20..b45e699 100644

//Synthetic comment -- @@ -286,10 +286,6 @@

/**
* Returns a new instance of {@code DateFormat} with the same properties.
*/
@Override
public Object clone() {








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DecimalFormat.java b/luni/src/main/java/java/text/DecimalFormat.java
//Synthetic comment -- index eded6a8..948bec1 100644

//Synthetic comment -- @@ -501,16 +501,7 @@

private transient DecimalFormatSymbols symbols;

    private transient NativeDecimalFormat ndf;

private transient RoundingMode roundingMode = RoundingMode.HALF_EVEN;

//Synthetic comment -- @@ -562,14 +553,14 @@

private void initNative(String pattern) {
try {
            this.ndf = new NativeDecimalFormat(pattern, symbols);
} catch (IllegalArgumentException ex) {
throw new IllegalArgumentException(pattern);
}
        super.setMaximumFractionDigits(ndf.getMaximumFractionDigits());
        super.setMaximumIntegerDigits(ndf.getMaximumIntegerDigits());
        super.setMinimumFractionDigits(ndf.getMinimumFractionDigits());
        super.setMinimumIntegerDigits(ndf.getMinimumIntegerDigits());
}

/**
//Synthetic comment -- @@ -582,7 +573,7 @@
*            if the pattern cannot be parsed.
*/
public void applyLocalizedPattern(String pattern) {
        ndf.applyLocalizedPattern(pattern);
}

/**
//Synthetic comment -- @@ -595,20 +586,17 @@
*            if the pattern cannot be parsed.
*/
public void applyPattern(String pattern) {
        ndf.applyPattern(pattern);
}

/**
* Returns a new instance of {@code DecimalFormat} with the same pattern and
     * properties.
*/
@Override
public Object clone() {
DecimalFormat clone = (DecimalFormat) super.clone();
        clone.ndf = (NativeDecimalFormat) ndf.clone();
clone.symbols = (DecimalFormatSymbols) symbols.clone();
return clone;
}
//Synthetic comment -- @@ -633,7 +621,7 @@
return false;
}
DecimalFormat other = (DecimalFormat) object;
        return (this.ndf == null ? other.ndf == null : this.ndf.equals(other.ndf)) &&
getDecimalFormatSymbols().equals(other.getDecimalFormatSymbols());
}

//Synthetic comment -- @@ -656,7 +644,7 @@
if (object == null) {
throw new NullPointerException("object == null");
}
        return ndf.formatToCharacterIterator(object);
}

private void checkBufferAndFieldPosition(StringBuffer buffer, FieldPosition position) {
//Synthetic comment -- @@ -686,14 +674,14 @@
setRoundingMode(RoundingMode.UNNECESSARY);
}
}
        buffer.append(ndf.formatDouble(value, position));
return buffer;
}

@Override
public StringBuffer format(long value, StringBuffer buffer, FieldPosition position) {
checkBufferAndFieldPosition(buffer, position);
        buffer.append(ndf.formatLong(value, position));
return buffer;
}

//Synthetic comment -- @@ -703,12 +691,12 @@
if (number instanceof BigInteger) {
BigInteger bigInteger = (BigInteger) number;
char[] chars = (bigInteger.bitLength() < 64)
                    ? ndf.formatLong(bigInteger.longValue(), position)
                    : ndf.formatBigInteger(bigInteger, position);
buffer.append(chars);
return buffer;
} else if (number instanceof BigDecimal) {
            buffer.append(ndf.formatBigDecimal((BigDecimal) number, position));
return buffer;
}
return super.format(number, buffer, position);
//Synthetic comment -- @@ -743,7 +731,7 @@
* @return the number of digits grouped together.
*/
public int getGroupingSize() {
        return ndf.getGroupingSize();
}

/**
//Synthetic comment -- @@ -753,7 +741,7 @@
* @return the multiplier.
*/
public int getMultiplier() {
        return ndf.getMultiplier();
}

/**
//Synthetic comment -- @@ -762,7 +750,7 @@
* @return the negative prefix.
*/
public String getNegativePrefix() {
        return ndf.getNegativePrefix();
}

/**
//Synthetic comment -- @@ -771,7 +759,7 @@
* @return the negative suffix.
*/
public String getNegativeSuffix() {
        return ndf.getNegativeSuffix();
}

/**
//Synthetic comment -- @@ -780,7 +768,7 @@
* @return the positive prefix.
*/
public String getPositivePrefix() {
        return ndf.getPositivePrefix();
}

/**
//Synthetic comment -- @@ -789,12 +777,12 @@
* @return the positive suffix.
*/
public String getPositiveSuffix() {
        return ndf.getPositiveSuffix();
}

@Override
public int hashCode() {
        return getPositivePrefix().hashCode();
}

/**
//Synthetic comment -- @@ -805,7 +793,7 @@
*         {@code false} otherwise.
*/
public boolean isDecimalSeparatorAlwaysShown() {
        return ndf.isDecimalSeparatorAlwaysShown();
}

/**
//Synthetic comment -- @@ -817,7 +805,7 @@
*         {@code Double}.
*/
public boolean isParseBigDecimal() {
        return ndf.isParseBigDecimal();
}

/**
//Synthetic comment -- @@ -838,7 +826,7 @@
// In this implementation, NativeDecimalFormat is wrapped to
// fulfill most of the format and parse feature. And this method is
// delegated to the wrapped instance of NativeDecimalFormat.
        ndf.setParseIntegerOnly(value);
}

/**
//Synthetic comment -- @@ -850,7 +838,7 @@
*/
@Override
public boolean isParseIntegerOnly() {
        return ndf.isParseIntegerOnly();
}

private static final Double NEGATIVE_ZERO_DOUBLE = new Double(-0.0);
//Synthetic comment -- @@ -880,7 +868,7 @@
*/
@Override
public Number parse(String string, ParsePosition position) {
        Number number = ndf.parse(string, position);
if (number == null) {
return null;
}
//Synthetic comment -- @@ -918,7 +906,7 @@
if (value != null) {
// The Java object is canonical, and we copy down to native code.
this.symbols = (DecimalFormatSymbols) value.clone();
            ndf.setDecimalFormatSymbols(this.symbols);
}
}

//Synthetic comment -- @@ -932,7 +920,7 @@
*/
@Override
public void setCurrency(Currency currency) {
        ndf.setCurrency(Currency.getInstance(currency.getCurrencyCode()));
symbols.setCurrency(currency);
}

//Synthetic comment -- @@ -945,7 +933,7 @@
*            formatted; {@code false} otherwise.
*/
public void setDecimalSeparatorAlwaysShown(boolean value) {
        ndf.setDecimalSeparatorAlwaysShown(value);
}

/**
//Synthetic comment -- @@ -957,7 +945,7 @@
*            the number of digits grouped together.
*/
public void setGroupingSize(int value) {
        ndf.setGroupingSize(value);
}

/**
//Synthetic comment -- @@ -969,7 +957,7 @@
*/
@Override
public void setGroupingUsed(boolean value) {
        ndf.setGroupingUsed(value);
}

/**
//Synthetic comment -- @@ -979,7 +967,7 @@
*/
@Override
public boolean isGroupingUsed() {
        return ndf.isGroupingUsed();
}

/**
//Synthetic comment -- @@ -992,7 +980,7 @@
@Override
public void setMaximumFractionDigits(int value) {
super.setMaximumFractionDigits(value);
        ndf.setMaximumFractionDigits(getMaximumFractionDigits());
// Changing the maximum fraction digits needs to update ICU4C's rounding configuration.
setRoundingMode(roundingMode);
}
//Synthetic comment -- @@ -1007,7 +995,7 @@
@Override
public void setMaximumIntegerDigits(int value) {
super.setMaximumIntegerDigits(value);
        ndf.setMaximumIntegerDigits(getMaximumIntegerDigits());
}

/**
//Synthetic comment -- @@ -1020,7 +1008,7 @@
@Override
public void setMinimumFractionDigits(int value) {
super.setMinimumFractionDigits(value);
        ndf.setMinimumFractionDigits(getMinimumFractionDigits());
}

/**
//Synthetic comment -- @@ -1033,7 +1021,7 @@
@Override
public void setMinimumIntegerDigits(int value) {
super.setMinimumIntegerDigits(value);
        ndf.setMinimumIntegerDigits(getMinimumIntegerDigits());
}

/**
//Synthetic comment -- @@ -1044,7 +1032,7 @@
*            the multiplier.
*/
public void setMultiplier(int value) {
        ndf.setMultiplier(value);
}

/**
//Synthetic comment -- @@ -1054,7 +1042,7 @@
*            the negative prefix.
*/
public void setNegativePrefix(String value) {
        ndf.setNegativePrefix(value);
}

/**
//Synthetic comment -- @@ -1064,7 +1052,7 @@
*            the negative suffix.
*/
public void setNegativeSuffix(String value) {
        ndf.setNegativeSuffix(value);
}

/**
//Synthetic comment -- @@ -1074,7 +1062,7 @@
*            the positive prefix.
*/
public void setPositivePrefix(String value) {
        ndf.setPositivePrefix(value);
}

/**
//Synthetic comment -- @@ -1084,7 +1072,7 @@
*            the positive suffix.
*/
public void setPositiveSuffix(String value) {
        ndf.setPositiveSuffix(value);
}

/**
//Synthetic comment -- @@ -1096,7 +1084,7 @@
*            {@code BigDecimal}; {@code false} otherwise.
*/
public void setParseBigDecimal(boolean newValue) {
        ndf.setParseBigDecimal(newValue);
}

/**
//Synthetic comment -- @@ -1106,7 +1094,7 @@
* @return the localized pattern.
*/
public String toLocalizedPattern() {
        return ndf.toLocalizedPattern();
}

/**
//Synthetic comment -- @@ -1116,7 +1104,7 @@
* @return the non-localized pattern.
*/
public String toPattern() {
        return ndf.toPattern();
}

// the fields list to be serialized
//Synthetic comment -- @@ -1157,27 +1145,27 @@
*/
private void writeObject(ObjectOutputStream stream) throws IOException, ClassNotFoundException {
ObjectOutputStream.PutField fields = stream.putFields();
        fields.put("positivePrefix", ndf.getPositivePrefix());
        fields.put("positiveSuffix", ndf.getPositiveSuffix());
        fields.put("negativePrefix", ndf.getNegativePrefix());
        fields.put("negativeSuffix", ndf.getNegativeSuffix());
fields.put("posPrefixPattern", (String) null);
fields.put("posSuffixPattern", (String) null);
fields.put("negPrefixPattern", (String) null);
fields.put("negSuffixPattern", (String) null);
        fields.put("multiplier", ndf.getMultiplier());
        fields.put("groupingSize", (byte) ndf.getGroupingSize());
        fields.put("groupingUsed", ndf.isGroupingUsed());
        fields.put("decimalSeparatorAlwaysShown", ndf.isDecimalSeparatorAlwaysShown());
        fields.put("parseBigDecimal", ndf.isParseBigDecimal());
fields.put("roundingMode", roundingMode);
fields.put("symbols", symbols);
fields.put("useExponentialNotation", false);
fields.put("minExponentDigits", (byte) 0);
        fields.put("maximumIntegerDigits", ndf.getMaximumIntegerDigits());
        fields.put("minimumIntegerDigits", ndf.getMinimumIntegerDigits());
        fields.put("maximumFractionDigits", ndf.getMaximumFractionDigits());
        fields.put("minimumFractionDigits", ndf.getMinimumFractionDigits());
fields.put("serialVersionOnStream", 4);
stream.writeFields();
}
//Synthetic comment -- @@ -1198,14 +1186,14 @@
this.symbols = (DecimalFormatSymbols) fields.get("symbols", null);

initNative("");
        ndf.setPositivePrefix((String) fields.get("positivePrefix", ""));
        ndf.setPositiveSuffix((String) fields.get("positiveSuffix", ""));
        ndf.setNegativePrefix((String) fields.get("negativePrefix", "-"));
        ndf.setNegativeSuffix((String) fields.get("negativeSuffix", ""));
        ndf.setMultiplier(fields.get("multiplier", 1));
        ndf.setGroupingSize(fields.get("groupingSize", (byte) 3));
        ndf.setGroupingUsed(fields.get("groupingUsed", true));
        ndf.setDecimalSeparatorAlwaysShown(fields.get("decimalSeparatorAlwaysShown", false));

setRoundingMode((RoundingMode) fields.get("roundingMode", RoundingMode.HALF_EVEN));

//Synthetic comment -- @@ -1218,8 +1206,8 @@
// behavior in this area is, and it's not obvious how we can second-guess ICU (or tell
// it to just do exactly what we ask). We only need to do this with maximumIntegerDigits
// because ICU doesn't seem to have its own ideas about the other options.
        ndf.setMaximumIntegerDigits(maximumIntegerDigits);
        super.setMaximumIntegerDigits(ndf.getMaximumIntegerDigits());

setMinimumIntegerDigits(minimumIntegerDigits);
setMinimumFractionDigits(minimumFractionDigits);
//Synthetic comment -- @@ -1253,7 +1241,7 @@
this.roundingMode = roundingMode;
if (roundingMode != RoundingMode.UNNECESSARY) { // ICU4C doesn't support UNNECESSARY.
double roundingIncrement = 1.0 / Math.pow(10, Math.max(0, getMaximumFractionDigits()));
            ndf.setRoundingMode(roundingMode, roundingIncrement);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/NumberFormat.java b/luni/src/main/java/java/text/NumberFormat.java
//Synthetic comment -- index 070174b..c285e3d 100644

//Synthetic comment -- @@ -165,11 +165,7 @@
}

/**
     * Returns a new {@code NumberFormat} with the same properties.
*/
@Override
public Object clone() {








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index da5af85..f682c0b 100644

//Synthetic comment -- @@ -411,9 +411,6 @@
/**
* Returns a new {@code SimpleDateFormat} with the same pattern and
* properties as this simple date format.
*/
@Override
public Object clone() {








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/NativeDecimalFormat.java b/luni/src/main/java/libcore/icu/NativeDecimalFormat.java
//Synthetic comment -- index 74775e5..f6b5214 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
import java.util.Currency;
import java.util.NoSuchElementException;

public final class NativeDecimalFormat implements Cloneable {
/**
* Constants corresponding to the native type UNumberFormatSymbol, for setSymbol.
*/
//Synthetic comment -- @@ -144,22 +144,6 @@
this.lastPattern = pattern;
}

public synchronized void close() {
if (address != 0) {
close(address);
//Synthetic comment -- @@ -167,9 +151,27 @@
}
}

    @Override protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    @Override public Object clone() {
        try {
            NativeDecimalFormat clone = (NativeDecimalFormat) super.clone();
            clone.address = cloneImpl(address);
            clone.lastPattern = lastPattern;
            clone.negPrefNull = negPrefNull;
            clone.negSuffNull = negSuffNull;
            clone.posPrefNull = posPrefNull;
            clone.posSuffNull = posSuffNull;
            return clone;
        } catch (CloneNotSupportedException unexpected) {
            throw new AssertionError(unexpected);
        }
}

/**







