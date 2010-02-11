package linearclassifier;

import filter.RandomPartitionFilter;
import id3.ID3;
import id3.ID3DataSet;
import id3.ID3Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LCTest {

	public static LCDataSet FileTesting(String fileName, int featureNum,
			int labelNum) throws IOException {
		LCDataSet data = new LCDataSet(featureNum, labelNum);
		FileReader fr = new FileReader(fileName);
		BufferedReader in = new BufferedReader(fr);
		String s;
		while (in.ready()) {
			s = in.readLine();
			String[] res = s.split(",");
			int[] l = new int[featureNum];
			for (int i = 0; i < featureNum; i++)
				l[i] = res[i].equals("1") ? 1 : 0;
			data.add(new LCInstance(l, Integer.valueOf(res[featureNum])));
		}
		return data;
	}

	public static double splitValidation(LCDataSet d) {
		LCDataSet[] p = d.split(2);
		LinearClassifier lc = new LinearClassifier(p[0]);
		return lc.test(p[1]);
	}

	public static double pairwiseTest(LCDataSet[] ds) {
		LCDataSet[][] p = new LCDataSet[10][2];
		for (int i = 0; i < 10; i++){
			p[i] = ds[i].split(2);
		}
		LinearClassifier[][] lc = new LinearClassifier[10][10];
		for (int i = 0; i < 9; i++)
			for (int j = i + 1; j < 10; j++) {
				LCDataSet train = new LCDataSet(256, 2);
				for (LCInstance x : p[i][0]) {
					x.setLabel(0);
					train.add(x);
				}
				for (LCInstance x : p[j][0]) {
					x.setLabel(1);
					train.add(x);
				}
				lc[i][j] = new LinearClassifier(train);
				System.out.println(i+" "+j+" "+lc[i][j].test(train));
			}
		int ac=0,wa=0;
		for (int lb = 0; lb < 10; lb++)
			for (LCInstance x : p[lb][1]) {
				int[] ct=new int[10];
				for (int i = 0; i < 9; i++)
					for (int j = i + 1; j < 10; j++){
						if(lc[i][j].classify(x)==0)
							ct[i]++;
						else
							ct[j]++;
					}
				int res=-1,max=-1;
				for(int i=0;i<10;i++){
					if(max<ct[i]){
						res=i;
						max=ct[i];
					}
				}
				if(res==x.getLabel()){
					ac++;
				}else{
					wa++;
				}
			}
		return (double)ac/(ac+wa);
	}

	public static void main(String[] args) throws IOException {
		LCDataSet d = FileTesting("Semeion", 256, 10);
		LCDataSet[] ds = d.splitByLabel();
		LCDataSet train=new LCDataSet(256,20);
		for(LCInstance x:ds[5]){
			x.setLabel(0);
			train.add(x);
		}
		for(LCInstance x:ds[7]){
			x.setLabel(1);
			train.add(x);
		}
		//System.out.println(splitValidation(train));

		System.out.println(pairwiseTest(ds));
		
	}
}
