/*Make private static field final.

Change-Id:I3f2862e19b4a35c47e52be2158bfb45ffa30ae35*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Date.java b/luni/src/main/java/java/util/Date.java
//Synthetic comment -- index 0eab8dc..4263041 100644

//Synthetic comment -- @@ -40,7 +40,7 @@
private static final long serialVersionUID = 7523967970034938905L;

// Used by parse()
    private static final int CREATION_YEAR = new Date().getYear();

private transient long milliseconds;

//Synthetic comment -- @@ -531,7 +531,7 @@
if (second == -1) {
second = 0;
}
            if (year < (CREATION_YEAR - 80)) {
year += 2000;
} else if (year < 100) {
year += 1900;







