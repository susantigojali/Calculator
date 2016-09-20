package calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator {

	public enum State {
		a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s;
                
		State changeState(String str) {
			switch (str) {
			case "a":
				return State.a;
			case "b":
				return State.b;
			case "c":
				return State.c;
			case "d":
				return State.d;
			case "e":
				return State.e;
			case "f":
				return State.f;
			case "g":
				return State.g;
			case "h":
				return State.h;
			case "i":
				return State.i;
			case "j":
				return State.j;
			case "k":
				return State.k;
			case "l":
				return State.l;
			case "m":
				return State.m;
			case "n":
				return State.n;
			case "o":
				return State.o;
			case "p":
				return State.p;
			case "q":
				return State.q;
			case "r":
				return State.r;
			case "s":
				return State.s;
			default:
				return null;
			}
		}
	};

	public static final String[] OPERATORS = { "x", ":", "+", "-" };
	public static final String[] PARENTHESIS = { "(", ")" };
	public static final String[] NUMBERS = { ".", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9" };
	public static final String OPEN_PARENTHESIS = "(";
	public static final String CLOSE_PARENTHESIS = ")";
	public static final String MULTIPLIER_SIGN = "x";
	public static final String DIVIDER_SIGN = ":";
	public static final String PLUS_SIGN = "+";
	public static final String MINUS_SIGN = "-";
	public static final String DOT = ".";
	public static final String EMPTY_STACK = "Z";
	public static final String END_OF_FILE = "=";
	public static final String EPSILON = "e";

        
        private final String inputFilePDA;
        private String inputFile;
	public static int cursor;
	public static double tempDouble;
	public static State state;
        
        Calculator(String _inputFile){
            inputFile = _inputFile;
            inputFilePDA = "PDA.txt";
            cursor = 0;
            state = State.a;
        }
        
	static boolean PDA(ArrayList<PDA> inputPDA, String newtop, String top,
			Stack<String> S) {
		for (int i = 0; i < inputPDA.size(); ++i) {
			if (state.changeState(inputPDA.get(i).state[0]) == state
					&& (inputPDA.get(i).state[1].equals(newtop)
							|| inputPDA.get(i).state[1].equals("0")
							&& isnumber(newtop) || inputPDA.get(i).state[1]
							.equals(EPSILON))
					&& (inputPDA.get(i).state[2].equals(top) || inputPDA.get(i).state[2]
							.equals("0") && isnumber(top))) {
                            
				if (state.changeState(inputPDA.get(i).state[0]) == State.d
						&& state.changeState(inputPDA.get(i).state[3]) == State.e
						|| state.changeState(inputPDA.get(i).state[0]) == State.d
						&& state.changeState(inputPDA.get(i).state[3]) == State.f) {
					tempDouble = Double.parseDouble(newtop);
				}
				if (!inputPDA.get(i).state[1].equals(EPSILON)
						&& !inputPDA.get(i).state[1].equals(END_OF_FILE)
						|| state.changeState(inputPDA.get(i).state[0]) == State.p) {
					++cursor;
				}
				state = state.changeState(inputPDA.get(i).state[3]);
				switch (inputPDA.get(i).state[4]) {
				case "e":
					S.pop();
					break;
				case "1":
					S.pop();
					S.push(String.valueOf(Double.parseDouble(top) * tempDouble));
					break;
				case "2":
					S.pop();
					S.push(String.valueOf(Double.parseDouble(top) / tempDouble));
					break;
				case "3":
					S.pop();
					S.push(String.valueOf(Double.parseDouble(top) + tempDouble));
					break;
				case "4":
					S.pop();
					S.push(String.valueOf(Double.parseDouble(top) - tempDouble));
					break;
				case "5":
					tempDouble = Double.parseDouble(top);
					S.pop();
					break;
				case "6":
					S.push(String.valueOf(tempDouble));
					break;
				case "7":
					S.push(newtop);
					break;
				default:
					break;
				}
				return true;
			}
		}
		return false;
	}

	static boolean PDA(String newtop, String top, Stack<String> S) {
		switch (state) {
		case a:
			if (newtop.equals(OPEN_PARENTHESIS) && top.equals(EMPTY_STACK)) {
				S.push(newtop);
				state = State.b;
				++cursor;
				return true;
			}
			if (isnumber(newtop) && top.equals(EMPTY_STACK)) {
				S.push(newtop);
				state = State.c;
				++cursor;
				return true;
			}
			return false;
		case b:
			if (newtop.equals(OPEN_PARENTHESIS) && top.equals(OPEN_PARENTHESIS)) {
				S.push(newtop);
				state = State.b;
				++cursor;
				return true;
			}
			if (isnumber(newtop) && top.equals(OPEN_PARENTHESIS)) {
				S.push(newtop);
				state = State.c;
				++cursor;
				return true;
			}
			return false;
		case c:
			if (newtop.equals(DIVIDER_SIGN) && isnumber(top)
					|| newtop.equals(MULTIPLIER_SIGN) && isnumber(top)) {
				S.push(newtop);
				state = State.d;
				++cursor;
				return true;
			}
			if (newtop.equals(PLUS_SIGN) && isnumber(top)
					|| newtop.equals(MINUS_SIGN) && isnumber(top)) {
				S.push(newtop);
				state = State.h;
				++cursor;
				return true;
			}
			if (newtop.equals(CLOSE_PARENTHESIS) && isnumber(top)) {
				tempDouble = Double.parseDouble(top);
				S.pop();
				state = State.i;
				++cursor;
				return true;
			}
			return false;
		case d:
			if (newtop.equals(OPEN_PARENTHESIS) && top.equals(MULTIPLIER_SIGN)
					|| newtop.equals(OPEN_PARENTHESIS)
					&& top.equals(DIVIDER_SIGN)) {
				S.push(newtop);
				state = State.b;
				++cursor;
				return true;
			}
			if (isnumber(newtop) && top.equals(MULTIPLIER_SIGN)) {
				tempDouble = Double.parseDouble(newtop);
				S.pop();
				state = State.e;
				++cursor;
				return true;
			}
			if (isnumber(newtop) && top.equals(DIVIDER_SIGN)) {
				tempDouble = Double.parseDouble(newtop);
				S.pop();
				state = State.f;
				++cursor;
				return true;
			}
			return false;
		case e:
			if (isnumber(top)) {
				S.pop();
				S.push(String.valueOf(Double.parseDouble(top) * tempDouble));
				state = State.g;
				return true;
			}
			return false;
		case f:
			if (isnumber(top)) {
				S.pop();
				S.push(String.valueOf(Double.parseDouble(top) / tempDouble));
				state = State.g;
				return true;
			}
			return false;
		case g:
			if (newtop.equals(MULTIPLIER_SIGN) && isnumber(top)
					|| newtop.equals(DIVIDER_SIGN) && isnumber(top)) {
				S.push(newtop);
				state = State.d;
				++cursor;
				return true;
			}
			if (newtop.equals(PLUS_SIGN) && isnumber(top)
					|| newtop.equals(MINUS_SIGN) && isnumber(top)) {
				S.push(newtop);
				state = State.h;
				++cursor;
				return true;
			}
			if (newtop.equals(CLOSE_PARENTHESIS) && isnumber(top)) {
				tempDouble = Double.parseDouble(top);
				S.pop();
				state = State.i;
				++cursor;
				return true;
			}
			if (newtop.equals(END_OF_FILE) && isnumber(top)) {
				state = State.k;
				return true;
			}
			return false;
		case h:
			if (newtop.equals(OPEN_PARENTHESIS) && top.equals(PLUS_SIGN)
					|| newtop.equals(OPEN_PARENTHESIS)
					&& top.equals(MINUS_SIGN)) {
				S.push(newtop);
				state = State.b;
				++cursor;
				return true;
			}
			if (isnumber(newtop) && top.equals(PLUS_SIGN)
					|| isnumber(newtop) && top.equals(MINUS_SIGN)) {
				S.push(newtop);
				state = State.g;
				++cursor;
				return true;
			}
			return false;
		case i:
			if (top.equals(OPEN_PARENTHESIS)) {
				S.pop();
				state = State.j;
				return true;
			}
			if (top.equals(PLUS_SIGN)) {
				S.pop();
				state = State.l;
				return true;
			}
			if (top.equals(MINUS_SIGN)) {
				S.pop();
				state = State.m;
				return true;
			}
			return false;
		case j:
			if (top.equals(MULTIPLIER_SIGN)) {
				S.pop();
				state = State.e;
				return true;
			}
			if (top.equals(DIVIDER_SIGN)) {
				S.pop();
				state = State.f;
				return true;
			}
			if (top.equals(PLUS_SIGN) || top.equals(MINUS_SIGN)
					|| top.equals(OPEN_PARENTHESIS)) {
				S.push(String.valueOf(tempDouble));
				state = State.g;
				return true;
			}
			if (newtop.equals(END_OF_FILE) && top.equals(EMPTY_STACK)) {
				S.push(String.valueOf(tempDouble));
				state = State.k;
				return true;
			}
			return false;
		case k:
			if (isnumber(top)) {
				tempDouble = Double.parseDouble(top);
				S.pop();
				state = State.o;
				return true;
			}
			return false;
		case l:
			if (isnumber(top)) {
				S.pop();
				S.push(String.valueOf(Double.parseDouble(top) + tempDouble));
				state = State.n;
				return true;
			}
			return false;
		case m:
			if (isnumber(top)) {
				S.pop();
				S.push(String.valueOf(Double.parseDouble(top) - tempDouble));
				state = State.n;
				return true;
			}
			return false;
		case n:
			if (isnumber(top)) {
				tempDouble = Double.parseDouble(top);
				S.pop();
				state = State.i;
				return true;
			}
			return false;
		case o:
			if (top.equals(EMPTY_STACK)) {
				state = State.p;
				return true;
			}
			if (top.equals(PLUS_SIGN)) {
				S.pop();
				state = State.q;
				return true;
			}
			if (top.equals(MINUS_SIGN)) {
				S.pop();
				state = State.r;
				return true;
			}
			return false;
		case p:
			++cursor;
			return true;
		case q:
			if (isnumber(top)) {
				S.pop();
				S.push(String.valueOf(Double.parseDouble(top) + tempDouble));
				state = State.s;
				return true;
			}
			return false;
		case r:
			if (isnumber(top)) {
				S.pop();
				S.push(String.valueOf(Double.parseDouble(top) - tempDouble));
				state = State.s;
				return true;
			}
			return false;
		case s:
			if (isnumber(top)) {
				tempDouble = Double.parseDouble(top);
				S.pop();
				state = State.o;
				return true;
			}
			return false;
		default:
			return false;
		}
	}

	static boolean contains(String s, String[] array) {
		for (int i = 0; i < s.length(); ++i) {
			boolean b = false;
			for (int j = 0; j < array.length; ++j) {
				if (String.valueOf(s.charAt(i)).equals(array[j])) {
					b = true;
					break;
				}
			}
			if (b == false) {
				return false;
			} else {
				b = false;
			}
		}
		return true;
	}
	
	static boolean isnumber(String s) {
		for (int i=0; i<10; ++i) {
			if (s.charAt(0) - '0' == i) {
				return true;
			}	
		}
		if (s.charAt(0) == '-' && s.length() > 1) {
			return true;
		}
		return false;
	}	

	public static String readFile(String file) {
		StringBuilder sb = new StringBuilder();
		try {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line = br.readLine();
                        
                        while (line != null) {
                            sb.append(line);
                            line = br.readLine();
                        }
                    }
		} catch (IOException e) {
		}
		return sb.toString();
	}

	public static ArrayList<PDA> readPDA(String file) {
		ArrayList<PDA> rulepda = new ArrayList<PDA>();
		try { // membaca isi file
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr); // membaca satu line

			String dummy; // string dummy sebagai reader
			dummy = br.readLine();
			int i = 0;
			while (dummy.charAt(i) != '{') {
				i++;
			}
			i++;
			while (dummy.charAt(i) != '}') {
				if (dummy.charAt(i) == ',') {
					++i;
				}
				ArrayList<String> namastate = new ArrayList<String>();
				namastate.add(String.valueOf(dummy.charAt(i)));
				i++;
			}

			i++;
			while (dummy.charAt(i) != '}') {
				i++;
			}
			i++;
			while (dummy.charAt(i) != '}') {
				i++;
			}
			i = i + 4;

			char startpda = dummy.charAt(i); // start state pda
			char stackkosong = dummy.charAt(i + 2); // tanda stack kosong
			char finishpda = dummy.charAt(i + 4); // finish state pda

			while ((dummy = br.readLine()) != null) {
				String a = dummy.charAt(2) + "";
				String b = dummy.charAt(4) + "";
				String c = dummy.charAt(6) + "";
				String d = dummy.charAt(11) + "";
				String e = dummy.charAt(13) + "";

				int j = 14;
				while (dummy.charAt(j) != ')') {
					e = e + dummy.charAt(j) + "";
					j++;
				}
				PDA temp = new PDA(a, b, c, d, e);
				rulepda.add(temp);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return rulepda;
	}

	String StartPDA() throws IOException {
		if (inputFile.charAt(inputFile.length()-1) != '=') {
			inputFile += '=';
		}
		ArrayList<PDA> inputPDA = readPDA(inputFilePDA);
		Stack<String> S = new Stack<>();
		S.push(EMPTY_STACK);

		boolean berhasil = true;
		while (cursor != inputFile.length() && berhasil) {
			String parsed = String.valueOf(inputFile.charAt(cursor));
			if (isnumber(parsed)) {
				Reader FA = new Reader("FA.txt");
				FA.Read();
				String outfa = FA.Start(inputFile, cursor);
				cursor--;
				if (outfa == null) {
					berhasil = false;
				} else {
					parsed = outfa;
				}

			}
			else if (parsed.equals("(") && inputFile.charAt(cursor + 1) == '-') {
				Reader FA = new Reader("FA.txt");
				FA.Read();
				cursor++;
				String outfa = FA.Start(inputFile, cursor);

				if (outfa == null) {
					berhasil = false;
				} else {
					if (inputFile.charAt(cursor) == ')') {
						parsed = outfa;
					} else {
						berhasil = false;
					}
				}
			}
			
			if (berhasil) {
				berhasil = PDA(inputPDA, parsed, S.peek(), S);
			}
		}

		if (berhasil) {
                        return String.valueOf(tempDouble);
		} else {
                        return null;
                        
		}

	}
}
