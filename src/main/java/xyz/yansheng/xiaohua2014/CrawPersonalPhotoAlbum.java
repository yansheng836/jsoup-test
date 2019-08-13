/**
 * @Title CrawPersonalPhotoAlbum.java
 * @Package xyz.yansheng.xiaohua2014
 * @Description TODO
 * @author yansheng
 * @date 2019-08-13 19:16:51
 * @version v1.0
 */
package xyz.yansheng.xiaohua2014;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-13 19:16:51
 * @version v1.0 
 */
public class CrawPersonalPhotoAlbum {

	/**
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 19:16:51
	 * @Description 爬取个人相册（如果有的话），比如：http://www.xiaohuar.com/p-1-290.html
	 */
	public static void main(String[] args) {

		// 1.先尝试获取一个人相册的一张照片的信息   
		// 测试方法:public static void crawlPersonalPhotoAlbum()
//		crawlPersonalPhotoAlbum();

		// 2.获取一个的相册的照片的url   
		// 测试方法:public static ArrayList<String> crawlPersonalPhotoAlbums(String url)
//		String url2 = "http://www.xiaohuar.com/s-1-290.html";
//		ArrayList<String> urlStrings = crawlPersonalPhotoAlbums(url2);
//		for (String string : urlStrings) {
//			System.out.println(string);
//		}

		/*3.下载所有相册的照片   
		 * 转换url:
		 * 个人信息主页：http://www.xiaohuar.com/p-1-290.html
		 * 个人相册网址为：http://www.xiaohuar.com/s-1-290.html
		 */
		ArrayList<XiaoHua> xiaoHuas = CrawXiaoHua.crawlAllXiaoHua();
		for (XiaoHua xiaoHua : xiaoHuas) {
			// 第26个有问题，直接跳过
			if (xiaoHua.getRank() == 26 || xiaoHua.getRank() == 27) {
				continue;
			}
			String url = xiaoHua.getPersonPageUrl().replace("p-1", "s-1");

			ArrayList<String> urlList = crawlPersonalPhotoAlbums(url);
			// 判断该主页是否有相册，如果有就下载
			if (urlList != null) {
				// 创建文件夹
				String parentDirPath = "2-2014大学校花排行榜-个人相册//";
				String rank = xiaoHua.getRank().toString();
				String dirPath = parentDirPath + rank + "-" + xiaoHua.getName();
				CrawXiaoHua.mkdir(dirPath);
				downloadPic(dirPath, urlList);
			}
		}
	}

	/**
	 * @Title crawlPersonalPhotoAlbum
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 22:42:57
	 * @Description 先尝试获取一个人相册的一张照片的信息   
	 */
	public static void crawlPersonalPhotoAlbum() {

		// 1.获取网页 ：http://www.xiaohuar.com/s-1-290.html#p1
		final String url = "http://www.xiaohuar.com/s-1-290.html";
		final String prefix = "http://www.xiaohuar.com";

		Document document = null;
		try {
			document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 2.获得标题
		Element titleElement = document.select("title").first();

		String title = titleElement.text();
		title = title.substring(0, title.indexOf("写真"));
		System.out.println("title:" + title);

		// 3.选择具体的校花的项，注意first方法，这里先只选取第一个进行测试
		Element itemElement = document.select("ul.ad-thumb-list li").first();

		Element picElement = itemElement.selectFirst("a");
		String picUrl = picElement.attr("href");
		if (!picUrl.contains(prefix)) {
			picUrl = prefix + picUrl;
		}
		System.out.println("picUrl:" + picUrl);

	}

	/**
	 * @Title crawlPersonalPhotoAlbums
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 22:38:16
	 * @Description 获取相册照片的网址列表
	 * @param url 个人主页网址
	 * @return   
	 * ArrayList<String> 返回相册照片的网址列表
	 */
	public static ArrayList<String> crawlPersonalPhotoAlbums(String url) {

		ArrayList<String> urlList = new ArrayList<String>();

		// 1.获取网页 ：http://www.xiaohuar.com/s-1-290.html#p1
		//		final String url = "http://www.xiaohuar.com/s-1-290.html";
		final String prefix = "http://www.xiaohuar.com";

		Document document = null;
		try {
			document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
			if (document == null) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 2.获得标题
		Element titleElement = document.select("title").first();

		String title = titleElement.text();
		title = title.substring(0, title.indexOf("写真"));

		// 3.选择具体的校花的项，注意first方法，这里先只选取第一个进行测试
		Elements itemElements = document.select("ul.ad-thumb-list li");

		// 如果没有相册，直接返回null
		if (itemElements == null) {
			return null;
		}
		for (Element itemElement : itemElements) {
			Element picElement = itemElement.selectFirst("a");
			String picUrl = picElement.attr("href");

			/*这里需要判断超链接的方式：
			 * 1.绝对路径：http://www.xiaohuar.com/d/file/20140811101850174.jpg
			 * 2.相对路径：/d/file/20140811101850174.jpg
			 * 3.相对路径：href="../../d/file/20140811101850174.jpg"
			 */
			String path = "../..";
			if (picUrl.contains(path)) {
				picUrl = picUrl.replace(path, prefix);
			} else {
				if (!picUrl.contains(prefix)) {
					picUrl = prefix + picUrl;
				}
			}

			urlList.add(picUrl);
		}
		return urlList;
	}

	/**
	 * @Title downloadPic
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 22:39:37
	 * @Description 下载一个人的相册的图片。
	 * @param dirPath 保存图片的父目录
	 * @param urlList 相册照片的网址列表
	 */
	public static void downloadPic(String dirPath, ArrayList<String> urlList) {

		// 获得图片超链接
		for (String picUrl : urlList) {

			// 拼接图片输出名，假设需要21(17+4 .jpg/.png或者22=17+5 .jpeg)个字符，如：
			/*
			 * 1.http://www.xiaohuar.com/d/file/20140811101854125.jpg
			 * 
			 * 2.http://www.xiaohuar.com/d/file/20160315/7145bf59e8e27fbee002662d878fadd9.jpg
			 * 
			 * 3.http://www.xiaohuar.com/d/file/cdb948e6a8efca672388f641b4e271aa.jpg
			 */

			String picUrlCopy = picUrl;
			String picName = "";

			//@see TestSpritUrl.java
			String[] strings = picUrlCopy.split("/");
			String string5 = strings[5];
			int string5Length = string5.length();
			if (strings.length == 6) {
				// 第一种情况:5个/的,20140811101854125.jpg
				if (string5Length <= 22) {
					picName = string5;
				} else {
					// 第三种情况：
					picName = string5.substring(string5Length - 21);
				}
			} else if (strings.length == 7) {
				// 第二种情况:6个/的
				String string6 = strings[6];
				picName = string5 + "-" + string6.substring(string6.length() - 14);
			} else {
				System.err.println("URL出现新情况！");
			}

			String outPicPath = dirPath + "//" + picName;
			File outFile = new File(outPicPath);
			// 如果图片已存在，则直接跳过下载该图片，因为没有必要再下载一次
			if (outFile.exists()) {
				System.out.println(" -图片：" + outPicPath + " 已存在，故不再下载。");
				continue;
			}

			// 创建URL对象，将字符串解析为URL
			URL url = null;
			// 建立一个网络链接对象
			HttpURLConnection con = null;
			try {
				url = new URL(picUrl);
				con = (HttpURLConnection) url.openConnection();
				//设置请求方式
				con.setRequestMethod("GET");
				//连接
				con.connect();
				//得到响应码
				int responseCode = con.getResponseCode();
				// 这里假设只要不是4xx（请求错误）,5xx（服务器错误）都表示可以下载图片
				if (responseCode < 400) {
					// 响应成功，可以建立连接
				} else {
					System.err.println("图片链接(" + picUrl + ")无效！响应状态码为：" + responseCode);
					return;
				}
			} catch (MalformedURLException e2) {
				System.err.println("图片链接(" + picUrl + ")中不含有合法的网络协议或者无法解析该字符串！");
				e2.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// 利用jdk1.7的新特性 ：try(resource){……} catch{……}，自动释放资源
			// 1.创建输入输出流  2.建立一个网络链接
			try (InputStream inputStream = con.getInputStream();
					OutputStream outputStream = new FileOutputStream(outFile);) {
				int n = -1;
				byte b[] = new byte[1024];
				while ((n = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, n);
				}
				outputStream.flush();
				System.out.println(" --下载图片:" + picUrl + " 成功！保存位置为：" + outPicPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
