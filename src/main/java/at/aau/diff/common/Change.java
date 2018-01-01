package at.aau.diff.common;

import com.github.gumtreediff.actions.model.Action;

public abstract class Change {

	@Override
	public abstract boolean equals(Object o);
	
	@Override
	public abstract int hashCode();

    public abstract void setAction(Action action);
}
