package com.segs.demo.service.impl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.segs.demo.model.User2;
import com.segs.demo.repository.User2Repository;
import com.segs.demo.service.FileService;

@Service
public class FileServiceImpl implements FileService{
    @Autowired 
    private User2Repository repository;

    @Override
    public boolean hasCsvFormat(MultipartFile file){
        String type = "text/csv";
        if(!type.equals(file.getContentType()))
            return false;
        return true;
    }
    @Override 
    public void processAndSaveData(MultipartFile file){
        try{
            List<User2> users = csvToUsers(file.getInputStream());
            repository.saveAll(users);
        }catch (IOException e){
            e.printStackTrace();
        }
        
    }

    public List<User2> csvToUsers(InputStream inputStream){
        try(BufferedReader fileReader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());){
                List<User2> users = new ArrayList<User2>();
                
                List<CSVRecord> records = csvParser.getRecords();
                for(CSVRecord csvRecord : records){
                    User2 user = new User2(
                        Long.parseLong(csvRecord.get("studentid")),
                        csvRecord.get("grade")
                    );
                    users.add(user);
                }
                return users;
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
