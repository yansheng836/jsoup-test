/**
 * @Title Test.java
 * @Package xyz.yansheng.top250
 * @Description TODO
 * @author yansheng
 * @date 2019-08-19 11:00:11
 * @version v1.0
 */
package xyz.yansheng.top250;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author yansheng
 * @date 2019-08-19 11:00:11
 * @version v1.0 
 */
public class TestAlias {

	/**
	 * @Title main
	 * @author yansheng
	 * @version v1.0
	 * @date 2019-08-19 11:00:11
	 * @Description test how to deal with the alias
	 */
	public static void main(String[] args) {

		String alias = "铁达尼号(港/台)/nihao";
		System.out.println("alias:" + alias.toString());
		String[] strings = null;
		String string1 = "";
		String string2 = "";
		String string3 = "";

		strings = alias.split("/");
		for (String string : strings) {
			System.out.println("string:" + string.toString());
		}

		if (alias.contains("(港/台)")) {
			string1 = strings[0];
			if (string1.contains("(港")) {
				string1 = string1 + strings[1];
			} else {
				string2 = strings[1];
				if (string2.contains("(港")) {
					string2 = string2 + strings[3];
				} else {
					string3 = strings[2];
					if (string3.contains("(港")) {
						string3 = string3 + strings[4];
					}
				}
			}
		} else {

		}

		System.out.println("string1:" + string1);
		System.out.println("string2:" + string2);
		System.out.println("string3:" + string3);

	}

}
