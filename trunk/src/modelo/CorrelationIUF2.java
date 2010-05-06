package modelo;

import java.util.Collection;
import java.util.Iterator;

public class CorrelationIUF2 extends Weight {
	
	private InverseUserFrequency iuf;

	public CorrelationIUF2(InverseUserFrequency invuf) {
		iuf = invuf;
	}

	@Override
	public double weightcalculate(User a, User i) {
		Collection<Integer> votosa, votosi;
		votosa = a.VotedItemsId();
		votosi = i.VotedItemsId();

		double suma, sumdifa2, sumdifi2;
		suma = 0;
		sumdifa2 = 0;
		sumdifi2 = 0;
		
		double fj;

		double difa;
		double difi;

		for (Iterator<Integer> it = votosa.iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			if (votosi.contains(itemj.intValue())) {
				fj = iuf.inverseuserf(itemj.intValue());
				
				difa = (fj*a.getVote(itemj.intValue())) - a.MeanVote();
				difi = (fj*i.getVote(itemj.intValue())) - i.MeanVote();
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

