//<Beginning of snippet n. 0>

public static final int MAP_ITEM = USHORT + USHORT + UINT + UINT;

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

public static class Try {
    final int startAddress;
    final int instructionCount;
    int handlerOffset;

    Try(int startAddress, int instructionCount, int handlerOffset) {
        this.startAddress = startAddress;
        this.instructionCount = instructionCount;
        this.handlerOffset = handlerOffset;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public int getHandlerOffset() {
        return handlerOffset;
    }

    public void setHandlerOffset(int handlerOffset) {
        this.handlerOffset = handlerOffset;
    }
}

public static class CatchHandler {
    final int[] typeIndexes;
    final int[] addresses;
    final int catchAllAddress;

    public CatchHandler(int[] typeIndexes, int[] addresses, int catchAllAddress) {
        this.typeIndexes = typeIndexes;
        this.addresses = addresses;
        this.catchAllAddress = catchAllAddress;
    }

    public int[] getTypeIndexes() {
        return typeIndexes;
    }

    public int[] getAddresses() {
        return addresses;
    }
    
    public int getCatchAllAddress() {
        return catchAllAddress;
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

import com.android.dx.dex.DexFormat;
import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.merge.TypeList;
import com.android.dx.util.ByteInput;
import com.android.dx.util.ByteOutput;

int debugInfoOffset = readInt();
int instructionsSize = readInt();
short[] instructions = readShortArray(instructionsSize);
int triesSize = readUleb128();
if (triesSize < 0) throw new IllegalArgumentException("triesSize must be non-negative");
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
    if (catchHandlersSize < 0) throw new IllegalArgumentException("catchHandlersSize must be non-negative");
    catchHandlers = new Code.CatchHandler[catchHandlersSize];
    for (int i = 0; i < catchHandlersSize; i++) {
        int[] handlerDetails = readCatchHandler();
        catchHandlers[i] = new Code.CatchHandler(handlerDetails[0], handlerDetails[1], handlerDetails[2]);
    }
}

return new Code(registersSize, insSize, outsSize, debugInfoOffset, instructions,
        tries, catchHandlers);

private int[] readCatchHandler() {
    int size = readSleb128();
    int handlersCount = Math.abs(size);
    if (handlersCount < 0) throw new IllegalArgumentException("handlersCount must be non-negative");
    int[] typeIndexes = new int[handlersCount];
    int[] addresses = new int[handlersCount];

    for (int i = 0; i < handlersCount; i++) {
        addresses[i] = readUleb128();
    }

    int catchAllAddress = size <= 0 ? readUleb128() : -1;
    return new int[]{typeIndexes, addresses, catchAllAddress};
}

private ClassData readClassData() {
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>

codeOut.writeUnsignedShort(code.getOutsSize());

Code.Try[] tries = code.getTries();
codeOut.writeUnsignedShort(tries.length);

int debugInfoOffset = code.getDebugInfoOffset();
if (newInstructions.length % 2 == 1) {
    codeOut.writeShort((short) 0); // padding
}

for (Code.Try tryItem : tries) {
    codeOut.writeInt(tryItem.getStartAddress());
    codeOut.writeUnsignedShort(tryItem.getInstructionCount());
    tryItem.setHandlerOffset(tryItem.getHandlerOffset()); // Use setter to recalculate or set updated offset
}

Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
codeOut.writeUleb128(catchHandlers.length);
for (Code.CatchHandler catchHandler : catchHandlers) {
    transformEncodedCatchHandler(catchHandler, indexMap);
}

//<End of snippet n. 3>