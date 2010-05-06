package principal;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import principal.Main;

public class PruebasGBAPI {
	public class Resultado {
		public String[] NDatos;
		public String[] URLDatos;
		public int[] Pesos;
		public int TotalRes;

		public Resultado(int tamaño, int totalr) {
			NDatos = new String[tamaño];
			URLDatos = new String[tamaño];
			Pesos = new int[tamaño];
			TotalRes = totalr;
		}
	}

	public static String APIDIR = "http://api.giantbomb.com/";
	private static String APIKEY = "ecefe4ed72bdb29dc1a619fd6dd9acb58c37e423";

	// FORMATOS DE RESPUESTA
	public static String FXML = "xml";
	public static String FJSON = "json";

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, InterruptedException {
		new PruebasGBAPI();
	}

	public PruebasGBAPI() throws IOException, ParserConfigurationException,
			SAXException, InterruptedException {

		String[] filters = new String[] { mkfilter("query", "sonic") };
		String[] fieldlist = new String[] { "name", "site_detail_url" };
		String surl = getResList("search", filters, fieldlist, "name", FXML,
				20, 0);
		Resultado results = readResList(surl);
		ArrayList<String> listurl = new ArrayList<String>(
				results.URLDatos.length);
		for (String s : results.URLDatos) {
			listurl.add(s);
		}

		// Main Sim = new Main(listurl);
	}

	public String searchapi(String query, int offset) throws IOException {
		String[] filters = new String[] { mkfilter("query", query) };
		String[] fieldlist = new String[] { "name", "site_detail_url" };
		return getResList("search", filters, fieldlist, "name", FXML, 20,
				offset);
	}

	public String mkfilter(String type, String value) {
		return (type + "=" + value);
	}

	public String getResList(String resname, String[] filters,
			String[] fieldlist, String sort, String format, int limit,
			int offset) throws IOException {
		String sfilters = new String();
		for (int i = 0; i < filters.length; i++) {
			sfilters = sfilters.concat("&" + filters[i]);
		}

		String sfieldlist = new String();
		for (int j = 0; j < fieldlist.length; j++) {
			if (j == 0) {
				sfieldlist = fieldlist[0];
			} else {
				sfieldlist = sfieldlist.concat("," + fieldlist[j]);
			}
		}

		String surl = APIDIR + resname + "/" + "?api_key=" + APIKEY + sfilters
				+ "&" + "field_list=" + sfieldlist + "&" + "sort=" + sort
				+ "&format=" + format + "&limit=" + limit + "&offset=" + offset;

		System.out.println(surl);

		// URL url = new URL(surl);
		// InputStreamReader isr = new InputStreamReader(url.openStream());
		// BufferedReader in = new BufferedReader(isr);

		return surl;
	}

	public String getTagContent(Document doc, String tag, int index) {
		NodeList nlist = doc.getElementsByTagName(tag);
		String content = nlist.item(index).getTextContent();
		System.out.println(content);
		return content;
	}

	public String getError(Document doc) {
		return getTagContent(doc, "error", 0);
	}

	public int getStatusCode(Document doc) {
		return Integer.valueOf(getTagContent(doc, "status_code", 0));
	}

	public int getNumberOfPageResults(Document doc) {
		return Integer.valueOf(getTagContent(doc, "number_of_page_results", 0));
	}

	public int getNumberOfTotalResults(Document doc) {
		return Integer
				.valueOf(getTagContent(doc, "number_of_total_results", 0));
	}

	public int getOffset(Document doc) {
		return Integer.valueOf(getTagContent(doc, "offset", 0));
	}

	public int getLimit(Document doc) {
		return Integer.valueOf(getTagContent(doc, "limit", 0));
	}

	public void readResList2(String surl) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(surl);

		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodol = doc.getElementsByTagName("error");
		System.out.println("error=" + nodol.item(0).getTextContent());
		Node nodo = doc.getElementsByTagName("results").item(0);
		nodol = nodo.getChildNodes();
		for (int i = 0; i < nodol.getLength(); i++) {
			System.out.println(nodol.item(i).getChildNodes().item(0)
					.getTextContent());
		}
	}

	public Resultado readResList(String surl)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(surl);

		String error = getError(doc);
		int status_code = getStatusCode(doc);
		int number_of_page_results = getNumberOfPageResults(doc);
		int number_of_total_results = getNumberOfTotalResults(doc);
		// int limit = getLimit(doc);
		// int offset = getOffset(doc);

		if (status_code == 1) {
			Node nodo = doc.getElementsByTagName("results").item(0);
			NodeList nodol = nodo.getChildNodes();
			Resultado results = new Resultado(number_of_page_results,
					number_of_total_results);
			results.TotalRes = number_of_total_results;
			for (int i = 0; i < nodol.getLength(); i = i + 1) {
				results.NDatos[i] = nodol.item(i).getChildNodes().item(0)
						.getTextContent();
				results.URLDatos[i] = nodol.item(i).getChildNodes().item(2)
						.getTextContent();
				System.out.println(results.NDatos[i] + ","
						+ results.URLDatos[i]);
			}
			return results;
		} else {
			System.out.println("Error: " + error);
			Resultado results = new Resultado(2, 0);
			results.NDatos[0] = "Error:";
			results.NDatos[1] = error;
			return results;
		}
	}
}
