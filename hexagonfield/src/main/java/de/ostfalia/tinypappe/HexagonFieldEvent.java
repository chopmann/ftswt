package de.ostfalia.tinypappe;

public enum HexagonFieldEvent {
	CONNECT_SESSION("connect"),
	INIT("init"),
	ADD_SELECTED("addSelected"),
    REMOVE_SELECTED("removeSelected"),
	CLICKED("clicked"),
	CHANGE_FIELD_BG("changeFieldBg"),
	CONTEXT_MENU("contextMenu");

	private String key;

	private HexagonFieldEvent(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key;
	}

	public static HexagonFieldEvent fromString(String key) {
		if (key != null) {
			for (HexagonFieldEvent event : HexagonFieldEvent.values()) {
				if (key.equals(event.getKey())) {
					return event;
				}
			}
		}
		return null;
	}
}