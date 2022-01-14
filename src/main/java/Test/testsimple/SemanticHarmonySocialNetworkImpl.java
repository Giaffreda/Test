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
	
	public SemanticHarmonySocialNetworkImpl(int id, String adress) throws Exception {
		 question=new ArrayList<String>();
		 peerId=id;
		 question.add("Ho una parola gentile per tutti");
		 question.add("Ho una parola gentile per tutti");
		 question.add("Ho una parola gentile per tutti");
		 
		 this.adress=adress;
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

 			}
 			public Object parseMessage(Object obj) {
 				
 				TextIO textIO = TextIoFactory.getTextIO();
 				TextTerminal terminal = textIO.getTextTerminal();
 				terminal.printf("\n"+peerid+"] (Direct Message Received) "+obj+"\n\n");
 				String a = (String) obj;
 				if(textIO.newBooleanInputReader().withDefaultValue(false).read("add?")) {
 					con.connection("test", a);
 				}
 				return "success";
 			}

 		}
			con=new Connector(peerId, adress, new MessageListenerImpl(peerId));
			/*con.searchFriends("name", _nick_name, _profile_key);
			con.connection("name", _nick_name);*/
			if(peerId==0)
			con.store("test", "ip");
			else
				con.getFriends(_nick_name, "test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		return false;
	}

	@Override
	public List<String> getFriends() {
		// TODO Auto-generated method stub
		return null;
	}

}
