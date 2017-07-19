package com.miaxis.storageroom.event;

public class ChangeFingerDialogMsgEvent {
	private String content;

	public ChangeFingerDialogMsgEvent(String content) {
		super();
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
