FZSWT -- Projekt 2015 (Sommer Semester)
======================================

	wildfly --> run

	hexagonfield --> install

	wildfly --> undeploy
	wildfly --> deploy

	OR 

	wildfly --> redeploy

Events
======================================
	CONNECT_SESSION("connect"),
	INIT("init"),
	ADD_SELECTEDTILE("addSelectedTile"),       			//2 Way Communication
    REMOVE_SELECTEDTILE("removeSelectedTile"),  	  	//2 Way Communication
	CLICKED("clicked"),
	CHANGE_FIELD_BG("changeFieldBg"),
	CONTEXT_MENU("contextMenu"),

	//New
	TEXTURE_MENU("textureMenu"), 					  	// Client --> Server
	ATTRIBUTE_FIELDMENU("attributeFieldMenu"),			// Client --> Server
	ATTRIBUTE_TILEMENU("attributeTileMenu"),			// Client --> Server
	ADD_SELECTEDBORDER("addSelectedBorder"),			// Server --> Client
	REMOVE_SELECTEDBORDER("removeSelectedBorder"),		// Server --> Client
	CHANGE_FIELD_FG("changeFieldFg"),					// Server --> Client
	KEY_PRESSED("keyPressed"),							// Client --> Server
	KEY_RELEASED("keyReleased"),						// Client --> Server
	RELOAD_MAP("reload"),								// Server --> Client
	GAME_START("startGame"),							// Server --> Client
	GAME_FINISH("finishGame"),							// Server --> Client
	GAME_OVER("gameOver"),								// Server --> Client
	GAME_WINCONDITION("checkedCondition"),				// Server --> Client
	GAME_ROUNDSTART("startRound"),						// Server --> Client
	GAME_ROUNDEND("endRound"),							// Server --> Client
	

