package modelo;

import java.util.Hashtable;

public abstract class Weight {
	private Hashtable<User, Hashtable<User, Double>> weights;

	public abstract double weightcalculate(User a, User i);

	public Weight() {
		weights = new Hashtable<User, Hashtable<User, Double>>();
	}

	public double weight(User a, User i) {
		if (weights.containsKey(a)) {
			Hashtable<User, Double> pesosa = weights.get(a);
			if (pesosa.containsKey(i)) {
				return pesosa.get(i).doubleValue();
			}
		}
		if (weights.containsKey(i)) {
			Hashtable<User, Double> pesosi = weights.get(i);
			if (pesosi.containsKey(a)) {
				return pesosi.get(a).doubleValue();
			}
		}

		// Solo llega aca si no fue previamente calculado
		double result = weightcalculate(a, i);

		if (weights.containsKey(a)) {
			weights.get(a).put(i, result);
		} else {
			if (weights.containsKey(i)) {
				weights.get(i).put(a, result);
			} else {
				Hashtable<User, Double> pesosi = new Hashtable<User, Double>();
				pesosi.put(i, result);
				weights.put(a, pesosi);
			}
		}

		return result;
	}
}
