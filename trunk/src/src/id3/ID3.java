package id3;

import filter.KeepTrueFilter;
import filter.RandomPartitionFilter;
import functionbuilder.BooleanFunctionBuilder;

public class ID3 {
	private DataSet trainingData;

	class ID3Node {
		int featureIdx;
		ID3Node[] children = new ID3Node[2];
	}

	class ID3Leaf extends ID3Node {
		boolean label;

		ID3Leaf(boolean l) {
			this.label = l;
		}
	}

	private ID3Node root;

	ID3(DataSet s) {
		this.trainingData = s;
		this.root = buildTree(s);
	}

	private ID3Node buildTree(DataSet s) {
		if (s.isEmpty() || s.allSameLabel())
			return new ID3Leaf(s.getMostFrequentLabel());

		double entropy = s.getEntropy();
		int maxIdx = 0;
		double maxInfoGain = entropy - s.getEntropyOnFeature(0);
		for (int i = 1; i < s.getFeatureNum(); i++) {
			double t = entropy - s.getEntropyOnFeature(i);
			if (t > maxInfoGain) {
				maxInfoGain = t;
				maxIdx = i;
			}
		}
		ID3Node res = new ID3Node();
		res.featureIdx = maxIdx;
		DataSetPair p = s.split(new KeepTrueFilter(maxIdx));

		res.children[0] = buildTree(p.first);
		res.children[1] = buildTree(p.second);
		return res;
	}

	public boolean classify(Instance x) {
		ID3Node cur = root;
		while (!(cur instanceof ID3Leaf)) {
			if (x.getFeature(cur.featureIdx)) {
				cur = cur.children[1];
			} else {
				cur = cur.children[0];
			}
		}
		return ((ID3Leaf) cur).label;
	}

	public double test(DataSet s) {
		int right = 0, wrong = 0;
		for (Instance x : s.getFalseList()) {
			if (!classify(x))
				right++;
			else
				wrong++;
		}
		for (Instance x : s.getTrueList()) {
			if (classify(x))
				right++;
			else
				wrong++;
		}
		return (double) right / s.size();
	}

	private void printTree(ID3Node cur, int level) {
		for (int i = 0; i < level; i++)
			System.out.print("|");
		if (cur instanceof ID3Leaf) {
			System.out.println(((ID3Leaf) cur).label);
		} else {
			System.out.println(cur.featureIdx);
			printTree(cur.children[0], level + 1);
			printTree(cur.children[1], level + 1);
		}
	}

	public void printTree() {
		printTree(root, 0);
	}

	public static void main(String[] agrs) {
	
	}
}
