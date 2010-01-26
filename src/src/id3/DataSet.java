package id3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import filter.Filter;

public class DataSet {
	private LinkedList<Instance> t, f;
	private int featureNum;

	public int getFeatureNum() {
		return featureNum;
	}

	public List<Instance> getTrueList() {
		return t;
	}

	public List<Instance> getFalseList() {
		return f;
	}

	public DataSet(int featureNum) {
		this.featureNum = featureNum;
		t = new LinkedList<Instance>();
		f = new LinkedList<Instance>();
	}

	public void add(Instance x) {
		if (x.getFeatureNum() != this.featureNum) {
			try {
				throw new Exception("Feature Number does not match");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (x.getLabel())
			t.add(x);
		else
			f.add(x);
	}

	public int size() {
		return t.size() + f.size();
	}

	public boolean isEmpty() {
		return t.isEmpty() && f.isEmpty();
	}

	public boolean allSameLabel() {
		return t.isEmpty() || f.isEmpty();
	}

	/**
	 * @param x
	 *            : A filter which determine which instance to keep or not
	 * @return return a DataSetPair, first DataSet contains data not to keep,
	 *         second DataSet contains data to keep
	 * 
	 * */
	public DataSetPair split(Filter x) {
		DataSet a = new DataSet(this.featureNum), b = new DataSet(
				this.featureNum);
		for (Instance tem : t) {
			if (!x.keep(tem)) {
				a.add(tem);
			} else {
				b.add(tem);
			}
		}
		for (Instance tem : f) {
			if (!x.keep(tem)) {
				a.add(tem);
			} else {
				b.add(tem);
			}
		}
		return new DataSetPair(a, b);
	}

	private double log2(double x) {
		if (x == 0)
			return 0;
		return Math.log(x) / Math.log(2.0);
	}

	public double getEntropy() {
		double p1 = (double) t.size() / this.size(), p2 = 1.0 - p1;
		return -p1 * log2(p1) - p2 * log2(p2);
	}

	public double getEntropyOnFeature(int i) {
		int tn1 = 0, fn1 = 0, tn0 = 0, fn0 = 0;
		double res = 0;
		for (Instance x : t)
			if (x.getFeature(i))
				tn1++;
			else
				tn0++;
		for (Instance x : f)
			if (x.getFeature(i))
				fn1++;
			else
				fn0++;
		double portion1 = (double) (tn0 + fn0) / this.size(), portion2 = 1 - portion1;
		if (tn0 + fn0 != 0) {
			double p1 = (double) tn0 / (tn0 + fn0), p2 = 1 - p1;
			double e1 = -p1 * log2(p1) - p2 * log2(p2);
			res += portion1 * e1;
		}
		if (tn1 + fn1 != 0) {
			double p1 = (double) tn1 / (tn1 + fn1);
			double p2 = 1 - p1;
			double e2 = -p1 * log2(p1) - p2 * log2(p2);
			res+=portion2 * e2;
		}
		return res;
	}

	public boolean getMostFrequentLabel() {
		return f.size() > t.size() ? false : true;
	}
}
