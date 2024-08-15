package com.agura.task.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
