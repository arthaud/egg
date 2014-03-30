package mg.egg.eggc.runtime.libjava;

import java.util.Properties;

import mg.egg.eggc.compiler.libegg.base.TDS;

public interface IEGGCompilationUnit extends ISourceUnit {
	public EGGOptions getOptions();

	public void analyzeOptions() throws EGGException;

	public TDS readTds() throws EGGException;

	public void writeTds(TDS tds) throws EGGException;

	public void clean() throws EGGException;

	public void setState();

	public void createGen() throws EGGException;

	public void createFile(String name, String contents) throws EGGException;

	public void checkPropFile(String filename, Properties contents)
			throws EGGException;

	public void checkPropFile(Properties contents) throws EGGException;

	public void deleteFile(String name) throws EGGException;
}
