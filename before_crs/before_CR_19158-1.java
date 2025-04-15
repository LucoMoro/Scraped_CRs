/*Optimize MediaThumbRequest RAM usage.

The MediaThumbRequest contains an BitmapFactory.Options instance
that is unused and a Random instance.

The BitmapFactory.Options instance can be removed and the Random
instance can be made static so that the object is shared among
the MediaThumbRequests. Since Random already is synchronized this
should mean no performance degrade and if the phone contains many
indexable objects the number of objects allocated goes down a lot.

Change-Id:I886948dc01baca9112cd345d939b63d3755ad235*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaThumbRequest.java b/src/com/android/providers/media/MediaThumbRequest.java
//Synthetic comment -- index 6fce3ef..1f30a3e 100644

//Synthetic comment -- @@ -69,8 +69,7 @@
State mState = State.WAIT;
long mMagic;

    private BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private final Random mRandom = new Random();

static Comparator<MediaThumbRequest> getComparator() {
return new Comparator<MediaThumbRequest>() {
//Synthetic comment -- @@ -218,7 +217,7 @@
if (data != null) {
// make a new magic number since things are out of sync
do {
                    magic = mRandom.nextLong();
} while (magic == 0);

miniThumbFile.saveMiniThumbToFile(data, mOrigId, magic);







