package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.service.FileService;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void write(String fileName, String text) throws IOException{
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));) {
            bw.write(text);
        }
    }
}
