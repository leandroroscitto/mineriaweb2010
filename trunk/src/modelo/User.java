package modelo;

import java.util.Collection;
import java.util.Iterator;

public abstract class User {

	public abstract int getId();

	public abstract float getVote(int ItemId);

	public abstract boolean hasVote(int ItemId);

	public abstract Collection<Integer> VotedItemsId();

	public float MeanVoteDin() {
		float suma = 0;
		int cantvotos = VotedItemsId().size();

		for (Iterator<Integer> it = VotedItemsId().iterator(); it.hasNext();) {
			Integer itemj = (Integer) it.next();
			suma = suma + getVote(itemj.intValue());
		}
		return (suma / cantvotos);
	}
	
	public abstract float MeanVote();
}
