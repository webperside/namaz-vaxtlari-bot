package com.webperside.namazvaxtlaribot.service;

import java.io.IOException;

public interface FileService {

    void write(String fileName, String text) throws IOException;
}
