import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author grape
 * @date 2019-05-15
 */
public class MedianSortedArrays {
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
		int length1=0,length2=0;
		if(null != nums1 && nums1.length >0){
			length1 = nums1.length;
		}
		if(null != nums2 && nums2.length > 0){
			length2 = nums2.length;
		}
		int[] nums = new int[length1+length2];
		int i=0,j=0,z=0;
		int num1,num2;
		while(i<nums1.length || j<nums2.length){
			if(i<nums1.length){
				num1 = nums1[i];
			}else{
				num1 = Integer.MAX_VALUE;
			}
			if(j < nums2.length){
				num2 = nums2[j];
			}else{
				num2 = Integer.MAX_VALUE;
			}
			if(num1 <= num2){
				nums[z++] = nums1[i++];
			}else{
				nums[z++] = nums2[j++];
			}
		}
		if(nums.length % 2 == 0){
			return (nums[nums.length / 2-1]+nums[nums.length / 2])/2.0;
		}else{
			return nums[nums.length / 2]/1.0;
		}
	}

	/**
	 * 优化时间复杂度
	 * @param nums1
	 * @param nums2
	 * @return
	 */
	public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
		int length1=0,length2=0;
		if(null != nums1 && nums1.length >0){
			length1 = nums1.length;
		}
		if(null != nums2 && nums2.length > 0){
			length2 = nums2.length;
		}
		boolean even = (length1+length2) % 2 ==0 ? true :false;
		int[] nums = new int[(length1+length2)/2+1];
		int i=0,j=0,z=0;
		int num1,num2;
		while(i<nums1.length || j<nums2.length){
			if(i<nums1.length){
				num1 = nums1[i];
			}else{
				num1 = Integer.MAX_VALUE;
			}
			if(j < nums2.length){
				num2 = nums2[j];
			}else{
				num2 = Integer.MAX_VALUE;
			}
			if(num1 <= num2){
				nums[z++] = nums1[i++];
			}else{
				nums[z++] = nums2[j++];
			}
			if(z >= nums.length){
				break;
			}
		}
		if(even){
			return (nums[nums.length-2]+nums[nums.length-1]) * 0.5;
		}else{
			return nums[nums.length-1] * 1.0;
		}
	}

	@Test
	public void testFindMedianSortedArrays(){
		int[] nums1 = new int[] { 1, 3 };
		Assert.assertEquals(null,true,nums1.length % 2 == 0);
		int[] nums2 = {};
		assertEquals(null,2.0,findMedianSortedArrays(nums1,nums2));
		assertEquals(null,2.0,findMedianSortedArrays2(nums1,nums2));

		nums2 = new int[]{2};
		assertEquals(null,2.0,findMedianSortedArrays(nums1,nums2));
		assertEquals(null,2.0,findMedianSortedArrays2(nums1,nums2));

		nums1 = new int[] {1};
		nums2 = new int[]{};
		assertEquals(null,1.0,findMedianSortedArrays(nums1,nums2));
		assertEquals(null,1.0,findMedianSortedArrays2(nums1,nums2));

		nums1 = new int[]{1,2,3};
		nums2 = new int[]{3,5,6};
		assertEquals(null,3.0,findMedianSortedArrays(nums1,nums2));
		assertEquals(null,3.0,findMedianSortedArrays2(nums1,nums2));

		nums1 = new int[]{1,3,5};
		nums2 = new int[]{2,4,6};
		assertEquals(null,3.5,findMedianSortedArrays(nums1,nums2));
		assertEquals(null,3.5,findMedianSortedArrays2(nums1,nums2));
	}
}
