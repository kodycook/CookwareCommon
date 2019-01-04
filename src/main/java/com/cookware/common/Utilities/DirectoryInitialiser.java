package com.cookware.common.Utilities;

import org.apache.log4j.Logger;
import com.cookware.common.Tools.DirectoryTools;

import java.io.*;
import java.nio.file.Paths;

public class DirectoryInitialiser {
    private static final Logger log = Logger.getLogger(DirectoryInitialiser.class);
    private final DirectoryTools directoryTools = new DirectoryTools();
    private final String rootDirectory;


    public DirectoryInitialiser(String mRootDir) {
        this.rootDirectory = mRootDir;
    }


    public void createDirectory(String directory) {
        directoryTools.createNewDirectory(Paths.get(this.rootDirectory, directory).toString());
    }


    public void copyResource(String resourceName, String directory){
        if(!directoryTools.checkIfPathExists(Paths.get(this.rootDirectory, directory, resourceName).toString())){
            log.info(String.format("Unpacking %s to %s", resourceName, Paths.get(this.rootDirectory, directory).toString()));
        unpackResourceFromJar(resourceName, Paths.get(this.rootDirectory, directory).toString());
        }
    }


    private String unpackResourceFromJar(String resourceName, String finalDirectory) {
        if(resourceName == null) {
            return null;
        }

        try {
            InputStream fileStream = getClass().getClassLoader().getResourceAsStream(resourceName);

            if (fileStream == null) {
                log.error("Failed to find configuration file in jar.");
                return null;
            }

            String[] chopped = resourceName.split("\\/");
            String fileName = chopped[chopped.length-1];

            File file = new File(Paths.get(finalDirectory, fileName).toString());

            OutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            fileStream.close();
            out.close();

            return file.getAbsolutePath();

        } catch (IOException e) {
            log.error("Failed to read in configuration file from jar.");
            e.printStackTrace();
            return null;
        }
    }
}
