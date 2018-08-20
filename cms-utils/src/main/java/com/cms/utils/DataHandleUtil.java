package com.cms.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

/**
 * 资料处理工具类
 */
public class DataHandleUtil {
	public static void main(String[] aregs) throws UnsupportedEncodingException{
		String charset = "GBK";
		String source = "小帅文";
		String append = "爽爽";
		source = DataHandleUtil.dataFilled(DataHandleUtil.transcoding(source, charset), append, charset, 'x', 15, 1);
	    System.out.println(source + ",長度 = " + source.getBytes(charset).length);
	}
	
	/**
	 * 字串转码
	 * @param source 原始资料
	 * @param charset 要转换之编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String transcoding(String source, String charset) throws UnsupportedEncodingException{
		return new String(source.getBytes(charset), charset);
	}
	
	/**
	 * 以byte长度计算，将原始资料贴上指定资料之后，头或尾补满指定字元
	 * @param source 原始资料
	 * @param appendData 贴上指定资料
	 * @param charset 原始资料字串编码
	 * @param filledChar 指定补齐字元
	 * @param totalLength 以byte计算总长度
	 * @param option 0:补头 1:补尾
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String dataFilled(String source, String appendData, String charset, char filledChar, int totalLength, int option) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(source).append(appendData);
		return dataFilled(sb.toString(), charset, filledChar, totalLength, option);
	}
	
	/**
	 * 以byte长度计算，将原始资料之头或尾补满指定字元
	 * @param source 原始资料
	 * @param charset 原始资料字串编码
	 * @param filledChar 指定补齐字元
	 * @param totalLength 以byte计算总长度
	 * @param option 0:补头 1:补尾
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String dataFilled(String source, String charset, char filledChar, int totalLength, int option) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder(source);
		while(sb.toString().getBytes(charset).length < totalLength){
			switch (option) {
			case 0:
				sb.insert(0, filledChar);
				break;
			case 1:
				sb.append(filledChar);
				break;
			}
		}
		return sb.toString();
	}
	
	private static final String[] ASCII_TABLE = { " ", "!", "\"", "#", "$", "%", "&", "'", "(",
		")", "*", "+", ",", "-", ".", "/", "0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", "@",
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
		"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
		"Y", "Z", "[", "\\", "]", "^", "_", "`", "a", "b", "c", "d",
		"e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
		"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|",
		"}", "~" };
	private static final String[] BIG_5_TABLE = { "　", "！", "”", "＃", "＄", "％", "＆", "’", "（",
		"）", "＊", "＋", "，", "－", "‧", "／", "０", "１", "２", "３", "４",
		"５", "６", "７", "８", "９", "：", "；", "＜", "＝", "＞", "？", "＠",
		"Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ",
		"Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ",
		"Ｙ", "Ｚ", "〔", "＼", "〕", "︿", "＿", "｀", "ａ", "ｂ", "ｃ", "ｄ",
		"ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ", "ｏ", "ｐ",
		"ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ", "｛", "｜",
		"｝", "～" }; 

	/**
	 * 全型半型轉換
	 * @param originalStr 原始字串
	 * @param option 0: toHalf, 1:toFull
	 */
	public static String convertFullorHalf(String originalStr, int option) {
		if (!StringUtils.isEmpty(originalStr)) {
			for (int i = 0; i < ASCII_TABLE.length; i++) {
				switch (option) {
				case 0:
					originalStr = originalStr.replace(BIG_5_TABLE[i], ASCII_TABLE[i]);
					break;
				case 1:
					originalStr = originalStr.replace(ASCII_TABLE[i], BIG_5_TABLE[i]);
					break;
				}
			}
		}
		return originalStr;
	}
	
	public static Integer[] stringToIntegerArray(String data) {
		Integer[] sratusArray = new Integer[data.split(",").length];
		for(int i = 0 ; i < data.split(",").length ; i++){
			sratusArray[i] = Integer.parseInt(data.split(",")[i]);
		}
		return sratusArray;
	}
}
