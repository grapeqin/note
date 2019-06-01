  
# LeetCode 2 : 两数相加  
  
## [题目描述](https://leetcode-cn.com/problems/add-two-numbers)  
  
   给定两个非空链表用来表示两个非负的整数。其中，它们各自的位数是按照逆序的方式存储的，
   并且它们的每个节点只能存储一位数字。如果我们将两个数相加起来，则会返回一个新的链表来表示它们的和，我们假设除了数字0之外，
   这两个数都不会以0开头。
  
**示例1：**  
  
 输入： （2 -> 4 -> 3) + (5 -> 6 -> 4)  

 返回： (7 -> 0 -> 8)  
 
 原因：342 + 465 = 807
  
## 题目分析  
  
### 1. 思路  
  
*  假设两个非空链表分别为L1和L2,链表按照逆序来存储这两个非负整数，其存储顺序与整数运算的顺序刚好一致。
分别取两个链表中的首位L1[i]、L2[i]累加得到sum即为个位运算结果，由于加法运算可能存在进位,最终得到的个位值为sum%10,进位值carry=sum/10;
然后取十位的L1[i]、L2[i]相加，再加上个位的进位carry即为十位运算结果sum，同理，最终得到的十位值为sum%10,进位值carry=sum%10;
依次类推，直到两个链表都遍历完成。

* 为了能结构化的编码，我们引入了一个哑节点，在链表遍历之前，将哑节点next指针指向个位运算结果，最后返回链表结果时，直接返回哑节点的next指针即可。

* 遍历结束之后，注意不要忘记carry > 0 的情况下需要在结果链表中追加进位Node，否则运算结果会与预期不符。

* 源码
  
```java
/**
 * 链表节点
 */
static class ListNode {
	int val;

	ListNode next;

	ListNode(int x){
		val = x;
	}
}
```

```java
/**
 * 两数相加
 * @author grape
 * @date 2019-05-31
 */
public class addTwoNumbers {
	
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		// 哑节点有助于后续的迭代逻辑结构化
		ListNode head = new ListNode(0);
		ListNode current = head;
		int x=0,y=0,carry=0,val;
		ListNode p = l1;
		ListNode q = l2;
		//两个链表一直迭代，直到两个链表迭代完成
		while(p != null || q != null){
			if(p!=null){
				x = p.val;
			}
			if(q != null){
				y = q.val;
			}
			val = (x+y+carry)%10;
			carry = (x+y+carry)/10;
			current.next = new ListNode(val);
			current = current.next;
			x = 0;
			y = 0;
			if(p!=null){
				p = p.next;
			}
			if(q!=null){
				q = q.next;
			}
		}
		if(carry > 0){
			current.next = new ListNode(carry);
		}
		return head.next;
	}
}
```

