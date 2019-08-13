/**
 * @Title CrawlMovie.java
 * @Package xyz.yansheng.top250
 * @Description TODO
 * @author yansheng
 * @date 2019-08-12 15:47:06
 * @version v1.0
 */
package xyz.yansheng.top250;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.yansheng.xiaohua2014.XiaoHua;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-12 15:47:06
 * @version v1.0 
 */
public class CrawlMovie {

	/**
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-12 15:47:06
	 * @Description 爬取豆瓣电影top250：https://movie.douban.com/top250
	 */
	public static void main(String[] args) {

		// 1.获取“单页-一个电影”的数据
		// 测试方法：Movie crawlMovie()
		// crawlMovie();

		// 2.获取“单页-所有电影-25个”的数据
		// 测试方法：Movie crawlMovie()
		/*final String URL = "https://movie.douban.com/top250";
		ArrayList<Movie> movies2 = crawlMovies(URL);
		for (Movie movie : movies2) {
			System.out.println(movie.toString());
		}*/

		// 3.获取“所有页面-所有电影-25+10=250个”的数据
		// 测试方法：ArrayList<Movie> crawlAllMovies()
		ArrayList<Movie> movies3 = crawlAllMovies();
		for (Movie movie : movies3) {
			System.out.println(movie.toString());
		}

		// 4.为了简单起见，这里仅仅是将数据保存为txt文件，不保存到excel或者是数据库。
		// 测试方法：void writeMovie(ArrayList<Movie> movies)
		writeMoviesToTxt(movies3);
	}

	// 1.获取“单页-一个电影”的数据
	public static void crawlMovie() {

		// 1.获取网页
		final String picUrl = "https://movie.douban.com/top250";

		Document document = null;
		try {
			document = Jsoup.connect(picUrl).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		System.out.println("document:" + document);

		Movie movie = new Movie();

		// 2.选择具体的电影的项，注意first方法，这里先只选取第一个进行测试
		Element itemElement = document.select("ol li").first();
		//		System.out.println("item:" + item.toString());

		// 3.1电影排名
		Element rankElement = itemElement.selectFirst("em");
		String rankString = rankElement.text();
		System.out.println("rankString:" + rankString.toString());
		movie.setRank(new Integer(rankString));

		// 3.2电影网址
		Element urlElement = itemElement.select("div.hd a").first();
		String urlString = urlElement.attr("href");
		System.out.println("urlString:" + urlString.toString());
		movie.setUrl(urlString);

		// 3.3电影名
		Element titleElement = urlElement.select("span.title").first();
		String titleString = titleElement.text();
		System.out.println("titleString:" + titleString.toString());
		movie.setTitle(titleString);

		// 3.4评分
		Element ratingNumElement = itemElement.select("div.star span.rating_num").first();
		String ratingNumString = ratingNumElement.text();
		System.out.println("ratingNumString:" + ratingNumString.toString());
		movie.setRatingNum(new Double(ratingNumString));

		// 3.5评价人数
		Element ratingPeopleNumElement = itemElement.select("div.star span").last();
		String ratingPeopleNumString = ratingPeopleNumElement.text();
		System.out.println("ratingPeopleNumString:" + ratingPeopleNumString.toString());

		// 注意这里文本是：1539997人评价，我们需要选取其中人数，进行裁剪
		movie.setRatingPeopleNum(
				new Integer(ratingPeopleNumString.substring(0, ratingPeopleNumString.length() - 3)));

		// 3.6 一句话简评
		Element quoteElement = itemElement.select("p.quote span.inq").first();
		String quoteString = quoteElement.text();
		System.out.println("quoteString:" + quoteString.toString());
		movie.setQuote(quoteString);

		System.out.println(movie.toString());
	}

	// 2.获取“单页-所有电影-25*1=25个”的数据
	public static ArrayList<Movie> crawlMovies(String picUrl) {

		// 1.获取网页
		//final String URL = "https://movie.douban.com/top250";

		Document document = null;
		try {
			document = Jsoup.connect(picUrl).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		System.out.println("document:" + document);

		// 2.选择具体的电影的项，注意这里和上面第一个不同，这里是选取的所有电影的项
		Elements itemElement = document.select("ol li");

		ArrayList<Movie> movies = new ArrayList<Movie>(25);

		for (Element element : itemElement) {
			Movie movie = new Movie();
			// 3.1电影排名
			Element rankElement = element.selectFirst("em");
			String rankString = rankElement.text();
			movie.setRank(new Integer(rankString));

			// 3.2电影网址
			Element urlElement = element.select("div.hd a").first();
			String urlString = urlElement.attr("href");
			movie.setUrl(urlString);

			// 3.3电影名
			Element titleElement = urlElement.select("span.title").first();
			String titleString = titleElement.text();
			movie.setTitle(titleString);

			// 3.4评分
			Element ratingNumElement = element.select("div.star span.rating_num").first();
			String ratingNumString = ratingNumElement.text();
			movie.setRatingNum(new Double(ratingNumString));

			// 3.5评价人数
			Element ratingPeopleNumElement = element.select("div.star span").last();
			String ratingPeopleNumString = ratingPeopleNumElement.text();
			movie.setRatingPeopleNum(
					new Integer(ratingPeopleNumString.substring(0, ratingPeopleNumString.length() - 3)));

			// 3.6 一句话简评
			Element quoteElement = element.select("p.quote span.inq").first();
			// 注意：这里可能会没有简评，如125的《我不是药神》，字符串会为null，如果是null，置为空字符串，否则会出现NPE问题
			String quoteString = null;
			if (quoteElement == null) {
				quoteString = "";
			} else {
				quoteString = quoteElement.text();
			}
			movie.setQuote(quoteString);

			movies.add(movie);
		}

		return movies;
	}

	// 3.获取“所有页面-所有电影-25+10=250个”的数据
	public static ArrayList<Movie> crawlAllMovies() {

		ArrayList<Movie> movies = new ArrayList<Movie>(250);

		/*注意查看网址之间的特点，然后拼接字符串：
		 * 第1页. https://movie.douban.com/top250
		 * 2. https://movie.douban.com/top250?start=25&filter=
		 * 3. https://movie.douban.com/top250?start=50&filter=
		 * 
		 * 通过观察我们可以发现：（如果有怀疑直接到浏览器测试拼接的网址是否正确）
		 * （1）前面都是一样的：“https://movie.douban.com/top250”，
		 * （2）后面加一个查询串：“?start=25*(-1)”，其中i表示页数，根据意思我们可以知道其实是：指定指定该页是从排名第几的电影开始，
		 * 即如果是“?start=20”，那么该页第一个就是21。
		 * （3）按照（2）的意思，其实第一个页面就是：https://movie.douban.com/top250?start=0&filter=
		 * （4）而最后面的"&filter="，其实有没有都无所谓。
		 */
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
	public static void writeMoviesToTxt(ArrayList<Movie> movies) {

		// 4.1 先将每个电影对象转化为字符串
		int size = movies.size();
		ArrayList<String> moviesString = new ArrayList<String>(size);
		for (Movie movie : movies) {
			moviesString.add(movie.toStringForWriteToTxt());
		}

		// 4.2写字节流
		String txtPath = "./1-豆瓣电影top250/0-豆瓣电影top250.txt";
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
}
