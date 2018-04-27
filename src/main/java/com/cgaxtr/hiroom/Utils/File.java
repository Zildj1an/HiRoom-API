package com.cgaxtr.hiroom.Utils;

import javax.validation.constraints.NotNull;
import java.io.*;

public class File {

    public static String USER_PROFILE_PATH="d://uploads/profile/";
    public static String ADVERTISEMENT_PATH ="d://uploads/advertisement/";

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
