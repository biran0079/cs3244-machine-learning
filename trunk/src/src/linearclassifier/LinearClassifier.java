package linearclassifier;

import java.util.Random;

public class LinearClassifier {

	private LCDataSet trainingSet, testingSet;
	private double[] w;
	private double ita = 0.005;
	private Random r;

	public LinearClassifier(LCDataSet d) {
		r = new Random();
		trainingSet = d;
		train();
	}

	public double eval(LCInstance x) {
		double res = 0;
		for (int i = 0; i < x.getFeatureNum(); i++)
			res += w[i] * x.getFeature(i);
		return res;
	}

	double test(LCDataSet d) {
		int ct = 0;
		for (LCInstance x : d) {
			if (x.getLabel() == this.classify(x))
				ct++;
		}
		return (double) ct / d.size();
	}

	public void train() {
		w = new double[trainingSet.getFeatureNum()];
		for (int i = 0; i < w.length; i++)
			w[i] = r.nextDouble() / 100;
		double[] dw = new double[w.length];
		for (int tm = 0; tm < 30; tm++) {
			for (LCInstance x : trainingSet) {
				for (int i = 0; i < w.length; i++) {
					double o = eval(x), t = x.getLabel();
					dw[i] = ita * x.getFeature(i) * (t - o);
				}
				for (int i = 0; i < w.length; i++) {
					w[i] += dw[i];
				}
			}
			double error = 0;
			for (LCInstance x : trainingSet) {
				double o = eval(x), t = x.getLabel();
				error += (o - t) * (o - t);
			}
			ita*=0.7;
			//System.out.println(error);
		}
	}

	public int classify(LCInstance x) {
		return eval(x) >= 0 ? 1 : 0;
	}

}
