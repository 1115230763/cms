package com.cms.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码 生成工具类
 * */
public class QrcodeGeneratorUtil {
	/**
	 * @param 用于生成二维码的文字（一般为编号）
	 * @return 二维码图片的字节流
	 * */
	public static byte[] getQrcode(String content) throws WriterException, IOException{ 
        int width = 300; // 图像宽度  
        int height = 300; // 图像高度  
        String format = "png";// 图像类型  
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
        BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream); 
        byte[] array= outputStream.toByteArray();
		return array;  
          
	}
	
}
