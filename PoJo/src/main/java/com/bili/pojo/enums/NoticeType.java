package com.bili.pojo.enums;

public enum NoticeType {

    DYNAMIC((byte) 0),
    COMMENT((byte) 1),
    LIKE((byte) 2),
    CHAT((byte) 3);

    private final byte value;

    NoticeType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static NoticeType fromValue(byte value) {
        for (NoticeType type : NoticeType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for NoticeType: " + value);
    }
}
