package domain.entity;

public enum Sort {
	REGIST_TIME("registTime"), UPDATE_TIME("updateTime");
	private String code;

	private Sort(final String code) {
		this.code = code;
	}

	public static Sort resolve(final String code) {
		for (final Sort sort : values()) {
			if (sort.toString().equals(code)) {
				return sort;
			}
		}

		throw new IllegalArgumentException("no such enum object for the code: "
				+ code);
	}

	@Override
	public String toString() {
		return this.code;
	}
}
