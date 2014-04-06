package mg.egg.eggc.compiler.libegg.type;

import java.io.Serializable;
import java.util.Vector;

/**
 * L'interface DType implémente un descripteur de types.
 *
 * @author CD2005
 */
public interface IType extends Serializable {
	/**
	 * Teste l'égalité entre 2 types, en comparant leur nom respectif
	 *
	 * @param type Le type à comparer
	 * @return true si les types sont égaux, false sinon
	 */
	public boolean equals(IType type);

	/**
	 * Renvoie le nom du type
	 *
	 * @return Le nom du type
	 */
	public String getNom();

	/**
	 * Teste si le type est un type primitif ou un type classe
	 *
	 * @return true si le type est primitif, false sinon
	 */
	public boolean estPrimitif();

	public Vector<IType> getPars();

	public void addPar(IType autre);

	public int hashCode();
}
