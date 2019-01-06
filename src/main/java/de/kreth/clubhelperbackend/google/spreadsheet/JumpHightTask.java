package de.kreth.clubhelperbackend.google.spreadsheet;

public class JumpHightTask {
	private String name;
	private String info;
	
	private JumpHightTask(Builder parameterObject) {
		super();
		this.name = parameterObject.name;
		this.info = parameterObject.info;
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static class Builder {
		String name;
		String info;
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setInfo(String info) {
			this.info = info;
			return this;
		}

		public JumpHightTask build() {
			if(name == null) {
				throw new IllegalStateException("Name must not be null");
			}
			return new JumpHightTask(this);
		}
	}
}
