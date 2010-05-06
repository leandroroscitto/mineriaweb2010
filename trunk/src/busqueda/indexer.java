package busqueda;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class indexer {
	private indexer() {
	}

	private static boolean deleting = false; // verdadero en la etapa de borrado
	private static IndexReader reader; // index existente
	private static IndexWriter writer; // nuevo index siendo escrito
	private static TermEnum uidIter; // iterador del id de documento

	public static void indexar(String index, boolean create, String rootd) {
		File findex = new File(index);
		File froot = new File(rootd);
		

		Date start = new Date();

		try {
			if (!create) { // delete stale docs
				deleting = true;
				indexDocs(froot, findex, create);
			}
			System.out.println(findex.getAbsolutePath()+","+froot.getAbsolutePath());
			System.out.println("Creando index...");
			writer = new IndexWriter(FSDirectory.open(findex),
					new StandardAnalyzer(Version.LUCENE_29), create,
					new IndexWriter.MaxFieldLength(1000000));
			indexDocs(froot, findex, create);

			System.out.println("Optimizando index...");
			writer.optimize();
			writer.close();

			Date end = new Date();

			System.out.print(end.getTime() - start.getTime());
			System.out.println(" milisegundos totales");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Indexer for HTML files. */
	@SuppressWarnings("deprecation")
	public static void main(String[] argv) {
		try {
			File index = new File("index");
			boolean create = false;
			File root = null;

			String usage = "IndexHTML [-create] [-index <index>] <root_directory>";

			if (argv.length == 0) {
				System.err.println("Usage: " + usage);
				return;
			}

			for (int i = 0; i < argv.length; i++) {
				if (argv[i].equals("-index")) { // parse -index option
					index = new File(argv[++i]);
				} else if (argv[i].equals("-create")) { // parse -create option
					create = true;
				} else if (i != argv.length - 1) {
					System.err.println("Usage: " + usage);
					return;
				} else
					root = new File(argv[i]);
			}

			if (root == null) {
				System.err.println("Specify directory to index");
				System.err.println("Usage: " + usage);
				return;
			}

			Date start = new Date();

			if (!create) { // delete stale docs
				deleting = true;
				indexDocs(root, index, create);
			}
			writer = new IndexWriter(FSDirectory.open(index),
					new StandardAnalyzer(Version.LUCENE_CURRENT), create,
					new IndexWriter.MaxFieldLength(1000000));
			indexDocs(root, index, create); // add new docs

			System.out.println("Optimizing index...");
			writer.optimize();
			writer.close();

			Date end = new Date();

			System.out.print(end.getTime() - start.getTime());
			System.out.println(" total milliseconds");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Walk directory hierarchy in uid order, while keeping uid iterator from /*
	 * existing index in sync. Mismatches indicate one of: (a) old documents to
	 * /* be deleted; (b) unchanged documents, to be left alone; or (c) new /*
	 * documents, to be indexed.
	 */

	private static void indexDocs(File file, File index, boolean create)
			throws Exception {
		if (!create) { // incrementally update

			reader = IndexReader.open(FSDirectory.open(index), false); // open
			// existing
			// index
			uidIter = reader.terms(new Term("uid", "")); // init uid iterator

			indexDocs(file);

			if (deleting) { // delete rest of stale docs
				while (uidIter.term() != null
						&& uidIter.term().field() == "uid") {
					System.out.println("deleting "
							+ HTMLDocument.uid2url(uidIter.term().text()));
					reader.deleteDocuments(uidIter.term());
					uidIter.next();
				}
				deleting = false;
			}

			uidIter.close(); // close uid iterator
			reader.close(); // close existing index

		} else
			// don't have exisiting
			indexDocs(file);
	}

	private static void indexDocs(File file) throws Exception {
		if (file.isDirectory()) { // if a directory
			String[] files = file.list(); // list its files
			Arrays.sort(files); // sort the files
			for (int i = 0; i < files.length; i++)
				// recursively index them
				indexDocs(new File(file, files[i]));

		} else if (file.getPath().endsWith(".html") || // index .html files
				file.getPath().endsWith(".htm") || // index .htm files
				file.getPath().endsWith(".txt") || // index .txt files
				file.getPath().endsWith(".java")) // index .java files
		{

			if (uidIter != null) {
				String uid = HTMLDocument.uid(file); // construct uid for doc

				while (uidIter.term() != null
						&& uidIter.term().field() == "uid"
						&& uidIter.term().text().compareTo(uid) < 0) {
					if (deleting) { // delete stale docs
						System.out.println("deleting "
								+ HTMLDocument.uid2url(uidIter.term().text()));
						reader.deleteDocuments(uidIter.term());
					}
					uidIter.next();
				}
				if (uidIter.term() != null && uidIter.term().field() == "uid"
						&& uidIter.term().text().compareTo(uid) == 0) {
					uidIter.next(); // keep matching docs
				} else if (!deleting) { // add new docs
					Document doc = HTMLDocument.Document(file);
					System.out.println("adding " + doc.get("path"));
					writer.addDocument(doc);
				}
			} else { // creating a new index
				Document doc = HTMLDocument.Document(file);
				System.out.println("adding " + doc.get("path"));
				writer.addDocument(doc); // add docs unconditionally
			}
		}
	}
}
