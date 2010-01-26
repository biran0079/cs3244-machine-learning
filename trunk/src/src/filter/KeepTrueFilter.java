package filter;

import id3.Instance;

public class KeepTrueFilter implements Filter {
	private int idx;

	public KeepTrueFilter(int idx) {
		this.idx = idx;
	}

	@Override
	public boolean keep(Instance x) {
		return x.getFeature(idx);
	}
}
