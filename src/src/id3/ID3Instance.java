package id3;

import java.util.ArrayList;
import java.util.Arrays;

public class ID3Instance {
	private boolean[] features;
	private int label;

	public ID3Instance(boolean[] f, int l) {
		this.features = Arrays.copyOf(f, f.length);
		this.label = l;
	}

	public int getLabel() {
		return label;
	}

	@Override
	public int hashCode(){
		int top=features.length,res=0;
		if(top>32)top=32;
		for(int i=0;i<top;i++){
			if(features[i])
				res+=(1<<i);
		}
		return res;
	}
	@Override
	public boolean equals(Object o){
		if(!(o instanceof ID3Instance))return false;
		ID3Instance t=(ID3Instance)o;
		for(int i=0;i<features.length;i++)
			if(t.getFeature(i)!=features[i])
				return false;
		return true;
	}
	
	public boolean getFeature(int i) {
		return features[i];
	}
	public int getFeatureNum() {
		return features.length;
	}
}
