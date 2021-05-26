package inpt.ac.ma;

public class Parentheses {
	private char elements[];
	private int tete;

	// implémenter la pile le parametre est le nombre des caracteres dans la ligne
	// analysée
	public Parentheses(int n) {
		elements = new char[n];
		tete = -1;
	}

	// ajouter les éléments a la pile
	void empiler(char c) {
		if (tete == elements.length - 1) {
			return;
		}
		tete++;
		elements[tete] = c;
	}

	// supprimer le dernier élément ajouté à la pile et renvoi cet élément supprimé
	char supprimer() {
		if (estVide()) {
			System.out.println("Stack empty");
			return (char) 0;
		}
		char p;
		p = elements[tete];
		tete--;
		return p;
	}

	// vérifier si la pile est vide ou pas
	boolean estVide() {
		if (tete == -1) {
			return true;
		} else {
			return false;
		}
	}

	// vérifier l'équilibre des parenthèses, accolades et crochets
	public static Boolean checkValidity(String texte) {
		char symbole, precedent;
		Parentheses s = new Parentheses(texte.length());
		for (int i = 0; i < texte.length(); i++) {
			symbole = texte.charAt(i);
			if (symbole == '(' || symbole == '{' || symbole == '[') {
				s.empiler(symbole);
			}
			if (symbole == ')' || symbole == '}' || symbole == ']') {
				if (s.estVide()) {
					// System.out.println("False");
					return false;
				} else {
					precedent = s.supprimer();
					if (!isPairMatch(precedent, symbole)) {
						// System.out.println("False");
						return false;
					}
				}
			}

		}
		if (!s.estVide()) {
			// System.out.println("False");
			return false;
		}
		// System.out.println("True");
		return true;
	}

	// vérifie la correspandance des parenthèses ..
	// le premier param est le parenthese supprimé de la pile et le 2ème est le
	// parenthèse fermant
	public static boolean isPairMatch(char character1, char character2) {
		if (character1 == '(' && character2 == ')') {
			return true;
		} else if (character1 == '{' && character2 == '}') {
			return true;
		} else if (character1 == '[' && character2 == ']') {
			return true;
		} else {
			return false;
		}
	}

}