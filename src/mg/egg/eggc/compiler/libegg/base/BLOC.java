package mg.egg.eggc.compiler.libegg.base;

public class BLOC {
	private TDS_ACTION locales;

	public TDS_ACTION getLocs() {
		return locales;
	}

	private BLOC frere;

	public BLOC getFrere() {
		return frere;
	}

	public void setFrere(BLOC f) {
		frere = f;
	}

	public BLOC(TDS_ACTION t) {
		locales = new TDS_ACTION(t);
		frere = null;
	}
}
