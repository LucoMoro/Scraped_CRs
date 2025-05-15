
//<Beginning of snippet n. 0>


* offset uint
*/
public static final int MAP_ITEM = USHORT + USHORT + UINT + UINT;

    /**
     * start_addr uint
     * insn_count ushort
     * handler_off ushort
     */
    public static final int TRY_ITEM = UINT + USHORT + USHORT;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public static class Try {
final int startAddress;
final int instructionCount;
        final int catchHandlerIndex;

        Try(int startAddress, int instructionCount, int catchHandlerIndex) {
this.startAddress = startAddress;
this.instructionCount = instructionCount;
            this.catchHandlerIndex = catchHandlerIndex;
}

public int getStartAddress() {
return instructionCount;
}

        /**
         * Returns this try's catch handler <strong>index</strong>. Note that
         * this is distinct from the its catch handler <strong>offset</strong>.
         */
        public int getCatchHandlerIndex() {
            return catchHandlerIndex;
}
}

final int[] typeIndexes;
final int[] addresses;
final int catchAllAddress;
        final int offset;

        public CatchHandler(int[] typeIndexes, int[] addresses, int catchAllAddress, int offset) {
this.typeIndexes = typeIndexes;
this.addresses = addresses;
this.catchAllAddress = catchAllAddress;
            this.offset = offset;
}

public int[] getTypeIndexes() {
public int getCatchAllAddress() {
return catchAllAddress;
}

        public int getOffset() {
            return offset;
        }
}
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.dx.dex.DexFormat;
import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.Code.Try;
import com.android.dx.merge.TypeList;
import com.android.dx.util.ByteInput;
import com.android.dx.util.ByteOutput;
int debugInfoOffset = readInt();
int instructionsSize = readInt();
short[] instructions = readShortArray(instructionsSize);
            Try[] tries;
            CatchHandler[] catchHandlers;
if (triesSize > 0) {
if (instructions.length % 2 == 1) {
readShort(); // padding
}

                /*
                 * We can't read the tries until we've read the catch handlers.
                 * Unfortunately they're in the opposite order in the dex file
                 * so we need to read them out-of-order.
                 */
                Section triesSection = open(position);
                skip(triesSize * SizeOf.TRY_ITEM);
                catchHandlers = readCatchHandlers();
                tries = triesSection.readTries(triesSize, catchHandlers);
            } else {
                tries = new Try[0];
                catchHandlers = new CatchHandler[0];
}
return new Code(registersSize, insSize, outsSize, debugInfoOffset, instructions,
tries, catchHandlers);
}

        private CatchHandler[] readCatchHandlers() {
            int baseOffset = position;
            int catchHandlersSize = readUleb128();
            CatchHandler[] result = new CatchHandler[catchHandlersSize];
            for (int i = 0; i < catchHandlersSize; i++) {
                int offset = position - baseOffset;
                result[i] = readCatchHandler(offset);
            }
            return result;
        }

        private Try[] readTries(int triesSize, CatchHandler[] catchHandlers) {
            Try[] result = new Try[triesSize];
            for (int i = 0; i < triesSize; i++) {
                int startAddress = readInt();
                int instructionCount = readUnsignedShort();
                int handlerOffset = readUnsignedShort();
                int catchHandlerIndex = findCatchHandlerIndex(catchHandlers, handlerOffset);
                result[i] = new Try(startAddress, instructionCount, catchHandlerIndex);
            }
            return result;
        }

        private int findCatchHandlerIndex(CatchHandler[] catchHandlers, int offset) {
            for (int i = 0; i < catchHandlers.length; i++) {
                CatchHandler catchHandler = catchHandlers[i];
                if (catchHandler.getOffset() == offset) {
                    return i;
                }
            }
            throw new IllegalArgumentException();
        }

        private CatchHandler readCatchHandler(int offset) {
int size = readSleb128();
int handlersCount = Math.abs(size);
int[] typeIndexes = new int[handlersCount];
addresses[i] = readUleb128();
}
int catchAllAddress = size <= 0 ? readUleb128() : -1;
            return new CatchHandler(typeIndexes, addresses, catchAllAddress, offset);
}

private ClassData readClassData() {
}
}

        public void skip(int count) {
            if (count < 0) {
                throw new IllegalArgumentException();
            }
            ensureCapacity(count);
            position += count;
        }

/**
* Writes 0x00 until the position is aligned to a multiple of 4.
*/

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


codeOut.writeUnsignedShort(code.getOutsSize());

Code.Try[] tries = code.getTries();
        Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
codeOut.writeUnsignedShort(tries.length);

int debugInfoOffset = code.getDebugInfoOffset();
if (newInstructions.length % 2 == 1) {
codeOut.writeShort((short) 0); // padding
}

            /*
             * We can't write the tries until we've written the catch handlers.
             * Unfortunately they're in the opposite order in the dex file so we
             * need to transform them out-of-order.
             */
            DexBuffer.Section triesSection = dexOut.open(codeOut.getPosition());
            codeOut.skip(tries.length * SizeOf.TRY_ITEM);
            int[] offsets = transformCatchHandlers(indexMap, catchHandlers);
            transformTries(triesSection, tries, offsets);
        }
    }

    /**
     * Writes the catch handlers to {@code codeOut} and returns their indices.
     */
    private int[] transformCatchHandlers(IndexMap indexMap, Code.CatchHandler[] catchHandlers) {
        int baseOffset = codeOut.getPosition();
        codeOut.writeUleb128(catchHandlers.length);
        int[] offsets = new int[catchHandlers.length];
        for (int i = 0; i < catchHandlers.length; i++) {
            offsets[i] = codeOut.getPosition() - baseOffset;
            transformEncodedCatchHandler(catchHandlers[i], indexMap);
        }
        return offsets;
    }

    private void transformTries(DexBuffer.Section out, Code.Try[] tries,
            int[] catchHandlerOffsets) {
        for (Code.Try tryItem : tries) {
            out.writeInt(tryItem.getStartAddress());
            out.writeUnsignedShort(tryItem.getInstructionCount());
            out.writeUnsignedShort(catchHandlerOffsets[tryItem.getCatchHandlerIndex()]);
}
}


//<End of snippet n. 3>








