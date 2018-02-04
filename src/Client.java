import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Scanner;

public class Client extends Thread {
	
	private int aufgabe;
	private boolean terminate;
	private Main main;
	
	//auskommentiertes wie dieses sind verworfene Ideen 
	
	//private int port;
	//private String name;
	//private LinkedList<Peer> list;
	
	public Client(Main m, int a) {
	//	name = n ;
	//	port = p;
		main = m;
		aufgabe = a;
		terminate = false;
	//	list = new LinkedList<Peer>();
	}
	
	public boolean status() {
		return !terminate;
	}
	public void end() {
		terminate = true;
	}	
	
	@Override
	public void run() {
		if(aufgabe < 0) {                           //der Client mit dieser Aufgebe poked alle 30s
			long time = System.currentTimeMillis();
			while(!terminate) {
			 {
				try {
					if(time + 30000 < System.currentTimeMillis()) {
						
						poke(main.getMyName(), InetAddress.getByName("localhost").toString(), main.getMyPort());
						for(Peer peer : main.getList()) {
							if(peer.getTime() +60000 < System.currentTimeMillis())
							{
								removes(peer);
								
							}						
						}
					time = System.currentTimeMillis();	
					}
				}catch(Exception e) {System.out.println("poker crashed");}
			 }
			}
		}
		
		else if( aufgabe == 0){         //der Client mit dieser Aufgabe wartet auf eine eingehende verbindung
			try {
			
			while(!terminate) {
				System.out.println("still listening");
				listen();
			}
		
			}catch(Exception e) {}
		}
		else {                       //der Client mit dieser Aufgabe wartet auf Nutzereingaben
			
			Scanner scanner = new Scanner(System.in);
			while(!terminate) {
				
				erwarteEingabe(scanner);
			}
			scanner.close();
		}
	}
	
	
	
	/*
	 * 
	 * 
	 * Methoden zum senden 
	 * 
	 * 
	 * */
	//Sender von (n , add, p) nach peer
	  
	
	public void send(String text,String n, String add, int p,  Peer peer) {
		try {					
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(InetAddress.getByName(peer.getIP()), peer.getPort()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(text);
			out.println(n);
			out.println(add);
			out.println(p);
			System.out.println("gesendet");
			socket.close();	
		}catch(Exception e) {System.out.println("send crashed");}
	}
	
	public void sentToUnkown(String text, String add, int p) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(InetAddress.getByName(add), p));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(text);
			out.println(main.getMyName());
			out.println(InetAddress.getByName("localhost").toString());
			out.println(main.getMyPort());
			System.out.println("gesendet an neuen nutzer");
			socket.close();
			
		}catch(Exception e) {System.out.println("stu crashed");}
	}
	
	
	/*
	 * 
	 * 
	 * Methoden zum senden der unterschiedlichen eingaben
	 * 
	 * 
	 * */
	
	//Sendet pokenachricht an peer 
	//n , add, p sind name ip und port des Absenders
	
	public void poke(String n, String add, int p) {
		System.out.println("sende poke an alle");
		String poke = "//Poke";
		for(Peer peer : main.getList()) {
			send(poke, n, add, p, peer);
		}
	}
	
	//Sendet Disconnect NAchricht an peer
	//n , add, p sind name ip und port des Absenders
	
	public void disconnect(String n, String add, int p) {
		System.out.println("sende disconn an alle");
		String disc = "//Disc";
		for(Peer peer : main.getList()) {
			send(disc, n, add, p, peer);
		}
	}
	
	//Sendet eine Nachricht an alle mit dem Namen n
	
	public void m(String text , String n) {
		try {
			String myAdd = InetAddress.getByName("localhost").toString();
			for(Peer peer : main.getList()) {
				if(peer.getName().equals(n)) {
					send(text, main.getMyName(), myAdd , main.getMyPort(), peer);
				}
			}
		}catch(Exception e) {System.out.println("m crashed");}
	}
	
	//Sendet eine Nachricht an ip add und port p
	
	public void mx(String text, String add, int p) {
		try {
			String myAdd = InetAddress.getByName("localhost").toString();
			Iterator<Peer> i = main.getList().iterator();
			boolean stop = false;
			while(i.hasNext() && !stop) {
				Peer peer = i.next();
				if(peer.getIP().equals(add) && p == peer.getPort() );
				{
					send(text ,main.getMyName() , myAdd, main.getMyPort(), peer);
					stop = true;
				}
			}
		}catch(Exception e) {System.out.println("mx crashed");}
	}
	
	/*
	 * FALSCH POSITIONIERT
	 * 
	 * 
	 *Operationen auf der Peerliste 
	 * 
	 * ------------Irgendwie funktioniert das mit der liste nicht sooooo optimal--------------------
	 * 
	 */
	
	public synchronized Peer getPeer(String n, String add, int p) {
		Iterator<Peer> i = main.getList().iterator();
		while(i.hasNext()) {
			Peer peer = i.next();
			if(peer.getName().equals(n) && peer.getIP().equals(add) && p == peer.getPort() );
			{
				return peer;
			}
		}
		return null;
	}
	
	public synchronized boolean inList(String n, String add, int p) {
		Iterator<Peer> i = main.getList().iterator();
		while(i.hasNext()) {
			Peer peer = i.next();
			if(peer.getName().equals(n) && peer.getIP().equals(add) && p == peer.getPort() );
			{
				return true;
			}
		}
		return false;
		
	} 
	public synchronized void adds(Peer p) {
		main.getList().add(p);

		System.out.println(p.getName() + " wurde hinzugefuegt " + main.getList().size());
	}
	public synchronized void removes(Peer p) {
		main.getList().remove(p);
	}
	public synchronized void printList() {
		System.out.println(main.getList().toString());
	}
	
	public void remove(String n, String add, int p) {
		Iterator<Peer> i = main.getList().iterator();
		boolean stop = false;
	while(i.hasNext() && !stop) {
			Peer peer = i.next();
			if(peer.getName().equals(n) && peer.getIP().equals(add) && p == peer.getPort() );
			{
				removes(peer);
				stop = true;
			}
		}
	}
	
	/*
	 * 
	 * 
	 * Werden aufgerufen wenn eine Poke/Disconnect empfangen wurde
	 * 
	 *-----------Fehlerhaft ab 3ter Person 
	 * 
	 * */
	
	public void gotPoked(String n, String add, int p) {
		System.out.println("poke erhalten");
		if(inList(n, add, p)) {
			getPeer(n, add, p).update();
		}
		else {
			try {
				String myAdd = "localhost";
				
				poke(n, add, p);
				adds(new Peer(n, add, p ));
				
				
				send("//Poke", main.getMyName(), myAdd, main.getMyPort(), getPeer(n, add, p));
			}catch(Exception e) {System.out.println("gpoke crashed");}
		}
	}
	
	public void gotDiscon(String n, String add, int p) {
		System.out.println("disconn erhalten");
		if(inList(n, add, p)) {
			
			if(inList(n, add, p)) {
				remove(n, add, p);
			}
			disconnect(n, add, p);
		}
	}
	
	/*
	 * 
	 * 
	 * 
	 * wartet auf eine eingehende Verbindung
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void listen() {	
		try {
			ServerSocket ssocket = new ServerSocket();
			ssocket.bind(new InetSocketAddress(main.getMyPort()));
			Socket eing = ssocket.accept();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(eing.getInputStream()));
			String text = in.readLine();
			String n = in.readLine();
			String add = in.readLine();
			int p = Integer.parseInt(in.readLine());
			
			
			System.out.println("daten empfangen  " + text + n + add + p);
			
			in.close();
			eing.close();
			ssocket.close();
			
			if(text.equals("//Poke")) {
				System.out.println("poke empfangen");
				gotPoked(n, add, p);
			}
			else if(text.equals("//Disc")) {
				System.out.println("disconn empfangen");
				gotDiscon(n, add, p);
			}else {
				System.out.println(n);
				System.out.println(text);
			}
			
		}catch(Exception e) {System.out.println("listen crashed");}		
		
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * Wartet auf die erforderlichen Nutzereingaben
	 * und verarbeitet diese
	 * 
	 * ----------bisher nur CONNECT und MX sind getestet und funktionieren weitesgehend----------
	 * ----------DISCONN und EXIT noch nicht fertig bzw getestet-----------------
	 * ----------M sendet noch nicht an alle mit dem namen---------------
	 * 
	 * 
	 * 
	 * 
	 * */
	
	public void erwarteEingabe(Scanner scanner){
		
		
		while(!scanner.hasNextLine()) {}
		
		System.out.println("Erwarte Eingabe");
		String eing = scanner.nextLine();
		
		
		
		if(eing.equals("CONNECT")) {
			System.out.println("Bitte geben die die IP des zu erreichenden ein");
			String ip = scanner.nextLine();
			System.out.println("Bitte geben die den Port des zu erreichenden ein");
			int port = Integer.parseInt(scanner.nextLine());
			
			sentToUnkown("//Poke", ip, port);
		}
		else if(eing.equals("DISCONNECT")) {
			try {
				synchronized(main.getList()) {
					disconnect(main.getMyName(), InetAddress.getByName("localhost").toString(), main.getMyPort());
					while(!main.getList().isEmpty()) {
						main.getList().removeFirst();
					}
				}
			}catch(Exception e) {}
			
			
		}
		else if (eing.equals("EXIT")) {
			try {
				disconnect(main.getMyName(), InetAddress.getByName("localhost").toString(), main.getMyPort());
				end();
			}catch(Exception e) {}
			
						
			
		}
		else if(eing.equals("M")) {
			
			System.out.println("Bitte geben sie den Namen des zu erreichenden ein");
			String name = scanner.nextLine();
			System.out.println("Bitte geben sie den Text ein");
			String txt = scanner.nextLine();
			
			m(txt, name);
		}
		else if(eing.equals("MX")) {
			
			System.out.println("Bitte geben sie die ip des zu erreichenden ein");
			String ip = scanner.nextLine();
			System.out.println("Bitte geben sie den Port des zu erreichenden ein");
			int port = Integer.parseInt(scanner.nextLine());
			System.out.println("Bitte geben sie den Text ein");
			String txt = scanner.nextLine();
			
			mx(txt, ip, port);
			}
		else {
			System.out.println("CONNECT, DISCONNECT, EXIT, M, MX sind gueltige Befehle");
		}
	}
	
}
