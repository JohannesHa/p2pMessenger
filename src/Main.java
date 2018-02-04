import java.net.InetAddress;
import java.util.LinkedList;

public class Main {

	private String name;
	private int port;
	private LinkedList<Peer> list;
	
	public Main(String n, int p) {  //Nutzerobjekt
		name = n;
		port = p;
		list = new LinkedList<Peer>();
	}
	
	
	//startet das Programm
	public static void main(String[] args) {
		try {

            // Eingabeparameter prüfen
            if (args.length != 2)
            {
                System.out.println("Sie müssen zwei Parameter angeben. Der erste ein Name, der zweite ein Port");
                return;
            }
            if (Integer.parseInt(args[1]) <= 0) {
                System.out.println("Der Port muss eine Zahl >0 sein.");
                return;
            }
            //TODO Expection Name eines Clients darf keine Leerzeichen/Zeilenbrüche enthalten 
     
        }
        catch(NumberFormatException nfe) {
            System.out.println("Der zweite Parameter muss eine Zahl sein, da er für einen Port steht.");
		}
		

		String n = args[0]; //Name vom Client
		int p = Integer.parseInt(args[1]); //Port vom Client
		
		Main main = new Main(n, p);

		InetAddress ipAddress = InetAddress.getLocalHost();
		String currentIp = ipAddress.getHostAddress();
		
		// Client hat gestartet und mögliche Benutzereingaben werden ausgegeben
		System.out.println("## Client started: " + n + " (" + currentIp + ":" + p + ")");
		System.out.println("	Deine möglichen Eingaben:");
		System.out.println("		CONNECT <IP> <Port>");
		System.out.println("		DISCONNECT <IP> <Port>");
		System.out.println("		EXIT");
		System.out.println("		M <Name> <Text>");
		System.out.println("		MX <Ip> <Port> <Text>");
		
		Client client = new Client(main, 1);      //fuer Nutzereingaben
		Client listener = new Client(main , 0);	  //fuer eingehende verbindungen
		Client poker = new Client(main, -1);      //zum aktuallisieren der liste
		
		client.start();
		listener.start();
		poker.start();
	//	poker.end();
	//	listener.end();
		//while(client.status()) {
			
	//	}
		
		System.out.println("geschafft");
		//client.end();
		//poker.end();
		//listen.end();
		//System.exit(1);;
	}
	
	
	public String getMyName() {
		return name;
	}
	public int getMyPort() {
		return port;
	}
	public LinkedList<Peer> getList() {
		return list;
	}
}
