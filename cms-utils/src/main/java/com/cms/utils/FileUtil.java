package com.cms.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 档案工具类
 */
public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	private final static int BUFFER = 1024;
	public static final String PREFIX = "inputStreamToFile";
    public static final String SUFFIX = ".tmp";

	/**
	 * 縮小圖片大小後上傳至指定檔案路徑下
	 * @param is
	 * @param filePath
	 * @throws IOException
	 */
	public static void reduceImg(InputStream is, String filePath, double scale) throws IOException {   
		BufferedImage tag = reduceImg(is, scale);
        FileOutputStream out = new FileOutputStream(filePath);   
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);   
        encoder.encode(tag);   
        out.close();   
	}  
	
	public static BufferedImage reduceImg(InputStream is, Double scale) throws IOException {   
        Image src = ImageIO.read(is); 
        int width = 0;
        int height = 0;
        if(scale == null){
        	width = src.getWidth(null);
            height = src.getHeight(null);
        }else{
        	width = (int)(src.getWidth(null) * scale);
            height = (int)(src.getHeight(null) * scale);
        	while(width > 150 || height > 100){
        		width = (int)(width * 0.95);
                height = (int)(height * 0.95);
        	}
        }
        
        BufferedImage tag= new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);   
        tag.getGraphics().drawImage(src.getScaledInstance(width, height,  Image.SCALE_SMOOTH), 0, 0,  null); 
        return tag;
	}  
	
	
	public static byte[] BufferedImageToByteArray(BufferedImage image) throws IOException {
		byte[] imageByte = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			imageByte = baos.toByteArray();
		} finally {
			if(baos != null){
				baos.flush();
				baos.close();
			}
		}
		return imageByte;
	}

	/**
	 * 取得檔案副檔名
	 * @param filename
	 * @return
	 */
	public static String getFilenameExtension(String filename){
	     return filename.substring(filename.lastIndexOf("."), filename.length());
	}
	
	/**
	 * 將檔案轉成byte陣列
	 * @param filename
	 * @param session
	 * @return
	 */
	public static byte[] fileToByteArray(String filePath){
		byte[] data = null;
		try {
			data = FileUtil.inputStreamToByteArray(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			logger.error(e);
		}
		return data;
	}
	
	/**
     * 將inputstream轉成File
     * @param is
     * @return
     * @throws Exception
     */
    public static File inputStreamToFile (InputStream is) throws Exception {
    	FileOutputStream out = null;
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try {
        	out = new FileOutputStream(tempFile);
            IOUtils.copy(is, out);
        } finally {
        	if(is != null){
        		is.close();
        	}
        	if(out != null){
        		out.close();
        	}
        }
        return tempFile;
    }
    
	/**
	 * 將inputstream轉成byte陣列
	 * @param filename
	 * @param session
	 * @return
	 */
	public static byte[] inputStreamToByteArray(InputStream inputStream){
		byte[] data = null;
		try {
			data = new byte[inputStream.available()]; 
			inputStream.read(data);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (Exception e) {
					logger.error(e);
				} 
			}
		}
		return data;
	}
	
	/**
	 * 複製檔案 
	 * @param strSourceFileName 指定檔案的全路徑名稱
	 * @param strDestDir 複製到指定資料夾
	 * @return
	 */
	public static boolean copyTo(String strSourceFileName, String strDestDir) {
		File fileSource = new File(strSourceFileName);
		File fileDest = new File(strDestDir);

		// 指定檔案不存在，或是為資料夾
		if (!fileSource.exists() || !fileSource.isFile()) {
			logger.debug("指定檔案[" + strSourceFileName + "],不存在或是資料夾!");
			return false;
		}

		// 複製到指定資料夾不存在
		if (!fileDest.isDirectory() || !fileDest.exists()) {
			if (!fileDest.mkdirs()) {
				logger.debug("指定複製之資料夾不存在!");
				return false;
			}
		}

		try {
			String strAbsFilename = strDestDir + File.separator + fileSource.getName();

			FileInputStream fileInput = new FileInputStream(strSourceFileName);
			FileOutputStream fileOutput = new FileOutputStream(strAbsFilename);

			logger.debug("開始複製檔案");

			int count = -1;
			long nWriteSize = 0;
			long nFileSize = fileSource.length();
			byte[] data = new byte[BUFFER];
			while (-1 != (count = fileInput.read(data, 0, BUFFER))) {
				fileOutput.write(data, 0, count);

				nWriteSize += count;
				long size = (nWriteSize * 100) / nFileSize;
				long t = nWriteSize;
				String msg = null;
				if (size <= 100 && size >= 0) {
					msg = "\r複製文件進度:   " + size + "%   \t" + "\t   已複製:   " + t;
					logger.debug(msg);
				} else if (size > 100) {
					msg = "\r複製文件進度:   " + 100 + "%   \t" + "\t   已複製:   " + t;
					logger.debug(msg);
				}
			}
			fileInput.close();
			fileOutput.close();
			logger.debug("檔案複製成功!");
			return true;

		} catch (Exception e) {
			logger.error(e);
			return false;
		}
	}

	/**
	 * 功 能: 删除指定的文件 参 数: 指定绝对路径的文件名 strFileName 返回值: 如果删除成功true否则false;
	 * 
	 * @param strFileName
	 * @return
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			logger.info(strFileName + "不存在!");
			return false;
		}

		return fileDelete.delete();
	}

	/**
	 * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName: 是指定的文件全路径名 strDestDir: 移动到指定的文件夹中 返回值: 如果成功true; 否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean moveFile(String strSourceFileName, String strDestDir) {
		if (copyTo(strSourceFileName, strDestDir))
			return delete(strSourceFileName);
		else
			return false;
	}

	/**
	 * 功 能: 创建文件夹 参 数: strDir 要创建的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean makeDir(String strDir) {
		File fileNew = new File(strDir);

		if (!fileNew.exists()) {
			return fileNew.mkdirs();
		} else {
			return true;
		}
	}

	/**
	 * 功 能: 删除文件夹 参 数: strDir 要删除的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean removeDir(String strDir) {
		File rmDir = new File(strDir);
		if (rmDir.isDirectory() && rmDir.exists()) {
			String[] fileList = rmDir.list();

			for (int i = 0; i < fileList.length; i++) {
				String subFile = strDir + File.separator + fileList[i];
				File tmp = new File(subFile);
				if (tmp.isFile())
					tmp.delete();
				else if (tmp.isDirectory())
					removeDir(subFile);
			}
			rmDir.delete();
		} else {
			return false;
		}
		return true;
	}
}
