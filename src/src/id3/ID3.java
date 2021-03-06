package id3;

import filter.KeepTrueFilter;
import filter.RandomPartitionFilter;
import functionbuilder.BooleanFunctionBuilder;

public class ID3 {
	private ID3DataSet trainingData;

	class ID3Node {
		int featureIdx;
		ID3Node[] children = new ID3Node[2];
	}

	class ID3Leaf extends ID3Node {
		int label;

		ID3Leaf(int l) {
			this.label = l;
		}
	}

	private ID3Node root;

	ID3(ID3DataSet s) {
		this.trainingData = s;
		this.root = buildTree(trainingData);
	}
	ID3(ID3DataSet[] ds) {
		this.trainingData = ID3DataSet.merge(ds);
		this.root = buildTree(trainingData);
	}
	private ID3Node buildTree(ID3DataSet s) {
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
		ID3DataSet[] p = s.split(new KeepTrueFilter(maxIdx));
		//System.out.println(p.first.size()+" "+p.second.size());
		res.children[0] = buildTree(p[0]);
		res.children[1] = buildTree(p[1]);
		return res;
	}

	public int classify(ID3Instance x) {
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

	public double test(ID3DataSet s) {
		int right = 0, wrong = 0;
		for (ID3Instance x:s) {
			if (classify(x)==x.getLabel())
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
