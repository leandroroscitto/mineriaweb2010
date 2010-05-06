package modelo;

import java.util.Collection;
import java.util.Iterator;

public class VectorSimilarity extends Weight {

	@Override
	public double weightcalculate(User a, User i) {
		Collection<Integer> votosa, votosi;
		votosa = a.VotedItemsId();
		votosi = i.VotedItemsId();

		double suma, sumvak2, sumvik2;
		suma = 0;
		sumvak2 = 0;
		sumvik2 = 0;

		float votoaj, votoij;

		for (Iterator<Integer> it = votosa.iterator(); it.hasNext();) {
			Integer itemk = (Integer) it.next();
			sumvak2 = sumvak2 + Math.pow(a.getVote(itemk.intValue()), 2);
		}

		for (Iterator<Integer> it = votosi.iterator(); it.hasNext();) {
			Integer itemk = (Integer) it.next();
			sumvik2 = sumvik2 + Math.pow(i.getVote(itemk.intValue()), 2);
		}

		for (Iterator<Integer> it = votosa.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			if (votosi.contains(itemj.intValue())) {
				votoaj = a.getVote(itemj.intValue());
				votoij = i.getVote(itemj.intValue());
				suma = suma + (votoaj * votoij);
			}
		}
		return (suma / (Math.sqrt(sumvak2) * Math.sqrt(sumvik2)));
	}
}
