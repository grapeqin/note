import org.junit.Assert;
import org.junit.Test;

/**
 * 两数相加
 * @author grape
 * @date 2019-05-31
 */
public class addTwoNumbers {


	@Test
	public void addTwoNumbersTest(){
		ListNode l1 = new ListNode(2);
		ListNode node1 = new ListNode(4);
		ListNode node2 = new ListNode(3);
		node1.next =node2;
		l1.next = node1;

		ListNode l2 = new ListNode(5);
		node1 = new ListNode(6);
		node2 = new ListNode(4);
		node1.next = node2;
		l2.next = node1;

		ListNode res = addTwoNumbers(l1,l2);
		Assert.assertEquals(7,res.val);
		Assert.assertEquals(0,res.next.val);
		Assert.assertEquals(8,res.next.next.val);
	}

	/**
	 * 链表节点
	 */
	static class  ListNode {
		int val;

		ListNode next;

		ListNode(int x){
			val = x;
		}
	}

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
