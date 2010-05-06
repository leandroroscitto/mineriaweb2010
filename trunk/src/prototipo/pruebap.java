package prototipo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

import modelo.CaseAmplification;
import modelo.Correlation;
import modelo.CorrelationDV;
import modelo.CorrelationDVIUF;
import modelo.CorrelationIUF2;
import modelo.CorrelationUIF;
import modelo.InverseUserFrequency;
import modelo.Predictor;
import modelo.User;
import modelo.VectorSimilarity;
import modelo.Weight;

public class pruebap {
	public static void generarTablas(Predictor[] predictores,
			Collection<User> usrs, int numpred, int interval)
			throws IOException {
		FileOutputStream fos = new FileOutputStream("datos2.txt");
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);

		int cant = predictores.length;
		double obs;
		double[] difpredobs = new double[cant];

		float d = 0;

		System.out.println("Generando tablas...");

		for (int i = 0; i < cant; i++) {
			difpredobs[i] = 0;
			bw.write(predictores[i].getName() + "\t");
		}
		bw.write("\n");

		DecimalFormat Formateador = new DecimalFormat("###,###.####");

		float porcent = -1;

		for (Iterator<User> it = usrs.iterator(); it.hasNext();) {
			User u = (User) it.next();
			for (Iterator<Integer> it2 = u.VotedItemsId().iterator(); (it2
					.hasNext() && (d < numpred));) {
				Integer itemj = (Integer) it2.next();
				d++;

				if ((Math.round((d / numpred) * 100.0) > porcent)) {
					porcent = Math.round((d / numpred) * 100);
					System.out.println(Math.round(porcent) + "%...");
				}

				obs = u.getVote(itemj.intValue());
				for (int i = 0; i < cant; i++) {
					difpredobs[i] += Math.abs(obs
							- (predictores[i].getPredictedVote(u, itemj)));

					if (d % interval == 0) {
						bw.write(Formateador.format(difpredobs[i] / d) + "\t");
					}
				}
				if (d % interval == 0) {
					bw.write("\n");
				}
			}
		}
		bw.close();
		System.out.println("Finalizado.");
	}

	public static void main(String[] args) throws Exception {
		Conversor conv1 = new Conversor();
		Conversor conv2 = new Conversor();
		Collection<User> usrs1 = conv1.genCollection("./datks/u2.base", 100000,
				0);
		Collection<Integer> itemsids1 = conv1.getItemsIds();
		Collection<User> usrs2 = conv2.genCollection("./datks/u2.test", 100000,
				0);

		System.out.println("Iniciando...");

		InverseUserFrequency iuf = new InverseUserFrequency(usrs1, itemsids1);
		Weight correlation = new Correlation();
		Weight correlationiuf = new CorrelationUIF(iuf);
		Weight correlationdv = new CorrelationDV(1000, 4);
		Weight vectorsim = new VectorSimilarity();

		Weight correlationdviuf = new CorrelationDVIUF(10000, 3, iuf);
		Weight correlationiuf2 = new CorrelationIUF2(iuf);

		Weight ampcorrelation = new CaseAmplification(correlation, 5);

		Predictor p1 = new Predictor(correlation, usrs1, "CORR");
		Predictor p2 = new Predictor(correlationiuf, usrs1, "IUF");
		Predictor p3 = new Predictor(vectorsim, usrs1, "SV");
		Predictor p4 = new Predictor(correlationdv, usrs1, "CORRDV");
		Predictor p5 = new Predictor(correlationdviuf, usrs1, "CORRDVIUF");
		Predictor p6 = new Predictor(correlationiuf2, usrs1, "IUF2");
		Predictor p7 = new Predictor(ampcorrelation, usrs1, "AMPCORR");

		generarTablas(new Predictor[] { p1, p2, p3, p4, p5, p6, p7 }, usrs2,
				20000, 200);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main1(String[] args) throws Exception {
		FileOutputStream fos = new FileOutputStream("datos1.txt");
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);

		Conversor conv1 = new Conversor();
		Conversor conv2 = new Conversor();
		Collection<User> usrs1 = conv1.genCollection("./datks/u2.base", 100000,
				0);
		Collection<Integer> itemsids1 = conv1.getItemsIds();
		Collection<User> usrs2 = conv2.genCollection("./datks/u2.test", 100000,
				0);

		InverseUserFrequency iuf = new InverseUserFrequency(usrs1, itemsids1);
		Weight correlation = new Correlation();
		Weight correlationiuf = new CorrelationUIF(iuf);

		Predictor psc = new Predictor(correlation, usrs1, "psc");
		Predictor amppsc2 = new Predictor(
				new CaseAmplification(correlation, 6), usrs1, "amppsc2");
		Predictor amppsc25 = new Predictor(new CaseAmplification(correlation,
				5.5), usrs1, "amppsc25");
		Predictor amppsc3 = new Predictor(
				new CaseAmplification(correlation, 5), usrs1, "amppsc3");
		// Predictor pdvc = new Predictor(new CorrelationDV(10000, 3), usrs1);
		Predictor puifc = new Predictor(correlationiuf, usrs1, "puifc");
		Predictor amppuifc2 = new Predictor(new CaseAmplification(
				correlationiuf, 0.8), usrs1, "amppuifc2");
		Predictor amppuifc25 = new Predictor(new CaseAmplification(
				correlationiuf, 0.6), usrs1, "amppuifc25");
		Predictor amppuifc3 = new Predictor(new CaseAmplification(
				correlationiuf, 0.4), usrs1, "amppuifc3");
		// Predictor pdviufc = new Predictor(new CorrelationDVIUF(1000, 3, iuf),
		// usrs1);
		// Predictor pvsc = new Predictor(new VectorSimilarity(), usrs1);

		// for (User u : usrs1) {
		// float means = u.MeanVote();
		// float meand = u.MeanVoteDin();
		// double suma = 0;
		// for (Integer i : u.VotedItemsId()) {
		// if (Math.random() > 0.5) {
		// suma = suma + Math.pow((u.getVote(i) - means), 2);
		// }
		// }
		// if (suma == 0) {
		// throw new Exception();
		// }
		// System.out.println("U=" + u.getId() + ", means=" + means
		// + ", meand=" + meand + ", suma=" + suma);
		// }

		double difpreobs1 = 0;
		double difpreobs2 = 0;
		double difpreobs3 = 0;
		double difpreobs4 = 0;
		double difpreobs5 = 0;
		double difpreobs6 = 0;
		double difpreobs7 = 0;
		double difpreobs8 = 0;
		long cant = 0;
		int d = 0;
		int k = 200;

		bw
				.write("AMPC1 \t AMPC2 \t AMPC3 \t CORR \t IUFC \t AMPIUF1 \t AMPIUF2 \t AMPIUF3 \n");

		for (Iterator<User> it = usrs2.iterator(); it.hasNext();) {
			User u = (User) it.next();
			for (Iterator<Integer> it2 = u.VotedItemsId().iterator(); (it2
					.hasNext() && (d < k));) {
				Integer i = (Integer) it2.next();
				d++;
				double predui1 = amppsc2.getPredictedVote(u, i);
				double predui5 = amppsc25.getPredictedVote(u, i);
				double predui6 = amppsc3.getPredictedVote(u, i);
				double predui2 = psc.getPredictedVote(u, i);
				double predui3 = puifc.getPredictedVote(u, i);
				double predui4 = amppuifc2.getPredictedVote(u, i);
				double predui7 = amppuifc25.getPredictedVote(u, i);
				double predui8 = amppuifc3.getPredictedVote(u, i);
				float obsui1 = u.getVote(i);

				cant++;

				if (cant == -1) {
					System.out.println(correlationiuf.weight(u, u));
					System.out.println(predui3);
					System.out.println(difpreobs3);
					System.out.println();
				}

				difpreobs1 = difpreobs1 + Math.abs(obsui1 - predui1);
				difpreobs2 = difpreobs2 + Math.abs(obsui1 - predui2);
				difpreobs3 = difpreobs3 + Math.abs(obsui1 - predui3);
				difpreobs4 = difpreobs4 + Math.abs(obsui1 - predui4);
				difpreobs5 = difpreobs5 + Math.abs(obsui1 - predui5);
				difpreobs6 = difpreobs6 + Math.abs(obsui1 - predui6);
				difpreobs7 = difpreobs7 + Math.abs(obsui1 - predui7);
				difpreobs8 = difpreobs8 + Math.abs(obsui1 - predui8);

				if (cant % 10 == 0) {
					System.out.println(cant);
					bw.write((difpreobs1 / cant) + "\t" + (difpreobs5 / cant)
							+ "\t" + (difpreobs6 / cant) + "\t"
							+ (difpreobs2 / cant) + "\t" + (difpreobs3 / cant)
							+ "\t" + (difpreobs4 / cant) + "\t"
							+ (difpreobs7 / cant) + "\t" + (difpreobs8 / cant)
							+ "\n");
					System.out.println("Razon amppsc4=" + (difpreobs1 / cant));
					System.out.println("Razon amppscs=" + (difpreobs5 / cant));
					System.out.println("Razon amppsc5=" + (difpreobs6 / cant));
					System.out.println("Razon psc=" + (difpreobs2 / cant));
					System.out.println("Razon puifc=" + (difpreobs3 / cant));
					System.out.println("Razon amppuifc08="
							+ (difpreobs4 / cant));
					System.out.println("Razon amppuifc06="
							+ (difpreobs7 / cant));
					System.out.println("Razon amppuifc04="
							+ (difpreobs8 / cant));
					System.out.println();
				}
			}
		}
		bw.close();
	}
}
