package etm.nmethodbug;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

import etm.nmethodbug.hotswap.HotswapService;

public class Main extends Thread {
	
	private static HotswapService helloWorldService2;
	
	// Generate enough memory allocation, might work without - but was needed for me do build the demo project.
	static List<Object> list = new ArrayList<>();  
	
	public static void main(String[] args) {

		System.out.println("24_17:41:09 " + "Hello World");  
		helloWorldService2 = new HotswapService();
		Main thread = new Main(); 
		thread.start();
		
	}

	@Override
	public void run() {  
         
		while (true) {
			try {
				list = new ArrayList<>();
				for (int i = 0; i < 10000; i++) {
					list.add(new A1("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")); 
					list.add(new A2("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")); 
					list.add(new A3("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
					list.add(new A4("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
					list.add(new A5("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));  
                }
                printStats();
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace(); 
			} 
		}
	}     
  
	private static void printStats() throws Exception {
		List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
			String name = memoryPoolMXBean.getName();
			if (name.contains("CodeHeap") || name.contains("CodeCache")) {     
				MemoryUsage usage = memoryPoolMXBean.getUsage();
				System.out.println("Memory Pool Name: " + name);   
				System.out.println("  Used: " + usage.getUsed() + "/" + usage.getMax() + " bytes (percentage: " + (usage.getUsed() * 100 / usage.getMax()) + "%)");
			
			} 
		}
	}

	/**
	 * Below records/methods are used to generate large bytecode, not sure if this is needed.
	 * As far as i can tell there just needs to be enough objects alive? Thats what List<A1> list is for.
	 */
	private static final record A1(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n, String o, String p, String q, String r, String s, String t, String u, String v, String w, String x, String y, String z) {}
	private static final record A2(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n, String o, String p, String q, String r, String s, String t, String u, String v, String w, String x, String y, String z) {}
	private static final record A3(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n, String o, String p, String q, String r, String s, String t, String u, String v, String w, String x, String y, String z) {}
	private static final record A4(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n, String o, String p, String q, String r, String s, String t, String u, String v, String w, String x, String y, String z) {}
	private static final record A5(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n, String o, String p, String q, String r, String s, String t, String u, String v, String w, String x, String y, String z) {}
    
}
