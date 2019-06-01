import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author grape
 * @date 2019-05-25
 */
public class TwoSum {

	/**
	 * 给定数组s和目标值t，返回数组中两数之和等于t的下标
	 * @param s
	 * @param t
	 * @return
	 */
	public int[] twoSum(int[] s,int t){
		Map<Integer,Integer> tMap = new HashMap<>();

		for(int i=0;i<s.length;i++){
			if(tMap.containsKey(t-s[i])){
				return new int[]{tMap.get(t - s[i]),i};
			}
			tMap.put(s[i],i);
		}

		throw new RuntimeException("数组s中不存在两数之和等于t的元素!");
	}

	@Test
	public void twoSumTest(){
		int[] s = new int[]{2,7,11,15};
		int t = 9;
		int[] result = twoSum(s,t);
		Assert.assertEquals(0,result[0]);
		Assert.assertEquals(1,result[1]);

		s = new int[]{2,7,9,2};
		t = 4;
		result = twoSum(s,t);
		Assert.assertEquals(0,result[0]);
		Assert.assertEquals(3,result[1]);
	}

	@Test
	public void twoSumNotFoundTest(){
		int[] s = new int[]{2,7,9,11};
		int t = 1;
		try {
			twoSum(s,t);
		}catch (RuntimeException e){
			Assert.assertEquals(e.getMessage(),"数组s中不存在两数之和等于t的元素!");
		}
	}
}
