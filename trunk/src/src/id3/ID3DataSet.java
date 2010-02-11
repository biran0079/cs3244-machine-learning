package id3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import filter.Filter;

public class ID3DataSet implements Iterable<ID3Instance> {
	@SuppressWarnings("unchecked")
	private ArrayList<LinkedList<ID3Instance>> lbs;
	private int featureNum, labelNum;
	private int totalSize;

	public static ID3DataSet merge(ID3DataSet[] ds) {
		ID3DataSet res = new ID3DataSet(ds[0].featureNum, ds[0].labelNum);
		for (int i = 0; i < ds.length; i++) {
			for (int j = 0; j < ds[0].labelNum; j++) {
				for(ID3Instance x:ds[i].lbs.get(j)){
					res.add(x);
				}
			}
		}
		return res;
	}

	public int getFeatureNum() {
		return featureNum;
	}

	@SuppressWarnings("unchecked")
	public List<ID3Instance> getListLabeled(int i) {
		return lbs.get(i);
	}

	public ID3DataSet(int featureNum, int labelNum) {
		this.featureNum = featureNum;
		this.labelNum = labelNum;
		lbs = new ArrayList<LinkedList<ID3Instance>>();
		for (int i = 0; i < labelNum; i++)
			lbs.add(new LinkedList<ID3Instance>());
		totalSize = 0;
	}

	@SuppressWarnings("unchecked")
	public void add(ID3Instance x) {
		if (x.getFeatureNum() != this.featureNum) {
			try {
				throw new Exception("Feature Number does not match");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		lbs.get(x.getLabel()).add(x);
		totalSize++;
	}

	public int size() {
		return totalSize;
	}

	public boolean isEmpty() {
		return totalSize == 0;
	}

	public boolean allSameLabel() {
		int ct = 0;
		for (int i = 0; i < labelNum; i++) {
			if (lbs.get(i).size() != 0)
				ct++;
		}
		return ct == 1;
	}

	/**
	 * @param x
	 *            : A filter which determine which instance to keep or not
	 * @return return a DataSetPair, first DataSet contains data not to keep,
	 *         second DataSet contains data to keep
	 * 
	 * */
	public ID3DataSet[] split(Filter x) {
		ID3DataSet a = new ID3DataSet(this.featureNum, labelNum), b = new ID3DataSet(
				this.featureNum, labelNum);
		for (LinkedList<ID3Instance> l : lbs)
			for (ID3Instance tem : l) {
				if (!x.keep(tem)) {
					a.add(tem);
				} else {
					b.add(tem);
				}
			}
		ID3DataSet[] res = new ID3DataSet[2];
		res[0] = a;
		res[1] = b;
		return res;
	}

	public ID3DataSet[] split(int n) {
		ArrayList<ID3Instance> lst = new ArrayList<ID3Instance>();
		for (ID3Instance i : this) {
			lst.add(i);
		}
		Collections.shuffle(lst);
		ID3DataSet[] res = new ID3DataSet[n];
		for (int i = 0; i < n; i++)
			res[i] = new ID3DataSet(this.featureNum, this.labelNum);
		int idx = 0;
		for (ID3Instance i : lst) {
			res[idx].add(i);
			idx = (idx + 1) % n;
		}
		return res;
	}

	private double log2(double x) {
		if (x == 0)
			return 0;
		return Math.log(x) / Math.log(2.0);
	}

	public double getEntropy() {
		double res = 0, p;
		for (LinkedList<ID3Instance> l : lbs) {
			p = (double) l.size() / totalSize;
			res += -p * log2(p);
		}
		return res;
	}

	public double getEntropyOnFeature(int idx) {
		int[][] ct = new int[2][labelNum];
		int S0 = 0, S1 = 0, S;
		for (int i = 0; i < labelNum; i++) {
			for (ID3Instance j : lbs.get(i)) {
				if (j.getFeature(idx)) {
					ct[1][i]++;
					S1++;
				} else {
					ct[0][i]++;
					S0++;
				}
			}
		}
		S = S0 + S1;
		double e0 = 0, e1 = 0, p;
		for (int i = 0; i < labelNum; i++) {
			if (S0 == 0) {
				p = 0;
			} else {
				p = (double) ct[0][i] / S0;
			}
			e0 += -p * log2(p);
			if (S1 == 0) {
				p = 0;
			} else {
				p = (double) ct[1][i] / S1;
			}
			e1 += -p * log2(p);
		}
		double res = 0;
		res += (double) S0 / totalSize * e0 + (double) S1 / totalSize * e1;
		return res;
	}

	public int getMostFrequentLabel() {
		int res = 0, maxSize = lbs.get(0).size();
		for (int i = 1; i < labelNum; i++)
			if (lbs.get(i).size() > maxSize) {
				maxSize = lbs.get(i).size();
				res = i;
			}
		return res;
	}

	class DatasetIterator implements Iterator<ID3Instance> {

		Iterator<LinkedList<ID3Instance>> i;
		Iterator<ID3Instance> j;

		DatasetIterator() {
			i = lbs.iterator();
			if (i.hasNext()) {
				j = i.next().iterator();
			} else {
				try {
					throw new Exception("Label number not correct");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public boolean hasNext() {
			while (!j.hasNext() && i.hasNext())
				j = i.next().iterator();
			return j.hasNext();
		}

		@Override
		public ID3Instance next() {
			return j.next();
		}

		@Override
		public void remove() {
			j.remove();
		}

	}

	@Override
	public Iterator<ID3Instance> iterator() {
		return new DatasetIterator();
	}
}
