Use a typed comparison of floating point field values rather than just a string comparison.
This addresses toolchain issues; seehttp://code.google.com/p/android/issues/detail?id=994












diff --git a/tools/apicheck/src/com/android/apicheck/FieldInfo.java b/tools/apicheck/src/com/android/apicheck/FieldInfo.java
index 9b467af..a962af5 100644

@@ -66,6 +66,40 @@
return parentQName + name();
}

    // Check the declared value with a typed comparison, not a string comparison,
    // to accommodate toolchains with different fp -> string conversions.
    public boolean valueEquals(FieldInfo other) {
        // Type mismatch means nonequal, as does a null/non-null mismatch
        if (!mType.equals(other.mType)
                || ((mValue == null) != (other.mValue == null))) {
            return false;
        }

        // Null values are considered equal
        if (mValue == null) {
            return true;
        }

        // Floating point gets an implementation-type comparison; all others just use the string
        // If float/double parse fails, fall back to string comparison -- it means that it's a
        // canonical droiddoc-generated constant expression that represents a NaN.
        try {
            if (mType.equals("float")) {
                float val = Float.parseFloat(mValue);
                float otherVal = Float.parseFloat(other.mValue);
                return (val == otherVal);
            } else if (mType.equals("double")) {
                double val = Double.parseDouble(mValue);
                double otherVal = Double.parseDouble(other.mValue);
                return (val == otherVal);
            }
        } catch (NumberFormatException e) {
            // fall through
        }
        
        return mValue.equals(other.mValue);
    }

public boolean isConsistent(FieldInfo fInfo) {
fInfo.mExistsInBoth = true;
mExistsInBoth = true;
@@ -75,8 +109,8 @@
"Field " + fInfo.qualifiedName() + " has changed type");
consistent = false;
}

      if (!this.valueEquals(fInfo)) {
Errors.error(Errors.CHANGED_VALUE, fInfo.position(),
"Field " + fInfo.qualifiedName() + " has changed value from "
+ mValue + " to " + fInfo.mValue);







