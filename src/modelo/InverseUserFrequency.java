package modelo;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

public class InverseUserFrequency {
	private Hashtable<Integer, Double> iuf;

	public InverseUserFrequency(Collection<User> usrs, Collection<Integer> items) {
		iuf = new Hashtable<Integer, Double>(items.size());
		for (Integer itemj : items) {
			iuf.put(itemj, ciuf(usrs, itemj));
		}
	}

	public double inverseuserf(Integer itemj) {
		if (iuf.containsKey(itemj)) {
			return (iuf.get(itemj));
		} else {
			return (Math.log(iuf.size() + 1));
		}

	}

	private double ciuf(Collection<User> users, int itemj) {
		int n = users.size();
		int nj = 0;

		for (Iterator<User> it = users.iterator(); it.hasNext();) {
			User user = (User) it.next();

			if (user.hasVote(itemj)) {
				nj = nj + 1;
			}
		}

		if (nj != 0) {
			return Math.log(n / nj);
		} else {
			return 1;
		}
	}
}
