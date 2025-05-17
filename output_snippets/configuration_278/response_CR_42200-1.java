//<Beginning of snippet n. 0>
import java.util.HashSet;
import java.util.Set;

public class DrmInfo {
    private static final Set<Integer> supportedTypes = new HashSet<>();

    public static void registerDrmType(int type) {
        supportedTypes.add(type);
    }

    public static boolean isValidType(int type) {
        return supportedTypes.contains(type);
    }
}

public class DrmInfoRequest {
    public static final int TYPE_REGISTRATION_INFO = 1;
    public static final int TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO = 2;

    static {
        DrmInfo.registerDrmType(TYPE_REGISTRATION_INFO);
        DrmInfo.registerDrmType(TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO);
    }
}

public static void testInvalidInfoTypes() throws Exception {
    Set<Integer> registeredTypes = new HashSet<>(DrmInfo.supportedTypes);
    for (int i = -1; i <= 10; i++) {
        if (!registeredTypes.contains(i)) {
            checkInvalidInfoType(i);
        }
    }
    for (int i = DrmInfoRequest.TYPE_RIGHTS_ACQUISITION_PROGRESS_INFO + 1; i < 100; i++) {
        if (!registeredTypes.contains(i)) {
            checkInvalidInfoType(i);
        }
    }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public class DrmInfoStatus {
    public static final int STATUS_OK = 0;
    public static final int STATUS_ERROR = -1;

    static {
        DrmInfo.registerDrmType(STATUS_OK);
        DrmInfo.registerDrmType(STATUS_ERROR);
    }
}

public static void testInvalidStatusCodes() throws Exception {
    Set<Integer> registeredTypes = new HashSet<>(DrmInfo.supportedTypes);
    for (int i = DrmInfoStatus.STATUS_ERROR - 1; i <= DrmInfoStatus.STATUS_OK + 1; i++) {
        if (!registeredTypes.contains(i)) {
            checkInvalidStatusCode(i);
        }
    }
//<End of snippet n. 1>