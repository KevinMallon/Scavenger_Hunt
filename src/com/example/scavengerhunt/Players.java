package com.example.scavengerhunt;

public class Players {
    private String Userid;
    private String Username;
    private boolean selected;

    public Players(String username) {
	this.Username = username;
	selected = false;
    }

    public String getUserid() {
	return Userid;
    }

    public void setUserid(String Userid) {
	this.Userid = Userid;
    }

    public String getUsername() {
	return Username;
    }

    public void setUsername(String Username) {
	this.Username = Username;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

}
