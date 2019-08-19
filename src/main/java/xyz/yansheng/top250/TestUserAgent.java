/**
 * @Title TestUserAgent.java
 * @Package xyz.yansheng.top250
 * @Description TODO
 * @author yansheng
 * @date 2019-08-19 16:01:12
 * @version v1.0
 */
package xyz.yansheng.top250;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-19 16:01:12
 * @version v1.0 
 */
public class TestUserAgent {

	/**
	 * @throws IOException 
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-19 16:01:12
	 * @Description TODO
	 */
	public static void main(String[] args) throws IOException {

//		Connection connect = Jsoup
//				.connect("https://img3.doubanio.com/view/photo/raw/public/p480747492.jpg");
//		Map<String, String> header = new HashMap<String, String>();
//		header.put("Host", "https://img3.doubanio.com");
//		header.put("User-Agent",
//				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
//						+ "Chrome/73.0.3683.86 Safari/537.36");
//		Connection data = connect.data(header);
//		Document document = data.get();
		String htmlString = Jsoup.connect("https://img3.doubanio.com/view/photo/raw/public/p2540924496.jpg")
				.proxy("58.218.200.223", 30385).header("Referer", "https://img3.doubanio.com/view/photo/raw/public/p2540924496.jpg")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
								+ "Chrome/73.0.3683.86 Safari/537.36")
				.timeout(10000).get().text();
		System.out.println("htmlString:" + htmlString);

	}

}
