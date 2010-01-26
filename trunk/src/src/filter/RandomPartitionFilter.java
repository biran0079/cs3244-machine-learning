package filter;

import java.util.Random;

import id3.Instance;


public class RandomPartitionFilter implements Filter {

	double d;
	Random r=new Random();
	public RandomPartitionFilter(double d) {
		this.d=d;
	}

	@Override
	public boolean keep(Instance x) {
		return r.nextDouble()>d;
	}

}
