package com.cms.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeException;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.xml.sax.SAXException;

/**
 * 条型码产生器工具类
 */
public class BarcodeGeneratorUtil {
	public static byte[] genBarcode(String code, int resolution) throws ConfigurationException, BarcodeException, SAXException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", resolution, BufferedImage.TYPE_BYTE_GRAY, true, 0);
	    StringBuilder sb = new StringBuilder();
	    BarcodeGenerator generator = BarcodeUtil.getInstance().createBarcodeGenerator(new DefaultConfigurationBuilder().buildFromFile(new File(sb.append(ResourceUtil.getBarcodeXmlRootPath()).append("code128.xml").toString())));
	    generator.generateBarcode(canvas, code);
	    canvas.finish();
	    return out.toByteArray();
	}
	
	public  static void main (String[] args){
		System.out.println(BarcodeGeneratorUtil.class.getClassLoader().getResource("").getPath());
	}
}
