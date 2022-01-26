package Test.testsimple;

import java.io.Serializable;

import net.tomp2p.peers.PeerAddress;

/**
 * Hello world!
 *
 */
public class App implements Serializable
{
    private String text;
    private int peerId;
    private PeerAddress adress;
    private String Nickname;
    public enum type{chat,friends,response}
    private type mytype;
    public App(String text,int peerId,String Nickname, PeerAddress adress) {
    	 this.text=text;
        this.peerId=peerId;
        this.Nickname=Nickname;
        this.adress= adress;
        
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
	}
	public type getMytype() {
		return mytype;
	}
	public void setMytype(type mytype) {
		this.mytype = mytype;
	};
	public String toString() {
		return " text= "+text+" peerId= "+peerId+" nickname= "+Nickname+" type ="+mytype;
	}
	public PeerAddress getAdress() {
		return adress;
	}
	public void setAdress(PeerAddress adress) {
		this.adress = adress;
	}
}
