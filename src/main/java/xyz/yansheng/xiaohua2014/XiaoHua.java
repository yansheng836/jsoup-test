/**
 * @Title XiaoHua.java
 * @Package xyz.yansheng.xiaohua2014
 * @Description TODO
 * @author yansheng
 * @date 2019-08-13 15:13:00
 * @version v1.0
 */
package xyz.yansheng.xiaohua2014;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-13 15:13:00
 * @version v1.0 
 */
public class XiaoHua {

	// 校花(排名,个人信息页，照片网址，投票数，学校-姓名)
	// XiaoHua(rank,personPageUrl,picUrl,voteNum,name)
	private Integer rank;
	private String personPageUrl;
	private String picUrl;
	private Integer voteNum;
	private String name;

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getPersonPageUrl() {
		return personPageUrl;
	}

	public void setPersonPageUrl(String personPageUrl) {
		this.personPageUrl = personPageUrl;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getVoteNum() {
		return voteNum;
	}

	public void setVoteNum(Integer voteNum) {
		this.voteNum = voteNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "XiaoHua [rank=" + rank + ", personPageUrl=" + personPageUrl + ", picUrl=" + picUrl
				+ ", voteNum=" + voteNum + ", name=" + name + "]";
	}

	public String toStringForWriteToTxt() {
		return rank + "," + personPageUrl + "," + picUrl + "," + voteNum + "," + name + "\n";
	}

}
