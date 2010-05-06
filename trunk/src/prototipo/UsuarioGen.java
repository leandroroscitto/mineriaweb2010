package prototipo;

import java.util.Hashtable;

public class UsuarioGen extends Usuario {
	private Hashtable<Integer, Float> pesosgen;

	public UsuarioGen(int i) {
		super(i);
		pesosgen = new Hashtable<Integer, Float>();
	}

}
