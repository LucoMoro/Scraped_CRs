
//<Beginning of snippet n. 0>


throw new ExceedMessageSizeException("Not enough memory to turn image into part: " +
getUri());
}
PduPersister persister = PduPersister.getPduPersister(mContext);
this.mSize = part.getData().length;
Uri newUri = persister.persistPart(part, messageId);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


boolean isStatusMessage = updateMessageStatus(context, messageUri, pdu);

// Called on the UI thread so don't block.
            MessagingNotification.nonBlockingUpdateNewMessageIndicator(context,
                    true, isStatusMessage);
}
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>



mSrc = mPath.substring(mPath.lastIndexOf('/') + 1);

// Some MMSCs appear to have problems with filenames
// containing a space.  So just replace them with
// underscores in the name, which is typically not

part.setData(data);
part.setContentType(getContentType().getBytes());
        String src = getSrc();
        byte[] srcBytes = src.getBytes();
        part.setContentLocation(srcBytes);
        part.setFilename(srcBytes);
        int period = src.lastIndexOf(".");
        byte[] contentId = period != -1 ? src.substring(0, period).getBytes() : srcBytes;
        part.setContentId(contentId);

return part;
}

//<End of snippet n. 2>








