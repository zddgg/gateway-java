package com.gateway.server.plugin.rewrite;

public interface MessageBodyDecoder {

	byte[] decode(byte[] encoded);

	String encodingType();

}
