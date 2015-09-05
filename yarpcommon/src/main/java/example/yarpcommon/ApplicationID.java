package example.yarpcommon;

import java.util.UUID;

public enum ApplicationID {

    ERROR(0, "Error Application", UUID.fromString("4937a238-a850-4c0a-9dca-706531ab5058")),
    POC(1001, "POC", UUID.fromString("8590fbd6-7073-4743-be16-fb728140932c")),
    ICUB(1006, "ICUB", UUID.fromString("ee5c75d9-20a3-4cb1-b7db-18248721567e")),
    VIDEO_TRACKING(1007, "VideoTracking", UUID.fromString("41a161cc-0980-469a-8ebd-86b95c9b07ea"));

    private int value;
    private String displayName;

    private UUID uuid;

    private ApplicationID(int value, String displayName, UUID uuid) {
        this.value = value;
        this.displayName = displayName;
        this.uuid = uuid;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static ApplicationID getApplicationID(int id) {
        for (ApplicationID applicationID : values()) {
            if (applicationID.value == id) {
                return applicationID;
            }
        }

        return ERROR;
    }
}
