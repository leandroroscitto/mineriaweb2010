package prototipo;

import java.io.BufferedReader; //import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import modelo.User;

public class Conversor {
	private Collection<Integer> ItemsIds;
	private ArrayList<User> Users;

	public Conversor() {
		ItemsIds = new ArrayList<Integer>();
	}

	public Collection<Integer> getItemsIds() {
		return ItemsIds;
	}
	
	public Collection<User> getUsers(){
		return Users;
	}

	public Collection<User> genCollection(String file, int maxcantrec,
			int offset) throws NumberFormatException, IOException {
		// File f = new File(".");
		// System.out.println(f.getCanonicalPath());

		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);

		BufferedReader br = new BufferedReader(isr);

		String linea;
		String[] datos;
		int uid, mid, rat;
		ArrayList<User> usuarios = new ArrayList<User>(10);
		int ultimouid = -1;
		Usuario ultimou = null;

		for (int i = 0; (br.ready() && i < maxcantrec + offset); i++) {
			linea = br.readLine();
			if (i > offset) {
				datos = linea.split("\t");
				// System.out.println(linea);
				uid = Integer.parseInt(datos[0]);
				mid = Integer.parseInt(datos[1]);
				rat = Integer.parseInt(datos[2]);

				if (!ItemsIds.contains(mid)) {
					ItemsIds.add(mid);
				}

				if (ultimouid != uid) {
					ultimouid = uid;
					ultimou = new Usuario(uid);
					ultimou.setearVoto(mid, rat);
					usuarios.add(ultimou);
				} else {
					ultimou.setearVoto(mid, rat);
				}
			}
		}

		/*
		 * for (User u : usuarios) { System.out.println(u.getId() + "," +
		 * u.VotedItemsId()); }
		 */
		Users=usuarios;
		return usuarios;
	}
}
