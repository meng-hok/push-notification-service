package com.kosign.push.commons;

import com.kosign.push.beans.BnkStatic;
import com.kosign.push.logging.AppLogManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils extends org.apache.commons.io.FileUtils {

    public static boolean isValidFilePath(String file) throws Exception {
        File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public static void main(String args[]) throws Exception {
        /*// true
        System.out.println(isValidFilePath("well.txt"));
        System.out.println(isValidFilePath("well well.txt"));
        System.out.println(isValidFilePath(""));

        //false
        System.out.println(isValidFilePath("test.T*T"));
        System.out.println(isValidFilePath("test|.TXT"));
        System.out.println(isValidFilePath("te?st.TXT"));
        System.out.println(isValidFilePath("con.TXT")); // windows
        System.out.println(isValidFilePath("prn.TXT")); // windows*/
        AppLogManager.info("test");
    }

    public static void downloadFileAsBinary(JspWriter out, HttpServletRequest request, HttpServletResponse response, URL url) throws Exception {
        File downloadFile = urlToFile(url);
        downloadFileAsBinary(out, request, response, downloadFile);
        //deleteQuietly(downloadFile);
    }

    public static File urlToFile(URL url) throws IOException {
        return urlToFile(url, UUID.randomUUID().toString(), null);
    }

    public static File urlToFile(URL url, String prefix, String suffix) throws IOException {
//        File file = new File(FilenameUtils.concat(BnkStatic.PATH_TO_NAS_ROOT, UUID.randomUUID().toString()));
        File file = createTempFile(prefix, suffix, new File(BnkStatic.PATH_TO_NAS_ROOT));
        copyURLToFile(url, file);
        return file;
    }
    

    /*
    public static void copyURLToTextFile(URL source, File destination, String encoding) throws IOException {
        InputStream input = source.openStream();

        try {
//            FileOutputStream output = openOutputStream(destination);
            copyInputStreamToFile(input, destination);

            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    */


    public static File createTempFile(File directory) throws IOException {
        String prefix = LocalDate.now().toString(DateTimeUtils.BNK_FORMATTER_DATE8) + "_";
        return createTempFile(prefix, null, directory);
    }

    public static File createTempFile() throws IOException {
        return createTempFile(new File(BnkStatic.PATH_TO_NAS_ROOT));
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix, new File(BnkStatic.PATH_TO_NAS_ROOT));
    }

    public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
        if(!directory.exists()) {
            forceMkdir(directory);
        }

        return File.createTempFile(prefix, suffix, directory);
    }

    public static File stringToFile(String str) throws IOException {
        File file = createTempFile();
        writeStringToFile(file, str, "utf-8");
        return file;
    }

    public static void downloadFileAsBinary(JspWriter out, HttpServletRequest request, HttpServletResponse response, URL url, String downloadFilename) throws Exception {
        File downloadFile = urlToFile(url);
        downloadFile(out, request, response, downloadFile, downloadFilename, "application/octet-stream");
        //deleteQuietly(downloadFile);
    }

    public static void downloadFile(JspWriter out, HttpServletRequest request, HttpServletResponse response, URL url) throws Exception {
        File downloadFile = urlToFile(url);
        downloadFile(out, request, response, downloadFile);
        //deleteQuietly(downloadFile);
    }

    public static void downloadFile(JspWriter out, HttpServletRequest request, HttpServletResponse response, URL url, String downloadFilename) throws Exception {
        File downloadFile = urlToFile(url);
        downloadFile(out, request, response, downloadFile, downloadFilename, null);
        //deleteQuietly(downloadFile);
    }

    public static void downloadFileAsBinary(JspWriter out, HttpServletRequest request, HttpServletResponse response, File downloadFile) throws Exception {
        downloadFileAsBinary(out, request, response, downloadFile, null);
    }

    public static void downloadFileAsBinary(JspWriter out, HttpServletRequest request, HttpServletResponse response, File downloadFile, String downloadFilename) throws Exception {
        downloadFile(out, request, response, downloadFile, downloadFilename, "application/octet-stream");
    }

    public static void downloadFile(JspWriter out, HttpServletRequest request, HttpServletResponse response, File downloadFile) throws Exception {
        downloadFile(out, request, response, downloadFile, null);
    }

    public static void downloadFile(JspWriter out, HttpServletRequest request, HttpServletResponse response, File downloadFile, String downloadFilename) throws Exception {
		downloadFile(out, request, response, downloadFile, downloadFilename, null);
    }

    public static void downloadFile(JspWriter out, HttpServletRequest request, HttpServletResponse response, File downloadFile, String downloadFilename, String mimeType) throws Exception {
    	try {
            downloadFilename = StringUtils.defaultIfBlank(downloadFilename, downloadFile.getName());

			FileInputStream inStream = new FileInputStream(downloadFile);

			/*
			// if you want to use a relative path to context root:
			String relativePath = getServletContext().getRealPath("");
			System.out.println("relativePath = " + relativePath);

			// obtains ServletContext
			ServletContext context = getServletContext();

			// gets MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			System.out.println("MIME type: " + mimeType);
			*/

			// modifies response
			if(StringUtils.isBlank(mimeType)) {
				mimeType = getMimeType(downloadFile);
			}
			
			response.reset();
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// forces download
			String headerKey = "Content-Disposition";
			String headerValue = MessageFormat.format("attachment; filename=\"{0}\"", downloadFilename);
			response.setHeader(headerKey, headerValue);
			
			// obtains response's output stream
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inStream.close();
			outStream.flush();
			outStream.close();

			if(out != null) {
			    out.clear();
            }
    	}
    	catch(Exception e) {
            AppLogManager.error(e);
            response.sendError(404);
        }
    	
    }

    private static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            AppLogManager.error(e);
        }
    }

    private static int doWaitFor(Process p) {

        int exitValue = -1;  // returned to caller when p is finished

        try {

            InputStream in  = p.getInputStream();
            InputStream err = p.getErrorStream();

            boolean finished = false; // Set to true when p is finished

            while( !finished) {
                try {

                    while( in.available() > 0) {

                        // Print the output of our system call
                        Character c = new Character( (char) in.read());
                        System.out.print( c);
                    }

                    while( err.available() > 0) {

                        // Print the output of our system call
                        Character c = new Character( (char) err.read());
                        System.out.print( c);
                    }

                    // Ask the process for its exitValue. If the process
                    // is not finished, an IllegalThreadStateException
                    // is thrown. If it is finished, we fall through and
                    // the variable finished is set to true.

                    exitValue = p.exitValue();
                    finished  = true;

                }
                catch (IllegalThreadStateException e) {

                    // Process is not finished yet;
                    // Sleep a little to save on CPU cycles
                    Thread.currentThread().sleep(500);
                }
            }


        }
        catch (Exception e) {

            // unexpected exception!  print it out for debugging...
            System.err.println( "doWaitFor(): unexpected exception - " + e.getMessage());
        }

        // return completion status to caller
        return exitValue;
    }


    public static File htmlToPdf(File htmlFile) throws Exception {
        try {
            // html -> pdf
            //String pathToPdf = FilenameUtils.concat(FilenameUtils.getFullPath(htmlFile.getAbsolutePath()), FilenameUtils.getBaseName(htmlFile.getName()) + ".pdf");
            String pathToPdf = FilenameUtils.concat(FilenameUtils.getFullPath(htmlFile.getAbsolutePath()), "pdf_" + LocalDateTime.now().toString(DateTimeUtils.BNK_FORMATTER_DTM14) + ".pdf");
            String command = "cat " + htmlFile.getAbsolutePath() + " | wkhtmltopdf --encoding utf-8 --no-stop-slow-scripts --javascript-delay 6000 - " + pathToPdf;

            String[] cmd = {
                    "/bin/sh",
                    "-c",
                    command
            };

            Process process = Runtime.getRuntime().exec(cmd);
            //process.waitFor();
            doWaitFor(process);

            // download pdf
            return new File(pathToPdf);
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }
        finally {
//            deleteQuietly(htmlFile);
        }

        return null;
    }

    public static File htmlToImage(File htmlFile) {
        File imageFile = null;
        File pdfFile = null;

        try {
            imageFile = createTempFile();
            pdfFile = htmlToPdf(htmlFile);

            PDDocument pd = PDDocument.load(pdfFile);
            PDFRenderer pr = new PDFRenderer(pd);
            BufferedImage bi = pr.renderImageWithDPI(0, 300);
            ImageIO.write(bi, "JPEG", imageFile);

            deleteQuietly(pdfFile);

            return renameExtension(imageFile, "jpg");
        }
        catch (Exception e) {
            AppLogManager.error(e);
        }
        finally {
            deleteQuietly(imageFile);
            deleteQuietly(pdfFile);
        }

        return null;
    }

    public static File renameExtension
            (File srcFile, String newExtension)
    {
        return renameExtension(srcFile.getAbsolutePath(), newExtension);
    }

    public static File renameExtension
            (String source, String newExtension)
    {
        String target;
        String currentExtension = FilenameUtils.getExtension(source);

        if (currentExtension.equals("")){
            target = source + "." + newExtension;
        }
        else {
            target = source.replaceFirst(Pattern.quote("." +
                    currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));

        }

        if(new File(source).renameTo(new File(target))) {
            return new File(target);
        }

        return null;
    }

    public static String getMimeType(File file) throws IOException {
        return Files.probeContentType(file.toPath());
    }



}