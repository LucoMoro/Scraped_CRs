/*Add detail messages to all the easy IllegalArgumentException cases.

Noticed during my recent Matcher change.

Change-Id:I415d911b26d0ee548ca04d56bba7fc3d4e6b3f88*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/io/BufferedReader.java b/luni/src/main/java/java/io/BufferedReader.java
//Synthetic comment -- index 5f2484b..9fba039 100644

//Synthetic comment -- @@ -190,7 +190,7 @@
@Override
public void mark(int markLimit) throws IOException {
if (markLimit < 0) {
            throw new IllegalArgumentException();
}
synchronized (lock) {
checkNotClosed();








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/StringReader.java b/luni/src/main/java/java/io/StringReader.java
//Synthetic comment -- index 9f8ff13..5579d62 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
@Override
public void mark(int readLimit) throws IOException {
if (readLimit < 0) {
            throw new IllegalArgumentException();
}

synchronized (lock) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/StringWriter.java b/luni/src/main/java/java/io/StringWriter.java
//Synthetic comment -- index f0b1d18..2946483 100644

//Synthetic comment -- @@ -49,11 +49,11 @@
* writer.
*
* @param initialSize
     *            the intial size of the target string buffer.
*/
public StringWriter(int initialSize) {
if (initialSize < 0) {
            throw new IllegalArgumentException();
}
buf = new StringBuffer(initialSize);
lock = buf;








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/System.java b/luni/src/main/java/java/lang/System.java
//Synthetic comment -- index f8f9146..b7c04cb 100644

//Synthetic comment -- @@ -412,58 +412,44 @@
/**
* Returns the value of a particular system property. The {@code
* defaultValue} will be returned if no such property has been found.
     *
     * @param prop
     *            the name of the system property to look up.
     * @param defaultValue
     *            the return value if the system property with the given name
     *            does not exist.
     * @return the value of the specified system property or the {@code
     *         defaultValue} if the property does not exist.
*/
    public static String getProperty(String prop, String defaultValue) {
        if (prop.isEmpty()) {
            throw new IllegalArgumentException();
}
        return getProperties().getProperty(prop, defaultValue);
}

/**
* Sets the value of a particular system property.
*
     * @param prop
     *            the name of the system property to be changed.
     * @param value
     *            the value to associate with the given property {@code prop}.
* @return the old value of the property or {@code null} if the property
*         didn't exist.
*/
    public static String setProperty(String prop, String value) {
        if (prop.isEmpty()) {
            throw new IllegalArgumentException();
}
        return (String) getProperties().setProperty(prop, value);
}

/**
* Removes a specific system property.
*
     * @param key
     *            the name of the system property to be removed.
* @return the property value or {@code null} if the property didn't exist.
* @throws NullPointerException
     *             if the argument {@code key} is {@code null}.
* @throws IllegalArgumentException
     *             if the argument {@code key} is empty.
*/
    public static String clearProperty(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
}
        if (key.isEmpty()) {
            throw new IllegalArgumentException();
}
        return (String) getProperties().remove(key);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Thread.java b/luni/src/main/java/java/lang/Thread.java
//Synthetic comment -- index a076a04..cc0975f 100644

//Synthetic comment -- @@ -779,7 +779,7 @@
*/
public final void join(long millis, int nanos) throws InterruptedException {
if (millis < 0 || nanos < 0 || nanos >= NANOS_PER_MILLI) {
            throw new IllegalArgumentException();
}

// avoid overflow: if total > 292,277 years, just wait forever








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/DatagramSocket.java b/luni/src/main/java/java/net/DatagramSocket.java
//Synthetic comment -- index c01f3af..35ef772 100644

//Synthetic comment -- @@ -614,7 +614,7 @@
public void setTrafficClass(int value) throws SocketException {
checkOpen();
if (value < 0 || value > 255) {
            throw new IllegalArgumentException();
}
impl.setOption(SocketOptions.IP_TOS, Integer.valueOf(value));
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/HttpCookie.java b/luni/src/main/java/java/net/HttpCookie.java
//Synthetic comment -- index 057afef..ce1a8d2 100644

//Synthetic comment -- @@ -445,7 +445,7 @@
public HttpCookie(String name, String value) {
String ntrim = name.trim(); // erase leading and trailing whitespace
if (!isValidName(ntrim)) {
            throw new IllegalArgumentException();
}

this.name = ntrim;
//Synthetic comment -- @@ -650,11 +650,11 @@
*
* @throws IllegalArgumentException if v is neither 0 nor 1
*/
    public void setVersion(int v) {
        if (v != 0 && v != 1) {
            throw new IllegalArgumentException();
}
        version = v;
}

@Override public Object clone() {








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/Socket.java b/luni/src/main/java/java/net/Socket.java
//Synthetic comment -- index 7661f28..6b59d49 100644

//Synthetic comment -- @@ -909,7 +909,7 @@
public void setTrafficClass(int value) throws SocketException {
checkOpenAndCreate(true);
if (value < 0 || value > 255) {
            throw new IllegalArgumentException();
}
impl.setOption(SocketOptions.IP_TOS, Integer.valueOf(value));
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URISyntaxException.java b/luni/src/main/java/java/net/URISyntaxException.java
//Synthetic comment -- index 957ea31..70fe6a1 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
}

if (index < -1) {
            throw new IllegalArgumentException();
}

this.input = input;








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/DatagramChannelImpl.java b/luni/src/main/java/java/nio/DatagramChannelImpl.java
//Synthetic comment -- index 4d2fc5a..a23010e 100644

//Synthetic comment -- @@ -253,7 +253,8 @@
}

if (isConnected() && !connectAddress.equals(isa)) {
            throw new IllegalArgumentException();
}

synchronized (writeLock) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/channels/spi/AbstractSelectableChannel.java b/luni/src/main/java/java/nio/channels/spi/AbstractSelectableChannel.java
//Synthetic comment -- index 4035090..1c7e190 100644

//Synthetic comment -- @@ -130,7 +130,7 @@
throw new ClosedChannelException();
}
if (!((interestSet & ~validOps()) == 0)) {
            throw new IllegalArgumentException();
}

synchronized (blockingLock) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/AlgorithmParameterGenerator.java b/luni/src/main/java/java/security/AlgorithmParameterGenerator.java
//Synthetic comment -- index 3edc167..61548d7 100644

//Synthetic comment -- @@ -146,7 +146,7 @@
public static AlgorithmParameterGenerator getInstance(String algorithm,
Provider provider) throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
throw new NullPointerException("algorithm == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyFactory.java b/luni/src/main/java/java/security/KeyFactory.java
//Synthetic comment -- index 8d39003..3bd05b9 100644

//Synthetic comment -- @@ -127,7 +127,7 @@
public static KeyFactory getInstance(String algorithm, Provider provider)
throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
throw new NullPointerException("algorithm == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyPairGenerator.java b/luni/src/main/java/java/security/KeyPairGenerator.java
//Synthetic comment -- index 5c17d79..8a713d0 100644

//Synthetic comment -- @@ -140,7 +140,7 @@
public static KeyPairGenerator getInstance(String algorithm,
Provider provider) throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
throw new NullPointerException("algorithm == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyStore.java b/luni/src/main/java/java/security/KeyStore.java
//Synthetic comment -- index 020010e..b0e4945 100644

//Synthetic comment -- @@ -179,7 +179,7 @@
public static KeyStore getInstance(String type, Provider provider) throws KeyStoreException {
// check parameters
if (provider == null) {
            throw new IllegalArgumentException();
}
if (type == null) {
throw new NullPointerException("type == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/MessageDigest.java b/luni/src/main/java/java/security/MessageDigest.java
//Synthetic comment -- index 6f1bf21..658b41f 100644

//Synthetic comment -- @@ -149,7 +149,7 @@
public static MessageDigest getInstance(String algorithm, Provider provider)
throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
throw new NullPointerException("algorithm == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/SecureRandom.java b/luni/src/main/java/java/security/SecureRandom.java
//Synthetic comment -- index 798f0e6..281885b 100644

//Synthetic comment -- @@ -207,7 +207,7 @@
public static SecureRandom getInstance(String algorithm, Provider provider)
throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
throw new NullPointerException("algorithm == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/Signature.java b/luni/src/main/java/java/security/Signature.java
//Synthetic comment -- index be89654..b2bd122 100644

//Synthetic comment -- @@ -168,7 +168,7 @@
throw new NullPointerException("algorithm == null");
}
if (provider == null) {
            throw new IllegalArgumentException();
}
return getSignatureInstance(algorithm, provider);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertPathValidator.java b/luni/src/main/java/java/security/cert/CertPathValidator.java
//Synthetic comment -- index fda3aeb..a3a666a 100644

//Synthetic comment -- @@ -157,7 +157,7 @@
public static CertPathValidator getInstance(String algorithm,
Provider provider) throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
throw new NullPointerException("algorithm == null");








//Synthetic comment -- diff --git a/luni/src/main/java/java/sql/Date.java b/luni/src/main/java/java/sql/Date.java
//Synthetic comment -- index 2434fbd..34ca5bd 100644

//Synthetic comment -- @@ -81,7 +81,7 @@
@Deprecated
@Override
public int getHours() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -94,7 +94,7 @@
@Deprecated
@Override
public int getMinutes() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -107,7 +107,7 @@
@Deprecated
@Override
public int getSeconds() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -121,7 +121,7 @@
@Deprecated
@Override
public void setHours(int theHours) {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -135,7 +135,7 @@
@Deprecated
@Override
public void setMinutes(int theMinutes) {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -149,7 +149,7 @@
@Deprecated
@Override
public void setSeconds(int theSeconds) {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -212,7 +212,7 @@
*/
public static Date valueOf(String dateString) {
if (dateString == null) {
            throw new IllegalArgumentException();
}
int firstIndex = dateString.indexOf('-');
int secondIndex = dateString.indexOf('-', firstIndex + 1);








//Synthetic comment -- diff --git a/luni/src/main/java/java/sql/Time.java b/luni/src/main/java/java/sql/Time.java
//Synthetic comment -- index bccce68..913379c 100644

//Synthetic comment -- @@ -76,7 +76,7 @@
@Deprecated
@Override
public int getDate() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -90,7 +90,7 @@
@Deprecated
@Override
public int getDay() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -104,7 +104,7 @@
@Deprecated
@Override
public int getMonth() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -118,7 +118,7 @@
@Deprecated
@Override
public int getYear() {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -131,7 +131,7 @@
@Deprecated
@Override
public void setDate(int i) {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -144,7 +144,7 @@
@Deprecated
@Override
public void setMonth(int i) {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -157,7 +157,7 @@
@Deprecated
@Override
public void setYear(int i) {
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -223,7 +223,7 @@
*/
public static Time valueOf(String timeString) {
if (timeString == null) {
            throw new IllegalArgumentException();
}
int firstIndex = timeString.indexOf(':');
int secondIndex = timeString.indexOf(':', firstIndex + 1);








//Synthetic comment -- diff --git a/luni/src/main/java/java/sql/Timestamp.java b/luni/src/main/java/java/sql/Timestamp.java
//Synthetic comment -- index 070e86f..2e6d3d9 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
throws IllegalArgumentException {
super(theYear, theMonth, theDate, theHour, theMinute, theSecond);
if (theNano < 0 || theNano > 999999999) {
            throw new IllegalArgumentException();
}
nanos = theNano;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/AttributedString.java b/luni/src/main/java/java/text/AttributedString.java
//Synthetic comment -- index d679283..289ad6d 100644

//Synthetic comment -- @@ -577,8 +577,8 @@
if (attribute == null) {
throw new NullPointerException("attribute == null");
}
        if (text.length() == 0) {
            throw new IllegalArgumentException();
}

List<Range> ranges = attributeMap.get(attribute);








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/ChoiceFormat.java b/luni/src/main/java/java/text/ChoiceFormat.java
//Synthetic comment -- index e0a1239..014b8c7 100644

//Synthetic comment -- @@ -168,10 +168,10 @@
next = nextDouble(value.doubleValue());
break;
default:
                    throw new IllegalArgumentException();
}
if (limitCount > 0 && next <= limits[limitCount - 1]) {
                throw new IllegalArgumentException();
}
buffer.setLength(0);
position.setIndex(index);
//Synthetic comment -- @@ -426,7 +426,8 @@
*/
public void setChoices(double[] limits, String[] formats) {
if (limits.length != formats.length) {
            throw new IllegalArgumentException();
}
choiceLimits = limits;
choiceFormats = formats;








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/Collator.java b/luni/src/main/java/java/text/Collator.java
//Synthetic comment -- index 2ddb516..72e428a 100644

//Synthetic comment -- @@ -339,7 +339,7 @@
case Collator.NO_DECOMPOSITION:
return RuleBasedCollatorICU.VALUE_OFF;
}
        throw new IllegalArgumentException();
}

private int decompositionMode_ICU_Java(int mode) {
//Synthetic comment -- @@ -366,7 +366,7 @@
case Collator.IDENTICAL:
return RuleBasedCollatorICU.VALUE_IDENTICAL;
}
        throw new IllegalArgumentException();
}

private int strength_ICU_Java(int value) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DateFormat.java b/luni/src/main/java/java/text/DateFormat.java
//Synthetic comment -- index 8441b59..ac64eed 100644

//Synthetic comment -- @@ -286,16 +286,14 @@
*            {@code Number} instance.
*/
@Override
    public final StringBuffer format(Object object, StringBuffer buffer,
            FieldPosition field) {
if (object instanceof Date) {
return format((Date) object, buffer, field);
}
if (object instanceof Number) {
            return format(new Date(((Number) object).longValue()), buffer,
                    field);
}
        throw new IllegalArgumentException();
}

/**
//Synthetic comment -- @@ -306,8 +304,7 @@
* @return the formatted string.
*/
public final String format(Date date) {
        return format(date, new StringBuffer(), new FieldPosition(0))
                .toString();
}

/**
//Synthetic comment -- @@ -328,8 +325,7 @@
*            of the alignment field in the formatted text.
* @return the string buffer.
*/
    public abstract StringBuffer format(Date date, StringBuffer buffer,
            FieldPosition field);

/**
* Returns an array of locales for which custom {@code DateFormat} instances
//Synthetic comment -- @@ -811,9 +807,8 @@
*/
public static Field ofCalendarField(int calendarField) {
if (calendarField < 0 || calendarField >= Calendar.FIELD_COUNT) {
                throw new IllegalArgumentException();
}

return table.get(Integer.valueOf(calendarField));
}
}
//Synthetic comment -- @@ -821,14 +816,14 @@
private static void checkDateStyle(int style) {
if (!(style == SHORT || style == MEDIUM || style == LONG
|| style == FULL || style == DEFAULT)) {
            throw new IllegalArgumentException("Illegal date style " + style);
}
}

private static void checkTimeStyle(int style) {
if (!(style == SHORT || style == MEDIUM || style == LONG
|| style == FULL || style == DEFAULT)) {
            throw new IllegalArgumentException("Illegal time style " + style);
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/NumberFormat.java b/luni/src/main/java/java/text/NumberFormat.java
//Synthetic comment -- index c285e3d..36fdd0f 100644

//Synthetic comment -- @@ -301,7 +301,7 @@
double dv = ((Number) object).doubleValue();
return format(dv, buffer, field);
}
        throw new IllegalArgumentException();
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index d845d45..b856d7a 100644

//Synthetic comment -- @@ -480,8 +480,7 @@
if (object instanceof Number) {
return formatToCharacterIteratorImpl(new Date(((Number) object).longValue()));
}
        throw new IllegalArgumentException();

}

private AttributedCharacterIterator formatToCharacterIteratorImpl(Date date) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ArrayList.java b/luni/src/main/java/java/util/ArrayList.java
//Synthetic comment -- index dc7b198..d193eec 100644

//Synthetic comment -- @@ -70,7 +70,7 @@
*/
public ArrayList(int capacity) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
array = (capacity == 0 ? EmptyArray.OBJECT : new Object[capacity]);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Collections.java b/luni/src/main/java/java/util/Collections.java
//Synthetic comment -- index d49ca85..0fafcff 100644

//Synthetic comment -- @@ -63,7 +63,7 @@

CopiesList(int length, E object) {
if (length < 0) {
                throw new IllegalArgumentException();
}
n = length;
element = object;
//Synthetic comment -- @@ -2648,7 +2648,7 @@
if (map.isEmpty()) {
return new SetFromMap<E>(map);
}
        throw new IllegalArgumentException();
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Date.java b/luni/src/main/java/java/util/Date.java
//Synthetic comment -- index b639003..da9f296 100644

//Synthetic comment -- @@ -363,6 +363,10 @@
return -1;
}

/**
* Returns the millisecond value of the date and time parsed from the
* specified {@code String}. Many date/time formats are recognized, including IETF
//Synthetic comment -- @@ -413,7 +417,7 @@
} else if ('0' <= next && next <= '9') {
nextState = NUMBERS;
} else if (!Character.isSpace(next) && ",+-:/".indexOf(next) == -1) {
                throw new IllegalArgumentException();
}

if (state == NUMBERS && nextState != NUMBERS) {
//Synthetic comment -- @@ -433,7 +437,7 @@
zoneOffset = sign == '-' ? -digit : digit;
sign = 0;
} else {
                        throw new IllegalArgumentException();
}
} else if (digit >= 70) {
if (year == -1
//Synthetic comment -- @@ -441,7 +445,7 @@
|| next == '/' || next == '\r')) {
year = digit;
} else {
                        throw new IllegalArgumentException();
}
} else if (next == ':') {
if (hour == -1) {
//Synthetic comment -- @@ -449,7 +453,7 @@
} else if (minute == -1) {
minute = digit;
} else {
                        throw new IllegalArgumentException();
}
} else if (next == '/') {
if (month == -1) {
//Synthetic comment -- @@ -457,7 +461,7 @@
} else if (date == -1) {
date = digit;
} else {
                        throw new IllegalArgumentException();
}
} else if (Character.isSpace(next) || next == ','
|| next == '-' || next == '\r') {
//Synthetic comment -- @@ -470,30 +474,30 @@
} else if (year == -1) {
year = digit;
} else {
                        throw new IllegalArgumentException();
}
} else if (year == -1 && month != -1 && date != -1) {
year = digit;
} else {
                    throw new IllegalArgumentException();
}
} else if (state == LETTERS && nextState != LETTERS) {
String text = buffer.toString().toUpperCase(Locale.US);
buffer.setLength(0);
if (text.length() == 1) {
                    throw new IllegalArgumentException();
}
if (text.equals("AM")) {
if (hour == 12) {
hour = 0;
} else if (hour < 1 || hour > 12) {
                        throw new IllegalArgumentException();
}
} else if (text.equals("PM")) {
if (hour == 12) {
hour = 0;
} else if (hour < 1 || hour > 12) {
                        throw new IllegalArgumentException();
}
hour += 12;
} else {
//Synthetic comment -- @@ -510,7 +514,7 @@
zone = true;
zoneOffset = value;
} else {
                        throw new IllegalArgumentException();
}
}
}
//Synthetic comment -- @@ -556,7 +560,7 @@
return new Date(year - 1900, month, date, hour, minute, second)
.getTime();
}
        throw new IllegalArgumentException();
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/EnumMap.java b/luni/src/main/java/java/util/EnumMap.java
//Synthetic comment -- index a721ee3..a9f1c0d 100644

//Synthetic comment -- @@ -437,7 +437,7 @@
initialization((EnumMap<K, V>) map);
} else {
if (map.size() == 0) {
                throw new IllegalArgumentException();
}
Iterator<K> iter = map.keySet().iterator();
K enumKey = iter.next();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/EnumSet.java b/luni/src/main/java/java/util/EnumSet.java
//Synthetic comment -- index cc8969e..20be6f7 100644

//Synthetic comment -- @@ -104,7 +104,7 @@
return copyOf((EnumSet<E>) c);
}
if (c.isEmpty()) {
            throw new IllegalArgumentException();
}
Iterator<E> iterator = c.iterator();
E element = iterator.next();
//Synthetic comment -- @@ -284,7 +284,7 @@
*/
public static <E extends Enum<E>> EnumSet<E> range(E start, E end) {
if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException();
}
EnumSet<E> set = EnumSet.noneOf(start.getDeclaringClass());
set.setRange(start, end);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/EventObject.java b/luni/src/main/java/java/util/EventObject.java
//Synthetic comment -- index 058e7e8..0fc92bb 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
*/
public EventObject(Object source) {
if (source == null) {
            throw new IllegalArgumentException();
}
this.source = source;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/IdentityHashMap.java b/luni/src/main/java/java/util/IdentityHashMap.java
//Synthetic comment -- index e693f7d..904eed5 100644

//Synthetic comment -- @@ -261,13 +261,12 @@
*            this map.
*/
public IdentityHashMap(int maxSize) {
        if (maxSize >= 0) {
            this.size = 0;
            threshold = getThreshold(maxSize);
            elementData = newElementArray(computeElementArraySize());
        } else {
            throw new IllegalArgumentException();
}
}

private int getThreshold(int maxSize) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/PriorityQueue.java b/luni/src/main/java/java/util/PriorityQueue.java
//Synthetic comment -- index e09eb05..cc9bfe6 100644

//Synthetic comment -- @@ -82,7 +82,7 @@
*/
public PriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
if (initialCapacity < 1) {
            throw new IllegalArgumentException();
}
elements = newElementArray(initialCapacity);
this.comparator = comparator;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Random.java b/luni/src/main/java/java/util/Random.java
//Synthetic comment -- index b0a92ff..4a67244 100644

//Synthetic comment -- @@ -171,18 +171,18 @@
* in the half-open range [0, n).
*/
public int nextInt(int n) {
        if (n > 0) {
            if ((n & -n) == n) {
                return (int) ((n * (long) next(31)) >> 31);
            }
            int bits, val;
            do {
                bits = next(31);
                val = bits % n;
            } while (bits - val + (n - 1) < 0);
            return val;
}
        throw new IllegalArgumentException();
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/SimpleTimeZone.java b/luni/src/main/java/java/util/SimpleTimeZone.java
//Synthetic comment -- index 93dc88e..0675480 100644

//Synthetic comment -- @@ -546,11 +546,10 @@
*            the daylight savings offset in milliseconds.
*/
public void setDSTSavings(int milliseconds) {
        if (milliseconds > 0) {
            dstSavings = milliseconds;
        } else {
            throw new IllegalArgumentException();
}
}

private void checkRange(int month, int dayOfWeek, int time) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index a5e0ad7..9a76fc7 100644

//Synthetic comment -- @@ -168,7 +168,7 @@
*/
public String getDisplayName(boolean daylightTime, int style, Locale locale) {
if (style != SHORT && style != LONG) {
            throw new IllegalArgumentException();
}

boolean useDaylight = daylightTime && useDaylightTime();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Timer.java b/luni/src/main/java/java/util/Timer.java
//Synthetic comment -- index afc745c..25ac432 100644

//Synthetic comment -- @@ -433,7 +433,7 @@
*/
public void schedule(TimerTask task, Date when) {
if (when.getTime() < 0) {
            throw new IllegalArgumentException();
}
long delay = when.getTime() - System.currentTimeMillis();
scheduleImpl(task, delay < 0 ? 0 : delay, -1, false);
//Synthetic comment -- @@ -454,7 +454,7 @@
*/
public void schedule(TimerTask task, long delay) {
if (delay < 0) {
            throw new IllegalArgumentException();
}
scheduleImpl(task, delay, -1, false);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Vector.java b/luni/src/main/java/java/util/Vector.java
//Synthetic comment -- index f8a456d..65e135a 100644

//Synthetic comment -- @@ -92,7 +92,7 @@
*/
public Vector(int capacity, int capacityIncrement) {
if (capacity < 0) {
            throw new IllegalArgumentException();
}
elementData = newElementArray(capacity);
elementCount = 0;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/WeakHashMap.java b/luni/src/main/java/java/util/WeakHashMap.java
//Synthetic comment -- index 6417679..2c07d28 100644

//Synthetic comment -- @@ -198,15 +198,14 @@
*                if the capacity is less than zero.
*/
public WeakHashMap(int capacity) {
        if (capacity >= 0) {
            elementCount = 0;
            elementData = newEntryArray(capacity == 0 ? 1 : capacity);
            loadFactor = 7500; // Default load factor of 0.75
            computeMaxSize();
            referenceQueue = new ReferenceQueue<K>();
        } else {
            throw new IllegalArgumentException();
}
}

/**
//Synthetic comment -- @@ -222,15 +221,17 @@
*             or equal to zero.
*/
public WeakHashMap(int capacity, float loadFactor) {
        if (capacity >= 0 && loadFactor > 0) {
            elementCount = 0;
            elementData = newEntryArray(capacity == 0 ? 1 : capacity);
            this.loadFactor = (int) (loadFactor * 10000);
            computeMaxSize();
            referenceQueue = new ReferenceQueue<K>();
        } else {
            throw new IllegalArgumentException();
}
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Matcher.java b/luni/src/main/java/java/util/regex/Matcher.java
//Synthetic comment -- index ce2dfe7..6ac32eb 100644

//Synthetic comment -- @@ -194,7 +194,7 @@
*/
private Matcher reset(CharSequence input, int start, int end) {
if (input == null) {
            throw new IllegalArgumentException();
}

if (start < 0 || end < 0 || start > input.length() || end > input.length() || start > end) {
//Synthetic comment -- @@ -224,7 +224,7 @@
*/
public Matcher usePattern(Pattern pattern) {
if (pattern == null) {
            throw new IllegalArgumentException();
}

this.pattern = pattern;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/Deflater.java b/luni/src/main/java/java/util/zip/Deflater.java
//Synthetic comment -- index 044b976..13f53c1 100644

//Synthetic comment -- @@ -185,7 +185,7 @@
*/
public Deflater(int level, boolean noHeader) {
if (level < DEFAULT_COMPRESSION || level > BEST_COMPRESSION) {
            throw new IllegalArgumentException();
}
compressLevel = level;
streamHandle = createStream(compressLevel, strategy, noHeader);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/DeflaterInputStream.java b/luni/src/main/java/java/util/zip/DeflaterInputStream.java
//Synthetic comment -- index 805ce17..d854fec 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
throw new NullPointerException("deflater == null");
}
if (bufferSize <= 0) {
            throw new IllegalArgumentException();
}
this.def = deflater;
this.buf = new byte[bufferSize];








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/DeflaterOutputStream.java b/luni/src/main/java/java/util/zip/DeflaterOutputStream.java
//Synthetic comment -- index d336e72..448f61c 100644

//Synthetic comment -- @@ -86,11 +86,11 @@
* @param def
*            is the specific {@code Deflater} that will be used to compress
*            data.
     * @param bsize
*            is the size to be used for the internal buffer.
*/
    public DeflaterOutputStream(OutputStream os, Deflater def, int bsize) {
        this(os, def, bsize, false);
}

/**
//Synthetic comment -- @@ -113,19 +113,19 @@
* @hide
* @since 1.7
*/
    public DeflaterOutputStream(OutputStream os, Deflater def, int bsize, boolean syncFlush) {
super(os);
if (os == null) {
throw new NullPointerException("os == null");
} else if (def == null) {
throw new NullPointerException("def == null");
}
        if (bsize <= 0) {
            throw new IllegalArgumentException();
}
this.def = def;
this.syncFlush = syncFlush;
        buf = new byte[bsize];
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterInputStream.java b/luni/src/main/java/java/util/zip/InflaterInputStream.java
//Synthetic comment -- index 6081037..397637e 100644

//Synthetic comment -- @@ -98,24 +98,24 @@
*            the {@code InputStream} to read data from.
* @param inflater
*            the specific {@code Inflater} for decompressing data.
     * @param bsize
*            the size to be used for the internal buffer.
*/
    public InflaterInputStream(InputStream is, Inflater inflater, int bsize) {
super(is);
if (is == null) {
throw new NullPointerException("is == null");
} else if (inflater == null) {
throw new NullPointerException("inflater == null");
}
        if (bsize <= 0) {
            throw new IllegalArgumentException();
}
this.inf = inflater;
if (is instanceof ZipFile.RAFStream) {
            nativeEndBufSize = bsize;
} else {
            buf = new byte[bsize];
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterOutputStream.java b/luni/src/main/java/java/util/zip/InflaterOutputStream.java
//Synthetic comment -- index 9a699a8..ca8a9508 100644

//Synthetic comment -- @@ -76,7 +76,7 @@
throw new NullPointerException("inf == null");
}
if (bufferSize <= 0) {
            throw new IllegalArgumentException();
}
this.inf = inf;
this.buf = new byte[bufferSize];








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipEntry.java b/luni/src/main/java/java/util/zip/ZipEntry.java
//Synthetic comment -- index e2bfc8d..3e58727 100644

//Synthetic comment -- @@ -183,11 +183,10 @@
*            the comment for this entry.
*/
public void setComment(String comment) {
        if (comment == null || comment.length() <= 0xFFFF) {
            this.comment = comment;
        } else {
            throw new IllegalArgumentException();
}
}

/**
//Synthetic comment -- @@ -225,11 +224,10 @@
*             when the length of data is greater than 0xFFFF bytes.
*/
public void setExtra(byte[] data) {
        if (data == null || data.length <= 0xFFFF) {
            extra = data;
        } else {
            throw new IllegalArgumentException();
}
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipFile.java b/luni/src/main/java/java/util/zip/ZipFile.java
//Synthetic comment -- index 6ecd489..20bdf42 100644

//Synthetic comment -- @@ -118,7 +118,7 @@
public ZipFile(File file, int mode) throws IOException {
fileName = file.getPath();
if (mode != OPEN_READ && mode != (OPEN_READ | OPEN_DELETE)) {
            throw new IllegalArgumentException();
}

if ((mode & OPEN_DELETE) != 0) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipOutputStream.java b/luni/src/main/java/java/util/zip/ZipOutputStream.java
//Synthetic comment -- index 47db8f6..77a993b 100644

//Synthetic comment -- @@ -367,7 +367,7 @@
*/
public void setLevel(int level) {
if (level < Deflater.DEFAULT_COMPRESSION || level > Deflater.BEST_COMPRESSION) {
            throw new IllegalArgumentException();
}
compressLevel = level;
}
//Synthetic comment -- @@ -382,7 +382,7 @@
*/
public void setMethod(int method) {
if (method != STORED && method != DEFLATED) {
            throw new IllegalArgumentException();
}
compressMethod = method;
}








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/NativeDecimalFormat.java b/luni/src/main/java/libcore/icu/NativeDecimalFormat.java
//Synthetic comment -- index f6b5214..1504510 100644

//Synthetic comment -- @@ -281,7 +281,7 @@

public AttributedCharacterIterator formatToCharacterIterator(Object object) {
if (!(object instanceof Number)) {
            throw new IllegalArgumentException();
}
Number number = (Number) object;
FieldPositionIterator fpIter = new FieldPositionIterator();








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpURLConnectionImpl.java b/luni/src/main/java/libcore/net/http/HttpURLConnectionImpl.java
//Synthetic comment -- index 260a9ad..9507745 100644

//Synthetic comment -- @@ -406,7 +406,7 @@
final boolean processAuthHeader(int responseCode, ResponseHeaders response,
RawHeaders successorRequestHeaders) throws IOException {
if (responseCode != HTTP_PROXY_AUTH && responseCode != HTTP_UNAUTHORIZED) {
            throw new IllegalArgumentException();
}

// keep asking for username/password until authorized








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/asn1/ObjectIdentifier.java b/luni/src/main/java/org/apache/harmony/security/asn1/ObjectIdentifier.java
//Synthetic comment -- index 24140e5..eb1e3e3 100644

//Synthetic comment -- @@ -190,7 +190,7 @@
if (! shouldThrow) {
return null;
}
            throw new IllegalArgumentException();
}

int length = str.length();








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/java/util/SimpleTimeZoneTest.java b/luni/src/test/java/tests/api/java/util/SimpleTimeZoneTest.java
//Synthetic comment -- index 618cbe4..eeb5b30 100644

//Synthetic comment -- @@ -442,8 +442,18 @@
st.setStartRule(0, 1, 1, 1);
st.setEndRule(11, 1, 1, 1);
st.setDSTSavings(1);
        assertEquals("Daylight savings amount not set", 1, st.getDSTSavings());
    }

/**
* java.util.SimpleTimeZone#setEndRule(int, int, int)








//Synthetic comment -- diff --git a/xml/src/main/java/org/kxml2/io/KXmlParser.java b/xml/src/main/java/org/kxml2/io/KXmlParser.java
//Synthetic comment -- index 2be8d36..80fd8ca 100644

//Synthetic comment -- @@ -1612,7 +1612,7 @@
boolean detectCharset = (charset == null);

if (is == null) {
            throw new IllegalArgumentException();
}

try {








//Synthetic comment -- diff --git a/xml/src/main/java/org/kxml2/io/KXmlSerializer.java b/xml/src/main/java/org/kxml2/io/KXmlSerializer.java
//Synthetic comment -- index d1965d6..8fa2756 100644

//Synthetic comment -- @@ -327,7 +327,7 @@
public void setOutput(OutputStream os, String encoding)
throws IOException {
if (os == null)
            throw new IllegalArgumentException();
setOutput(
encoding == null
? new OutputStreamWriter(os)







