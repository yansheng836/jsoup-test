/**
 * @Title TestSpritUrl.java
 * @Package xyz.yansheng.xiaohua2014
 * @Description TODO
 * @author yansheng
 * @date 2019-08-13 20:28:45
 * @version v1.0
 */
package xyz.yansheng.xiaohua2014;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-13 20:28:45
 * @version v1.0 
 */
public class TestSpritUrl {

	/**
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 20:28:45
	 * @Description 测试分割url后的情况
	 */
	public static void main(String[] args) {

		/*
		 * 1.http://www.xiaohuar.com/d/file/20140811101854125.jpg
		 * 
		 * 2.http://www.xiaohuar.com/d/file/20160315/7145bf59e8e27fbee002662d878fadd9.jpg
		 */
		String picUrl1 = "http://www.xiaohuar.com/d/file/20140811101854125.jpg";
		String picUrl2 = "http://www.xiaohuar.com/d/file/20160315/7145bf59e8e27fbee002662d878fadd9.jpg";

		String[] strings1 = picUrl1.split("/");
		String[] strings2 = picUrl2.split("/");
		
		System.out.println("strings1.length:"+strings1.length);
		System.out.println("strings2.length:"+strings2.length);
		
	}

}
