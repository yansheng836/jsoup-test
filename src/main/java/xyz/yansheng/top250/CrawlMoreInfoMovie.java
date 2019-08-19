/**
 * @Title CrawlMoreInfoMovie.java
 * @Package xyz.yansheng.top250
 * @Description TODO
 * @author yansheng
 * @date 2019-08-19 09:14:16
 * @version v1.0
 */
package xyz.yansheng.top250;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
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
 * @date 2019-08-19 09:14:16
 * @version v1.0 
 */
public class CrawlMoreInfoMovie {

	/**
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-19 09:14:16
	 * @Description TODO
	 */
	public static void main(String[] args) {

		// 1.获取“单页-一个电影”的数据
		// 测试方法：Movie crawlMovie()
//		crawlMovie();

		// 2.单页25个数据
		String moviePageUrl = "https://movie.douban.com/top250";
		String dirPath = "./1-豆瓣电影top250/电影海报";
		ArrayList<MoreInfoMovie> movies1 = crawlMovies(moviePageUrl);
		movies1.forEach(movice -> {
			System.out.println(movice.getSmallPosterUrl());
		});

		downloadPic(dirPath, movies1);

		// 2.全部数据（250个）
//		ArrayList<MoreInfoMovie> movies3 = crawlAllMovies();
//		for (MoreInfoMovie movie : movies3) {
//			System.out.println(movie.toStringForWriteToTxt());
//		}
//
//		writeMoviesToTxt(movies3);

	}

	// 1.获取“单页-一个电影”的数据
	public static void crawlMovie() {

		// 1.获取网页
		final String moviePageUrl = "https://movie.douban.com/top250";

		Document document = null;
		try {
			//Jsoup.parse(new URL(url).openStream(), "GBK", url);
			document = Jsoup.parse(new URL(moviePageUrl).openStream(), "UTF-8", moviePageUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		System.out.println("document:" + document);

		MoreInfoMovie movie = new MoreInfoMovie();

		// 2.选择具体的电影的项，注意first方法，这里先只选取第一个进行测试
		Element itemElement = document.select("ol li").first();
		//		System.out.println("item:" + item.toString());
		// 排名，电影名，(别名1，别名2，别名3，导演，主演，放映年份，制片国家，分类)，平均评分，评价人数，电影的豆瓣页网址，引用（一句话评语）

		// 一、电影的图片元素
		Element picElement = itemElement.selectFirst("div.pic");

		// 3.1电影排名
		Element rankElement = picElement.selectFirst("em");
		String rankString = rankElement.text();
		System.out.println("rankString:" + rankString.toString());
		movie.setRank(new Integer(rankString));

		// 3.2电影网址
		Element urlElement = picElement.select("a").first();
		String urlString = urlElement.attr("href");
		System.out.println("urlString:" + urlString.toString());
		movie.setUrl(urlString);

		// 3.3电影海报小图
		Element smallPosterUrlElement = picElement.select("img").first();
		String smallPosterUrl = smallPosterUrlElement.attr("src");
		System.out.println("smallPosterUrl:" + smallPosterUrl.toString());
		movie.setSmallPosterUrl(smallPosterUrl);

		// 小图：https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp
		// 大图：https://img3.doubanio.com/view/photo/raw/public/p480747492.jpg

		// 3.4电影海报大图
		String smallPosterUrlString = new String(smallPosterUrl.toString());
		String bigPosterUrl = smallPosterUrlString.replace("s_ratio_poster", "raw");
		if (bigPosterUrl.contains(".webp")) {
			bigPosterUrl = bigPosterUrl.replace(".webp", ".jpg");
		}
		System.out.println("bigPosterUrl:" + bigPosterUrl.toString());
		movie.setBigPosterUrl(bigPosterUrl);

		// 二、电影的信息元素
		Element infoElement = itemElement.selectFirst("div.info");

		// 3.5电影名
		Elements titleElements = infoElement.select("div.hd span.title");
		String titleString = titleElements.get(0).text();
		System.out.println("titleString:" + titleString.toString());
		movie.setTitle(titleString);

		// 3.6电影别名alias1,2,3
		String alias1 = "";
		String alias2 = "";
		String alias3 = "";
		String aliasString = titleElements.get(1).text();
		String nbsp2 = "&nbsp;/&nbsp;/ ";
		// 一般第二个title都是本地语言，如果是中国的就没有第二个title，置为空字符串
		if (aliasString != null) {
			alias1 = aliasString.replace(nbsp2, "");
			alias1 = alias1.replace("/ ", "");
		} else {
			//alias1 = "";
		}
		Element aliasElement = infoElement.select("div.hd span.other").first();
		String aliasString2 = aliasElement.text().replace(" ", "");
		String[] alias = aliasString2.replace(nbsp2, "").split("/");

		alias2 = alias[1];
		alias3 = alias[2];

		System.out.println("alias1:" + alias1.toString());
		System.out.println("alias2:" + alias2.toString());
		System.out.println("alias3:" + alias3.toString());
		movie.setAlias1(alias1);
		movie.setAlias2(alias2);
		movie.setAlias3(alias3);

		// 3.7导演director,protagonist,year,country,category
//		<div class="bd">
//      <p class="">
//          导演: 弗兰克·德拉邦特 Frank Darabont &nbsp;&nbsp;&nbsp;主演: 蒂姆·罗宾斯 Tim Robbins /...
//          <br>
//          1994 &nbsp;/&nbsp;美国&nbsp;/&nbsp;犯罪 剧情
//      </p>
		Element pElement = infoElement.select("div.bd p").first();
		String pString = pElement.toString();
		pString = pString.replace("<p class=\"\">", "").replace("</p>", "");
		System.out.println("pString:" + pString);
		String[] p = pString.split("<br>");
		for (String string : p) {
			System.out.println("p string:" + string.toString());
		}

		String[] people = p[0].split("&nbsp;&nbsp;&nbsp;");
		for (String string : people) {
			System.out.println("people string:" + string.toString());
		}
		String director = people[0].replace(" 导演: ", "");
		String protagonist = people[1].replace("主演: ", "");
		if (protagonist.contains("/...")) {
			protagonist = protagonist.replace("/...", "");
		} else {
			protagonist = protagonist.replace("...", "");
		}
		String[] thing = p[1].split("&nbsp;/&nbsp;");
		for (String string : thing) {
			System.out.println("thing string:" + string.toString());
		}
		Integer year = Integer.parseInt(thing[0].replace(" ", ""));
		String country = thing[1];
		String category = thing[2];

		System.out.println("director:" + director.toString());
		System.out.println("protagonist:" + protagonist.toString());
		System.out.println("year:" + year.toString());
		System.out.println("country:" + country.toString());
		System.out.println("category:" + category.toString());
		movie.setDirector(director);
		movie.setProtagonist(protagonist);
		movie.setYear(year);
		movie.setCountry(country);
		movie.setCategory(category);

		// 3.8评分
		Element ratingNumElement = infoElement.select("div.star span.rating_num").first();
		String ratingNumString = ratingNumElement.text();
		System.out.println("ratingNumString:" + ratingNumString.toString());
		movie.setRatingNum(new Double(ratingNumString));

		// 3.8评价人数
		Element ratingPeopleNumElement = infoElement.select("div.star span").last();
		String ratingPeopleNumString = ratingPeopleNumElement.text();
		System.out.println("ratingPeopleNumString:" + ratingPeopleNumString.toString());

		// 注意这里文本是：1539997人评价，我们需要选取其中人数，进行裁剪
		movie.setRatingPeopleNum(new Integer(
				ratingPeopleNumString.substring(0, ratingPeopleNumString.length() - 3)));

		// 3.9 一句话简评
		Element quoteElement = infoElement.select("p.quote span.inq").first();
		String quoteString = quoteElement.text();
		System.out.println("quoteString:" + quoteString.toString());
		movie.setQuote(quoteString);

		System.out.println(movie.toString());
	}

	// 2.获取“单页-所有电影-25*1=25个”的数据
	public static ArrayList<MoreInfoMovie> crawlMovies(String moviePageUrl) {

		Document document = null;
		// 会出现：java.io.IOException: Mark invalid异常，好像是版本问题，要降到1.10.3
//			document = Jsoup.parse(new URL(moviePageUrl).openStream(), "UTF-8", moviePageUrl);
		try {
			document = Jsoup.connect(moviePageUrl).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 2.选择具体的电影的项，注意这里和上面第一个不同，这里是选取的所有电影的项
		Elements itemElements = document.select("ol li");

		ArrayList<MoreInfoMovie> movies = new ArrayList<MoreInfoMovie>(25);

		for (Element itemElement : itemElements) {
			MoreInfoMovie movie = new MoreInfoMovie();
			// 一、电影的图片元素
			Element picElement = itemElement.selectFirst("div.pic");

			// 3.1电影排名
			Element rankElement = picElement.selectFirst("em");
			String rankString = rankElement.text();
			movie.setRank(new Integer(rankString));

			// 3.2电影网址
			Element urlElement = picElement.select("a").first();
			String urlString = urlElement.attr("href");
			movie.setUrl(urlString);

			// 3.3电影海报小图
			Element smallPosterUrlElement = picElement.select("img").first();
			String smallPosterUrl = smallPosterUrlElement.attr("src");
			movie.setSmallPosterUrl(smallPosterUrl);

			// 小图：https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp
			// 大图：https://img3.doubanio.com/view/photo/raw/public/p480747492.jpg

			// 3.4电影海报大图
			String smallPosterUrlString = new String(smallPosterUrl.toString());
			String bigPosterUrl = smallPosterUrlString.replace("s_ratio_poster", "raw");
			if (bigPosterUrl.contains(".webp")) {
				bigPosterUrl = bigPosterUrl.replace(".webp", ".jpg");
			}
			movie.setBigPosterUrl(bigPosterUrl);

			// 二、电影的信息元素
			Element infoElement = itemElement.selectFirst("div.info");

			// 3.5电影名
			Elements titleElements = infoElement.select("div.hd span.title");
			String titleString = titleElements.get(0).text();
			movie.setTitle(titleString);

			// 3.6电影别名alias1,2,3
			String alias1 = "";
			String alias2 = "";
			String alias3 = "";

			String nbsp2 = "&nbsp;/&nbsp;/ ";
			if (titleElements.size() == 2) {
				String aliasString = titleElements.get(1).text();
				// 一般第二个title都是本地语言，如果是中国的就没有第二个title，置为空字符串
				alias1 = aliasString.replace(nbsp2, "");
				alias1 = alias1.replace("/ ", "");

				// 如果有逗号，替换为空格，因为后面对保存为CVS影响
				if (alias1.contains(",")) {
					alias1 = alias1.replace(",", " ");
				}

			} else {
				//alias1 = "";
			}

			Element aliasElement = infoElement.select("div.hd span.other").first();
			String aliasString2 = aliasElement.text().replace(" ", "");
//			System.out.println("aliasString2:" + aliasString2);
			aliasString2 = aliasString2.replace(nbsp2, "");
			if (aliasString2.contains(",")) {
				aliasString2 = aliasString2.replace(",", " ");
			}
			String[] alias = aliasString2.split("/");
			// 包含“(港/台)”的电影别名有问题，会被分割
			if (aliasString2.contains("(港/台)")) {
				alias2 = alias[0];
				if (alias2.contains("(港")) {
					alias2 = alias2 + alias[1];
				} else {
					alias3 = alias[1];
					if (alias3.contains("(港")) {
						alias3 = alias3 + alias[2];
					}
				}
			} else {
				alias2 = alias[1];
				if (alias.length == 3) {
					alias3 = alias[2];
				}
			}

//			System.out.println("alias1:" + alias1.toString());
//			System.out.println("alias2:" + alias2.toString());
//			System.out.println("alias3:" + alias3.toString());
			movie.setAlias1(alias1);
			movie.setAlias2(alias2);
			movie.setAlias3(alias3);

			// 3.7导演director,protagonist,year,country,category
			Element pElement = infoElement.select("div.bd p").first();
			String pString = pElement.toString();
			pString = pString.replace("<p class=\"\">", "").replace("</p>", "");
			String[] p = pString.split("<br>");

			String[] people = p[0].split("&nbsp;&nbsp;&nbsp;");

			String director = people[0].replace(" 导演: ", "");
			// 因为列表也的数据仅为一部分数据，过长会自动截断
			String protagonist = "";
			if (people.length == 2) {
				protagonist = people[1];
			}
			if (protagonist != null) {
				if (protagonist.contains("主演: ")) {
					protagonist = protagonist.replace("主演: ", "");
				} else if (protagonist.contains("主演:")) {
					protagonist = protagonist.replace("主演:", "");
				} else if (protagonist.contains("主演")) {
					protagonist = protagonist.replace("主演", "");
				} else if (protagonist.contains("主")) {
					protagonist = protagonist.replace("主", "");
				}

				if (protagonist.contains("/...")) {
					protagonist = protagonist.replace("/...", "");
				} else {
					protagonist = protagonist.replace("...", "");
				}
			}

			String[] thing = p[1].split("&nbsp;/&nbsp;");
			String yearString = thing[0].replace(" ", "");
			// 62:有重新上映时间时，以第一时间为准
			Integer year = Integer.parseInt(yearString.substring(0, 4));
			String country = thing[1];
			String category = thing[2];

//			System.out.println("director:" + director.toString());
//			System.out.println("protagonist:" + protagonist.toString());
//			System.out.println("year:" + year.toString());
//			System.out.println("country:" + country.toString());
//			System.out.println("category:" + category.toString());
			movie.setDirector(director);
			movie.setProtagonist(protagonist);
			movie.setYear(year);
			movie.setCountry(country);
			movie.setCategory(category);

			// 3.8评分
			Element ratingNumElement = infoElement.select("div.bd div.star span.rating_num")
					.first();
			String ratingNumString = ratingNumElement.text();
			movie.setRatingNum(new Double(ratingNumString));

			// 3.8评价人数
			Element ratingPeopleNumElement = infoElement.select("div.bd div.star span").last();
			String ratingPeopleNumString = ratingPeopleNumElement.text();

			// 注意这里文本是：1539997人评价，我们需要选取其中人数，进行裁剪
			movie.setRatingPeopleNum(new Integer(
					ratingPeopleNumString.substring(0, ratingPeopleNumString.length() - 3)));

			// 3.9 一句话简评
			Element quoteElement = infoElement.select("div.bd p.quote span.inq").first();
			// 注意：这里可能会没有简评，如125的《我不是药神》，字符串会为null，如果是null，置为空字符串，否则会出现NPE问题
			String quoteString = null;
			if (quoteElement == null) {
				quoteString = "";
			} else {
				quoteString = quoteElement.text();
			}
			movie.setQuote(quoteString);

//			System.out.println(movie.toString());
			movies.add(movie);
		}

		return movies;
	}

	// 3.获取“所有页面-所有电影-25+10=250个”的数据
	public static ArrayList<MoreInfoMovie> crawlAllMovies() {

		ArrayList<MoreInfoMovie> movies = new ArrayList<MoreInfoMovie>(250);

		String prefix = "https://movie.douban.com/top250";

		// 为了方便起见我先将网址拼接好，后面直接就可以用了
		ArrayList<String> urlList = new ArrayList<String>(10);
		for (int i = 0; i < 11; i++) {
			String url = prefix + "?start=" + new Integer(i * 25).toString() + "&filter=";
			urlList.add(url);
		}

		// 这里直接调用上面的方法2（找到每页的电影），将得到的（25个电影）集合添加到（250个电影）集合，通过循环遍历10个页面。
		for (String url : urlList) {
			movies.addAll(crawlMovies(url));
		}

		return movies;
	}

	// 4.为了简单起见，这里仅仅是将数据保存为txt文件，不保存到excel或者是数据库。
	public static void writeMoviesToTxt(ArrayList<MoreInfoMovie> movies) {

		// 4.1 先将每个电影对象转化为字符串
		int size = movies.size();
		ArrayList<String> moviesString = new ArrayList<String>(size);
		for (MoreInfoMovie movie : movies) {
			moviesString.add(movie.toStringForWriteToTxt());
		}

		// 4.2写字节流
		String txtPath = "./1-豆瓣电影top250/豆瓣电影top250-more-info.txt";
		try (FileOutputStream out = new FileOutputStream(txtPath);) {
			for (String string : moviesString) {
				out.write(string.getBytes());
			}
			System.out.println("保存：'" + txtPath + "' 成功！");
		} catch (IOException e) {
			System.out.println("保存：'" + txtPath + "' 时，发生异常！");
			e.printStackTrace();
		}
	}

	// 5.下载壁纸
	public static void downloadPic(String dirPath, ArrayList<MoreInfoMovie> movies) {

		// 获得图片超链接
		for (MoreInfoMovie movie : movies) {

			String picUrl = movie.getBigPosterUrl();
			String picName = "2019-08-19_"+movie.getTitle() + "海报.jpg";

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
//				Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,
//						new InetSocketAddress("58.218.200.223", 30385));
//				con = (HttpURLConnection) url.openConnection(proxy);
				con = (HttpURLConnection) url.openConnection();
//				con.setRequestProperty("User-Agent",
//						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
//								+ "Chrome/73.0.3683.86 Safari/537.36");
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
					System.err
							.println("图片链接(" + picUrl + ")无效！响应状态码为：" + responseCode + outPicPath);
					continue;
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
