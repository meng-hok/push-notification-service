package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class FilenameUtils extends org.apache.commons.io.FilenameUtils {

    public static String getExtentionByContentType(String contentType) {
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType mimeType;
        try {
            mimeType = allTypes.forName(contentType);
            return mimeType.getExtension();
        } catch (MimeTypeException e) {
            AppLogManager.error(e);
        }

        return null;
    }


    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }

    public static void main(String[] args) throws MimeTypeException {

    }

}
