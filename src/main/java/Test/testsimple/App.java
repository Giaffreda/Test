package Test.testsimple;

import java.io.Serializable;

/**
 * Hello world!
 *
 */
public class App implements Serializable
{
    private String text;
    private int peerId;
    private String Nickname;
    public enum type{chat,friends}
    public App(String text,int peerId,String Nickname) {
    	 this.text=text;
        this.peerId=peerId;
        this.Nickname=Nickname;
    }
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getPeerId() {
		return peerId;
	}
	public void setPeerId(int peerId) {
		this.peerId = peerId;
	}
	public String getNickname() {
		return Nickname;
	}
	public void setNickname(String nickname) {
		Nickname = nickname;
	};
}
