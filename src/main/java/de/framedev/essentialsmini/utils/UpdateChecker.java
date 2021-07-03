package de.framedev.essentialsmini.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.utils
 * ClassName UpdateChecker
 * Date: 04.04.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class UpdateChecker {

    public void download(String fileUrl, String fileName, String name) {
        File file = new File(fileName, name);
        if (!file.exists())
            file.getParentFile().mkdirs();
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            URL url = new URL(fileUrl);
            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(new File(fileName, name));
            final byte[] data = new byte[4096];
            int count;
            while ((count = in.read(data, 0, 4096)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (Exception e) {
            System.out.println("Updater tried to download the update, but was unsuccessful.");
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        new File("plugins/update/" + name).getParentFile().mkdirs();
        if (!file.renameTo(new File("plugins/update/" + name)))
            System.err.println("File cannot be renamed!");
    }
}
