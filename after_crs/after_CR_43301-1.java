/*Fix LocaleData.toString once and for all.

Change-Id:I207a3226470557ac26caba165ef35f5df6859273*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/LocaleData.java b/luni/src/main/java/libcore/icu/LocaleData.java
//Synthetic comment -- index 97d5d30..8ec2294 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import libcore.util.Objects;

/**
* Passes locale-specific from ICU native code to Java.
//Synthetic comment -- @@ -129,44 +130,7 @@
}

@Override public String toString() {
        return Objects.toString(this);
}

public String getDateFormat(int style) {








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/util/Objects.java b/luni/src/main/java/libcore/util/Objects.java
//Synthetic comment -- index 7817316..573b973 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package libcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public final class Objects {
private Objects() {}

//Synthetic comment -- @@ -29,4 +33,64 @@
public static int hashCode(Object o) {
return (o == null) ? 0 : o.hashCode();
}

    /**
     * Returns a string reporting the value of each declared field, via reflection.
     * Static and transient fields are automatically skipped. Produces output like
     * "SimpleClassName[integer=1234,string="hello",character='c',intArray=[1,2,3]]".
     */
    public static String toString(Object o) {
        Class<?> c = o.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append(c.getSimpleName()).append('[');
        int i = 0;
        for (Field f : c.getDeclaredFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) {
                continue;
            }
            f.setAccessible(true);
            try {
                Object value = f.get(o);

                if (i++ > 0) {
                    sb.append(',');
                }

                sb.append(f.getName());
                sb.append('=');

                if (value.getClass().isArray()) {
                    if (value.getClass() == boolean[].class) {
                        sb.append(Arrays.toString((boolean[]) value));
                    } else if (value.getClass() == byte[].class) {
                        sb.append(Arrays.toString((byte[]) value));
                    } else if (value.getClass() == char[].class) {
                        sb.append(Arrays.toString((char[]) value));
                    } else if (value.getClass() == double[].class) {
                        sb.append(Arrays.toString((double[]) value));
                    } else if (value.getClass() == float[].class) {
                        sb.append(Arrays.toString((float[]) value));
                    } else if (value.getClass() == int[].class) {
                        sb.append(Arrays.toString((int[]) value));
                    } else if (value.getClass() == long[].class) {
                        sb.append(Arrays.toString((long[]) value));
                    } else if (value.getClass() == short[].class) {
                        sb.append(Arrays.toString((short[]) value));
                    } else {
                        sb.append(Arrays.toString((Object[]) value));
                    }
                } else if (value.getClass() == Character.class) {
                    sb.append('\'').append(value).append('\'');
                } else if (value.getClass() == String.class) {
                    sb.append('"').append(value).append('"');
                } else {
                    sb.append(value);
                }
            } catch (IllegalAccessException unexpected) {
                throw new AssertionError(unexpected);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}







