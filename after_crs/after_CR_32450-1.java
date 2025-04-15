/*Adjust a variable type conversion.

Integer division may assign 0.0 to a float variable in
NameDistance.java. So change int/int to int/float to realize
automatic type conversion.

Change-Id:I1721eb25a8796ee6b5affa9300c7de1cdd8a1061*/




//Synthetic comment -- diff --git a/src/com/android/providers/contacts/NameDistance.java b/src/com/android/providers/contacts/NameDistance.java
//Synthetic comment -- index 46dded6..d685ba8 100644

//Synthetic comment -- @@ -149,7 +149,7 @@
}

float m = matches;
        float jaro = ((m / length1 + m / length2 + (m - (transpositions / 2f)) / m)) / 3;

if (jaro < WINKLER_BONUS_THRESHOLD) {
return jaro;







