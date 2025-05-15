//<Beginning of snippet n. 0>


* offset uint
*/
public static final int MAP_ITEM = USHORT + USHORT + UINT + UINT;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


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
        return startAddress;
    }

    public int getHandlerOffset() {
        return handlerOffset;
    }
}

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

public int getCatchAllAddress() {
    return catchAllAddress;
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
Code.Try[] tries = new Code.Try[triesSize];
Code.CatchHandler[] catchHandlers = new Code.CatchHandler[0];

if (triesSize > 0) {
    if (instructions.length % 2 == 1) {
        readShort(); // padding
    }

    for (int i = 0; i < triesSize; i++) {
        int startAddress = readInt();
        int instructionCount = readUnsignedShort();
        int handlerOffset = readUnsignedShort(); // This offset needs to be recalculated later.
        tries[i] = new Code.Try(startAddress, instructionCount, handlerOffset);
    }

    int catchHandlersSize = readUleb128();
    catchHandlers = new Code.CatchHandler[catchHandlersSize];
    for (int i = 0; i < catchHandlersSize; i++) {
        catchHandlers[i] = readCatchHandler();
    }
}

return new Code(registersSize, insSize, outsSize, debugInfoOffset, instructions, tries, catchHandlers);

private Code.CatchHandler readCatchHandler() {
    int size = readSleb128();
    int handlersCount = Math.abs(size);
    int[] typeIndexes = new int[handlersCount];
    int[] addresses = new int[handlersCount];
    
    for (int i = 0; i < handlersCount; i++) {
        typeIndexes[i] = readUleb128();
        addresses[i] = readUleb128(); // Read addresses after typeIndexes.
    }

    int catchAllAddress = size <= 0 ? readUleb128() : -1;
    return new Code.CatchHandler(typeIndexes, addresses, catchAllAddress);
}

private ClassData readClassData() {
}
//</End of snippet n. 2>










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
    codeOut.writeUnsignedShort(tryItem.getHandlerOffset()); // Ensure offset is updated based on new merging logic.
}

Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
codeOut.writeUleb128(catchHandlers.length);
for (Code.CatchHandler catchHandler : catchHandlers) {
    transformEncodedCatchHandler(catchHandler, indexMap);
}
//</End of snippet n. 3>