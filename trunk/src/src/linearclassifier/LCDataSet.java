package linearclassifier;

import id3.ID3DataSet;
import id3.ID3Instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class LCDataSet implements Iterable<LCInstance> {
	ArrayList<LCInstance> lst = new ArrayList<LCInstance>();
	private int len, labelNum;

	LCDataSet(int l, int n) {
		this.len = l;
		this.labelNum = n;
	}


	public void add(LCInstance i) {
		if (i.getFeatureNum() != len)
			try {
				throw new Exception("feature number not match");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		lst.add(i);
	}

	public LCDataSet[] split(int n) {

		Collections.shuffle(lst);
		LCDataSet[] res = new LCDataSet[n];
		for (int i = 0; i < n; i++)
			res[i] = new LCDataSet(this.getFeatureNum(), this.labelNum);
		int idx = 0;
		for (LCInstance i : lst) {
			res[idx].add(i);
			idx = (idx + 1) % n;
		}
		return res;
	}

	public LCDataSet[] splitByLabel() {
		LCDataSet[] res = new LCDataSet[this.labelNum];
		for (int i = 0; i < this.labelNum; i++)
			res[i] = new LCDataSet(this.getFeatureNum(), this.labelNum);
		for (LCInstance i : lst) {
			res[i.getLabel()].add(i);
		}
		return res;
	}

	public int size() {
		return lst.size();
	}

	public int getFeatureNum() {
		return len;
	}

	@Override
	public Iterator<LCInstance> iterator() {
		return lst.iterator();
	}

}
