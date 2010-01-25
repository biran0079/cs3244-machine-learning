package functionbuilder;

import id3.DataSet;
import id3.Instance;

import java.util.Random;

public class BooleanFunctionBuilder {
	enum Operator {
		AND, OR, NOT
	}

	Random r = new Random();

	class Node {
		Operator op;
		Node[] children;
	}

	class Leaf extends Node {
		int idx;
	}

	private Node root;
	private int top,varNum;
	
	public BooleanFunctionBuilder(int varNum) {
		this.varNum=varNum;
		root = BuildRandom(varNum);
		top = 0;
		allocateIdx(root);
	}

	private void allocateIdx(Node cur) {
		if (cur instanceof Leaf) {
			((Leaf) cur).idx = top++;
		} else {
			for (int i = 0; i < cur.children.length; i++)
				allocateIdx(cur.children[i]);
		}
	}

	private Node BuildRandom(int n) {
		int t = r.nextInt();
		if (n == 1) {
			if (t % 2 == 0) {
				Node res = new Node();
				res.op = Operator.NOT;
				res.children = new Node[1];
				res.children[0] = new Leaf();
				return res;
			} else {
				return new Leaf();
			}
		} else {
			Node res = new Node();
			if (t % 2 == 0) {
				res.op = Operator.AND;
			} else {
				res.op = Operator.OR;
			}
			res.children = new Node[2];
			res.children[0] = BuildRandom(n / 2);
			res.children[1] = BuildRandom(n - n / 2);
			return res;
		}
	}

	boolean eval(Node cur, boolean[] l) {
		if (cur instanceof Leaf) {
			return l[((Leaf) cur).idx];
		} else {
			if (cur.op == Operator.NOT) {
				return !eval(cur.children[0], l);
			} else if (cur.op == Operator.OR) {
				return eval(cur.children[0], l) || eval(cur.children[1], l);
			} else if (cur.op == Operator.AND) {
				return eval(cur.children[0], l) && eval(cur.children[1], l);
			}
		}
		//System.out.println('!');
		return true; // never reached
	}

	boolean eval(boolean[] l) {
		return eval(root, l);
	}

	private void printExp(Node cur) {
		if (cur instanceof Leaf) {
			System.out.print((char)('A'+((Leaf) cur).idx));
		} else {
			if(cur.op==Operator.NOT){
				System.out.print("!(");
				printExp(cur.children[0]);
				System.out.print(")");
			}else{
				System.out.print("(");
				printExp(cur.children[0]);
				System.out.print(")");
				
				System.out.print(cur.op==Operator.AND?" && ":" || ");
				
				System.out.print("(");
				printExp(cur.children[1]);
				System.out.print(")");
			}
		}
	}

	public void printExp() {
		printExp(root);
	}
	public DataSet getDatSet(int num){
		if(varNum>30 || (1<<varNum)>num){
			return randomDataSet(num);
		}else{
			return completeDataSet();
		}
	}
	private DataSet randomDataSet(int num) {
		boolean[] l=new boolean[varNum];
		DataSet res=new DataSet(varNum);
		for(int i=0;i<num;i++){
			for(int j=0;j<varNum;j++)
				l[j]=r.nextBoolean();
			res.add(new Instance(l,eval(l)));
		}
		return res;
	}

	private DataSet completeDataSet() {
		boolean[] l=new boolean[varNum];
		DataSet res=new DataSet(varNum);
		for(int i=0;i<(1<<varNum);i++){
			for(int j=0;j<varNum;j++){
				if((i & (1<<j))!=0){
					l[j]=true;
				}else{
					l[j]=false;
				}
			}
			res.add(new Instance(l,eval(l)));
		}
		return res;
	}

	public static void main(String[] args) {
		BooleanFunctionBuilder tree = new BooleanFunctionBuilder(3);
		boolean[] l = new boolean[3];
		
	}
}
