package inpt.ac.ma;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LexicalAnalysis {

	// on ne peut pas utiliser la classe File sans FileNotFoundException
	// il faut ajouter l'exception
	public static void main(String[] args) throws FileNotFoundException {

		// this in case we want purselves to set the path
		// String fichier = "compiler\\badSyntaxFile.mrl";
		// or "compiler\\badSyntaxFile.mrl"

		// this in case we want the user to enter the file
		Scanner keyB = new Scanner(System.in);
		System.out.println("Enter your file's name: ");
		String nomDuFichier = keyB.nextLine();

		// verifier l'extension du fichier
		if (nomDuFichier.endsWith(".mrl")) {

			Scanner lecteur = new Scanner(new File(nomDuFichier));
			ArrayList<String> lignes = new ArrayList<>();
			ArrayList<String> erreurs = new ArrayList<>();
			Map<String, List<String>> tableauFinal = new HashMap<String, List<String>>();
			List<String> motsCles = new ArrayList<String>();
			List<String> operateurs = new ArrayList<String>();
			List<String> nombres = new ArrayList<String>();
			List<String> variables = new ArrayList<String>();
			List<String> caractereSpecial = new ArrayList<String>();
			int numeroDeLigne = 1;

			while (lecteur.hasNextLine()) {

				String ligne = lecteur.nextLine();

				// s'il s'agit d'un commentaire on va negliger la ligne
				if (ligne.startsWith("//")) {
					numeroDeLigne++;
					continue;
				}

				// on decompose les elements de l'instruction
				if (ligne.length() != 0) {
					// les expressions regulieres pour la decomposition
					String[] ligneSplit = ligne.trim().split("\\s+|\\s*,\\s*|\\;+|\\[+|\\]+|\\\"+|\\:+");
					List<String> liste = Arrays.asList(ligneSplit);
					lignes.addAll(liste);
				}

				// detection des erreurs syntaxiques
				Boolean resultat = Parentheses.checkValidity(ligne);
				if (!resultat) {
					String erreur = "Syntax error a parenthesis expected at line number : " + numeroDeLigne;
					erreurs.add(erreur);
				}
				if (!ligne.endsWith(";")) {
					String erreurPointVirgule = "syntax error \';\' expected to complete the instruction at line : "
							+ numeroDeLigne;
					erreurs.add(erreurPointVirgule);
				}
				numeroDeLigne++;

			}
			if (lignes.contains("/*") || lignes.contains("*/")) {
				if (lignes.contains("/*")) {
					int debutComment = lignes.indexOf("/*");
					// indexOf retourne la position de la 1ere occurence de */
					// donc la fin du commentaire courant
					int finComment = lignes.indexOf("*/");
					int commentaire = finComment - debutComment + 1;

					while (commentaire > 0) {
						lignes.remove(debutComment);
						commentaire = commentaire - 1;
					}
				}
			}

			String[] arrayDesLignes = lignes.toArray(new String[lignes.size()]);

			// on cherche les operateurs
			for (int i = 0; i < arrayDesLignes.length; i++) {
				if (arrayDesLignes[i].matches(
						"\\<|\\>|\\=|\\+|\\-|\\*|\\<=|\\>=|\\++|\\--|\\/|\\-=|\\+=|\\*=|\\/=|\\==|\\|\\||\\&&|\\!")) {
					operateurs.add(arrayDesLignes[i]);
				}
			}
			// on ajoute la liste des operateurs au tableau final
			tableauFinal.put("operateurs", operateurs);

			// les caracteres speciaux
			for (int i = 0; i < arrayDesLignes.length; i++) {
				if (arrayDesLignes[i].matches("\\(|\\)|\\{|\\}")) {
					caractereSpecial.add(arrayDesLignes[i]);
				}
			}
			// on ajoute la liste des caracteres speciaux au tableau final
			tableauFinal.put("caracteres speciaux", caractereSpecial);

			// trouver les mots cles
			for (int i = 0; i < arrayDesLignes.length; i++) {

				if (arrayDesLignes[i]
						.matches(
								"\\bbyte\\b+|\\bconst\\b+|\\bdouble\\b+|\\bfalse\\b+|\\blong\\b+|\\bnegat\\b+|\\bnull\\b+|\\bposit\\b+|\\bshort\\b+|\\btrue\\b+|\\bbool\\b+|\\bchar\\b+|\\bfloat\\b+|\\bint\\b+|\\bstring\\b+|\\bvoid\\b+|\\btab\\b+|\\bif\\b+|\\belse\\b+|\\belif\\b+|\\bfor\\b+|\\bwhile\\b+|\\bcase\\b+|\\bdefault\\b+|\\bend\\b+|\\bfun\\b+|\\bprint\\b+|\\breturn\\b+|\\btest\\b+|\\start\\b+|\\exit\\b")) {
					motsCles.add(arrayDesLignes[i]);
				}
			}
			// on ajoute la liste des mots cles au tableau final
			tableauFinal.put("mots cles", motsCles);

			// les nombres
			for (int i = 0; i < arrayDesLignes.length; i++) {
				if (arrayDesLignes[i].matches("\\d+|\\d+\\.\\d+")) {
					nombres.add(arrayDesLignes[i]);
				}
			}
			tableauFinal.put("nombres", nombres);

			// les variables
			for (int i = 0; i < arrayDesLignes.length; i++) {
				if (arrayDesLignes[i].matches("\\w+")
						&& !arrayDesLignes[i].matches("\\d+|\\d+\\.\\d+")
						&& !arrayDesLignes[i].matches(
								"\\bbyte\\b+|\\bconst\\b+|\\bdouble\\b+|\\bfalse\\b+|\\blong\\b+|\\bnegat\\b+|\\bnull\\b+|\\bposit\\b+|\\bshort\\b+|\\btrue\\\\b+|\\bbool\\b+|\\bchar\\b+|\\bfloat\\b+|\\bint\\b+|\\bstring\\b+|\\bvoid\\b+|\\btab\\b+|\\bif\\b+|\\belse\\b+|\\belif\\b+|\\bfor\\b+|\\bwhile\\b+|\\bcase\\b+|\\bdefault\\b+|\\bend\\b+|\\bfun\\b+|\\bprint\\b+|\\breturn\\b+|\\btest\\b")) {
					if (!variables.contains(arrayDesLignes[i])) {
						variables.add(arrayDesLignes[i]);
					}
				}
			}
			tableauFinal.put("variables", variables);

			// afficher les erreurs
			if (erreurs != null) {
				for (int j = 0; j < erreurs.size(); j++) {
					System.out.println(erreurs.get(j));
				}
			}

			/**
			 * // on affiche les lignes System.out.println("Les mots séparés: ");
			 * System.out.println(lignes);
			 * 
			 * // afficher le tableau final for (Map.Entry<String, List<String>> entry :
			 * tableauFinal.entrySet()) { String clé = entry.getKey(); List<String> valeurs
			 * = entry.getValue(); System.out.print(clé + ": ");
			 * System.out.println(valeurs); }
			 **/

		} else {
			System.out.println("Please enter a file with the extension mrl");
		}

	}
}
