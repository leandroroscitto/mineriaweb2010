package modelo;

import java.util.Collection;

public class CorrelationUIF extends Weight {
	private InverseUserFrequency iuf;

	public CorrelationUIF(InverseUserFrequency invuf) {
		super();
		iuf = invuf;
	}

	@Override
	public double weightcalculate(User a, User i) {
		Collection<Integer> votosa = a.VotedItemsId();
		Collection<Integer> votosi = a.VotedItemsId();
		Collection<Integer> votos;

		if (votosa.size() < votosi.size()) {
			votos = votosa;
		} else {
			votos = votosi;
		}

		float votoa, votoi;
		double fj;
		double sumafjvavi = 0;
		double sumafjfjvavi = 0;
		double sumafjva = 0;
		double sumafjvi = 0;
		double sumafjva2 = 0;
		double sumafjvi2 = 0;
		double u = 0;
		double v = 0;

		for (Integer itemj : votos) {
			if (i.hasVote(itemj) && a.hasVote(itemj)) {
				votoa = a.getVote(itemj.intValue());
				votoi = i.getVote(itemj.intValue());
				fj = iuf.inverseuserf(itemj.intValue());

				sumafjvavi = sumafjvavi + (fj * votoa * votoi);
				sumafjva = sumafjva + (fj * votoa);
				sumafjvi = sumafjvi + (fj * votoi);

				sumafjva2 = sumafjva2 + (fj * Math.pow(votoa, 2));
				sumafjvi2 = sumafjvi2 + (fj * Math.pow(votoi, 2));
			}
		}

		for (Integer itemk : votos) {
			if (i.hasVote(itemk) && a.hasVote(itemk)) {
				fj = iuf.inverseuserf(itemk.intValue());
				sumafjfjvavi = sumafjfjvavi + (fj * sumafjvavi);
				u = u + (fj * (sumafjva2 - Math.pow(sumafjva, 2)));
				v = v + (fj * (sumafjvi2 - Math.pow(sumafjvi, 2)));
			}
		}

		// System.out.println(u + "," + v);

		double result;
		if ((u == 0) || (v == 0)) {
			result = 1;
		} else {
			result = (sumafjfjvavi - (sumafjva * sumafjvi))
					/ Math.sqrt(Math.abs(u * v));
		}

		return result;
	}

}
