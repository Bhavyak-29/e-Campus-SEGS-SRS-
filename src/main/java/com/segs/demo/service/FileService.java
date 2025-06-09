package com.segs.demo.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.segs.demo.model.User2;

public interface FileService {
    boolean hasCsvFormat(MultipartFile file);
    void processAndSaveData(MultipartFile file);
    List<User2> csvToUsers(InputStream inputStream);
}
