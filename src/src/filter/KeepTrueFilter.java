package filter;

import id3.ID3Instance;

public class KeepTrueFilter implements Filter {
	private int idx;

	public KeepTrueFilter(int idx) {
		this.idx = idx;
	}

	@Override
	public boolean keep(ID3Instance x) {
		return x.getFeature(idx);
	}
}
