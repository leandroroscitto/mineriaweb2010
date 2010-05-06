package modelo;

import java.util.Collection;
import java.util.Iterator;

public class CorrelationDVIUF extends Weight {
	private int k = 0;
	private float defval = 0;
	private InverseUserFrequency iuf;

	public CorrelationDVIUF(int additems, float defaultval,
			InverseUserFrequency invuf) {
		k = additems;
		defval = defaultval;
		iuf = invuf;
	}

	@Override
	public double weightcalculate(User a, User i) {
		Collection<Integer> votosa, votosi;
		votosa = a.VotedItemsId();
		votosi = i.VotedItemsId();

		double sumprovaij = 0;
		double sumvaj = 0;
		double sumvij = 0;
		double sumvaj2 = 0;
		double sumvij2 = 0;
		float votoaj, votoij;
		int n = 0;
		double fj;

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

			fj = iuf.inverseuserf(itemj.intValue());

			sumprovaij = sumprovaij + fj * (votoaj * votoij);

			sumvaj = sumvaj + fj * votoaj;
			sumvij = sumvij + fj * votoij;

			sumvaj2 = sumvaj2 + fj * Math.pow(votoaj, 2);
			sumvij2 = sumvij2 + fj * Math.pow(votoij, 2);
		}

		for (Iterator<Integer> it = votosi.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			votosa.add(itemj);
			n = n + 1;

			votoij = i.getVote(itemj.intValue());
			votoaj = defval;

			fj = iuf.inverseuserf(itemj.intValue());

			sumprovaij = sumprovaij + fj * (votoaj * votoij);

			sumvaj = sumvaj + fj * votoaj;
			sumvij = sumvij + fj * votoij;

			sumvaj2 = sumvaj2 + fj * Math.pow(votoaj, 2);
			sumvij2 = sumvij2 + fj * Math.pow(votoij, 2);
		}

		double part1n = (n + k) * (sumprovaij + (k * Math.pow(defval, 2)));
		double part2n = (sumvaj + (k * defval)) * (sumvij + (k * defval));
		// double numerador = part1n - part2n;
		double part1nfj = 0;
		double numerador;

		double part1ad = (n + k) * (sumvaj2 + (k * Math.pow(defval, 2)));
		double part1bd = Math.pow(sumvaj + (k * defval), 2);
		double part1d = part1ad - part1bd;

		double part2ad = (n + k) * (sumvij2 + (k * Math.pow(defval, 2)));
		double part2bd = Math.pow(sumvij + (k * defval), 2);
		double part2d = part2ad - part2bd;

		// double denominador = Math.sqrt(part1d * part2d);
		double part1dfj = 0;
		double part2dfj = 0;
		double denominador;

		for (Iterator<Integer> it = votosa.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			fj = iuf.inverseuserf(itemj);
			part1nfj = part1nfj + fj * part1n;
			part1dfj = part1dfj + fj * part1d;
			part2dfj = part2dfj + fj * part2d;
		}

		numerador = part1nfj - part2n;
		denominador = Math.sqrt(part1dfj * part2dfj);

		return (numerador / denominador);
	}
}
