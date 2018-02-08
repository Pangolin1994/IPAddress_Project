import java.util.Scanner;
import java.util.regex.*;

class IP {
	private final String ip;
	
	IP(String ip) {
		this.ip = ip;
	}
	
	private String getIp() {
		return ip;
	}
	
	// Removes spaces between input addresses and returns changed string
	private static String[] checkInput() {
		Scanner sc = new Scanner(System.in);
		String addresses = sc.nextLine(), Trim_addresses;
		int space1 = addresses.indexOf(" ", 7);
		int space2 = addresses.lastIndexOf(" ");
		if (space2 >= space1 && space1 > -1)
			Trim_addresses =  addresses.replace(addresses.substring(space1, space2), "");
		else
			throw new NumberFormatException("Неверный ввод");
		return Trim_addresses.split(" ");
	}
	
	// Check valid format of address by Java Regexp classes
	private void checkWithRegexp() {
		Pattern pt = Pattern.compile("(25[0-5]|2[0-4]\\d|[1]?\\d?\\d)(\u002E(25[0-5]|2[0-4]\\d|[1]?\\d?\\d)){3}");
		Matcher mt = pt.matcher(ip);
		if (!mt.matches())
			throw new NumberFormatException("Адрес не удовлетворяет регулярному выражению");
	}
	
	// Process addresses for rightness format
	private void check(IP other) throws NumberFormatException {
		checkWithRegexp();
		other.checkWithRegexp();
		String[] ip1 = ip.split("\\.");
		String[] ip2 = other.getIp().split("\\.");
		
		// Both addresses doesn't have identical length or one have length differently from 4
		if (ip1.length != ip2.length || ip1.length != 4)
			throw new NumberFormatException("Длины обоих адресов должны быть равны 4: "+ip1.length+" "+ip2.length);
		
		for (int i = 0; i < 4; ++i) {
			// Addresses must contain numbers only in 0...255
			if (Integer.valueOf(ip1[i]) < 0 || Integer.valueOf(ip1[i]) > 255)
				throw new NumberFormatException("Адрес должен включать числа в диапазоне [0,255]");
			
			// IF numbers on appropriate places:
			// 1) first less than second => addresses format is legal
			// 2) first greater than second => throw an exception because of wrong format
			// 3) else => go to the next number
			if (Integer.valueOf(ip1[i]) < Integer.valueOf(ip2[i])) break;
			else if (Integer.valueOf(ip1[i]) > Integer.valueOf(ip2[i]))
				throw new NumberFormatException("Неверный ввод "+ip1[i]+" > "+ip2[i]);
		}
	}
	
	// Process boundary situations like 192.168.0.255 -> 192.168.1.0
	private static void checkBoundaryValues(String[] values) {
		int length = values.length - 1, incr = -1;
		while (Integer.valueOf(values[length--]) == 255) {
			incr = length;
		}
		if (incr != -1) {
			int dec = Integer.valueOf(values[incr]);
			values[incr] = String.valueOf(++dec);
			for (int i = incr + 1; i < values.length; ++i)
				values[i] = String.valueOf(0);
		}
	}
	
	private static String[] executeChecks() {
		String[] inputs = checkInput();
		IP ip1 = new IP(inputs[0]);
		IP ip2 = new IP(inputs[1]);
		ip1.check(ip2);
		return new String[] {ip1.getIp(), ip2.getIp()};
	}
	
	public static void runComputations() {
		try {
			String[] addr = executeChecks();
			String[] addr1 = addr[0].split("\\.");
			String[] addr2 = addr[1].split("\\.");
			checkBoundaryValues(addr1);
			int x1 = Integer.valueOf(addr1[0]);
			int x2 = Integer.valueOf(addr1[1]);
			int x3 = Integer.valueOf(addr1[2]);
			int x4 = Integer.valueOf(addr1[3]);
			int y1 = Integer.valueOf(addr2[0]);
			int y2 = Integer.valueOf(addr2[1]);
			int y3 = Integer.valueOf(addr2[2]);
			int y4 = Integer.valueOf(addr2[3]);
			final int MAX = 255;
			int i1, i2, i3, i4, bound2 = MAX, bound3 = MAX, bound4 = MAX;
			
			for (i1 = x1; i1 <= y1; ++i1) {
				if (i1 == y1) bound2 = y2;
				for (i2 = x2; i2 <= bound2; ++i2) {
					if (i2 == bound2 && i1 == y1) bound3 = y3;
					for (i3 = x3; i3 <= bound3; ++i3) {
						if (i1 == y1 && i2 == bound2 && i3 == bound3)
							bound4 = y4 - 1;
						for (i4 = x4+1; i4 <= bound4; ++i4) {
							System.out.println(new StringBuilder(i1 + ".").append(i2).append(".").append(i3).append(".").append(i4));
						}
						x4 = -1;
					}
					x3 = 0;
				}
				x2 = 0;
			}
		} catch (NumberFormatException iae) {
			System.out.println(iae.getMessage());
		}
	}
}