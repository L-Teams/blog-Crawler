package com.spider.util;

import java.io.Serializable;
import java.util.BitSet;

/**
 * 布隆过滤(判断url是否重复)
 * 当jvm和定时任务会将该对象序列化到本地
 * @author 孙洪亮
 *
 */
public class BloomFilter implements Serializable{
	private static final long serialVersionUID = -1603764300772258583L;
	// bitSet默认长度
	private int DEFAULT_LEN = 1 << 25;
	private BitSet[] bitSet = null;
	private int[] seeds = { 3, 5, 7, 11 ,31 };
	private BloomHash[] bh = null;
	
	{
		bh = new BloomHash[seeds.length];
		bitSet = new BitSet[seeds.length];
		for (int i = 0; i < seeds.length; i++) {
			bh[i] = new BloomHash(seeds[i]);
			bitSet[i] = new BitSet(DEFAULT_LEN);
		}
	}
	
	public boolean isExistUrl(String url) {
		boolean flg = true;
		for(int i = 0 ; i < bh.length ; i++){
			int hash = bh[i].bloomHash(url);
			if(!bitSet[i].get(hash)){
				bitSet[i].set(hash, true);
				flg = false;
			}
		}
		return flg;
	}
	
	private class BloomHash implements Serializable{
		private static final long serialVersionUID = 5641732620746914430L;
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
