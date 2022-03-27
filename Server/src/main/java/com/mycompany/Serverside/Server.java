package com.mycompany.Serverside;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Server {
    String directory = "C:\\Users\\gaime\\OneDrive\\Documents\\AUI\\Spring 2022\\Advanced Distributed Programming Paradigms\\Homework 2\\GDrive";
    File dir = new File(directory);

    @GetMapping("/")
    public Hashtable<String, String> sync() throws IOException {
        File[] files = dir.listFiles();
        Hashtable<String, String> result = new Hashtable<String, String>();
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            for (File file : files) {
                String check = getFileChecksum(md5Digest, file);
                result.put(file.getName(), check);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/file")
    public byte[] SendFile( @RequestParam String fileName) throws IOException{
        List<File> files = Arrays.asList(dir.listFiles());
        if(fileName.length() == 0)
        throw new IOException("no file name provided");
        for (File file : files) {
            String filename = file.getName();
            if(filename.equals(fileName)){
                byte[] bytes = loadFile(file);
                return bytes;
            }
        }
        throw new IOException("could not find the wanted file");
    }

    @DeleteMapping("/{filename}")
    public void deleteFile(@PathVariable("filename") String filename) throws IOException{
        List<File> files = Arrays.asList(dir.listFiles());
        if(filename.length() == 0)
        throw new IOException("no file name provided");
        for (File file : files) {
            String filename1 = file.getName();
            if(filename1.equals(filename)){
                file.delete();
                return;
            }
        }
        throw new IOException("could not find the wanted file");
    }
    @PutMapping("/{filename}")
    public void AddFile(@PathVariable(value = "filename") String filename,@RequestParam byte[] data) throws IOException{
        File file = new File(dir, filename);
        file.delete();
        file.createNewFile();
        FileOutputStream fis = new FileOutputStream(file);
        fis.write(data);
        fis.close();
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        ;
        fis.close();

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
