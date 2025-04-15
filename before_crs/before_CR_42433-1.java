/*Fix an ugly bug where try/catch offsets weren't being mapped properly.

In dex files, there are several places where one object refers
to another object by its position in the file. The dex merger
is generally very careful to adjust these mappings when combining
dex files.

Unfortunately one of these cases was broken. Each try_item refers
to a corresponding encoded_catch_handler by its byte offset in a
list. Most of the time this byte offset is the same in the input
dex file and the output dex file. But encoded_catch_handlers are
variable-length because they use a variable-length uleb128 encoding
to address the type_idx being caught. When dex files are merged,
some exception types may go from having a small index to having
a large index, increasing the number of bytes required to encode
that index. This breaks our ability to directly copy over offsets
as we were doing previously.

Bug:http://code.google.com/p/android/issues/detail?id=36491Change-Id:I3bdadf20899fdb5d4d074e69103b33c0404a31f8*/
//Synthetic comment -- diff --git a/dx/src/com/android/dx/dex/SizeOf.java b/dx/src/com/android/dx/dex/SizeOf.java
//Synthetic comment -- index 476f7bb..6ded782 100644

//Synthetic comment -- @@ -100,4 +100,11 @@
* offset uint
*/
public static final int MAP_ITEM = USHORT + USHORT + UINT + UINT;
}








//Synthetic comment -- diff --git a/dx/src/com/android/dx/io/Code.java b/dx/src/com/android/dx/io/Code.java
//Synthetic comment -- index ba95d1b..82da862 100644

//Synthetic comment -- @@ -67,12 +67,12 @@
public static class Try {
final int startAddress;
final int instructionCount;
        final int handlerOffset;

        Try(int startAddress, int instructionCount, int handlerOffset) {
this.startAddress = startAddress;
this.instructionCount = instructionCount;
            this.handlerOffset = handlerOffset;
}

public int getStartAddress() {
//Synthetic comment -- @@ -83,8 +83,12 @@
return instructionCount;
}

        public int getHandlerOffset() {
            return handlerOffset;
}
}

//Synthetic comment -- @@ -92,11 +96,13 @@
final int[] typeIndexes;
final int[] addresses;
final int catchAllAddress;

        public CatchHandler(int[] typeIndexes, int[] addresses, int catchAllAddress) {
this.typeIndexes = typeIndexes;
this.addresses = addresses;
this.catchAllAddress = catchAllAddress;
}

public int[] getTypeIndexes() {
//Synthetic comment -- @@ -110,5 +116,9 @@
public int getCatchAllAddress() {
return catchAllAddress;
}
}
}








//Synthetic comment -- diff --git a/dx/src/com/android/dx/io/DexBuffer.java b/dx/src/com/android/dx/io/DexBuffer.java
//Synthetic comment -- index 39e5858..9fbc78c 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.dx.dex.DexFormat;
import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.merge.TypeList;
import com.android.dx.util.ByteInput;
import com.android.dx.util.ByteOutput;
//Synthetic comment -- @@ -443,31 +445,64 @@
int debugInfoOffset = readInt();
int instructionsSize = readInt();
short[] instructions = readShortArray(instructionsSize);
            Code.Try[] tries = new Code.Try[triesSize];
            Code.CatchHandler[] catchHandlers = new Code.CatchHandler[0];
if (triesSize > 0) {
if (instructions.length % 2 == 1) {
readShort(); // padding
}

                for (int i = 0; i < triesSize; i++) {
                    int startAddress = readInt();
                    int instructionCount = readUnsignedShort();
                    int handlerOffset = readUnsignedShort();
                    tries[i] = new Code.Try(startAddress, instructionCount, handlerOffset);
                }

                int catchHandlersSize = readUleb128();
                catchHandlers = new Code.CatchHandler[catchHandlersSize];
                for (int i = 0; i < catchHandlersSize; i++) {
                    catchHandlers[i] = readCatchHandler();
                }
}
return new Code(registersSize, insSize, outsSize, debugInfoOffset, instructions,
tries, catchHandlers);
}

        private Code.CatchHandler readCatchHandler() {
int size = readSleb128();
int handlersCount = Math.abs(size);
int[] typeIndexes = new int[handlersCount];
//Synthetic comment -- @@ -477,7 +512,7 @@
addresses[i] = readUleb128();
}
int catchAllAddress = size <= 0 ? readUleb128() : -1;
            return new Code.CatchHandler(typeIndexes, addresses, catchAllAddress);
}

private ClassData readClassData() {
//Synthetic comment -- @@ -548,6 +583,14 @@
}
}

/**
* Writes 0x00 until the position is aligned to a multiple of 4.
*/








//Synthetic comment -- diff --git a/dx/src/com/android/dx/merge/DexMerger.java b/dx/src/com/android/dx/merge/DexMerger.java
//Synthetic comment -- index c48f436..87a59fc 100644

//Synthetic comment -- @@ -790,6 +790,7 @@
codeOut.writeUnsignedShort(code.getOutsSize());

Code.Try[] tries = code.getTries();
codeOut.writeUnsignedShort(tries.length);

int debugInfoOffset = code.getDebugInfoOffset();
//Synthetic comment -- @@ -812,16 +813,39 @@
if (newInstructions.length % 2 == 1) {
codeOut.writeShort((short) 0); // padding
}
            for (Code.Try tryItem : tries) {
                codeOut.writeInt(tryItem.getStartAddress());
                codeOut.writeUnsignedShort(tryItem.getInstructionCount());
                codeOut.writeUnsignedShort(tryItem.getHandlerOffset());
            }
            Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
            codeOut.writeUleb128(catchHandlers.length);
            for (Code.CatchHandler catchHandler : catchHandlers) {
                transformEncodedCatchHandler(catchHandler, indexMap);
            }
}
}








