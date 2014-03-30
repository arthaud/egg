package mg.egg.eggc.runtime.libjava;

public interface ISourceUnit {
	public char[] getContents() throws Exception;

	public String getFileName();

	public boolean isOk();

	public void setOk(boolean ok);
}
