package Test.testsimple;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork{
	
	List<String>question;
	private int peerId;
	final private String adress;
	public Connector con;
	private ArrayList<Object[]> friendList;
	public SemanticHarmonySocialNetworkImpl(int id, String adress) throws Exception {
		 question=new ArrayList<String>();
		 peerId=id;
		 question.add("Ho una parola gentile per tutti");
		 question.add("Ho una parola gentile per tutti");
		 question.add("Ho una parola gentile per tutti");
		 question.add("Ho una parola gentile per tutti");
		 this.adress=adress;
		 friendList=new ArrayList<Object[]>();
		/* peer= new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT+peerId).start();
	 	_dht = new PeerBuilderDHT(peer).start();
	 	FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(adress)).ports(DEFAULT_MASTER_PORT).start();
 		fb.awaitUninterruptibly();
 		if(fb.isSuccess()) {
 			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
 		}else {
 			throw new Exception("Error in master peer bootstrap.");
 		}
 		/*peer.objectDataReply(new ObjectDataReply() {
 			
 			public Object reply(PeerAddress sender, Object request) throws Exception {
 				return _listener.parseMessage(request);
 			}
 		});*/

	}
	@Override
	public List<String> getUserProfileQuestions() {
		List <String> answer=new ArrayList<String>();
		for(int i=0;i<question.size();i++){
			TextIO textIO = TextIoFactory.getTextIO();
			TextTerminal terminal = textIO.getTextTerminal();
		answer.add( textIO.newStringInputReader().withDefaultValue("defaultValue").read(question.get(i)));
		}
		return answer;
	}

	@Override
	public String createAuserProfileKey(List<Integer> _answer) {
		String key="";
		for (int i=0;i<_answer.size();i++) {
			//if(_answer.get(i).equals(1))
			key=key+_answer.get(i);
		}
		return key;	
	}

	@Override
	public boolean join(String _profile_key, String _nick_name) {
		try {
			 class MessageListenerImpl implements MessageListener{
 			int peerid;
 			
 			public MessageListenerImpl(int peerid)
 			{
 				this.peerid=peerid;
 				//type.valueOf(_nick_name);

 			}
 			public Object parseMessage(Object obj) {
 				
 				TextIO textIO = TextIoFactory.getTextIO();
 				TextTerminal terminal = textIO.getTextTerminal();
 				;
 				terminal.printf("\n"+peerid+"] (Direct Message Received) "+obj+"\n\n");
 				//String a= (String) obj;
 					try {
 						App a = (App) obj;
 						
 						if(a.getMytype()==App.type.friends) {
 						//terminal.printf("\n"+peerid+"] (Direct Message Received) to nickname "+a.getNickname()+"\n\n");
 						//if(textIO.newBooleanInputReader().withDefaultValue(false).read("add?")) {
 			 				if(hammingDistance(a.getText(), _profile_key)<2) {
						//con.getFriends(_nick_name, a.getNickname());
						//terminal.printf("\n"+"] (Direct Message Received) message"+a.getText()+"\n\n");
 			 					Object newFriends[]= {a.getNickname(),a.getAdress()};
 			 					if(!friendList.contains(newFriends))
 			 					{
 			 					terminal.printf("\n"+peerid+" invia response amico con i dati che ha i dati"+a+"\n\n");
 			 					terminal.printf("\n"+peerid+" risultati getfreinds"+con.getFriends5(_nick_name, a.getNickname())+"\n\n");
						
 			 					friendList.add(newFriends);
 			 					}
 			 				}else {
 			 					Object newFriends[]= {a.getNickname(),a.getAdress()};
 			 					if(friendList.contains(newFriends))
 			 					{
 			 					terminal.printf("\n"+peerid+" rimosso amico con i dati che ha i dati"+a+"\n\n");
 			 			
 			 					friendList.remove(newFriends);
 			 					}
 			 					terminal.printf("\n"+peerid+" amicizia non corrisposta"+a+"\n\n");
 			 				}
						//}
						}else if(a.getMytype()==App.type.chat){
							terminal.printf("\n"+peerid+"] (Direct Message Received) message"+a.getText()+"\n\n");
							}else if(a.getMytype()==App.type.multichat){
								terminal.printf("\n"+peerid+"] (Direct Message Received) message"+con.getmultichat(_nick_name, a.getNickname())+"\n\n");
								} else {
								terminal.printf("\n"+peerid+" aggiunge un nuovo amico che ha i dati"+a+"\n\n");
								Object newFriends[]= {a.getNickname(),a.getAdress()};
								friendList.add(newFriends);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				
 				return "success";
 			}
 		
    	
 		}
			con=new Connector(peerId, adress, new MessageListenerImpl(peerId));
			/*con.searchFriends("name", _nick_name, _profile_key);
			con.connection("name", _nick_name);*/
			if(peerId==0) {
			con.store("test", "ip");
			//con.store(_nick_name,"null");
			con.store2(_nick_name,"null");
			}else {
				con.store("test", "ip");
				con.store2(_nick_name,"null");
				//con.get( "test");
				System.out.println("nick name per search ="+_nick_name);
				con.searchFriends2("test", _nick_name, _profile_key);
			}
			//con.store2(_nick_name,"null");
			con.get( "test");
			//con.get( _nick_name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		return false;
	}
	public void message(String nick) {
		TextIO textIO = TextIoFactory.getTextIO();
	    TextTerminal terminal = textIO.getTextTerminal();
		String destination=textIO.newStringInputReader().withDefaultValue("default").read("destination");
	    //int destination=textIO.newIntInputReader().withDefaultValue(0).read("id destinazione");
		int i=0;
		/*while(!destination.equals(friendList.get(i)[0])) {
			i++;
		}*/
		String message=textIO.newStringInputReader().withDefaultValue("default").read("message");
		//con.sendMessage2((PeerAddress) friendList.get(i)[1], nick, message);
		con.sendMessagebyid(destination, nick, message);
		
	}
	public void groupChat() {
		ArrayList<PeerAddress> peerfreinds=new ArrayList<PeerAddress>();
		System.out.print("AAAAAAAAAAAAAAAAAAAAAAAAAA friends"+friendList.size());
		for (int i=0;i<friendList.size();i++) {
			System.out.println("peer ="+friendList.get(i)[0]+" peeradress" +friendList.get(i)[1]);
			peerfreinds.add((PeerAddress) friendList.get(i)[1]);
		}
		con.createGroupChat("gruppo", peerfreinds);
	}
	 public int hammingDistance(String a, String b) {
			int count=0;
			System.out.print("AAAAAAAAAAAAAAAAAAAAAAAAAA"+a.length());
			for (int i=0; i<a.length();i++) {
				if(a.charAt(i)!=b.charAt(i))
					count++;
			}
			return count;
		}
	@Override
	public List<String> getFriends() {
		ArrayList<String> nameFriend= new ArrayList<>();
		for(int i=0;i<friendList.size();i++) {
			nameFriend.add((String) (friendList.get(i))[0]);
		}
		return nameFriend;
	}
	public void changeKey(String nickName) {
		List<String> question= getUserProfileQuestions();
    	List <Integer> answer =new ArrayList<>();
    	for (int i=0; i<question.size();i++) {
    		answer.add(Integer.parseInt(question.get(i)));
    	}
    	String key= createAuserProfileKey(answer);
    	friendList.clear();
    	try {
			con.searchFriends2("test", nickName, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
