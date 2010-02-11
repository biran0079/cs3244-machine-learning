package linearclassifier;

import java.util.Iterator;
import java.util.LinkedList;

import id3.ID3Instance;

public class LCInstance   {
	private int label;
	private int[] feature;

	LCInstance(int[] a,int l){
		feature=new int[a.length];
		for(int i=0;i<a.length;i++)
			feature[i]=a[i];
		this.label=l;
	}
	public int getLabel(){
		return label;
	}
	public void setLabel(int x){
		this.label=x;
	}
	public int getFeature(int i){
		return feature[i];
	}
	public int getFeatureNum(){
		return feature.length;
	}

}
