/*Clarify use of InputType flags

Some developers were confused about how to use the inputType field
and were omitting the class type when setting variations.

There are places in the framework where it specifically checks for
a class and variation before it invokes the desired behavior.

For instance, in EditText when setting the input type to a visible
password, it specifically checks for this condition:
inputType == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)*/
//Synthetic comment -- diff --git a/core/java/android/text/InputType.java b/core/java/android/text/InputType.java
//Synthetic comment -- index d50684a..41d5b84 100644

//Synthetic comment -- @@ -20,7 +20,25 @@

/**
* Bit definitions for an integer defining the basic content type of text
 * held in an {@link Editable} object.
*/
public interface InputType {
/**







