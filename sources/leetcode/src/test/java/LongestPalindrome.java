import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author grape
 * @date 2019-05-15
 */
public class LongestPalindrome {

	@Test
	public void testLongestPalindrome(){
		String str = "abc";
		Assert.assertEquals("c",longestPalindrome(str));
		Assert.assertEquals("c",longestPalindrome2(str));

		str = "";
		Assert.assertEquals("",longestPalindrome(str));
		Assert.assertEquals("",longestPalindrome2(str));

		str = "babad";
		Assert.assertEquals("aba",longestPalindrome(str));
		Assert.assertEquals("aba",longestPalindrome2(str));

		str = "cbbd";
		Assert.assertEquals("bb",longestPalindrome(str));
		Assert.assertEquals("bb",longestPalindrome2(str));
	}

	public String longestPalindrome(String s) {
		String longestPalindrome = "";
		if(checkIsPalindrome(s)){
			return s;
		}
		for(int i=0;i<s.length();i++){
			for(int j=s.length();j>=i;j--){
				if(checkIsPalindrome(s.substring(i,j))
						&& longestPalindrome.length() < j-i+1){
					longestPalindrome = s.substring(i,j);
				}
			}
		}
		return longestPalindrome;
	}

	public boolean checkIsPalindrome(String s){
		if(null==s || s.length() < 1){
			return false;
		}
		int i=0,j=s.length()-1;
		while(i<j){
			if(s.charAt(i++) != s.charAt(j--)){
				return false;
			}
		}
		return true;
	}


	public String longestPalindrome2(String s){
		if(null==s || s.length() < 1) return "";
		int start = 0;
		int end = 0;
		int odd = 0;
		int even = 0;
		int length = 0;
		for(int i=0;i<s.length();i++){
			odd = longestPalindromeGap(s,i,i);
			even = longestPalindromeGap(s,i,i+1);
			length = Math.max(odd,even);
			if(end-start < length){
				start = i-(length-1)/2;
				end = i+length/2;
			}
		}
		return s.substring(start,end+1);
	}

	/**
	 * 返回s[i]和s[j]为回文中心最长回文子串的长度
	 * @param s
	 * @param i
	 * @param j
	 * @return
	 */
	private int longestPalindromeGap(String s,int i,int j){
		int start = i;
		int end = j;
		while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)){
			start--;
			end++;
		}
		return end-start-1;
	}
}
