package id3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import filter.RandomPartitionFilter;
import functionbuilder.BooleanFunctionBuilder;

public class ID3Testing {
	public static DataSetPair RandomBooleanFunctionTesting() {
		int n = 80;
		boolean[] l = new boolean[n];
		BooleanFunctionBuilder func = new BooleanFunctionBuilder(n);
		DataSet data = func.getDatSet(1000);
		return data.split(new RandomPartitionFilter(0.66));
	}

	public static DataSetPair FileDataSetZeroTesting() throws IOException {
		DataSet data = new DataSet(256,2);
		FileReader fr = new FileReader("dataSetZero");
		BufferedReader in = new BufferedReader(fr);
		String s;
		while (in.ready()) {
			s = in.readLine();
			String[] res = s.split(",");
			boolean[] l = new boolean[256];
			for (int i = 0; i < 256; i++)
				l[i] = res[i].equals("1") ? true : false;
			data.add(new Instance(l, res[256].equals("1") ? 1 : 0));
		}
		return data.split(new RandomPartitionFilter(0.66));

	}
	
	public static DataSetPair FileSemeionTesting() throws IOException {
		DataSet data = new DataSet(256,10);
		FileReader fr = new FileReader("Semeion");
		BufferedReader in = new BufferedReader(fr);
		String s;
		while (in.ready()) {
			s = in.readLine();
			String[] res = s.split(",");
			boolean[] l = new boolean[256];
			for (int i = 0; i < 256; i++)
				l[i] = res[i].equals("1") ? true : false;
			data.add(new Instance(l, Integer.valueOf(res[256])));
		}
		return data.split(new RandomPartitionFilter(0.66));

	}

	public static DataSetPair FileMNISTTesting() throws IOException {
		DataSet data = new DataSet(784,10);
		FileReader fr = new FileReader("MNIST");
		BufferedReader in = new BufferedReader(fr);
		String s;
		while (in.ready()) {
			s = in.readLine();
			String[] res = s.split(",");
			boolean[] l = new boolean[784];
			for (int i = 0; i < 784; i++)
				l[i] = res[i].equals("1") ? true : false;
			data.add(new Instance(l, Integer.valueOf(res[784])));
		}
		return data.split(new RandomPartitionFilter(0.66));

	}

	public static void main(String[] args) throws IOException {
		DataSetPair p = FileMNISTTesting();

		DataSet trainingData = p.first;
		DataSet testingData = p.second;
		System.out.println(trainingData.size());
		System.out.println(testingData.size());
		ID3 tree = new ID3(trainingData);
		//tree.printTree();
		System.out.println(tree.test(testingData));
	}
}
