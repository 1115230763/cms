package com.cms.utils;

/**
 * 演算法工具类
 */
public class AlgorithmUtil {
	
	/**
	 * 泡泡排序
	 * @param data 原始未排序资料
	 * @return
	 */
	public static int[] bubbleSort(int[] data){
		if(data != null && data.length > 0){
			int temp;
			for (int i = 0; i < data.length - 1; i++) {
				for (int j = 0; j < data.length - 1; j++) {
					if (data[j] > data[j + 1]) {// 如果后面那位数字小于前面那数字则交换
						temp = data[j + 1];
						data[j + 1] = data[j];
						data[j] = temp;
					}
				}
			}
		}
		return data;
	}
}
