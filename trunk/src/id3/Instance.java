package id3;

import java.util.ArrayList;
import java.util.Arrays;

public class Instance {
	private boolean[] features;
	private boolean label;

	public Instance(boolean[] f, boolean l) {
		this.features = Arrays.copyOf(f, f.length);
		this.label = l;
	}

	public boolean getLabel() {
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
		if(!(o instanceof Instance))return false;
		Instance t=(Instance)o;
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
