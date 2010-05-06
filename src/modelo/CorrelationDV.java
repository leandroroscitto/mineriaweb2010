package modelo;

import java.util.Collection;
import java.util.Iterator;

public class CorrelationDV extends Weight {
	private int k = 0;
	private float defval = 0;

	public CorrelationDV(int extraitems, float defaultval) {
		k = extraitems;
		defval = defaultval;
	}

	@Override
	public double weightcalculate(User a, User i) {
		Collection<Integer> votosa, votosi;
		votosa = a.VotedItemsId();
		votosi = i.VotedItemsId();

		float sumprovaij = 0;
		float sumvaj = 0;
		float sumvij = 0;
		double sumvaj2 = 0;
		double sumvij2 = 0;
		float votoaj, votoij;
		int n = 0;

		for (Iterator<Integer> it = votosa.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			n = n + 1;

			votoaj = a.getVote(itemj.intValue());
			if (i.hasVote(itemj.intValue())) {
				votosi.remove(itemj);
				votoij = i.getVote(itemj.intValue());
			} else {
				votoij = defval;
			}

			sumprovaij = sumprovaij + (votoaj * votoij);

			sumvaj = sumvaj + votoaj;
			sumvij = sumvij + votoij;

			sumvaj2 = sumvaj2 + Math.pow(votoaj, 2);
			sumvij2 = sumvij2 + Math.pow(votoij, 2);
		}

		for (Iterator<Integer> it = votosi.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			n = n + 1;

			votoij = i.getVote(itemj.intValue());
			votoaj = defval;

			sumprovaij = sumprovaij + (votoaj * votoij);

			sumvaj = sumvaj + votoaj;
			sumvij = sumvij + votoij;

			sumvaj2 = sumvaj2 + Math.pow(votoaj, 2);
			sumvij2 = sumvij2 + Math.pow(votoij, 2);
		}

		double part1n = (n + k) * (sumprovaij + (k * Math.pow(defval, 2)));
		double part2n = (sumvaj + (k * defval)) * (sumvij + (k * defval));
		double numerador = part1n - part2n;

		double part1ad = (n + k) * (sumvaj2 + (k * Math.pow(defval, 2)));
		double part1bd = Math.pow(sumvaj + (k * defval), 2);
		double part1d = part1ad - part1bd;

		double part2ad = (n + k) * (sumvij2 + (k * Math.pow(defval, 2)));
		double part2bd = Math.pow(sumvij + (k * defval), 2);
		double part2d = part2ad - part2bd;

		double denominador = Math.sqrt(Math.abs(part1d * part2d));

		if (denominador == 0) {
			return 1;
		} else {
			return (numerador / denominador);
		}
	}
}
