package com.reports.dto;

import org.springframework.core.io.ByteArrayResource;

import java.util.UUID;

public class MessageDto {

    private String receiver;
    private String text;
    private byte[] byteFile;
    private UUID fileName;


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public byte[] getByteFile() {
        return byteFile;
    }

    public void setByteFile(byte[] byteFile) {
        this.byteFile = byteFile;
    }

    public UUID getFileName() {
        return fileName;
    }

    public void setFileName(UUID fileName) {
        this.fileName = fileName;
    }
}
