package com.bili.web.mq.enums;

import com.bili.pojo.enums.LikeType;

public enum MqBaseType {

    INSERT((byte) 0),
    UPDATE((byte) 1),
    DELETE((byte) 2),
    OTHER((byte) 3);

    private final byte value;

        MqBaseType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static MqBaseType fromValue(byte value) {
        for (MqBaseType type : MqBaseType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for LikeType: " + value);
    }
}
