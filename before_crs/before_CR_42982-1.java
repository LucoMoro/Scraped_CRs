/*Add consistent reasons for NullPointerException

Semi-automated replacement of empty and non-conforming
NullPointerException reason messages.

(cherry-pick of 86acc043d3334651ee26c65467d78d6cefedd397.)

Change-Id:I6d893979f5c20a50e841e32af9fd7b2d8bc9d54d*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/beans/PropertyChangeSupport.java b/luni/src/main/java/java/beans/PropertyChangeSupport.java
//Synthetic comment -- index 04f8155..1db12b7 100644

//Synthetic comment -- @@ -66,7 +66,7 @@
*/
public PropertyChangeSupport(Object sourceBean) {
if (sourceBean == null) {
            throw new NullPointerException();
}
this.sourceBean = sourceBean;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/BufferedWriter.java b/luni/src/main/java/java/io/BufferedWriter.java
//Synthetic comment -- index e4cbe7c..55ae121 100644

//Synthetic comment -- @@ -163,33 +163,33 @@

/**
* Writes {@code count} characters starting at {@code offset} in
     * {@code cbuf} to this writer. If {@code count} is greater than this
* writer's buffer, then the buffer is flushed and the characters are
* written directly to the target writer.
*
     * @param cbuf
*            the array containing characters to write.
* @param offset
     *            the start position in {@code cbuf} for retrieving characters.
* @param count
*            the maximum number of characters to write.
* @throws IndexOutOfBoundsException
*             if {@code offset < 0} or {@code count < 0}, or if
*             {@code offset + count} is greater than the size of
     *             {@code cbuf}.
* @throws IOException
*             if this writer is closed or another I/O error occurs.
*/
@Override
    public void write(char[] cbuf, int offset, int count) throws IOException {
synchronized (lock) {
checkNotClosed();
            if (cbuf == null) {
throw new NullPointerException("buffer == null");
}
            Arrays.checkOffsetAndCount(cbuf.length, offset, count);
if (pos == 0 && count >= this.buf.length) {
                out.write(cbuf, offset, count);
return;
}
int available = this.buf.length - pos;
//Synthetic comment -- @@ -197,7 +197,7 @@
available = count;
}
if (available > 0) {
                System.arraycopy(cbuf, offset, this.buf, pos, available);
pos += available;
}
if (pos == this.buf.length) {
//Synthetic comment -- @@ -207,11 +207,11 @@
offset += available;
available = count - available;
if (available >= this.buf.length) {
                        out.write(cbuf, offset, available);
return;
}

                    System.arraycopy(cbuf, offset, this.buf, pos, available);
pos += available;
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/File.java b/luni/src/main/java/java/io/File.java
//Synthetic comment -- index ac67e7c..ec87fed 100644

//Synthetic comment -- @@ -147,7 +147,7 @@
*/
public File(String dirPath, String name) {
if (name == null) {
            throw new NullPointerException();
}
if (dirPath == null || dirPath.isEmpty()) {
this.path = fixSlashes(name);








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/InputStreamReader.java b/luni/src/main/java/java/io/InputStreamReader.java
//Synthetic comment -- index 59be9ed..2e9b381 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
throws UnsupportedEncodingException {
super(in);
if (enc == null) {
            throw new NullPointerException();
}
this.in = in;
try {








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectInputStream.java b/luni/src/main/java/java/io/ObjectInputStream.java
//Synthetic comment -- index 0d75a44..0476901 100644

//Synthetic comment -- @@ -2344,7 +2344,7 @@
public int skipBytes(int length) throws IOException {
// To be used with available. Ok to call if reading primitive buffer
if (input == null) {
            throw new NullPointerException();
}

int offset = 0;








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectStreamField.java b/luni/src/main/java/java/io/ObjectStreamField.java
//Synthetic comment -- index db450e0..78a6903 100644

//Synthetic comment -- @@ -58,8 +58,10 @@
*             if {@code name} or {@code cl} is {@code null}.
*/
public ObjectStreamField(String name, Class<?> cl) {
        if (name == null || cl == null) {
            throw new NullPointerException();
}
this.name = name;
this.type = new WeakReference<Class<?>>(cl);
//Synthetic comment -- @@ -81,8 +83,10 @@
* @see ObjectOutputStream#writeUnshared(Object)
*/
public ObjectStreamField(String name, Class<?> cl, boolean unshared) {
        if (name == null || cl == null) {
            throw new NullPointerException();
}
this.name = name;
this.type = (cl.getClassLoader() == null) ? cl : new WeakReference<Class<?>>(cl);
//Synthetic comment -- @@ -100,7 +104,7 @@
*/
ObjectStreamField(String signature, String name) {
if (name == null) {
            throw new NullPointerException();
}
this.name = name;
this.typeString = signature.replace('.', '/').intern();








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/OutputStreamWriter.java b/luni/src/main/java/java/io/OutputStreamWriter.java
//Synthetic comment -- index 86b62fc..c09c9e3 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
throws UnsupportedEncodingException {
super(out);
if (enc == null) {
            throw new NullPointerException();
}
this.out = out;
try {








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PipedOutputStream.java b/luni/src/main/java/java/io/PipedOutputStream.java
//Synthetic comment -- index a674bb3..1b139e9 100644

//Synthetic comment -- @@ -81,7 +81,7 @@
*/
public void connect(PipedInputStream stream) throws IOException {
if (stream == null) {
            throw new NullPointerException();
}
synchronized (stream) {
if (this.target != null) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PipedWriter.java b/luni/src/main/java/java/io/PipedWriter.java
//Synthetic comment -- index ece899a..ad8974b 100644

//Synthetic comment -- @@ -85,7 +85,7 @@
*/
public void connect(PipedReader reader) throws IOException {
if (reader == null) {
            throw new NullPointerException();
}
synchronized (reader) {
if (this.destination != null) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/PrintStream.java b/luni/src/main/java/java/io/PrintStream.java
//Synthetic comment -- index ba48b04..ea67d46 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
public PrintStream(OutputStream out) {
super(out);
if (out == null) {
            throw new NullPointerException();
}
}

//Synthetic comment -- @@ -80,7 +80,7 @@
public PrintStream(OutputStream out, boolean autoFlush) {
super(out);
if (out == null) {
            throw new NullPointerException();
}
this.autoFlush = autoFlush;
}
//Synthetic comment -- @@ -106,8 +106,10 @@
public PrintStream(OutputStream out, boolean autoFlush, String enc)
throws UnsupportedEncodingException {
super(out);
        if (out == null || enc == null) {
            throw new NullPointerException();
}
this.autoFlush = autoFlush;
try {
//Synthetic comment -- @@ -136,30 +138,30 @@

/**
* Constructs a new {@code PrintStream} with {@code file} as its target. The
     * character set named {@code csn} is used for character encoding.
*
* @param file
*            the target file. If the file already exists, its contents are
*            removed, otherwise a new file is created.
     * @param csn
*            the name of the character set used for character encoding.
* @throws FileNotFoundException
*             if an error occurs while opening or creating the target file.
* @throws NullPointerException
     *             if {@code csn} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code csn} is not supported.
*/
    public PrintStream(File file, String csn) throws FileNotFoundException,
UnsupportedEncodingException {
super(new FileOutputStream(file));
        if (csn == null) {
            throw new NullPointerException();
}
        if (!Charset.isSupported(csn)) {
            throw new UnsupportedEncodingException(csn);
}
        encoding = csn;
}

/**
//Synthetic comment -- @@ -179,24 +181,24 @@

/**
* Constructs a new {@code PrintStream} with the file identified by
     * {@code fileName} as its target. The character set named {@code csn} is
* used for character encoding.
*
* @param fileName
*            the target file's name. If the file already exists, its
*            contents are removed, otherwise a new file is created.
     * @param csn
*            the name of the character set used for character encoding.
* @throws FileNotFoundException
*             if an error occurs while opening or creating the target file.
* @throws NullPointerException
     *             if {@code csn} is {@code null}.
* @throws UnsupportedEncodingException
     *             if the encoding specified by {@code csn} is not supported.
*/
    public PrintStream(String fileName, String csn)
throws FileNotFoundException, UnsupportedEncodingException {
        this(new File(fileName), csn);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/Reader.java b/luni/src/main/java/java/io/Reader.java
//Synthetic comment -- index 310a57c..e947d08 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
*/
protected Reader(Object lock) {
if (lock == null) {
            throw new NullPointerException();
}
this.lock = lock;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/SequenceInputStream.java b/luni/src/main/java/java/io/SequenceInputStream.java
//Synthetic comment -- index 9ae1901..8333834 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
*/
public SequenceInputStream(InputStream s1, InputStream s2) {
if (s1 == null) {
            throw new NullPointerException();
}
Vector<InputStream> inVector = new Vector<InputStream>(1);
inVector.addElement(s2);
//Synthetic comment -- @@ -73,7 +73,7 @@
if (e.hasMoreElements()) {
in = e.nextElement();
if (in == null) {
                throw new NullPointerException();
}
}
}
//Synthetic comment -- @@ -112,7 +112,7 @@
if (e.hasMoreElements()) {
in = e.nextElement();
if (in == null) {
                throw new NullPointerException();
}
} else {
in = null;








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/StreamTokenizer.java b/luni/src/main/java/java/io/StreamTokenizer.java
//Synthetic comment -- index 0522be6..a16dc4b 100644

//Synthetic comment -- @@ -167,7 +167,7 @@
public StreamTokenizer(InputStream is) {
this();
if (is == null) {
            throw new NullPointerException();
}
inStream = is;
}
//Synthetic comment -- @@ -194,7 +194,7 @@
public StreamTokenizer(Reader r) {
this();
if (r == null) {
            throw new NullPointerException();
}
inReader = r;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/StringBufferInputStream.java b/luni/src/main/java/java/io/StringBufferInputStream.java
//Synthetic comment -- index 1fada57..1768abe 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
*/
public StringBufferInputStream(String str) {
if (str == null) {
            throw new NullPointerException();
}
buffer = str;
count = str.length();








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/Writer.java b/luni/src/main/java/java/io/Writer.java
//Synthetic comment -- index 2e28b80..33d7604 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
*/
protected Writer(Object lock) {
if (lock == null) {
            throw new NullPointerException();
}
this.lock = lock;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/AbstractStringBuilder.java b/luni/src/main/java/java/lang/AbstractStringBuilder.java
//Synthetic comment -- index 6e46d26..c3107f2 100644

//Synthetic comment -- @@ -437,7 +437,7 @@
}
if (start == end) {
if (string == null) {
                    throw new NullPointerException();
}
insert0(start, string);
return;








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Character.java b/luni/src/main/java/java/lang/Character.java
//Synthetic comment -- index 1a41ec2..cf0ab84 100644

//Synthetic comment -- @@ -532,7 +532,7 @@
*/
protected Subset(String string) {
if (string == null) {
                throw new NullPointerException();
}
name = string;
}
//Synthetic comment -- @@ -1502,7 +1502,7 @@
*/
public static UnicodeBlock forName(String blockName) {
if (blockName == null) {
                throw new NullPointerException();
}
int block = forNameImpl(blockName);
if (block == -1) {
//Synthetic comment -- @@ -1798,7 +1798,7 @@
*/
public static int codePointAt(CharSequence seq, int index) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length();
if (index < 0 || index >= len) {
//Synthetic comment -- @@ -1840,7 +1840,7 @@
*/
public static int codePointAt(char[] seq, int index) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length;
if (index < 0 || index >= len) {
//Synthetic comment -- @@ -1923,7 +1923,7 @@
*/
public static int codePointBefore(CharSequence seq, int index) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length();
if (index < 1 || index > len) {
//Synthetic comment -- @@ -1965,7 +1965,7 @@
*/
public static int codePointBefore(char[] seq, int index) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length;
if (index < 1 || index > len) {
//Synthetic comment -- @@ -2012,7 +2012,7 @@
*/
public static int codePointBefore(char[] seq, int index, int start) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length;
if (index <= start || index > len || start < 0 || start >= len) {
//Synthetic comment -- @@ -2055,7 +2055,7 @@
public static int toChars(int codePoint, char[] dst, int dstIndex) {
checkValidCodePoint(codePoint);
if (dst == null) {
            throw new NullPointerException();
}
if (dstIndex < 0 || dstIndex >= dst.length) {
throw new IndexOutOfBoundsException();
//Synthetic comment -- @@ -2126,7 +2126,7 @@
public static int codePointCount(CharSequence seq, int beginIndex,
int endIndex) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length();
if (beginIndex < 0 || endIndex > len || beginIndex > endIndex) {
//Synthetic comment -- @@ -2215,7 +2215,7 @@
*/
public static int offsetByCodePoints(CharSequence seq, int index, int codePointOffset) {
if (seq == null) {
            throw new NullPointerException();
}
int len = seq.length();
if (index < 0 || index > len) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ClassLoader.java b/luni/src/main/java/java/lang/ClassLoader.java
//Synthetic comment -- index 0cdc448..c99d57c 100644

//Synthetic comment -- @@ -195,7 +195,7 @@
*/
ClassLoader(ClassLoader parentLoader, boolean nullAllowed) {
if (parentLoader == null && !nullAllowed) {
            throw new NullPointerException("Parent ClassLoader may not be null");
}
parent = parentLoader;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Enum.java b/luni/src/main/java/java/lang/Enum.java
//Synthetic comment -- index 3d9e68b..7a0f514 100644

//Synthetic comment -- @@ -181,8 +181,10 @@
*             have a constant value called {@code name}.
*/
public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        if (enumType == null || name == null) {
            throw new NullPointerException("enumType == null || name == null");
}
T[] values = getSharedConstants(enumType);
if (values == null) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ProcessBuilder.java b/luni/src/main/java/java/lang/ProcessBuilder.java
//Synthetic comment -- index 5b7efdc..57e21b6 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
*/
public ProcessBuilder(List<String> command) {
if (command == null) {
            throw new NullPointerException();
}
this.command = command;

//Synthetic comment -- @@ -102,7 +102,7 @@
*/
public ProcessBuilder command(List<String> command) {
if (command == null) {
            throw new NullPointerException();
}
this.command = command;
return this;








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/ProcessManager.java b/luni/src/main/java/java/lang/ProcessManager.java
//Synthetic comment -- index 1e820a9..28314b7 100644

//Synthetic comment -- @@ -168,10 +168,10 @@
boolean redirectErrorStream) throws IOException {
// Make sure we throw the same exceptions as the RI.
if (taintedCommand == null) {
            throw new NullPointerException();
}
if (taintedCommand.length == 0) {
            throw new IndexOutOfBoundsException();
}

// Handle security and safety by copying mutable inputs and checking them.
//Synthetic comment -- @@ -179,16 +179,16 @@
String[] environment = taintedEnvironment != null ? taintedEnvironment.clone() : null;

// Check we're not passing null Strings to the native exec.
        for (String arg : command) {
            if (arg == null) {
                throw new NullPointerException();
}
}
// The environment is allowed to be null or empty, but no element may be null.
if (environment != null) {
            for (String env : environment) {
                if (env == null) {
                    throw new NullPointerException();
}
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index efe2303..a2debfd9 100644

//Synthetic comment -- @@ -224,9 +224,9 @@
public Process exec(String prog, String[] envp, File directory) throws java.io.IOException {
// Sanity checks
if (prog == null) {
            throw new NullPointerException();
        } else if (prog.length() == 0) {
            throw new IllegalArgumentException();
}

// Break down into tokens, as described in Java docs
//Synthetic comment -- @@ -331,11 +331,11 @@
/*
* Loads and links a library without security checks.
*/
    void load(String filename, ClassLoader loader) {
        if (filename == null) {
            throw new NullPointerException("library path was null.");
}
        String error = nativeLoad(filename, loader);
if (error != null) {
throw new UnsatisfiedLinkError(error);
}
//Synthetic comment -- @@ -538,7 +538,7 @@
public void addShutdownHook(Thread hook) {
// Sanity checks
if (hook == null) {
            throw new NullPointerException("Hook may not be null.");
}

if (shuttingDown) {
//Synthetic comment -- @@ -571,7 +571,7 @@
public boolean removeShutdownHook(Thread hook) {
// Sanity checks
if (hook == null) {
            throw new NullPointerException("Hook may not be null.");
}

if (shuttingDown) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/StackTraceElement.java b/luni/src/main/java/java/lang/StackTraceElement.java
//Synthetic comment -- index b83120c..a59935a 100644

//Synthetic comment -- @@ -58,8 +58,10 @@
*             if {@code cls} or {@code method} is {@code null}.
*/
public StackTraceElement(String cls, String method, String file, int line) {
        if (cls == null || method == null) {
            throw new NullPointerException();
}
declaringClass = cls;
methodName = method;








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/String.java b/luni/src/main/java/java/lang/String.java
//Synthetic comment -- index 224a268..f3aeb64 100644

//Synthetic comment -- @@ -514,7 +514,7 @@
*/
public String(int[] codePoints, int offset, int count) {
if (codePoints == null) {
            throw new NullPointerException();
}
if ((offset | count) < 0 || count > codePoints.length - offset) {
throw failedBoundsCheck(codePoints.length, offset, count);
//Synthetic comment -- @@ -1222,7 +1222,7 @@
*/
public boolean regionMatches(int thisStart, String string, int start, int length) {
if (string == null) {
            throw new NullPointerException();
}
if (start < 0 || string.count - start < length) {
return false;
//Synthetic comment -- @@ -1719,7 +1719,7 @@
*/
public boolean contentEquals(CharSequence cs) {
if (cs == null) {
            throw new NullPointerException();
}

int len = cs.length();
//Synthetic comment -- @@ -1912,7 +1912,7 @@
*/
public boolean contains(CharSequence cs) {
if (cs == null) {
            throw new NullPointerException();
}
return indexOf(cs.toString()) >= 0;
}
//Synthetic comment -- @@ -1981,7 +1981,7 @@
*/
public static String format(Locale locale, String format, Object... args) {
if (format == null) {
            throw new NullPointerException("null format argument");
}
int bufferSize = format.length() + (args == null ? 0 : args.length * 10);
Formatter f = new Formatter(new StringBuilder(bufferSize), locale);








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/StringBuilder.java b/luni/src/main/java/java/lang/StringBuilder.java
//Synthetic comment -- index d886100..a944e68 100644

//Synthetic comment -- @@ -624,7 +624,7 @@
*            the inclusive begin index.
* @param end
*            the exclusive end index.
     * @param str
*            the replacement string.
* @return this builder.
* @throws StringIndexOutOfBoundsException
//Synthetic comment -- @@ -633,8 +633,8 @@
* @throws NullPointerException
*            if {@code str} is {@code null}.
*/
    public StringBuilder replace(int start, int end, String str) {
        replace0(start, end, str);
return this;
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/System.java b/luni/src/main/java/java/lang/System.java
//Synthetic comment -- index de99161..df84c61 100644

//Synthetic comment -- @@ -455,7 +455,7 @@
*/
public static String clearProperty(String key) {
if (key == null) {
            throw new NullPointerException();
}
if (key.isEmpty()) {
throw new IllegalArgumentException();
//Synthetic comment -- @@ -679,7 +679,7 @@

private String toNonNullString(Object o) {
if (o == null) {
                throw new NullPointerException();
}
return (String) o;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Thread.java b/luni/src/main/java/java/lang/Thread.java
//Synthetic comment -- index a61f669..210b90c 100644

//Synthetic comment -- @@ -232,7 +232,7 @@
*/
public Thread(Runnable runnable, String threadName) {
if (threadName == null) {
            throw new NullPointerException();
}

create(null, runnable, threadName, 0);
//Synthetic comment -- @@ -252,7 +252,7 @@
*/
public Thread(String threadName) {
if (threadName == null) {
            throw new NullPointerException();
}

create(null, null, threadName, 0);
//Synthetic comment -- @@ -296,7 +296,7 @@
*/
public Thread(ThreadGroup group, Runnable runnable, String threadName) {
if (threadName == null) {
            throw new NullPointerException();
}

create(group, runnable, threadName, 0);
//Synthetic comment -- @@ -317,7 +317,7 @@
*/
public Thread(ThreadGroup group, String threadName) {
if (threadName == null) {
            throw new NullPointerException();
}

create(group, null, threadName, 0);
//Synthetic comment -- @@ -346,7 +346,7 @@
*/
public Thread(ThreadGroup group, Runnable runnable, String threadName, long stackSize) {
if (threadName == null) {
            throw new NullPointerException();
}
create(group, runnable, threadName, stackSize);
}
//Synthetic comment -- @@ -943,7 +943,7 @@
*/
public final void setName(String threadName) {
if (threadName == null) {
            throw new NullPointerException();
}

name = threadName;








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Throwable.java b/luni/src/main/java/java/lang/Throwable.java
//Synthetic comment -- index 31eb538..b20b882 100644

//Synthetic comment -- @@ -219,9 +219,9 @@
*/
public void setStackTrace(StackTraceElement[] trace) {
StackTraceElement[] newTrace = trace.clone();
        for (StackTraceElement element : newTrace) {
            if (element == null) {
                throw new NullPointerException();
}
}
stackTrace = newTrace;
//Synthetic comment -- @@ -413,10 +413,10 @@
*/
public final void addSuppressed(Throwable throwable) {
if (throwable == this) {
            throw new IllegalArgumentException("suppressed == this");
}
if (throwable == null) {
            throw new NullPointerException("suppressed == null");
}
if (suppressedExceptions != null) {
// suppressed exceptions are enabled








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/reflect/GenericArrayType.java b/luni/src/main/java/java/lang/reflect/GenericArrayType.java
//Synthetic comment -- index 6344019..fc03f78 100644

//Synthetic comment -- @@ -36,4 +36,4 @@
*             instantiated for some reason
*/
Type getGenericComponentType();
}
\ No newline at end of file








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/reflect/Proxy.java b/luni/src/main/java/java/lang/reflect/Proxy.java
//Synthetic comment -- index a24514d..3b10887 100644

//Synthetic comment -- @@ -89,13 +89,13 @@
Class<?>... interfaces) throws IllegalArgumentException {
// check that interfaces are a valid array of visible interfaces
if (interfaces == null) {
            throw new NullPointerException();
}
String commonPackageName = null;
for (int i = 0, length = interfaces.length; i < length; i++) {
Class<?> next = interfaces[i];
if (next == null) {
                throw new NullPointerException();
}
String name = next.getName();
if (!next.isInterface()) {
//Synthetic comment -- @@ -206,7 +206,7 @@
Class<?>[] interfaces, InvocationHandler h)
throws IllegalArgumentException {
if (h == null) {
            throw new NullPointerException();
}
try {
return getProxyClass(loader, interfaces).getConstructor(
//Synthetic comment -- @@ -241,7 +241,7 @@
*/
public static boolean isProxyClass(Class<?> cl) {
if (cl == null) {
            throw new NullPointerException();
}
synchronized (proxyCache) {
return proxyCache.containsKey(cl);








//Synthetic comment -- diff --git a/luni/src/main/java/java/math/BigDecimal.java b/luni/src/main/java/java/math/BigDecimal.java
//Synthetic comment -- index 3a5f3cd..335e3bc 100644

//Synthetic comment -- @@ -276,7 +276,7 @@
long newScale; // the new scale

if (in == null) {
            throw new NullPointerException();
}
if ((last >= in.length) || (offset < 0) || (len <= 0) || (last < 0)) {
throw new NumberFormatException("Bad offset/length: offset=" + offset +
//Synthetic comment -- @@ -601,7 +601,7 @@
*/
public BigDecimal(BigInteger unscaledVal, int scale) {
if (unscaledVal == null) {
            throw new NullPointerException();
}
this.scale = scale;
setUnscaledValue(unscaledVal);
//Synthetic comment -- @@ -1059,7 +1059,7 @@
public BigDecimal divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
// Let be: this = [u1,s1]  and  divisor = [u2,s2]
if (roundingMode == null) {
            throw new NullPointerException();
}
if (divisor.isZero()) {
throw new ArithmeticException("Division by zero");
//Synthetic comment -- @@ -1916,7 +1916,7 @@
*/
public BigDecimal setScale(int newScale, RoundingMode roundingMode) {
if (roundingMode == null) {
            throw new NullPointerException();
}
long diffScale = newScale - (long)scale;
// Let be:  'this' = [u,s]








//Synthetic comment -- diff --git a/luni/src/main/java/java/math/BigInt.java b/luni/src/main/java/java/math/BigInt.java
//Synthetic comment -- index 1768676..614dbb4 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
*/
String checkString(String s, int base) {
if (s == null) {
            throw new NullPointerException();
}
// A valid big integer consists of an optional '-' or '+' followed by
// one or more digit characters appropriate to the given base,








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/CookieStore.java b/luni/src/main/java/java/net/CookieStore.java
//Synthetic comment -- index d09b7e8..619d65c 100644

//Synthetic comment -- @@ -100,4 +100,4 @@
* @return true if any cookies were removed as a result of this call.
*/
boolean removeAll();
}
\ No newline at end of file








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/DatagramSocket.java b/luni/src/main/java/java/net/DatagramSocket.java
//Synthetic comment -- index 2b468fa..c01f3af 100644

//Synthetic comment -- @@ -244,7 +244,7 @@
checkOpen();
ensureBound();
if (pack == null) {
            throw new NullPointerException();
}
if (pendingConnectException != null) {
throw new SocketException("Pending connect failure", pendingConnectException);
//Synthetic comment -- @@ -295,7 +295,7 @@
*/
public void setNetworkInterface(NetworkInterface netInterface) throws SocketException {
if (netInterface == null) {
            throw new NullPointerException("networkInterface == null");
}
try {
Libcore.os.setsockoptIfreq(impl.fd, SOL_SOCKET, SO_BINDTODEVICE, netInterface.getName());
//Synthetic comment -- @@ -374,7 +374,7 @@
*/
protected DatagramSocket(DatagramSocketImpl socketImpl) {
if (socketImpl == null) {
            throw new NullPointerException();
}
impl = socketImpl;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URISyntaxException.java b/luni/src/main/java/java/net/URISyntaxException.java
//Synthetic comment -- index e08f444..957ea31 100644

//Synthetic comment -- @@ -49,8 +49,10 @@
public URISyntaxException(String input, String reason, int index) {
super(reason);

        if (input == null || reason == null) {
            throw new NullPointerException();
}

if (index < -1) {
//Synthetic comment -- @@ -76,8 +78,10 @@
public URISyntaxException(String input, String reason) {
super(reason);

        if (input == null || reason == null) {
            throw new NullPointerException();
}

this.input = input;








//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URLClassLoader.java b/luni/src/main/java/java/net/URLClassLoader.java
//Synthetic comment -- index 1c8bc43..efb7531 100644

//Synthetic comment -- @@ -822,7 +822,7 @@
while (!searchList.isEmpty()) {
URL nextCandidate = searchList.remove(0);
if (nextCandidate == null) {
                throw new NullPointerException("A URL is null");
}
if (!handlerMap.containsKey(nextCandidate)) {
URLHandler result;








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/DatagramChannelImpl.java b/luni/src/main/java/java/nio/DatagramChannelImpl.java
//Synthetic comment -- index a0e064d..4d2fc5a 100644

//Synthetic comment -- @@ -448,7 +448,7 @@
*/
private void checkNotNull(ByteBuffer source) {
if (source == null) {
            throw new NullPointerException();
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/SocketChannelImpl.java b/luni/src/main/java/java/nio/SocketChannelImpl.java
//Synthetic comment -- index a9d9c53..ff2c157 100644

//Synthetic comment -- @@ -318,7 +318,7 @@
@Override
public int write(ByteBuffer src) throws IOException {
if (src == null) {
            throw new NullPointerException();
}
checkOpenConnected();
if (!src.hasRemaining()) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/nio/channels/Channels.java b/luni/src/main/java/java/nio/channels/Channels.java
//Synthetic comment -- index 3af1465..b59eeac 100644

//Synthetic comment -- @@ -150,7 +150,7 @@
public static Reader newReader(ReadableByteChannel channel,
String charsetName) {
if (charsetName == null) {
            throw new NullPointerException();
}
return newReader(channel, Charset.forName(charsetName).newDecoder(), -1);
}
//Synthetic comment -- @@ -193,7 +193,7 @@
public static Writer newWriter(WritableByteChannel channel,
String charsetName) {
if (charsetName == null) {
            throw new NullPointerException();
}
return newWriter(channel, Charset.forName(charsetName).newEncoder(), -1);
}
//Synthetic comment -- @@ -207,7 +207,7 @@

ChannelInputStream(ReadableByteChannel channel) {
if (channel == null) {
                throw new NullPointerException();
}
this.channel = channel;
}
//Synthetic comment -- @@ -247,7 +247,7 @@

ChannelOutputStream(WritableByteChannel channel) {
if (channel == null) {
                throw new NullPointerException();
}
this.channel = channel;
}
//Synthetic comment -- @@ -289,7 +289,7 @@

InputStreamChannel(InputStream inputStream) {
if (inputStream == null) {
                throw new NullPointerException();
}
this.inputStream = inputStream;
}
//Synthetic comment -- @@ -328,7 +328,7 @@

OutputStreamChannel(OutputStream outputStream) {
if (outputStream == null) {
                throw new NullPointerException();
}
this.outputStream = outputStream;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/AlgorithmParameterGenerator.java b/luni/src/main/java/java/security/AlgorithmParameterGenerator.java
//Synthetic comment -- index daefa3c..3edc167 100644

//Synthetic comment -- @@ -88,7 +88,7 @@
public static AlgorithmParameterGenerator getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new AlgorithmParameterGenerator((AlgorithmParameterGeneratorSpi) sap.spi,
//Synthetic comment -- @@ -149,7 +149,7 @@
throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new AlgorithmParameterGenerator((AlgorithmParameterGeneratorSpi) spi, provider,








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/AlgorithmParameters.java b/luni/src/main/java/java/security/AlgorithmParameters.java
//Synthetic comment -- index 8bbbe1b..073460e 100644

//Synthetic comment -- @@ -92,7 +92,7 @@
public static AlgorithmParameters getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new AlgorithmParameters((AlgorithmParametersSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -120,7 +120,7 @@
String provider) throws NoSuchAlgorithmException,
NoSuchProviderException {
if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException();
}
Provider p = Security.getProvider(provider);
if (p == null) {
//Synthetic comment -- @@ -148,10 +148,10 @@
public static AlgorithmParameters getInstance(String algorithm,
Provider provider) throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new AlgorithmParameters((AlgorithmParametersSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyFactory.java b/luni/src/main/java/java/security/KeyFactory.java
//Synthetic comment -- index 554885a..8d39003 100644

//Synthetic comment -- @@ -76,7 +76,7 @@
public static KeyFactory getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new KeyFactory((KeyFactorySpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -130,7 +130,7 @@
throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new KeyFactory((KeyFactorySpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyPairGenerator.java b/luni/src/main/java/java/security/KeyPairGenerator.java
//Synthetic comment -- index d5851fa..5c17d79 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
public static KeyPairGenerator getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
Object spi = sap.spi;
//Synthetic comment -- @@ -143,7 +143,7 @@
throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
if (spi instanceof KeyPairGenerator) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/KeyStore.java b/luni/src/main/java/java/security/KeyStore.java
//Synthetic comment -- index c233a5b..3d856f7 100644

//Synthetic comment -- @@ -110,7 +110,7 @@
*/
public static KeyStore getInstance(String type) throws KeyStoreException {
if (type == null) {
            throw new NullPointerException();
}
try {
Engine.SpiAndProvider sap = ENGINE.getInstance(type, null);
//Synthetic comment -- @@ -182,7 +182,7 @@
throw new IllegalArgumentException();
}
if (type == null) {
            throw new NullPointerException();
}
// return KeyStore instance
try {








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/MessageDigest.java b/luni/src/main/java/java/security/MessageDigest.java
//Synthetic comment -- index 3154028..6f1bf21 100644

//Synthetic comment -- @@ -86,7 +86,7 @@
public static MessageDigest getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
Object spi = sap.spi;
//Synthetic comment -- @@ -152,7 +152,7 @@
throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
if (spi instanceof MessageDigest) {
//Synthetic comment -- @@ -217,7 +217,7 @@
*/
public void update(byte[] input) {
if (input == null) {
            throw new NullPointerException();
}
engineUpdate(input, 0, input.length);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/Provider.java b/luni/src/main/java/java/security/Provider.java
//Synthetic comment -- index 018e268..899625a 100644

//Synthetic comment -- @@ -407,8 +407,10 @@
*/
public synchronized Provider.Service getService(String type,
String algorithm) {
        if (type == null || algorithm == null) {
            throw new NullPointerException();
}

if (type.equals(lastServiceName) && algorithm.equalsIgnoreCase(lastAlgorithm)) {
//Synthetic comment -- @@ -475,7 +477,7 @@
*/
protected synchronized void putService(Provider.Service s) {
if (s == null) {
            throw new NullPointerException();
}
if ("Provider".equals(s.getType())) { // Provider service type cannot be added
return;
//Synthetic comment -- @@ -507,7 +509,7 @@
*/
protected synchronized void removeService(Provider.Service s) {
if (s == null) {
            throw new NullPointerException();
}
servicesChanged();
if (serviceTable != null) {
//Synthetic comment -- @@ -852,9 +854,14 @@
*/
public Service(Provider provider, String type, String algorithm,
String className, List<String> aliases, Map<String, String> attributes) {
            if (provider == null || type == null || algorithm == null
                    || className == null) {
                throw new NullPointerException();
}
this.provider = provider;
this.type = type;
//Synthetic comment -- @@ -943,7 +950,7 @@
*/
public final String getAttribute(String name) {
if (name == null) {
                throw new NullPointerException();
}
if (attributes == null) {
return null;








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/SecureRandom.java b/luni/src/main/java/java/security/SecureRandom.java
//Synthetic comment -- index 9091f73..6ed631c 100644

//Synthetic comment -- @@ -153,7 +153,7 @@
*/
public static SecureRandom getInstance(String algorithm) throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new SecureRandom((SecureRandomSpi) sap.spi, sap.provider,
//Synthetic comment -- @@ -212,7 +212,7 @@
throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new SecureRandom((SecureRandomSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/Security.java b/luni/src/main/java/java/security/Security.java
//Synthetic comment -- index a4cc6e1..b5bd02a 100644

//Synthetic comment -- @@ -227,7 +227,7 @@
*/
public static Provider[] getProviders(String filter) {
if (filter == null) {
            throw new NullPointerException();
}
if (filter.length() == 0) {
throw new InvalidParameterException();
//Synthetic comment -- @@ -271,7 +271,7 @@
*/
public static synchronized Provider[] getProviders(Map<String,String> filter) {
if (filter == null) {
            throw new NullPointerException();
}
if (filter.isEmpty()) {
return null;








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/Signature.java b/luni/src/main/java/java/security/Signature.java
//Synthetic comment -- index d9e1e41..be89654 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
public static Signature getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
Object spi = sap.spi;
//Synthetic comment -- @@ -134,7 +134,7 @@
public static Signature getInstance(String algorithm, String provider)
throws NoSuchAlgorithmException, NoSuchProviderException {
if (algorithm == null) {
            throw new NullPointerException();
}
if (provider == null || provider.isEmpty()) {
throw new IllegalArgumentException();
//Synthetic comment -- @@ -165,7 +165,7 @@
public static Signature getInstance(String algorithm, Provider provider)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
if (provider == null) {
throw new IllegalArgumentException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/Signer.java b/luni/src/main/java/java/security/Signer.java
//Synthetic comment -- index 1e4412a..b892090 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
*/
public final void setKeyPair(KeyPair pair) throws InvalidParameterException, KeyException {
if (pair == null) {
            throw new NullPointerException();
}

if (pair.getPrivate() == null || pair.getPublic() == null) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertPathBuilder.java b/luni/src/main/java/java/security/cert/CertPathBuilder.java
//Synthetic comment -- index aa65fe7..42029e5 100644

//Synthetic comment -- @@ -102,7 +102,7 @@
public static CertPathBuilder getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new CertPathBuilder((CertPathBuilderSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -128,7 +128,7 @@
public static CertPathBuilder getInstance(String algorithm, String provider)
throws NoSuchAlgorithmException, NoSuchProviderException {
if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException();
}
Provider impProvider = Security.getProvider(provider);
if (impProvider == null) {
//Synthetic comment -- @@ -156,10 +156,10 @@
public static CertPathBuilder getInstance(String algorithm,
Provider provider) throws NoSuchAlgorithmException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new CertPathBuilder((CertPathBuilderSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertPathValidator.java b/luni/src/main/java/java/security/cert/CertPathValidator.java
//Synthetic comment -- index 69b9f99..ddf78bf 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
public static CertPathValidator getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new CertPathValidator((CertPathValidatorSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -160,7 +160,7 @@
throw new IllegalArgumentException();
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new CertPathValidator((CertPathValidatorSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertStore.java b/luni/src/main/java/java/security/cert/CertStore.java
//Synthetic comment -- index 6cdaea7..2e28828 100644

//Synthetic comment -- @@ -97,7 +97,7 @@
public static CertStore getInstance(String type, CertStoreParameters params)
throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
if (type == null) {
            throw new NullPointerException();
}
try {
Engine.SpiAndProvider sap = ENGINE.getInstance(type, params);
//Synthetic comment -- @@ -140,7 +140,7 @@
throws InvalidAlgorithmParameterException,
NoSuchAlgorithmException, NoSuchProviderException {
if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException();
}
Provider impProvider = Security.getProvider(provider);
if (impProvider == null) {
//Synthetic comment -- @@ -172,10 +172,10 @@
CertStoreParameters params, Provider provider)
throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (type == null) {
            throw new NullPointerException();
}
try {
Object spi = ENGINE.getInstance(type, provider, params);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CertificateFactory.java b/luni/src/main/java/java/security/cert/CertificateFactory.java
//Synthetic comment -- index 1aac1a0..83d40d3 100644

//Synthetic comment -- @@ -84,7 +84,7 @@
public static final CertificateFactory getInstance(String type)
throws CertificateException {
if (type == null) {
            throw new NullPointerException();
}
try {
Engine.SpiAndProvider sap = ENGINE.getInstance(type, null);
//Synthetic comment -- @@ -117,7 +117,7 @@
String provider) throws CertificateException,
NoSuchProviderException {
if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException();
}
Provider impProvider = Security.getProvider(provider);
if (impProvider == null) {
//Synthetic comment -- @@ -147,10 +147,10 @@
public static final CertificateFactory getInstance(String type,
Provider provider) throws CertificateException {
if (provider == null) {
            throw new IllegalArgumentException();
}
if (type == null) {
            throw new NullPointerException();
}
try {
Object spi = ENGINE.getInstance(type, provider, null);








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/CollectionCertStoreParameters.java b/luni/src/main/java/java/security/cert/CollectionCertStoreParameters.java
//Synthetic comment -- index de3c85d..9373b40 100644

//Synthetic comment -- @@ -58,10 +58,10 @@
*             if {@code collection is null}.
*/
public CollectionCertStoreParameters(Collection<?> collection) {
        this.collection = collection;
        if (this.collection == null) {
            throw new NullPointerException();
}
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/LDAPCertStoreParameters.java b/luni/src/main/java/java/security/cert/LDAPCertStoreParameters.java
//Synthetic comment -- index 5b01f90..163c99a 100644

//Synthetic comment -- @@ -43,11 +43,11 @@
*             is {@code serverName} is {@code null}.
*/
public LDAPCertStoreParameters(String serverName, int port) {
this.port = port;
this.serverName = serverName;
        if (this.serverName == null) {
            throw new NullPointerException();
        }
}

/**
//Synthetic comment -- @@ -71,11 +71,11 @@
*             if {@code serverName} is {@code null}.
*/
public LDAPCertStoreParameters(String serverName) {
this.port = DEFAULT_LDAP_PORT;
this.serverName = serverName;
        if (this.serverName == null) {
            throw new NullPointerException();
        }
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/PKIXCertPathBuilderResult.java b/luni/src/main/java/java/security/cert/PKIXCertPathBuilderResult.java
//Synthetic comment -- index 589ce82..2359612 100644

//Synthetic comment -- @@ -49,10 +49,10 @@
public PKIXCertPathBuilderResult(CertPath certPath, TrustAnchor trustAnchor,
PolicyNode policyTree, PublicKey subjectPublicKey) {
super(trustAnchor, policyTree, subjectPublicKey);
        this.certPath = certPath;
        if (this.certPath == null) {
throw new NullPointerException("certPath == null");
}
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/cert/X509CRL.java b/luni/src/main/java/java/security/cert/X509CRL.java
//Synthetic comment -- index 4badd59..4addb0e 100644

//Synthetic comment -- @@ -218,7 +218,7 @@
*/
public X509CRLEntry getRevokedCertificate(X509Certificate certificate) {
if (certificate == null) {
            throw new NullPointerException();
}
return getRevokedCertificate(certificate.getSerialNumber());
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/security/spec/ECGenParameterSpec.java b/luni/src/main/java/java/security/spec/ECGenParameterSpec.java
//Synthetic comment -- index fe66b1e..c22038d 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
public ECGenParameterSpec(String name) {
this.name = name;
if (this.name == null) {
            throw new NullPointerException();
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/sql/DriverManager.java b/luni/src/main/java/java/sql/DriverManager.java
//Synthetic comment -- index 4cee1fe..c547585 100644

//Synthetic comment -- @@ -329,7 +329,7 @@
*/
public static void registerDriver(Driver driver) throws SQLException {
if (driver == null) {
            throw new NullPointerException();
}
synchronized (theDrivers) {
theDrivers.add(driver);








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/AttributedString.java b/luni/src/main/java/java/text/AttributedString.java
//Synthetic comment -- index 1335067..d679283 100644

//Synthetic comment -- @@ -519,7 +519,7 @@
*/
public AttributedString(String value) {
if (value == null) {
            throw new NullPointerException();
}
text = value;
attributeMap = new HashMap<Attribute, List<Range>>(11);
//Synthetic comment -- @@ -542,7 +542,7 @@
public AttributedString(String value,
Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {
if (value == null) {
            throw new NullPointerException();
}
if (value.length() == 0 && !attributes.isEmpty()) {
throw new IllegalArgumentException("Cannot add attributes to empty string");
//Synthetic comment -- @@ -575,7 +575,7 @@
*/
public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object value) {
if (attribute == null) {
            throw new NullPointerException();
}
if (text.length() == 0) {
throw new IllegalArgumentException();
//Synthetic comment -- @@ -612,7 +612,7 @@
public void addAttribute(AttributedCharacterIterator.Attribute attribute,
Object value, int start, int end) {
if (attribute == null) {
            throw new NullPointerException();
}
if (start < 0 || end > text.length() || start >= end) {
throw new IllegalArgumentException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/Collator.java b/luni/src/main/java/java/text/Collator.java
//Synthetic comment -- index 0fa8c71..2ddb516 100644

//Synthetic comment -- @@ -286,7 +286,7 @@
*/
public static Collator getInstance(Locale locale) {
if (locale == null) {
            throw new NullPointerException();
}
return new RuleBasedCollator(new RuleBasedCollatorICU(locale));
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DateFormatSymbols.java b/luni/src/main/java/java/text/DateFormatSymbols.java
//Synthetic comment -- index 3c4768d..e2a2345 100644

//Synthetic comment -- @@ -142,7 +142,7 @@
*/
public static final DateFormatSymbols getInstance(Locale locale) {
if (locale == null) {
            throw new NullPointerException();
}
return new DateFormatSymbols(locale);
}
//Synthetic comment -- @@ -410,7 +410,7 @@
*/
public void setLocalPatternChars(String data) {
if (data == null) {
            throw new NullPointerException();
}
localPatternChars = data;
}
//Synthetic comment -- @@ -471,7 +471,7 @@
*/
public void setZoneStrings(String[][] zoneStrings) {
if (zoneStrings == null) {
            throw new NullPointerException();
}
for (String[] row : zoneStrings) {
if (row.length < 5) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DecimalFormat.java b/luni/src/main/java/java/text/DecimalFormat.java
//Synthetic comment -- index c1a48db..eded6a8 100644

//Synthetic comment -- @@ -654,7 +654,7 @@
@Override
public AttributedCharacterIterator formatToCharacterIterator(Object object) {
if (object == null) {
            throw new NullPointerException();
}
return dform.formatToCharacterIterator(object);
}
//Synthetic comment -- @@ -1248,7 +1248,7 @@
*/
public void setRoundingMode(RoundingMode roundingMode) {
if (roundingMode == null) {
            throw new NullPointerException();
}
this.roundingMode = roundingMode;
if (roundingMode != RoundingMode.UNNECESSARY) { // ICU4C doesn't support UNNECESSARY.








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/DecimalFormatSymbols.java b/luni/src/main/java/java/text/DecimalFormatSymbols.java
//Synthetic comment -- index 9d2bcc1..708b291 100644

//Synthetic comment -- @@ -127,7 +127,7 @@
*/
public static DecimalFormatSymbols getInstance(Locale locale) {
if (locale == null) {
            throw new NullPointerException();
}
return new DecimalFormatSymbols(locale);
}
//Synthetic comment -- @@ -389,7 +389,7 @@
*/
public void setCurrency(Currency currency) {
if (currency == null) {
            throw new NullPointerException();
}
if (currency == this.currency) {
return;
//Synthetic comment -- @@ -558,7 +558,7 @@
*/
public void setExponentSeparator(String value) {
if (value == null) {
            throw new NullPointerException();
}
this.exponentSeparator = value;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/MessageFormat.java b/luni/src/main/java/java/text/MessageFormat.java
//Synthetic comment -- index a98e4fd..2ab78db 100644

//Synthetic comment -- @@ -506,7 +506,7 @@
@Override
public AttributedCharacterIterator formatToCharacterIterator(Object object) {
if (object == null) {
            throw new NullPointerException();
}

StringBuffer buffer = new StringBuffer();








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/NumberFormat.java b/luni/src/main/java/java/text/NumberFormat.java
//Synthetic comment -- index 3bff6ad..070174b 100644

//Synthetic comment -- @@ -570,7 +570,7 @@
@Override
public final Object parseObject(String string, ParsePosition position) {
if (position == null) {
            throw new NullPointerException("position is null");
}
try {
return parse(string, position);








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/RuleBasedCollator.java b/luni/src/main/java/java/text/RuleBasedCollator.java
//Synthetic comment -- index 4fd8650..cda06db 100644

//Synthetic comment -- @@ -284,7 +284,7 @@
*/
public RuleBasedCollator(String rules) throws ParseException {
if (rules == null) {
            throw new NullPointerException();
}
if (rules.isEmpty()) {
throw new ParseException("empty rules", 0);
//Synthetic comment -- @@ -314,7 +314,7 @@
*/
public CollationElementIterator getCollationElementIterator(CharacterIterator source) {
if (source == null) {
            throw new NullPointerException();
}
return new CollationElementIterator(icuColl.getCollationElementIterator(source));
}
//Synthetic comment -- @@ -328,7 +328,7 @@
*/
public CollationElementIterator getCollationElementIterator(String source) {
if (source == null) {
            throw new NullPointerException();
}
return new CollationElementIterator(icuColl.getCollationElementIterator(source));
}
//Synthetic comment -- @@ -385,8 +385,10 @@
*/
@Override
public int compare(String source, String target) {
        if (source == null || target == null) {
            throw new NullPointerException();
}
return icuColl.compare(source, target);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index f39f770..da5af85 100644

//Synthetic comment -- @@ -471,7 +471,7 @@
@Override
public AttributedCharacterIterator formatToCharacterIterator(Object object) {
if (object == null) {
            throw new NullPointerException();
}
if (object instanceof Date) {
return formatToCharacterIteratorImpl((Date) object);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/AbstractQueue.java b/luni/src/main/java/java/util/AbstractQueue.java
//Synthetic comment -- index b046559..47f81fd 100644

//Synthetic comment -- @@ -150,9 +150,9 @@
*/
public boolean addAll(Collection<? extends E> c) {
if (c == null)
            throw new NullPointerException();
if (c == this)
            throw new IllegalArgumentException();
boolean modified = false;
for (E e : c)
if (add(e))








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ArrayDeque.java b/luni/src/main/java/java/util/ArrayDeque.java
//Synthetic comment -- index c4d67b8..5ee3f81 100644

//Synthetic comment -- @@ -193,7 +193,7 @@
*/
public void addFirst(E e) {
if (e == null)
            throw new NullPointerException();
elements[head = (head - 1) & (elements.length - 1)] = e;
if (head == tail)
doubleCapacity();
//Synthetic comment -- @@ -209,7 +209,7 @@
*/
public void addLast(E e) {
if (e == null)
            throw new NullPointerException();
elements[tail] = e;
if ( (tail = (tail + 1) & (elements.length - 1)) == head)
doubleCapacity();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Arrays.java b/luni/src/main/java/java/util/Arrays.java
//Synthetic comment -- index 588410f..4a149b7 100644

//Synthetic comment -- @@ -35,7 +35,7 @@

ArrayList(E[] storage) {
if (storage == null) {
                throw new NullPointerException();
}
a = storage;
}
//Synthetic comment -- @@ -2611,7 +2611,7 @@
*/
public static <T> T[] copyOf(T[] original, int newLength) {
if (original == null) {
            throw new NullPointerException();
}
if (newLength < 0) {
throw new NegativeArraySizeException(Integer.toString(newLength));








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Calendar.java b/luni/src/main/java/java/util/Calendar.java
//Synthetic comment -- index 4cabd4a..81d01fb 100644

//Synthetic comment -- @@ -1389,7 +1389,7 @@
*/
public int compareTo(Calendar anotherCalendar) {
if (anotherCalendar == null) {
            throw new NullPointerException();
}
long timeInMillis = getTimeInMillis();
long anotherTimeInMillis = anotherCalendar.getTimeInMillis();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Collections.java b/luni/src/main/java/java/util/Collections.java
//Synthetic comment -- index b6729b4..d49ca85 100644

//Synthetic comment -- @@ -1412,7 +1412,7 @@
@SuppressWarnings("unchecked")
public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T object) {
if (list == null) {
            throw new NullPointerException();
}
if (list.isEmpty()) {
return -1;
//Synthetic comment -- @@ -1916,7 +1916,7 @@
@SuppressWarnings("unchecked")
public static void swap(List<?> list, int index1, int index2) {
if (list == null) {
            throw new NullPointerException();
}
final int size = list.size();
if (index1 < 0 || index1 >= size || index2 < 0 || index2 >= size) {
//Synthetic comment -- @@ -2174,7 +2174,7 @@
public static <T> Collection<T> synchronizedCollection(
Collection<T> collection) {
if (collection == null) {
            throw new NullPointerException();
}
return new SynchronizedCollection<T>(collection);
}
//Synthetic comment -- @@ -2189,7 +2189,7 @@
*/
public static <T> List<T> synchronizedList(List<T> list) {
if (list == null) {
            throw new NullPointerException();
}
if (list instanceof RandomAccess) {
return new SynchronizedRandomAccessList<T>(list);
//Synthetic comment -- @@ -2207,7 +2207,7 @@
*/
public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
if (map == null) {
            throw new NullPointerException();
}
return new SynchronizedMap<K, V>(map);
}
//Synthetic comment -- @@ -2222,7 +2222,7 @@
*/
public static <E> Set<E> synchronizedSet(Set<E> set) {
if (set == null) {
            throw new NullPointerException();
}
return new SynchronizedSet<E>(set);
}
//Synthetic comment -- @@ -2238,7 +2238,7 @@
public static <K, V> SortedMap<K, V> synchronizedSortedMap(
SortedMap<K, V> map) {
if (map == null) {
            throw new NullPointerException();
}
return new SynchronizedSortedMap<K, V>(map);
}
//Synthetic comment -- @@ -2253,7 +2253,7 @@
*/
public static <E> SortedSet<E> synchronizedSortedSet(SortedSet<E> set) {
if (set == null) {
            throw new NullPointerException();
}
return new SynchronizedSortedSet<E>(set);
}
//Synthetic comment -- @@ -2271,7 +2271,7 @@
public static <E> Collection<E> unmodifiableCollection(
Collection<? extends E> collection) {
if (collection == null) {
            throw new NullPointerException();
}
return new UnmodifiableCollection<E>((Collection<E>) collection);
}
//Synthetic comment -- @@ -2288,7 +2288,7 @@
@SuppressWarnings("unchecked")
public static <E> List<E> unmodifiableList(List<? extends E> list) {
if (list == null) {
            throw new NullPointerException();
}
if (list instanceof RandomAccess) {
return new UnmodifiableRandomAccessList<E>((List<E>) list);
//Synthetic comment -- @@ -2309,7 +2309,7 @@
public static <K, V> Map<K, V> unmodifiableMap(
Map<? extends K, ? extends V> map) {
if (map == null) {
            throw new NullPointerException();
}
return new UnmodifiableMap<K, V>((Map<K, V>) map);
}
//Synthetic comment -- @@ -2326,7 +2326,7 @@
@SuppressWarnings("unchecked")
public static <E> Set<E> unmodifiableSet(Set<? extends E> set) {
if (set == null) {
            throw new NullPointerException();
}
return new UnmodifiableSet<E>((Set<E>) set);
}
//Synthetic comment -- @@ -2344,7 +2344,7 @@
public static <K, V> SortedMap<K, V> unmodifiableSortedMap(
SortedMap<K, ? extends V> map) {
if (map == null) {
            throw new NullPointerException();
}
return new UnmodifiableSortedMap<K, V>((SortedMap<K, V>) map);
}
//Synthetic comment -- @@ -2360,7 +2360,7 @@
*/
public static <E> SortedSet<E> unmodifiableSortedSet(SortedSet<E> set) {
if (set == null) {
            throw new NullPointerException();
}
return new UnmodifiableSortedSet<E>(set);
}
//Synthetic comment -- @@ -2381,7 +2381,7 @@
*/
public static int frequency(Collection<?> c, Object o) {
if (c == null) {
            throw new NullPointerException();
}
if (c.isEmpty()) {
return 0;
//Synthetic comment -- @@ -2834,8 +2834,10 @@
Class<E> type;

public CheckedCollection(Collection<E> c, Class<E> type) {
            if (c == null || type == null) {
                throw new NullPointerException();
}
this.c = c;
this.type = type;
//Synthetic comment -- @@ -3079,8 +3081,12 @@
Class<V> valueType;

private CheckedMap(Map<K, V> m, Class<K> keyType, Class<V> valueType) {
            if (m == null || keyType == null || valueType == null) {
                throw new NullPointerException();
}
this.m = m;
this.keyType = keyType;
//Synthetic comment -- @@ -3172,7 +3178,7 @@

public CheckedEntry(Map.Entry<K, V> e, Class<V> valueType) {
if (e == null) {
                    throw new NullPointerException();
}
this.e = e;
this.valueType = valueType;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/DuplicateFormatFlagsException.java b/luni/src/main/java/java/util/DuplicateFormatFlagsException.java
//Synthetic comment -- index d04db8e..2a2bc2e 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
*/
public DuplicateFormatFlagsException(String f) {
if (f == null) {
            throw new NullPointerException();
}
flags = f;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/EnumMap.java b/luni/src/main/java/java/util/EnumMap.java
//Synthetic comment -- index d3d42b4..a721ee3 100644

//Synthetic comment -- @@ -773,7 +773,7 @@
@SuppressWarnings("unchecked")
private V putImpl(K key, V value) {
if (key == null) {
            throw new NullPointerException();
}
keyType.cast(key); // Called to throw ClassCastException.
int keyOrdinal = key.ordinal();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/FormatFlagsConversionMismatchException.java b/luni/src/main/java/java/util/FormatFlagsConversionMismatchException.java
//Synthetic comment -- index 5792877..5c36788 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
*/
public FormatFlagsConversionMismatchException(String f, char c) {
if (f == null) {
            throw new NullPointerException();
}
this.f = f;
this.c = c;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Formatter.java b/luni/src/main/java/java/util/Formatter.java
//Synthetic comment -- index e9a2f4a..021da08 100644

//Synthetic comment -- @@ -884,7 +884,7 @@
*/
public Formatter(PrintStream ps) {
if (ps == null) {
            throw new NullPointerException();
}
out = ps;
locale = Locale.getDefault();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Hashtable.java b/luni/src/main/java/java/util/Hashtable.java
//Synthetic comment -- index 4ea26fb..a4e24bc 100644

//Synthetic comment -- @@ -313,7 +313,7 @@
*/
public synchronized boolean containsValue(Object value) {
if (value == null) {
            throw new NullPointerException();
}

HashtableEntry[] tab = table;
//Synthetic comment -- @@ -361,8 +361,10 @@
* @see java.lang.Object#equals
*/
public synchronized V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key=" + key + " value=" + value);
}
int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
//Synthetic comment -- @@ -395,8 +397,10 @@
* ensure that capacity is sufficient, and does not increment modCount.
*/
private void constructorPut(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key=" + key + " value=" + value);
}
int hash = secondaryHash(key.hashCode());
HashtableEntry<K, V>[] tab = table;
//Synthetic comment -- @@ -680,7 +684,7 @@

public final V setValue(V value) {
if (value == null) {
                throw new NullPointerException("key=" + key + " value=" + value);
}
V oldValue = this.value;
this.value = value;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/IllegalFormatConversionException.java b/luni/src/main/java/java/util/IllegalFormatConversionException.java
//Synthetic comment -- index 31c0eed..af986f6 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
public IllegalFormatConversionException(char c, Class<?> arg) {
this.c = c;
if (arg == null) {
            throw new NullPointerException();
}
this.arg = arg;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/IllegalFormatFlagsException.java b/luni/src/main/java/java/util/IllegalFormatFlagsException.java
//Synthetic comment -- index 6947912..4946c55 100644

//Synthetic comment -- @@ -38,7 +38,7 @@
*/
public IllegalFormatFlagsException(String flags) {
if (flags == null) {
            throw new NullPointerException();
}
this.flags = flags;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ListResourceBundle.java b/luni/src/main/java/java/util/ListResourceBundle.java
//Synthetic comment -- index 1508b93..fc6ab97 100644

//Synthetic comment -- @@ -108,7 +108,7 @@
public final Object handleGetObject(String key) {
initializeTable();
if (key == null) {
            throw new NullPointerException();
}
return table.get(key);
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/MissingFormatArgumentException.java b/luni/src/main/java/java/util/MissingFormatArgumentException.java
//Synthetic comment -- index ce72efa..1733501 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
*/
public MissingFormatArgumentException(String s) {
if (s == null) {
            throw new NullPointerException();
}
this.s = s;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/MissingFormatWidthException.java b/luni/src/main/java/java/util/MissingFormatWidthException.java
//Synthetic comment -- index b6d0ca6..0a3b5ae 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
*/
public MissingFormatWidthException(String s) {
if (s == null) {
            throw new NullPointerException();
}
this.s = s;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Observable.java b/luni/src/main/java/java/util/Observable.java
//Synthetic comment -- index 2c2877e..c984c68 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
*/
public void addObserver(Observer observer) {
if (observer == null) {
            throw new NullPointerException();
}
synchronized (this) {
if (!observers.contains(observer))








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/PriorityQueue.java b/luni/src/main/java/java/util/PriorityQueue.java
//Synthetic comment -- index 10c5968..e09eb05 100644

//Synthetic comment -- @@ -186,7 +186,7 @@
*/
public boolean offer(E o) {
if (o == null) {
            throw new NullPointerException();
}
growToSize(size + 1);
elements[size] = o;
//Synthetic comment -- @@ -387,7 +387,7 @@

private void initSize(Collection<? extends E> c) {
if (c == null) {
            throw new NullPointerException();
}
if (c.isEmpty()) {
elements = newElementArray(1);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Properties.java b/luni/src/main/java/java/util/Properties.java
//Synthetic comment -- index 1731ad8..57c6a00 100644

//Synthetic comment -- @@ -243,7 +243,7 @@
*/
public synchronized void load(InputStream in) throws IOException {
if (in == null) {
            throw new NullPointerException();
}
load(new InputStreamReader(in, "ISO-8859-1"));
}
//Synthetic comment -- @@ -276,7 +276,7 @@
@SuppressWarnings("fallthrough")
public synchronized void load(Reader in) throws IOException {
if (in == null) {
            throw new NullPointerException();
}
int mode = NONE, unicode = 0, count = 0;
char nextChar, buf[] = new char[40];
//Synthetic comment -- @@ -578,7 +578,7 @@
public synchronized void loadFromXML(InputStream in) throws IOException,
InvalidPropertiesFormatException {
if (in == null) {
            throw new NullPointerException();
}

if (builder == null) {
//Synthetic comment -- @@ -690,8 +690,10 @@
public synchronized void storeToXML(OutputStream os, String comment,
String encoding) throws IOException {

        if (os == null || encoding == null) {
            throw new NullPointerException();
}

/*








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/PropertyResourceBundle.java b/luni/src/main/java/java/util/PropertyResourceBundle.java
//Synthetic comment -- index 4029ee1..dbbd139 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
*/
public PropertyResourceBundle(InputStream stream) throws IOException {
if (stream == null) {
            throw new NullPointerException();
}
resources = new Properties();
resources.load(stream);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ResourceBundle.java b/luni/src/main/java/java/util/ResourceBundle.java
//Synthetic comment -- index ff38b5b..f5c8285 100644

//Synthetic comment -- @@ -211,8 +211,10 @@
*/
public static ResourceBundle getBundle(String bundleName, Locale locale,
ClassLoader loader) throws MissingResourceException {
        if (loader == null || bundleName == null) {
            throw new NullPointerException();
}
Locale defaultLocale = Locale.getDefault();
if (!cacheLocale.equals(defaultLocale)) {
//Synthetic comment -- @@ -610,14 +612,14 @@

public static void clearCache(ClassLoader loader) {
if (loader == null) {
            throw new NullPointerException();
}
cache.remove(loader);
}

public boolean containsKey(String key) {
if (key == null) {
            throw new NullPointerException();
}
return keySet().contains(key);
}
//Synthetic comment -- @@ -665,8 +667,10 @@

@Override
public Locale getFallbackLocale(String baseName, Locale locale) {
            if (baseName == null || locale == null) {
                throw new NullPointerException();
}
return null;
}
//Synthetic comment -- @@ -804,8 +808,10 @@
* {@code locale}.
*/
public List<Locale> getCandidateLocales(String baseName, Locale locale) {
            if (baseName == null || locale == null) {
                throw new NullPointerException();
}
List<Locale> retList = new ArrayList<Locale>();
String language = locale.getLanguage();
//Synthetic comment -- @@ -829,7 +835,7 @@
*/
public List<String> getFormats(String baseName) {
if (baseName == null) {
                throw new NullPointerException();
}
return format;
}
//Synthetic comment -- @@ -838,8 +844,10 @@
* Returns the fallback locale for {@code baseName} in {@code locale}.
*/
public Locale getFallbackLocale(String baseName, Locale locale) {
            if (baseName == null || locale == null) {
                throw new NullPointerException();
}
if (Locale.getDefault() != locale) {
return Locale.getDefault();
//Synthetic comment -- @@ -872,8 +880,10 @@
String format, ClassLoader loader, boolean reload)
throws IllegalAccessException, InstantiationException,
IOException {
            if (format == null || loader == null) {
                throw new NullPointerException();
}
final String bundleName = toBundleName(baseName, locale);
final ClassLoader clsloader = loader;
//Synthetic comment -- @@ -938,8 +948,10 @@
* default is TTL_NO_EXPIRATION_CONTROL.
*/
public long getTimeToLive(String baseName, Locale locale) {
            if (baseName == null || locale == null) {
                throw new NullPointerException();
}
return TTL_NO_EXPIRATION_CONTROL;
}
//Synthetic comment -- @@ -966,7 +978,7 @@
long loadTime) {
if (bundle == null) {
// FIXME what's the use of bundle?
                throw new NullPointerException();
}
String bundleName = toBundleName(baseName, locale);
String suffix = format;
//Synthetic comment -- @@ -1004,7 +1016,7 @@
final String preString = UNDER_SCORE;
final String underline = UNDER_SCORE;
if (baseName == null) {
                throw new NullPointerException();
}
StringBuilder ret = new StringBuilder();
StringBuilder prefix = new StringBuilder();
//Synthetic comment -- @@ -1044,7 +1056,7 @@
*/
public final String toResourceName(String bundleName, String suffix) {
if (suffix == null) {
                throw new NullPointerException();
}
StringBuilder ret = new StringBuilder(bundleName.replace('.', '/'));
ret.append('.');








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Scanner.java b/luni/src/main/java/java/util/Scanner.java
//Synthetic comment -- index 8f889b3..5f7d0e3 100644

//Synthetic comment -- @@ -241,7 +241,7 @@
*/
public Scanner(Readable src) {
if (src == null) {
            throw new NullPointerException();
}
input = src;
initialization();
//Synthetic comment -- @@ -1664,7 +1664,7 @@
*/
public Scanner useLocale(Locale l) {
if (l == null) {
            throw new NullPointerException();
}
this.locale = l;
return this;
//Synthetic comment -- @@ -1724,7 +1724,7 @@
*/
private void checkNull(Pattern pattern) {
if (pattern == null) {
            throw new NullPointerException();
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/ServiceLoader.java b/luni/src/main/java/java/util/ServiceLoader.java
//Synthetic comment -- index beacaab..016ab3f 100644

//Synthetic comment -- @@ -78,7 +78,7 @@
// It makes no sense for service to be null.
// classLoader is null if you want the system class loader.
if (service == null) {
            throw new NullPointerException();
}
this.service = service;
this.classLoader = classLoader;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/StringTokenizer.java b/luni/src/main/java/java/util/StringTokenizer.java
//Synthetic comment -- index 1b07604..e6686e8 100644

//Synthetic comment -- @@ -91,13 +91,13 @@
*/
public StringTokenizer(String string, String delimiters,
boolean returnDelimiters) {
        if (string != null) {
            this.string = string;
            this.delimiters = delimiters;
            this.returnDelimiters = returnDelimiters;
            this.position = 0;
        } else
            throw new NullPointerException();
}

/**
//Synthetic comment -- @@ -143,7 +143,7 @@
*/
public boolean hasMoreTokens() {
if (delimiters == null) {
            throw new NullPointerException();
}
int length = string.length();
if (position < length) {
//Synthetic comment -- @@ -180,7 +180,7 @@
*/
public String nextToken() {
if (delimiters == null) {
            throw new NullPointerException();
}
int i = position;
int length = string.length();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/TimeZone.java b/luni/src/main/java/java/util/TimeZone.java
//Synthetic comment -- index b7d7da1..85011bc 100644

//Synthetic comment -- @@ -405,7 +405,7 @@
*/
public void setID(String id) {
if (id == null) {
            throw new NullPointerException();
}
ID = id;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Timer.java b/luni/src/main/java/java/util/Timer.java
//Synthetic comment -- index b4f7a83..afc745c 100644

//Synthetic comment -- @@ -362,7 +362,7 @@
*/
public Timer(String name, boolean isDaemon) {
if (name == null) {
            throw new NullPointerException("name is null");
}
this.impl = new TimerImpl(name, isDaemon);
this.finalizer = new FinalizerHelper(impl);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/UUID.java b/luni/src/main/java/java/util/UUID.java
//Synthetic comment -- index a932bb2..8ac0a63 100644

//Synthetic comment -- @@ -142,7 +142,7 @@
*/
public static UUID nameUUIDFromBytes(byte[] name) {
if (name == null) {
            throw new NullPointerException();
}
try {
MessageDigest md = MessageDigest.getInstance("MD5");
//Synthetic comment -- @@ -179,7 +179,7 @@
*/
public static UUID fromString(String uuid) {
if (uuid == null) {
            throw new NullPointerException();
}

int[] position = new int[5];








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/UnknownFormatConversionException.java b/luni/src/main/java/java/util/UnknownFormatConversionException.java
//Synthetic comment -- index e26de91..29af4e1 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
*/
public UnknownFormatConversionException(String s) {
if (s == null) {
            throw new NullPointerException();
}
this.s = s;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/UnknownFormatFlagsException.java b/luni/src/main/java/java/util/UnknownFormatFlagsException.java
//Synthetic comment -- index 9daa3f1..990432b 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
*/
public UnknownFormatFlagsException(String f) {
if (f == null) {
            throw new NullPointerException();
}
flags = f;
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/jar/JarOutputStream.java b/luni/src/main/java/java/util/jar/JarOutputStream.java
//Synthetic comment -- index 78e3aa5..fc0645e 100644

//Synthetic comment -- @@ -37,20 +37,20 @@
*
* @param os
*            the {@code OutputStream} to write to
     * @param mf
*            the {@code Manifest} to output for this JAR file.
* @throws IOException
*             if an error occurs creating the {@code JarOutputStream}.
*/
    public JarOutputStream(OutputStream os, Manifest mf) throws IOException {
super(os);
        if (mf == null) {
            throw new NullPointerException();
}
        manifest = mf;
ZipEntry ze = new ZipEntry(JarFile.MANIFEST_NAME);
putNextEntry(ze);
        manifest.write(this);
closeEntry();
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/util/logging/FileHandler.java b/luni/src/main/java/java/util/logging/FileHandler.java
//Synthetic comment -- index 980955a..6ffef87 100644

//Synthetic comment -- @@ -214,8 +214,10 @@
String className = this.getClass().getName();
pattern = (p == null) ? getStringProperty(className + ".pattern",
DEFAULT_PATTERN) : p;
        if (pattern == null || pattern.isEmpty()) {
            throw new NullPointerException("Pattern cannot be empty or null");
}
append = (a == null) ? getBooleanProperty(className + ".append",
DEFAULT_APPEND) : a.booleanValue();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/logging/Handler.java b/luni/src/main/java/java/util/logging/Handler.java
//Synthetic comment -- index 5a6c14e..13dbdd5 100644

//Synthetic comment -- @@ -227,7 +227,7 @@
*/
public boolean isLoggable(LogRecord record) {
if (record == null) {
            throw new NullPointerException();
}
if (this.level.intValue() == Level.OFF.intValue()) {
return false;
//Synthetic comment -- @@ -294,17 +294,17 @@
/**
* Sets the error manager for this handler.
*
     * @param em
*            the error manager to set.
* @throws NullPointerException
*             if {@code em} is {@code null}.
*/
    public void setErrorManager(ErrorManager em) {
LogManager.getLogManager().checkAccess();
        if (em == null) {
            throw new NullPointerException();
}
        this.errorMan = em;
}

/**
//Synthetic comment -- @@ -327,7 +327,7 @@
*/
void internalSetFormatter(Formatter newFormatter) {
if (newFormatter == null) {
            throw new NullPointerException();
}
this.formatter = newFormatter;
}
//Synthetic comment -- @@ -356,7 +356,7 @@
*/
public void setLevel(Level newLevel) {
if (newLevel == null) {
            throw new NullPointerException();
}
LogManager.getLogManager().checkAccess();
this.level = newLevel;








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/logging/LogManager.java b/luni/src/main/java/java/util/logging/LogManager.java
//Synthetic comment -- index 449c263..8877cd5 100644

//Synthetic comment -- @@ -446,7 +446,7 @@
*/
public void addPropertyChangeListener(PropertyChangeListener l) {
if (l == null) {
            throw new NullPointerException();
}
checkAccess();
listeners.addPropertyChangeListener(l);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/logging/StreamHandler.java b/luni/src/main/java/java/util/logging/StreamHandler.java
//Synthetic comment -- index 7581835..60b4321 100644

//Synthetic comment -- @@ -167,7 +167,7 @@
*/
protected void setOutputStream(OutputStream os) {
if (os == null) {
            throw new NullPointerException();
}
LogManager.getLogManager().checkAccess();
close(true);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/prefs/AbstractPreferences.java b/luni/src/main/java/java/util/prefs/AbstractPreferences.java
//Synthetic comment -- index d86d789..71110c3 100644

//Synthetic comment -- @@ -372,7 +372,7 @@
@Override
public void exportNode(OutputStream ostream) throws IOException, BackingStoreException {
if (ostream == null) {
            throw new NullPointerException("Stream is null");
}
checkState();
XMLParser.exportPrefs(this, ostream, false);
//Synthetic comment -- @@ -381,7 +381,7 @@
@Override
public void exportSubtree(OutputStream ostream) throws IOException, BackingStoreException {
if (ostream == null) {
            throw new NullPointerException("Stream is null");
}
checkState();
XMLParser.exportPrefs(this, ostream, true);
//Synthetic comment -- @@ -402,7 +402,7 @@
@Override
public String get(String key, String deflt) {
if (key == null) {
            throw new NullPointerException();
}
String result = null;
synchronized (lock) {
//Synthetic comment -- @@ -597,7 +597,7 @@
@Override
public boolean nodeExists(String name) throws BackingStoreException {
if (name == null) {
            throw new NullPointerException();
}
AbstractPreferences startNode = null;
synchronized (lock) {
//Synthetic comment -- @@ -640,8 +640,10 @@

@Override
public void put(String key, String value) {
        if (key == null || value == null) {
            throw new NullPointerException();
}
if (key.length() > MAX_KEY_LENGTH || value.length() > MAX_VALUE_LENGTH) {
throw new IllegalArgumentException();
//Synthetic comment -- @@ -730,7 +732,7 @@
@Override
public void addNodeChangeListener(NodeChangeListener ncl) {
if (ncl == null) {
            throw new NullPointerException();
}
checkState();
synchronized (nodeChangeListeners) {
//Synthetic comment -- @@ -741,7 +743,7 @@
@Override
public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
if (pcl == null) {
            throw new NullPointerException();
}
checkState();
synchronized (preferenceChangeListeners) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/DeflaterInputStream.java b/luni/src/main/java/java/util/zip/DeflaterInputStream.java
//Synthetic comment -- index c6e95f2..805ce17 100644

//Synthetic comment -- @@ -72,8 +72,10 @@
*/
public DeflaterInputStream(InputStream in, Deflater deflater, int bufferSize) {
super(in);
        if (in == null || deflater == null) {
            throw new NullPointerException();
}
if (bufferSize <= 0) {
throw new IllegalArgumentException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/DeflaterOutputStream.java b/luni/src/main/java/java/util/zip/DeflaterOutputStream.java
//Synthetic comment -- index b0bcb99..d336e72 100644

//Synthetic comment -- @@ -115,8 +115,10 @@
*/
public DeflaterOutputStream(OutputStream os, Deflater def, int bsize, boolean syncFlush) {
super(os);
        if (os == null || def == null) {
            throw new NullPointerException();
}
if (bsize <= 0) {
throw new IllegalArgumentException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterInputStream.java b/luni/src/main/java/java/util/zip/InflaterInputStream.java
//Synthetic comment -- index 580d605..6081037 100644

//Synthetic comment -- @@ -103,8 +103,10 @@
*/
public InflaterInputStream(InputStream is, Inflater inflater, int bsize) {
super(is);
        if (is == null || inflater == null) {
            throw new NullPointerException();
}
if (bsize <= 0) {
throw new IllegalArgumentException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/InflaterOutputStream.java b/luni/src/main/java/java/util/zip/InflaterOutputStream.java
//Synthetic comment -- index c9687b6..9a699a8 100644

//Synthetic comment -- @@ -70,8 +70,10 @@
*/
public InflaterOutputStream(OutputStream out, Inflater inf, int bufferSize) {
super(out);
        if (out == null || inf == null) {
            throw new NullPointerException();
}
if (bufferSize <= 0) {
throw new IllegalArgumentException();








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipEntry.java b/luni/src/main/java/java/util/zip/ZipEntry.java
//Synthetic comment -- index 988bd2c..e2bfc8d 100644

//Synthetic comment -- @@ -71,7 +71,7 @@
*/
public ZipEntry(String name) {
if (name == null) {
            throw new NullPointerException();
}
if (name.length() > 0xFFFF) {
throw new IllegalArgumentException("Name too long: " + name.length());








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipFile.java b/luni/src/main/java/java/util/zip/ZipFile.java
//Synthetic comment -- index 363f57e..6ecd489 100644

//Synthetic comment -- @@ -223,7 +223,7 @@
public ZipEntry getEntry(String entryName) {
checkNotClosed();
if (entryName == null) {
            throw new NullPointerException();
}

ZipEntry ze = mEntries.get(entryName);








//Synthetic comment -- diff --git a/luni/src/main/java/java/util/zip/ZipInputStream.java b/luni/src/main/java/java/util/zip/ZipInputStream.java
//Synthetic comment -- index d082fc7..e7c4566 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
public ZipInputStream(InputStream stream) {
super(new PushbackInputStream(stream, BUF_SIZE), new Inflater(true));
if (stream == null) {
            throw new NullPointerException();
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/Cipher.java b/luni/src/main/java/javax/crypto/Cipher.java
//Synthetic comment -- index 1dacd46..aeb5def 100644

//Synthetic comment -- @@ -141,10 +141,10 @@
protected Cipher(CipherSpi cipherSpi, Provider provider,
String transformation) {
if (cipherSpi == null) {
            throw new NullPointerException();
}
if (!(cipherSpi instanceof NullCipherSpi) && provider == null) {
            throw new NullPointerException();
}
this.provider = provider;
this.transformation = transformation;
//Synthetic comment -- @@ -1332,7 +1332,7 @@
public static final int getMaxAllowedKeyLength(String transformation)
throws NoSuchAlgorithmException {
if (transformation == null) {
            throw new NullPointerException();
}
checkTransformation(transformation);
//FIXME jurisdiction policy files
//Synthetic comment -- @@ -1356,7 +1356,7 @@
public static final AlgorithmParameterSpec getMaxAllowedParameterSpec(
String transformation) throws NoSuchAlgorithmException {
if (transformation == null) {
            throw new NullPointerException();
}
checkTransformation(transformation);
//FIXME jurisdiction policy files








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/CipherInputStream.java b/luni/src/main/java/javax/crypto/CipherInputStream.java
//Synthetic comment -- index 39dcfda..a59a425 100644

//Synthetic comment -- @@ -135,7 +135,7 @@
@Override
public int read(byte[] buf, int off, int len) throws IOException {
if (in == null) {
            throw new NullPointerException("Underlying input stream is null");
}

int i;








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/EncryptedPrivateKeyInfo.java b/luni/src/main/java/javax/crypto/EncryptedPrivateKeyInfo.java
//Synthetic comment -- index 034f07a..0fb5b76 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
* Creates an {@code EncryptedPrivateKeyInfo} instance from an algorithm
* name and its encrypted data.
*
     * @param encrAlgName
*            the name of an algorithm.
* @param encryptedData
*            the encrypted data.
//Synthetic comment -- @@ -133,12 +133,12 @@
* @throws IllegalArgumentException
*             if {@code encryptedData} is empty.
*/
    public EncryptedPrivateKeyInfo(String encrAlgName, byte[] encryptedData)
throws NoSuchAlgorithmException {
        if (encrAlgName == null) {
            throw new NullPointerException("the algName parameter is null");
}
        this.algName = encrAlgName;
if (!mapAlgName()) {
throw new NoSuchAlgorithmException("Unsupported algorithm: " + this.algName);
}








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/ExemptionMechanism.java b/luni/src/main/java/javax/crypto/ExemptionMechanism.java
//Synthetic comment -- index 2ac4994..8745b78 100644

//Synthetic comment -- @@ -98,7 +98,7 @@
public static final ExemptionMechanism getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new ExemptionMechanism((ExemptionMechanismSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -134,7 +134,7 @@
throw new NoSuchProviderException(provider);
}
if (algorithm == null) {
            throw new NullPointerException();
}
return getInstance(algorithm, impProvider);
}
//Synthetic comment -- @@ -159,7 +159,7 @@
public static final ExemptionMechanism getInstance(String algorithm,
Provider provider) throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
if (provider == null) {
throw new IllegalArgumentException("provider == null");








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/KeyAgreement.java b/luni/src/main/java/javax/crypto/KeyAgreement.java
//Synthetic comment -- index 9c5d86c..51b4cd1 100644

//Synthetic comment -- @@ -99,7 +99,7 @@
public static final KeyAgreement getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new KeyAgreement((KeyAgreementSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -161,7 +161,7 @@
throw new IllegalArgumentException("provider == null");
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new KeyAgreement((KeyAgreementSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/KeyGenerator.java b/luni/src/main/java/javax/crypto/KeyGenerator.java
//Synthetic comment -- index 77b1a82..606998a 100644

//Synthetic comment -- @@ -98,7 +98,7 @@
public static final KeyGenerator getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new KeyGenerator((KeyGeneratorSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -158,7 +158,7 @@
throw new IllegalArgumentException("provider == null");
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new KeyGenerator((KeyGeneratorSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/Mac.java b/luni/src/main/java/javax/crypto/Mac.java
//Synthetic comment -- index 1a05b59..46be141 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
public static final Mac getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new Mac((MacSpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -163,7 +163,7 @@
throw new IllegalArgumentException("provider == null");
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new Mac((MacSpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/SecretKeyFactory.java b/luni/src/main/java/javax/crypto/SecretKeyFactory.java
//Synthetic comment -- index 5e47abe1..8ab3eb8 100644

//Synthetic comment -- @@ -103,7 +103,7 @@
public static final SecretKeyFactory getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException();
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new SecretKeyFactory((SecretKeyFactorySpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -165,7 +165,7 @@
throw new IllegalArgumentException("provider == null");
}
if (algorithm == null) {
            throw new NullPointerException();
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new SecretKeyFactory((SecretKeyFactorySpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/spec/DESedeKeySpec.java b/luni/src/main/java/javax/crypto/spec/DESedeKeySpec.java
//Synthetic comment -- index fe86aeb..fcfe749 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
*/
public DESedeKeySpec(byte[] key) throws InvalidKeyException {
if (key == null) {
            throw new NullPointerException();
}
if (key.length < DES_EDE_KEY_LEN) {
throw new InvalidKeyException();
//Synthetic comment -- @@ -71,7 +71,7 @@
*/
public DESedeKeySpec(byte[] key, int offset) throws InvalidKeyException {
if (key == null) {
            throw new NullPointerException();
}
if (key.length - offset < DES_EDE_KEY_LEN) {
throw new InvalidKeyException();








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/spec/OAEPParameterSpec.java b/luni/src/main/java/javax/crypto/spec/OAEPParameterSpec.java
//Synthetic comment -- index 3bc9ab4..340e57f 100644

//Synthetic comment -- @@ -73,8 +73,12 @@
*/
public OAEPParameterSpec(String mdName, String mgfName,
AlgorithmParameterSpec mgfSpec, PSource pSrc) {
        if ((mdName == null) || (mgfName == null) || (pSrc == null)) {
            throw new NullPointerException();
}
this.mdName = mdName;
this.mgfName = mgfName;








//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/spec/PSource.java b/luni/src/main/java/javax/crypto/spec/PSource.java
//Synthetic comment -- index 0efa1e9..f644316 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
*/
protected PSource(String pSrcName) {
if (pSrcName == null) {
            throw new NullPointerException();
}
this.pSrcName = pSrcName;
}
//Synthetic comment -- @@ -85,7 +85,7 @@
public PSpecified(byte[] p) {
super("PSpecified");
if (p == null) {
                throw new NullPointerException();
}
//TODO: It is unknown which name should be used!
//super("");








//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/KeyManagerFactory.java b/luni/src/main/java/javax/net/ssl/KeyManagerFactory.java
//Synthetic comment -- index 82ce8a1..0b3db61 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
public static final KeyManagerFactory getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException("algorithm is null");
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new KeyManagerFactory((KeyManagerFactorySpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -127,7 +127,7 @@
throw new IllegalArgumentException("Provider is null");
}
if (algorithm == null) {
            throw new NullPointerException("algorithm is null");
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new KeyManagerFactory((KeyManagerFactorySpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/SSLContext.java b/luni/src/main/java/javax/net/ssl/SSLContext.java
//Synthetic comment -- index 9097bb9..a59f301 100644

//Synthetic comment -- @@ -93,7 +93,7 @@
*/
public static SSLContext getInstance(String protocol) throws NoSuchAlgorithmException {
if (protocol == null) {
            throw new NullPointerException("protocol is null");
}
Engine.SpiAndProvider sap = ENGINE.getInstance(protocol, null);
return new SSLContext((SSLContextSpi) sap.spi, sap.provider, protocol);
//Synthetic comment -- @@ -154,7 +154,7 @@
throw new IllegalArgumentException("provider is null");
}
if (protocol == null) {
            throw new NullPointerException("protocol is null");
}
Object spi = ENGINE.getInstance(protocol, provider, null);
return new SSLContext((SSLContextSpi) spi, provider, protocol);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/net/ssl/TrustManagerFactory.java b/luni/src/main/java/javax/net/ssl/TrustManagerFactory.java
//Synthetic comment -- index bf3bf8c..be9db06 100644

//Synthetic comment -- @@ -67,7 +67,7 @@
public static final TrustManagerFactory getInstance(String algorithm)
throws NoSuchAlgorithmException {
if (algorithm == null) {
            throw new NullPointerException("algorithm is null");
}
Engine.SpiAndProvider sap = ENGINE.getInstance(algorithm, null);
return new TrustManagerFactory((TrustManagerFactorySpi) sap.spi, sap.provider, algorithm);
//Synthetic comment -- @@ -126,7 +126,7 @@
throw new IllegalArgumentException("Provider is null");
}
if (algorithm == null) {
            throw new NullPointerException("algorithm is null");
}
Object spi = ENGINE.getInstance(algorithm, provider, null);
return new TrustManagerFactory((TrustManagerFactorySpi) spi, provider, algorithm);








//Synthetic comment -- diff --git a/luni/src/main/java/javax/security/auth/Subject.java b/luni/src/main/java/javax/security/auth/Subject.java
//Synthetic comment -- index a958484..6c9c036 100644

//Synthetic comment -- @@ -116,8 +116,12 @@
public Subject(boolean readOnly, Set<? extends Principal> subjPrincipals,
Set<?> pubCredentials, Set<?> privCredentials) {

        if (subjPrincipals == null || pubCredentials == null || privCredentials == null) {
            throw new NullPointerException();
}

principals = new SecureSet<Principal>(_PRINCIPALS, subjPrincipals);
//Synthetic comment -- @@ -467,7 +471,7 @@
*/
public static Subject getSubject(final AccessControlContext context) {
if (context == null) {
            throw new NullPointerException("AccessControlContext cannot be null");
}
PrivilegedAction<DomainCombiner> action = new PrivilegedAction<DomainCombiner>() {
public DomainCombiner run() {
//Synthetic comment -- @@ -554,7 +558,7 @@
private void verifyElement(Object o) {

if (o == null) {
                throw new NullPointerException();
}
if (permission == _PRINCIPALS && !(Principal.class.isAssignableFrom(o.getClass()))) {
throw new IllegalArgumentException("Element is not instance of java.security.Principal");
//Synthetic comment -- @@ -607,7 +611,7 @@
public boolean retainAll(Collection<?> c) {

if (c == null) {
                throw new NullPointerException();
}
return super.retainAll(c);
}
//Synthetic comment -- @@ -624,7 +628,7 @@
protected final <E> Set<E> get(final Class<E> c) {

if (c == null) {
                throw new NullPointerException();
}

AbstractSet<E> s = new AbstractSet<E>() {
//Synthetic comment -- @@ -652,7 +656,7 @@
public boolean retainAll(Collection<?> c) {

if (c == null) {
                        throw new NullPointerException();
}
return super.retainAll(c);
}








//Synthetic comment -- diff --git a/luni/src/main/java/javax/security/auth/x500/X500Principal.java b/luni/src/main/java/javax/security/auth/x500/X500Principal.java
//Synthetic comment -- index e6453e9..cedebe0 100644

//Synthetic comment -- @@ -123,7 +123,7 @@
*/
public X500Principal(String name) {
if (name == null) {
            throw new NullPointerException("Name cannot be null");
}
try {
dn = new Name(name);
//Synthetic comment -- @@ -134,7 +134,7 @@

public X500Principal(String name, Map<String,String> keywordMap){
if (name == null) {
            throw new NullPointerException("Name cannot be null");
}
try {
dn = new Name(substituteNameFromMap(name, keywordMap));








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/datatype/DatatypeFactory.java b/luni/src/main/java/javax/xml/datatype/DatatypeFactory.java
//Synthetic comment -- index 6a89dae..68291b6 100644

//Synthetic comment -- @@ -326,7 +326,7 @@
*/
public Duration newDurationDayTime(final String lexicalRepresentation) {
if (lexicalRepresentation == null) {
            throw new NullPointerException("The lexical representation cannot be null.");
}
// The lexical representation must match the pattern [^YM]*(T.*)?
int pos = lexicalRepresentation.indexOf('T');
//Synthetic comment -- @@ -539,7 +539,7 @@
*/
public Duration newDurationYearMonth(final String lexicalRepresentation) {
if (lexicalRepresentation == null) {
            throw new NullPointerException("The lexical representation cannot be null.");
}
// The lexical representation must match the pattern [^DT]*.
int length = lexicalRepresentation.length();








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/datatype/Duration.java b/luni/src/main/java/javax/xml/datatype/Duration.java
//Synthetic comment -- index 8121d36..fcdd4c5 100644

//Synthetic comment -- @@ -552,11 +552,7 @@

// check data parameter
if (date == null) {
            throw new NullPointerException(
                    "Cannot call "
                    + this.getClass().getName()
                    + "#addTo(Date date) with date == null."
            );
}

Calendar cal = new GregorianCalendar();








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/validation/SchemaFactory.java b/luni/src/main/java/javax/xml/validation/SchemaFactory.java
//Synthetic comment -- index 23e4798..2018067 100644

//Synthetic comment -- @@ -203,8 +203,10 @@
*/
public static SchemaFactory newInstance(String schemaLanguage, String factoryClassName,
ClassLoader classLoader) {
        if (schemaLanguage == null || factoryClassName == null) {
            throw new NullPointerException("schemaLanguage == null || factoryClassName == null");
}
if (classLoader == null) {
classLoader = Thread.currentThread().getContextClassLoader();
//Synthetic comment -- @@ -265,7 +267,7 @@
public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {

if (name == null) {
            throw new NullPointerException("the name parameter is null");
}
throw new SAXNotRecognizedException(name);
}
//Synthetic comment -- @@ -313,7 +315,7 @@
*/
public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
if (name == null) {
            throw new NullPointerException("the name parameter is null");
}
throw new SAXNotRecognizedException(name);
}
//Synthetic comment -- @@ -340,7 +342,7 @@
*/
public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
if (name == null) {
            throw new NullPointerException("the name parameter is null");
}
throw new SAXNotRecognizedException(name);
}
//Synthetic comment -- @@ -371,7 +373,7 @@
*/
public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
if (name == null) {
            throw new NullPointerException("the name parameter is null");
}
throw new SAXNotRecognizedException(name);
}








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/validation/SchemaFactoryFinder.java b/luni/src/main/java/javax/xml/validation/SchemaFactoryFinder.java
//Synthetic comment -- index 3a6cb83..636777c 100644

//Synthetic comment -- @@ -131,7 +131,9 @@
*      If the <tt>schemaLanguage</tt> parameter is null.
*/
public SchemaFactory newFactory(String schemaLanguage) {
        if(schemaLanguage==null)        throw new NullPointerException();
SchemaFactory f = _newFactory(schemaLanguage);
if (debug) {
if (f != null) {








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/validation/Validator.java b/luni/src/main/java/javax/xml/validation/Validator.java
//Synthetic comment -- index b4ee1ca..ea7908a 100644

//Synthetic comment -- @@ -339,7 +339,9 @@
* @see #setFeature(String, boolean)
*/
public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null) throw new NullPointerException("the name parameter is null");
throw new SAXNotRecognizedException(name);
}

//Synthetic comment -- @@ -372,7 +374,9 @@
* @see #getFeature(String)
*/
public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null) throw new NullPointerException("the name parameter is null");
throw new SAXNotRecognizedException(name);
}

//Synthetic comment -- @@ -400,7 +404,9 @@
*          When the name parameter is null.
*/
public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null) throw new NullPointerException("the name parameter is null");
throw new SAXNotRecognizedException(name);
}

//Synthetic comment -- @@ -431,7 +437,9 @@
* @see #setProperty(String, Object)
*/
public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null) throw new NullPointerException("the name parameter is null");
throw new SAXNotRecognizedException(name);
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/validation/ValidatorHandler.java b/luni/src/main/java/javax/xml/validation/ValidatorHandler.java
//Synthetic comment -- index 9606193..2b621ff 100644

//Synthetic comment -- @@ -347,8 +347,9 @@
* @see #setFeature(String, boolean)
*/
public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null)
            throw new NullPointerException();
throw new SAXNotRecognizedException(name);
}

//Synthetic comment -- @@ -381,8 +382,9 @@
* @see #getFeature(String)
*/
public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null)
            throw new NullPointerException();
throw new SAXNotRecognizedException(name);
}

//Synthetic comment -- @@ -411,8 +413,9 @@
*          When the name parameter is null.
*/
public void setProperty(String name, Object object) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null)
            throw new NullPointerException();
throw new SAXNotRecognizedException(name);
}

//Synthetic comment -- @@ -443,8 +446,9 @@
* @see #setProperty(String, Object)
*/
public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if(name==null)
            throw new NullPointerException();
throw new SAXNotRecognizedException(name);
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/xpath/XPathException.java b/luni/src/main/java/javax/xml/xpath/XPathException.java
//Synthetic comment -- index 8db369b..376d477 100644

//Synthetic comment -- @@ -48,8 +48,8 @@
*/
public XPathException(String message) {
super(message);
        if ( message == null ) {
            throw new NullPointerException ( "message can't be null");
}
this.cause = null;
}
//Synthetic comment -- @@ -66,8 +66,8 @@
public XPathException(Throwable cause) {
super(cause == null ? null : cause.toString());
this.cause = cause;
        if ( cause == null ) {
            throw new NullPointerException ( "cause can't be null");
}
}









//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/xpath/XPathFactory.java b/luni/src/main/java/javax/xml/xpath/XPathFactory.java
//Synthetic comment -- index 8b1c1fa..57f2195 100644

//Synthetic comment -- @@ -133,9 +133,7 @@
public static final XPathFactory newInstance(final String uri)
throws XPathFactoryConfigurationException {
if (uri == null) {
            throw new NullPointerException(
                "XPathFactory#newInstance(String uri) cannot be called with uri == null"
            );
}
if (uri.length() == 0) {
throw new IllegalArgumentException(
//Synthetic comment -- @@ -167,9 +165,7 @@
public static XPathFactory newInstance(String uri, String factoryClassName,
ClassLoader classLoader) throws XPathFactoryConfigurationException {
if (uri == null) {
            throw new NullPointerException(
                "XPathFactory#newInstance(String uri) cannot be called with uri == null"
            );
}
if (uri.length() == 0) {
throw new IllegalArgumentException(








//Synthetic comment -- diff --git a/luni/src/main/java/javax/xml/xpath/XPathFactoryFinder.java b/luni/src/main/java/javax/xml/xpath/XPathFactoryFinder.java
//Synthetic comment -- index 652a1d8..0113e7d 100644

//Synthetic comment -- @@ -126,7 +126,9 @@
*      If the parameter is null.
*/
public XPathFactory newFactory(String uri) {
        if(uri==null)        throw new NullPointerException();
XPathFactory f = _newFactory(uri);
if (debug) {
if (f != null) {








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/NativeIDN.java b/luni/src/main/java/libcore/icu/NativeIDN.java
//Synthetic comment -- index 9bf5cb1..db93379 100644

//Synthetic comment -- @@ -34,7 +34,7 @@

private static String convert(String s, int flags, boolean toAscii) {
if (s == null) {
            throw new NullPointerException();
}
return convertImpl(s, flags, toAscii);
}








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/RuleBasedCollatorICU.java b/luni/src/main/java/libcore/icu/RuleBasedCollatorICU.java
//Synthetic comment -- index d036c98..4221fe6 100644

//Synthetic comment -- @@ -46,7 +46,7 @@

public RuleBasedCollatorICU(String rules) throws ParseException {
if (rules == null) {
            throw new NullPointerException();
}
address = NativeCollation.openCollatorFromRules(rules, VALUE_OFF, VALUE_DEFAULT_STRENGTH);
}








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/StrictLineReader.java b/luni/src/main/java/libcore/io/StrictLineReader.java
//Synthetic comment -- index 5f8d452..36556a0 100644

//Synthetic comment -- @@ -106,8 +106,10 @@
*         or the specified charset is not supported.
*/
public StrictLineReader(InputStream in, int capacity, Charset charset) {
        if (in == null || charset == null) {
            throw new NullPointerException();
}
if (capacity < 0) {
throw new IllegalArgumentException("capacity <= 0");








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/UriCodec.java b/luni/src/main/java/libcore/net/UriCodec.java
//Synthetic comment -- index bde922b..6624474 100644

//Synthetic comment -- @@ -94,7 +94,7 @@
private void appendEncoded(StringBuilder builder, String s, Charset charset,
boolean isPartiallyEncoded) {
if (s == null) {
            throw new NullPointerException();
}

int escapeStart = -1;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/BasicLruCache.java b/luni/src/main/java/libcore/util/BasicLruCache.java
//Synthetic comment -- index 13ab3db..75e4a75 100644

//Synthetic comment -- @@ -43,7 +43,7 @@
*/
public synchronized final V get(K key) {
if (key == null) {
            throw new NullPointerException();
}

V result = map.get(key);
//Synthetic comment -- @@ -68,8 +68,10 @@
*     no longer cached, it has not been passed to {@link #entryEvicted}.
*/
public synchronized final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
}

V previous = map.put(key, value);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/crypto/internal/NullCipherSpi.java b/luni/src/main/java/org/apache/harmony/crypto/internal/NullCipherSpi.java
//Synthetic comment -- index 270f63b..151b403 100644

//Synthetic comment -- @@ -115,8 +115,10 @@
@Override
public int engineUpdate(ByteBuffer input, ByteBuffer output)
throws ShortBufferException {
        if (input == null || output == null) {
            throw new NullPointerException();
}
int result = input.limit() - input.position();
try {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/SystemScope.java b/luni/src/main/java/org/apache/harmony/security/SystemScope.java
//Synthetic comment -- index 89cf56b..bf4f849 100644

//Synthetic comment -- @@ -79,7 +79,7 @@
*/
public synchronized Identity getIdentity(String name) {
if (name == null) {
            throw new NullPointerException();
}
return (Identity) names.get(name);
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CRLImpl.java b/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CRLImpl.java
//Synthetic comment -- index 134d8f9..68ec38a 100644

//Synthetic comment -- @@ -237,7 +237,7 @@
*/
public X509CRLEntry getRevokedCertificate(X509Certificate certificate) {
if (certificate == null) {
            throw new NullPointerException();
}
if (!entriesRetrieved) {
retrieveEntries();








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/provider/crypto/SHA1withDSA_SignatureImpl.java b/luni/src/main/java/org/apache/harmony/security/provider/crypto/SHA1withDSA_SignatureImpl.java
//Synthetic comment -- index d2a9c6d..2958e00 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
protected Object engineGetParameter(String param)
throws InvalidParameterException {
if (param == null) {
            throw new NullPointerException();
}
return null;
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xml/ExpatAttributes.java b/luni/src/main/java/org/apache/harmony/xml/ExpatAttributes.java
//Synthetic comment -- index b92fc50..5ec632c 100644

//Synthetic comment -- @@ -76,10 +76,10 @@

public int getIndex(String uri, String localName) {
if (uri == null) {
            throw new NullPointerException("uri");
}
if (localName == null) {
            throw new NullPointerException("local name");
}
int pointer = getPointer();
if (pointer == 0) {
//Synthetic comment -- @@ -90,7 +90,7 @@

public int getIndex(String qName) {
if (qName == null) {
            throw new NullPointerException("uri");
}
int pointer = getPointer();
if (pointer == 0) {
//Synthetic comment -- @@ -101,10 +101,10 @@

public String getType(String uri, String localName) {
if (uri == null) {
            throw new NullPointerException("uri");
}
if (localName == null) {
            throw new NullPointerException("local name");
}
return getIndex(uri, localName) == -1 ? null : CDATA;
}
//Synthetic comment -- @@ -115,10 +115,10 @@

public String getValue(String uri, String localName) {
if (uri == null) {
            throw new NullPointerException("uri");
}
if (localName == null) {
            throw new NullPointerException("local name");
}
int pointer = getPointer();
if (pointer == 0) {
//Synthetic comment -- @@ -129,7 +129,7 @@

public String getValue(String qName) {
if (qName == null) {
            throw new NullPointerException("qName");
}
int pointer = getPointer();
if (pointer == 0) {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xml/dom/NodeImpl.java b/luni/src/main/java/org/apache/harmony/xml/dom/NodeImpl.java
//Synthetic comment -- index 1f1293b..af4002f 100644

//Synthetic comment -- @@ -697,7 +697,7 @@

public final Object setUserData(String key, Object data, UserDataHandler handler) {
if (key == null) {
            throw new NullPointerException();
}
Map<String, UserData> map = document.getUserDataMap(this);
UserData previous = data == null
//Synthetic comment -- @@ -708,7 +708,7 @@

public final Object getUserData(String key) {
if (key == null) {
            throw new NullPointerException();
}
Map<String, UserData> map = document.getUserDataMapForRead(this);
UserData userData = map.get(key);








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xml/parsers/DocumentBuilderFactoryImpl.java b/luni/src/main/java/org/apache/harmony/xml/parsers/DocumentBuilderFactoryImpl.java
//Synthetic comment -- index 8efaa30..debbb20 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
@Override
public boolean getFeature(String name) throws ParserConfigurationException {
if (name == null) {
            throw new NullPointerException();
}

if (NAMESPACES.equals(name)) {
//Synthetic comment -- @@ -90,7 +90,7 @@
public void setFeature(String name, boolean value)
throws ParserConfigurationException {
if (name == null) {
            throw new NullPointerException();
}

if (NAMESPACES.equals(name)) {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xml/parsers/SAXParserFactoryImpl.java b/luni/src/main/java/org/apache/harmony/xml/parsers/SAXParserFactoryImpl.java
//Synthetic comment -- index 08657b9..e2e3778 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
@Override
public boolean getFeature(String name) throws SAXNotRecognizedException {
if (name == null) {
            throw new NullPointerException();
}

if (!name.startsWith("http://xml.org/sax/features/")) {
//Synthetic comment -- @@ -86,7 +86,7 @@
@Override
public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
if (name == null) {
            throw new NullPointerException();
}

if (!name.startsWith("http://xml.org/sax/features/")) {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/FileClientSessionCache.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/FileClientSessionCache.java
//Synthetic comment -- index 6619d1d..0f1fe24 100644

//Synthetic comment -- @@ -121,7 +121,7 @@
*/
private static String fileName(String host, int port) {
if (host == null) {
                throw new NullPointerException("host");
}
return host + "." + port;
}
//Synthetic comment -- @@ -182,7 +182,7 @@
byte[] sessionData) {
String host = session.getPeerHost();
if (sessionData == null) {
                throw new NullPointerException("sessionData");
}

String name = fileName(host, session.getPeerPort());







