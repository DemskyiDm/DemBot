package org.BotSasSE.botComands.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileDownloader {


    // String fileName = session.getSurnamePhoto();
    // Шлях до папки, де ви хочете зберегти файл


    public static void sentFilePhoto(String fileUrl, String fileName, String photoYear, String photoMonth) {
        setSaveDirectory(photoYear, photoMonth);
        String saveDirectory = setSaveDirectory(photoYear, photoMonth);
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            String fileExtension = getFileExtension(fileUrl);
            File file = new File(saveDirectory + fileName + fileExtension);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileExtension(String fileName) {
        String fileExtension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            fileExtension = fileName.substring(dotIndex);
        }
        return fileExtension;
    }

    public static String setSaveDirectory(String photoYear, String photoMonth) {
        String saveDirectory = "C:/photo/" + photoYear + "/" + photoMonth + "/";
        try {
            Files.createDirectories(Paths.get(saveDirectory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return saveDirectory;
    }
}