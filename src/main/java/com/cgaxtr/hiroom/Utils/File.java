package com.cgaxtr.hiroom.Utils;

import javax.validation.constraints.NotNull;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class File {

    //public static String USER_PROFILE_PATH="C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\api\\uploads\\profile\\";
    public static String USER_PROFILE_PATH="d://uploads/";
    public static String ADVERTISEMENT_PATH ="C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\api\\uploads\\advertisement\\";

    public static void save(InputStream uploadedInputStream, @NotNull String uploadedFileLocation) throws IOException{

        int read = 0;
        byte[] bytes = new byte[1024];

        OutputStream out = new FileOutputStream(new java.io.File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(bytes)) != -1)
        {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
  }
}
