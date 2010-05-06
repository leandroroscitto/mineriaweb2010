package prototipo;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import modelo.User;

public class Usuario extends User {
	private int id;
	private Hashtable<Integer, Float> votos;
	private float meanvote;

	public Usuario(int i) {
		id = i;
		votos = new Hashtable<Integer, Float>();
		meanvote = 0;
	}

	public void setearVoto(int itemj, float voto) {
		if (votos.containsKey(itemj)) {
			meanvote = ((meanvote * votos.size()) - votos.get(itemj))/(votos.size()-1);
			votos.remove(itemj);
		}
		meanvote = ((meanvote*votos.size())+voto)/(votos.size()+1);
		votos.put(itemj, voto);
	}

	@Override
	public Collection<Integer> VotedItemsId() {
		return Collections.list(votos.keys());
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public float getVote(int ItemId) {
		return votos.get(ItemId);
	}

	@Override
	public boolean hasVote(int ItemId) {
		return votos.containsKey(ItemId);
	}

	@Override
	public float MeanVote() {
		return meanvote;
	}

}
