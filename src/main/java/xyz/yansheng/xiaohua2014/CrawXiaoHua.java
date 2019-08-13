/**
 * @Title CrawXiaoHua.java
 * @Package xyz.yansheng.xiaohua2014
 * @Description TODO
 * @author yansheng
 * @date 2019-08-13 15:20:17
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

import xyz.yansheng.top250.Movie;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-13 15:20:17
 * @version v1.0 
 */
public class CrawXiaoHua {

	/**
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 15:20:17
	 * @Description 爬取2014的大学校花排行榜：http://www.xiaohuar.com/2014.html，数据来自校花网，可能有局限性。
	 * 	2019年的校花，但是没有排名，界面不友好：http://www.xiaohuar.com/2015.html
	 */
	public static void main(String[] args) {

		// 1.获取“一个校花”的数据
		// 测试方法：void crawlXiaoHua()
		//crawlXiaoHua();

		// 2.获取“整个页面的所有校花”的数据
		// 测试方法：void crawlAllXiaoHua()
		// crawlAllXiaoHua();

		// 3.下载图片
		String picUrl = "http://www.xiaohuar.com/p-1-1244.html";
		String name = "宁波大红鹰学院校花褚青";
		String dirPath = "./2-2014大学校花排行榜/";

		XiaoHua xiaoHua = new XiaoHua();
		xiaoHua.setPicUrl(picUrl);
		xiaoHua.setName(name);

		// downloadPic(dirPath, xiaoHua);

		// 4. 下载所有图片
		ArrayList<XiaoHua> xiaoHuas = crawlAllXiaoHua();
		downloadPics(dirPath, xiaoHuas);

		// 5. 将数据保存到TXT
		// void writeXiaoHuasToTxt(ArrayList<XiaoHua> xiaoHuas)
		writeXiaoHuasToTxt(xiaoHuas);

	}

	/**
	 * @Title crawlXiaoHua
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 16:23:05
	 * @Description 1.获取“一个校花”的数据
	 */
	public static void crawlXiaoHua() {
		// 1.获取网页
		final String url = "http://www.xiaohuar.com/2014.html";
		final String prefix = "http://www.xiaohuar.com";

		Document document = null;
		try {
			/*1.原方法：document = Jsoup.connect(url).get();会出现乱码问题！
			 *2.处理乱码问题：
			 * 使用方法：Jsoup.parse(InputStream in, String charsetName, String baseUri) 
			 * 示例：Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
			 */
			document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		XiaoHua xiaoHua = new XiaoHua();

		// 2.选择具体的校花的项，注意first方法，这里先只选取第一个进行测试
		Element itemElement = document.select("div.items").first();
		System.out.println("itemElement:" + itemElement.toString());

		// 3.XiaoHua(rank,personPageUrl,picUrl,voteNum,name)
		// 3.1 rank
		Element rankElement = itemElement.selectFirst("div.top-title");
		String rankString = rankElement.text();
		Integer rank = Integer.parseInt(rankString.substring(3, rankString.length()));
		xiaoHua.setRank(rank);

		// 3.2 personPageUrl
		Element personPageUrlElement = itemElement.selectFirst("a");
		String personPageUrl = personPageUrlElement.attr("href");
		xiaoHua.setPersonPageUrl(personPageUrl);

		// 3.3 picUrl
		Element picUrlElement = itemElement.selectFirst("img");
		// 这里需要拼接字符串
		String picUrl = prefix + picUrlElement.attr("src");
		xiaoHua.setPicUrl(picUrl);

		// 3.4 voteNum
		Element voteNumElement = itemElement.selectFirst("span");
		String voteNumString = voteNumElement.text();
		Integer voteNum = Integer.parseInt(voteNumString.substring(0, voteNumString.length() - 1));
		xiaoHua.setVoteNum(voteNum);

		// 3.5 name
		Element nameElement = itemElement.selectFirst("p a");
		String name = nameElement.text();
		xiaoHua.setName(name);

		System.out.println(xiaoHua.toString());
	}

	/**
	 * @Title crawlAllXiaoHua
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 16:22:42
	 * @Description 2.获取“整个页面的所有校花”的数据
	 * @return   
	 * ArrayList<XiaoHua> 校花列表
	 */
	public static ArrayList<XiaoHua> crawlAllXiaoHua() {
		// 1.获取网页
		final String url = "http://www.xiaohuar.com/2014.html";
		final String prefix = "http://www.xiaohuar.com";

		Document document = null;
		try {
			/*1.原方法：document = Jsoup.connect(url).get();会出现乱码问题！
			 *2.处理乱码问题：
			 * 使用方法：Jsoup.parse(InputStream in, String charsetName, String baseUri) 
			 * 示例：Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
			 */
			document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 好像是有120个，这里设置容量稍大一点
		ArrayList<XiaoHua> xiaoHuas = new ArrayList<XiaoHua>(130);

		// 2.选择具体的校花的项，选择所有项
		Elements itemElements = document.select("div.items");
		for (Element itemElement : itemElements) {
			XiaoHua xiaoHua = new XiaoHua();
			// 3.XiaoHua(rank,personPageUrl,picUrl,voteNum,name)
			// 3.1 rank
			Element rankElement = itemElement.selectFirst("div.top-title");
			String rankString = rankElement.text();
			Integer rank = Integer.parseInt(rankString.substring(3, rankString.length()));
			xiaoHua.setRank(rank);

			// 3.2 personPageUrl
			Element personPageUrlElement = itemElement.selectFirst("a");
			String personPageUrl = personPageUrlElement.attr("href");

			// 注意这里，有些网址是没有前缀的就像这样：/p-1-64.html,本应该是这样的：http://www.xiaohuar.com/p-1-1244.html
			// 需要做一个判断：如果没有前缀，就加上去
			if (!personPageUrl.contains(prefix)) {
				personPageUrl = prefix + personPageUrl;
			}
			xiaoHua.setPersonPageUrl(personPageUrl);

			// 3.3 picUrl
			Element picUrlElement = itemElement.selectFirst("img");
			// 这里需要拼接字符串
			String picUrl = picUrlElement.attr("src");
			// 注意这里，有些网址是没有前缀的就像这样：/p-1-64.html,本应该是这样的：http://www.xiaohuar.com/p-1-1244.html
			// 需要做一个判断：如果没有前缀，就加上去
			if (!picUrl.contains(prefix)) {
				picUrl = prefix + picUrl;
			}
			xiaoHua.setPicUrl(picUrl);

			// 3.4 voteNum
			Element voteNumElement = itemElement.selectFirst("span");
			String voteNumString = voteNumElement.text();
			Integer voteNum = Integer.parseInt(voteNumString.substring(0, voteNumString.length() - 1));
			xiaoHua.setVoteNum(voteNum);

			// 3.5 name
			Element nameElement = itemElement.selectFirst("p a");
			String name = nameElement.text();
			xiaoHua.setName(name);

			xiaoHuas.add(xiaoHua);
		}

//		for (XiaoHua xiaoHua : xiaoHuas) {
//			System.out.println(xiaoHua.toString());
//		}
		return xiaoHuas;

	}

	/**
	 * @Title mkdir
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-10 22:46:14
	 * @Description 创建文件夹
	 * @param dirPath 文件夹名
	 * @return   
	 * int 文件夹存在返回0，文件夹创建成功过返回1，文件夹创建失败过返回-1
	 */
	public static int mkdir(String dirPath) {

		int reslut = 0;

		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			if (dirFile.mkdirs()) {
				System.out.println("创建文件夹：" + dirPath + " 成功");
				reslut = 1;
			} else {
				System.err.println("创建文件夹：" + dirPath + " 失败");
				reslut = -1;
			}
		} else {
			System.out.println("文件夹：" + dirPath + " 已存在");
			reslut = 0;
		}
		return reslut;
	}

	/**
	 * @Title downloadPic
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 16:21:25
	 * @Description 下载一张图片，下载图片，用XiaoHua.name命名。
	 * @param dirPath  保存图片的父目录
	 * @param xiaoHua   一个校花的信息
	 */
	public static void downloadPic(String dirPath, XiaoHua xiaoHua) {

		// 获得图片超链接
		String picUrl = xiaoHua.getPicUrl();

		// 拼接图片输出名，如：1-宁波大红鹰学院校花褚青.jpg
		String rank = xiaoHua.getRank().toString();
		String picName = rank + "-" + xiaoHua.getName() + ".jpg";

		String outPicPath = dirPath + picName;
		File outFile = new File(outPicPath);
		// 如果图片已存在，则直接跳过下载该图片，因为没有必要再下载一次
		if (outFile.exists()) {
			System.out.println(" -图片：" + outPicPath + " 已存在，故不再下载。");
			return;
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

	/**
	 * @Title downloadPics
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 16:20:32
	 * @Description 循环下载所有图片
	 * @param dirPath 保存图片的父目录
	 * @param xiaoHuas 校花列表
	 */
	public static void downloadPics(String dirPath, ArrayList<XiaoHua> xiaoHuas) {
		// 保证文件夹存在
		mkdir(dirPath);
		// 循环下载所有图片
		for (XiaoHua xiaoHua : xiaoHuas) {
			downloadPic(dirPath, xiaoHua);
		}
	}

	/**
	 * @Title writeXiaoHuasToTxt
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-13 22:54:56
	 * @Description  5.为了简单起见，这里仅仅是将数据保存为txt文件，不保存到excel或者是数据库。
	 * @param xiaoHuas   校花列表
	 */
	public static void writeXiaoHuasToTxt(ArrayList<XiaoHua> xiaoHuas) {

		// 5.1 先将每个电影对象转化为字符串
		int size = xiaoHuas.size();
		ArrayList<String> xiaoHuasString = new ArrayList<String>(size);
		for (XiaoHua xiaoHua : xiaoHuas) {
			xiaoHuasString.add(xiaoHua.toStringForWriteToTxt());
		}

		// 5.2写字节流
		String txtPath = "./2-2014大学校花排行榜/0-2014大学校花排行榜.txt";
		try (FileOutputStream out = new FileOutputStream(txtPath);) {
			for (String string : xiaoHuasString) {
				out.write(string.getBytes());
			}
			System.out.println("保存：'" + txtPath + "' 成功！");
		} catch (IOException e) {
			System.out.println("保存：'" + txtPath + "' 时，发生异常！");
			e.printStackTrace();
		}
	}
}
