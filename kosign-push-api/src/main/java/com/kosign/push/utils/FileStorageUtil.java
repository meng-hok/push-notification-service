package com.kosign.push.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;


public class FileStorageUtil {

    private static String imagePath = "src/main/resources/static/images"; 
    // run java
    public static final String PUTP8FILEPATH = "kosign-push-api/src/main/resources/static/files";
    // public static final String GETP8FILEPATH = "kosign-push-api/src/main/resources/static/files/";
    // run Gradle
    // @Value("${pfile.path}")
    // public static String PUTP8FILEPATH = "/home/lms/push-file";
    // public static String PUTP8FILEPATH = "/home/kosignpush/push-file";
 
    
    public static String uploadImage(final MultipartFile files) throws Exception {
        
        final String fileName = UUID.randomUUID() + "." + files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf(".") +  1);
        Files.copy(files.getInputStream(), Paths.get(imagePath,fileName ));
       //Files.copy(files.getInputStream(), Paths.get(fileName));
        return fileName;
    }
    public static String uploadFile(final MultipartFile files) throws Exception {

        final Path currentRelativePath = Paths.get("/");
        final String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        final String extension = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf(".") +  1);

        String fileName = "";
        if (extension.equals("p8")) {
            fileName=  UUID.randomUUID() + "." + extension;
        }else{
            throw new Exception("Invalid File Extension");
        }
      
        Files.copy(files.getInputStream(), Paths.get(PUTP8FILEPATH ,fileName));
        return fileName;
    }
}