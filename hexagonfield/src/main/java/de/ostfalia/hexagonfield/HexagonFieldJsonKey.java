package de.ostfalia.hexagonfield;

public enum HexagonFieldJsonKey {
	VALUE("value"),
	SESSION_ID("sessionId"),
	INIT("init"),
	EVENT("event"),
	WIDTH("width"),
	HEIGHT("height"),
	SIZE("size"),
	FIELDS("fields"),
	FIELD_X("fieldX"),
	FIELD_Y("fieldY"),
	BACKGROUND_IMG("bgImg"),
	CONTEXT_MENU("contextMenu");

	private String key;

	private HexagonFieldJsonKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}
	
	public static HexagonFieldJsonKey fromString(String key) {
		if (key != null) {
			for (HexagonFieldJsonKey jsonKey : HexagonFieldJsonKey.values()) {
				if (key.equals(jsonKey.getKey())) {
					return jsonKey;
				}
			}
		}
		return null;
	}
}