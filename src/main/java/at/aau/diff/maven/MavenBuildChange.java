package at.aau.diff.maven;

import at.aau.diff.common.Change;
import com.github.gumtreediff.actions.model.*;

public class MavenBuildChange extends Change {
	private String name;
	private String oldValue;
	private String newValue;

	private Action action;

	private PositionInfo srcPositionInfo=new PositionInfo();
	private PositionInfo dstPositionInfo=new PositionInfo();

	private String changeType="";

    @Deprecated
	public MavenBuildChange(String name) {
		super();
		this.setName(name);
	}

	public MavenBuildChange(String name, String oldValue, String newValue) {
		super();
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;

	}

    public PositionInfo getSrcPositionInfo() {
        return srcPositionInfo;
    }

    public void setSrcPositionInfo(PositionInfo srcPositionInfo) {
        this.srcPositionInfo = srcPositionInfo;
    }

    public PositionInfo getDstPositionInfo() {
        return dstPositionInfo;
    }

    public void setDstPositionInfo(PositionInfo dstPositionInfo) {
        this.dstPositionInfo = dstPositionInfo;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((newValue == null) ? 0 : newValue.hashCode());
		result = prime * result + ((oldValue == null) ? 0 : oldValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MavenBuildChange other = (MavenBuildChange) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (newValue == null) {
			if (other.newValue != null)
				return false;
		} else if (!newValue.equals(other.newValue))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MavenBuildChange [name=" + name + ", oldValue=" + oldValue + ", newValue=" + newValue + "]";
	}

    public void setAction(Action action) {
        this.action = action;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public class PositionInfo {
        private int startLineNumber=0;
        private int endLineNumber=0;
        private int startLineOffset=0;
        private int endLineOffset=0;

        public int getStartLineNumber() {
            return startLineNumber;
        }

        public void setStartLineNumber(int startLineNumber) {
            this.startLineNumber = startLineNumber;
        }

        public int getEndLineNumber() {
            return endLineNumber;
        }

        public void setEndLineNumber(int endLineNumber) {
            this.endLineNumber = endLineNumber;
        }

        public int getStartLineOffset() {
            return startLineOffset;
        }

        public void setStartLineOffset(int startLineOffset) {
            this.startLineOffset = startLineOffset;
        }

        public int getEndLineOffset() {
            return endLineOffset;
        }

        public void setEndLineOffset(int endLineOffset) {
            this.endLineOffset = endLineOffset;
        }

    }
}

