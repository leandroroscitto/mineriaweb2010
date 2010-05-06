package modelo;

import java.util.Collection;
import java.util.Iterator;

public class Predictor {
	private Weight w;
	private Collection<User> users;
	private String name = "Default";

	public Predictor(Weight weight, Collection<User> usrs, String nomb) {
		w = weight;
		users = usrs;
		name = nomb;
	}

	public String getName() {
		return name;
	}
	
	private User getUser(int uid){
		for(User u:users){
			if (u.getId()==uid){
				return u;
			}
		}
		return null;
	}

	public double getPredictedVote(User a, int itemj) {
		float va = a.MeanVote();
		double k = 0;
		double suma = 0;
		double weightai = 0;
		
		User aint=getUser(a.getId());
		if (aint==null){
			aint=a;
			System.out.println("Usando Parámetro de entrada...");
		}
		
		for (Iterator<User> it = users.iterator(); it.hasNext();) {
			User i = (User) it.next();
			if (i.hasVote(itemj)) {
				weightai = w.weight(a, i);

				k = k + Math.abs(weightai);
				if (weightai != 0) {
					suma = suma
							+ (weightai * (i.getVote(itemj) - i.MeanVote()));
				}
			}
		}

		if (k != 0) {
			return (va + (1 / k) * suma);
		} else {
			return (va);
		}
	}
}
