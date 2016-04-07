package com.qzj.logistics.utils.print;

/**
 * 数组拼接工具类
 * @author zhrjian
 *
 */
public class ArrayUtils {
	/**
	 * 两个数组连接
	 * @param father 在前
	 * @param son 在后
	 * @return
	 */
	public static byte[] twoToOne(byte[] father,byte[] son){
		if(father!=null&&son!=null){ //两个数组都不为空
			byte[] all = new byte[father.length+son.length];
			for(int i=0;i<all.length;i++){
				if(i<father.length){
					all[i] = father[i];
				}else{
					all[i] = son[i-father.length];
				}
			}
			father = null;//释放掉
			return all;
		}else if(father==null&&son!=null){ //father为空son不为空
			return son;
		}else if(father!=null&&son==null){//father不为空son为空
			return father;
		}else{ //father和son全为空
			return null;
		}
	}
	
	/**
	 * 将byte类型变量加入数组
	 * @param father
	 * @param data
	 * @return
	 */
	public static byte[] twoToOne(byte[] father,byte data){
		if(father!=null){ //两个数组都不为空
			byte[] all = new byte[father.length+1];
			for(int i=0;i<all.length-1;i++){
					all[i] = father[i];
			}
			all[all.length-1] = data;
			father = null;//释放掉
			return all;
		}else{ //father为空son不为空
			return new byte[]{data};
		}
	}
	
}
