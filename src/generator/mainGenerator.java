package generator;

import common.RandomSingleton;

public class mainGenerator {

	public static void main(String[] args) {
		float v;
		int h;
		for(h=0;h<8;h++) {
			v = RandomSingleton.randFloat(15 +h/4, 17);
			print(h, v);
		}
		h = 8;
		v = RandomSingleton.randFloat(17, 19);
		print(h, v);

		h = 9;
		v = RandomSingleton.randFloat(17, 19);
		print(h, v);

		h = 10;
		v = RandomSingleton.randFloat(18, 20);
		print(h, v);

		h = 11;
		v = RandomSingleton.randFloat(18, 20);
		print(h, v);

		h = 12;
		v = RandomSingleton.randFloat(19, 21);
		print(h, v);

		for(h=13;h<21;h++) {
			v = RandomSingleton.randFloat(20, 24);
			print(h, v);
		}

		h = 21;
		v = RandomSingleton.randFloat(19, 21);
		print(h, v);

		h = 22;
		v = RandomSingleton.randFloat(18, 20);
		print(h, v);

		h = 23;
		v = RandomSingleton.randFloat(17, 19);
		print(h, v);

		h = 24;
		v = RandomSingleton.randFloat(16, 18);
		print(h, v);

	}

	private static void print(int h, float v) {
		System.out.println(h + ";" + v);
	}
}
