package com.bili.pojo.enums;

public enum LikeType {

    LIKE_DYNAMIC((byte) 0),
    LIKE_COMMENT((byte) 1),
    LIKE_VIDEO((byte) 2);

    private final byte value;

    LikeType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static LikeType fromValue(byte value) {
        for (LikeType type : LikeType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for LikeType: " + value);
    }
}
