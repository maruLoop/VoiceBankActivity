package domain.entity;

public enum ActivitySort {
	UPDATE_TIME("updateTime"), PLAY_COUNT("playCount");
	private String code;

	private ActivitySort(final String code) {
		this.code = code;
	}

	public static ActivitySort resolve(final String code) {
		for (final ActivitySort sort : values()) {
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
