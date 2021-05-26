package inpt.ac.ma;

public class Parentheses {
	private char elements[];
	private int tete;

	// impl�menter la pile le parametre est le nombre des caracteres dans la ligne
	// analys�e
	public Parentheses(int n) {
		elements = new char[n];
		tete = -1;
	}

	// ajouter les �l�ments a la pile
	void empiler(char c) {
		if (tete == elements.length - 1) {
			return;
		}
		tete++;
		elements[tete] = c;
	}

	// supprimer le dernier �l�ment ajout� � la pile et renvoi cet �l�ment supprim�
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

	// v�rifier si la pile est vide ou pas
	boolean estVide() {
		if (tete == -1) {
			return true;
		} else {
			return false;
		}
	}

	// v�rifier l'�quilibre des parenth�ses, accolades et crochets
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

	// v�rifie la correspandance des parenth�ses ..
	// le premier param est le parenthese supprim� de la pile et le 2�me est le
	// parenth�se fermant
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