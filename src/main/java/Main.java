public class Main {
	static long iptoint(String ip) {
		long result = 0;
		String[] nums = ip.split("\\.");
		for (int i = nums.length - 1; i >= 0; --i)
			result += (long) Integer.valueOf(nums[i]) * (1 << (8 * (nums.length - 1 - i)));
		return result;
	}
	public static void main(String[] argc) throws NumberFormatException {
		IP.runComputations();
	}
}
