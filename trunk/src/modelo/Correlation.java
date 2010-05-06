package modelo;

import java.util.Collection;
import java.util.Iterator;

public class Correlation extends Weight {

	@Override
	public double weightcalculate(User a, User i) {
		Collection<Integer> votosa, votosi;
		votosa = a.VotedItemsId();
		votosi = i.VotedItemsId();

		double suma, sumdifa2, sumdifi2;
		suma = 0;
		sumdifa2 = 0;
		sumdifi2 = 0;

		float difa, difi;

		for (Iterator<Integer> it = votosa.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			if (votosi.contains(itemj.intValue())) {
				difa = a.getVote(itemj.intValue()) - a.MeanVote();
				difi = i.getVote(itemj.intValue()) - i.MeanVote();
				suma = suma + (difa * difi);

				sumdifa2 = sumdifa2 + Math.pow(difa, 2);
				sumdifi2 = sumdifi2 + Math.pow(difi, 2);
			}
		}

		if ((sumdifa2 == 0) || (sumdifi2 == 0)) {
			return 1;
		} else {
			return (suma / Math.sqrt(sumdifa2 * sumdifi2));
		}
	}

}
