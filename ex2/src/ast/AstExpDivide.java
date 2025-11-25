package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpDivide extends AstExp
{
	public AstExp left;
	public AstExp right;
	
	public AstExpDivide(AstExp left, AstExp right)
	{
		super("exp -> exp divide exp");

		this.left = left;
		this.right = right;
	}

	@Override
	protected String GetNodeName() {
		return "ExpDivide";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, right);
	}
}
