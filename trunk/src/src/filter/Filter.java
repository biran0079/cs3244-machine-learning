package filter;

import id3.ID3Instance;

public interface Filter {
	public boolean keep(ID3Instance x);
}
