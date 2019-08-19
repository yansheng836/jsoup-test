/**
 * @Title MoreInfoMovie.java
 * @Package xyz.yansheng.top250
 * @Description TODO
 * @author yansheng
 * @date 2019-08-19 08:44:20
 * @version v1.0
 */
package xyz.yansheng.top250;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-19 08:44:20
 * @version v1.0 
 */
public class MoreInfoMovie extends Movie {

//	 排名，电影名，电影的豆瓣页网址，(国家，放映年份)，平均评分，评价人数，引用（一句话评语）
//	 排名，电影名，(别名1，别名2，别名3，导演，主演，放映年份，制片国家，分类，海报地址)，平均评分，评价人数，电影的豆瓣页网址，引用（一句话评语）
	private String alias1;
	private String alias2;
	private String alias3;
	private String director;
	private String protagonist;
	private Integer year;
	private String country;
	private String category;
	// https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp
	// https://img3.doubanio.com/view/photo/raw/public/p480747492.jpg
	private String smallPosterUrl;
	private String bigPosterUrl;

	public String getSmallPosterUrl() {
		return smallPosterUrl;
	}

	public void setSmallPosterUrl(String smallPosterUrl) {
		this.smallPosterUrl = smallPosterUrl;
	}

	public String getBigPosterUrl() {
		return bigPosterUrl;
	}

	public void setBigPosterUrl(String bigPosterUrl) {
		this.bigPosterUrl = bigPosterUrl;
	}

	public String getAlias1() {
		return alias1;
	}

	public void setAlias1(String alias1) {
		this.alias1 = alias1;
	}

	public String getAlias2() {
		return alias2;
	}

	public void setAlias2(String alias2) {
		this.alias2 = alias2;
	}

	public String getAlias3() {
		return alias3;
	}

	public void setAlias3(String alias3) {
		this.alias3 = alias3;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getProtagonist() {
		return protagonist;
	}

	public void setProtagonist(String protagonist) {
		this.protagonist = protagonist;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "MoreInfoMovie [rank=" + super.rank + ", title=" + super.title + ",alias1=" + alias1
				+ ", alias2=" + alias2 + ", alias3=" + alias3 + ", director=" + director
				+ ", protagonist=" + protagonist + ", year=" + year + ", country=" + country
				+ ", category=" + category + ", ratingNum=" + super.ratingNum + ", ratingPeopleNum="
				+ super.ratingPeopleNum + ", quote=" + super.quote + ", url=" + super.url
				+ ", smallPosterUrl=" + smallPosterUrl + ", bigPosterUrl=" + bigPosterUrl + "]";
	}

	// 排名，电影名，别名1，别名2，别名3，导演，主演，放映年份，制片国家，分类，平均评分，评价人数，电影的豆瓣页网址，引用（一句话评语）
	// 
	@Override
	public String toStringForWriteToTxt() {
		return super.rank + "," + super.title + "," + alias1 + "," + alias2 + "," + alias3 + ","
				+ director + "," + protagonist + "," + year + "," + country + "," + category + ","
				+ super.ratingNum + "," + super.ratingPeopleNum + "," + super.quote + ","
				+ super.url + "," + smallPosterUrl + "," + bigPosterUrl + "\n";
	}

}
