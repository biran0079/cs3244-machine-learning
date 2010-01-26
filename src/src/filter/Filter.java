package filter;

import id3.Instance;

public interface Filter {
	public boolean keep(Instance x);
}
