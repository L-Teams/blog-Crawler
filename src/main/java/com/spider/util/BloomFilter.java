package com.spider.util;

import java.util.BitSet;

/**
 * 布隆过滤(判断url是否重复)
 * 
 * @author 孙洪亮
 *
 */
public class BloomFilter {
	// bitSet默认长度
	private int DEFAULT_LEN = 1 << 30;
	private BitSet bitSet = new BitSet(DEFAULT_LEN);
	private int[] seeds = { 3, 5, 7, 11, 17, 21, 31 };
	private BloomHash[] bh = null;
	
	{
		bh = new BloomHash[seeds.length];
		for (int i = 0; i < seeds.length; i++) {
			bh[i] = new BloomHash(seeds[i]);
		}
	}
	
	public boolean isExistUrl(String url) {
		boolean flg = true;
		for(int i = 0 ; i < bh.length ; i++){
			int hash = bh[i].bloomHash(url);
			if(!bitSet.get(hash)){
				flg = false;
				break;
			}
		}
		return flg;
	}
	
	public BitSet getBiteSet(){
		return bitSet;
	}

	private class BloomHash {
		int sale;
		public BloomHash(int sale) {
			this.sale = sale;
		}
		public int bloomHash(String str) {
			if (str == null || "".equals(str.trim()))
				return 0;
			int h = 0;
			int length = str.length();
			if (h == 0 && length > 0) {
				char val[] = str.toCharArray();
				for (int i = 0; i < length; i++) {
					h = sale * h + val[i];
				}
			}
			return h & (DEFAULT_LEN - 1);
		}
	}
}
