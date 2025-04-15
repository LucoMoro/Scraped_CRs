/*Fix wrong behavior in Turkeish locale

getSqlStatementType uses toUpperCase() for comparing SQL statement.
In Turkish locale, "i" of "INS" returns i(U+0049), not I(U+0130).
Should be wary of the toUpperCase() overloads that don't take a Locale.
Just added simple parameter Locale.US for any complex locale environment.

Change-Id:I6c8da04ff4ce7148eaa1ce5b8d39af61447684aaSigned-off-by: Zaharang <zaharang@nemustech.com>*/
//Synthetic comment -- diff --git a/core/java/android/database/DatabaseUtils.java b/core/java/android/database/DatabaseUtils.java
//Synthetic comment -- index a10ca15..28c1db3 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import java.text.Collator;
import java.util.HashMap;
import java.util.Map;

/**
* Static utility methods for dealing with databases and {@link Cursor}s.
//Synthetic comment -- @@ -1318,7 +1319,7 @@
if (sql.length() < 3) {
return STATEMENT_OTHER;
}
        String prefixSql = sql.substring(0, 3).toUpperCase();
if (prefixSql.equals("SEL")) {
return STATEMENT_SELECT;
} else if (prefixSql.equals("INS") ||







