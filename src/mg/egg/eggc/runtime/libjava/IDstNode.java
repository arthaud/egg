package mg.egg.eggc.runtime.libjava;

import java.util.List;

public interface IDstNode {
	public void addChild(IDstNode node);

	public List<IDstNode> getChildren();

	public IDstNode getParent();

	public void setParent(IDstNode p);

	public boolean isLeaf();

	public void accept(IDstVisitor visitor);

	public int getOffset();

	public int getLength();

	public void setOffset(int o);

	public void setLength(int l);

	public void setMalformed();

	public boolean isMalformed();
}
