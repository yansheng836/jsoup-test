/**
 * @Title Movie.java
 * @Package xyz.yansheng.top250
 * @Description TODO
 * @author yansheng
 * @date 2019-08-12 15:34:16
 * @version v1.0
 */
package xyz.yansheng.top250;

/**
 * <p>Title: Movie</p>
 * <p>Description: 定义Movie类，用于保存电影的相关数据。包含：排名，电影名，电影的豆瓣页网址，(国家，放映年份)，平均评分，评价人数，引用（一句话评语）。</p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-12 15:34:16
 * @version v1.0 
 */
public class Movie {

	//	 排名，电影名，电影的豆瓣页网址，(国家，放映年份)，平均评分，评价人数，引用（一句话评语）

	protected Integer rank;
	protected String title;
	protected String url;
	protected Double ratingNum;
	protected Integer ratingPeopleNum;
	protected String quote;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getRatingNum() {
		return ratingNum;
	}

	public void setRatingNum(Double ratingNum) {
		this.ratingNum = ratingNum;
	}

	public Integer getRatingPeopleNum() {
		return ratingPeopleNum;
	}

	public void setRatingPeopleNum(Integer ratingPeopleNum) {
		this.ratingPeopleNum = ratingPeopleNum;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	@Override
	public String toString() {

		// 形式1.为了方便控制台打印
		return "Movie [rank=" + rank + ", title=" + title + ", url=" + url + ", ratingNum=" + ratingNum
				+ ", ratingPeopleNum=" + ratingPeopleNum + ", quote=" + quote + "]";

	}

	/*形式2.为了方便保存数据到本地txt
	 * 在将数据写到本地txt保存时，建议用下面这个格式，数据比较干净，有利用导入到数据库等。
	 */
	public String toStringForWriteToTxt() {
		return rank + "," + title + "," + url + "," + ratingNum + "," + ratingPeopleNum + "," + quote + "\n";
	}
}
