package domain.entity;

public enum Order {
	ASC("asc"), DESC("desc");

	private String code;

	private Order(final String code) {
		this.code = code;
	}

	public static Order resolve(final String code) {
		for (final Order order : values()) {
			if (order.toString().equals(code)) {
				return order;
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
