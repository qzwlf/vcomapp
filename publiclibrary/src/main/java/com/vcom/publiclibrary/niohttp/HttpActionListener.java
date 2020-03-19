package com.vcom.publiclibrary.niohttp;

public interface HttpActionListener {
    NIOHttpServer.Status reciver(String str);
}
