package com.gateway.server.plugin.rewrite;

import org.springframework.core.io.buffer.DataBuffer;

public interface MessageBodyEncoder {

    byte[] encode(DataBuffer original);

    String encodingType();

}
