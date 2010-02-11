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
	public static ID3DataSet RandomBooleanFunctionTesting(int varNum,int size) {
		boolean[] l = new boolean[varNum];
		BooleanFunctionBuilder func = new BooleanFunctionBuilder(varNum);
		ID3DataSet data = func.getDatSet(size);
		return data;
	}


	public static ID3DataSet FileTesting(String fileName,int featureNum,int distLabelNum) throws IOException {
		ID3DataSet data = new ID3DataSet(featureNum,distLabelNum);
		FileReader fr = new FileReader(fileName);
		BufferedReader in = new BufferedReader(fr);
		String s;
		while (in.ready()) {
			s = in.readLine();
			String[] res = s.split(",");
			boolean[] l = new boolean[featureNum];
			for (int i = 0; i < featureNum; i++)
				l[i] = res[i].equals("1") ? true : false;
			data.add(new ID3Instance(l, Integer.valueOf(res[featureNum])));
		}
		return data;
	}
	
	public static double splitValidation(ID3DataSet d,double x){
		if(x<=0 || x>=1){
			try {
				throw new Exception("probability should between 0 and 1");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ID3DataSet[] p=d.split(new RandomPartitionFilter(x));
		ID3 tree = new ID3(p[0]);
		return tree.test(p[1]);
	}
	
	public static double crossValidation(ID3DataSet d,int foldNum){
		if(foldNum<1 || foldNum>d.size()){
			try {
				throw new Exception("Invalid fold number");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ID3DataSet[] ds=d.split(foldNum);
		double res=0;
		for(int i=0;i<foldNum;i++){
			ID3DataSet[] train=new ID3DataSet[foldNum-1];
			ID3DataSet test=ds[i];
			for(int j=0,k=0;j<foldNum;j++){
				if(j==i)continue;
				train[k++]=ds[j];
			}
			ID3 tree=new ID3(train);
			res+=tree.test(test);
		}
		return res/foldNum;
	}
	public static void main(String[] args) throws IOException {
		ID3DataSet d = FileTesting("Semeion",256,10);
		//DataSet d=RandomBooleanFunctionTesting(30,20000);
		System.out.println(splitValidation(d,0.66));
		System.out.println(crossValidation(d,2));
	}
}
