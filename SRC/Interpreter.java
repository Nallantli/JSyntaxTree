import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Interpreter {
	public static Node interpret(String filename) {
		String total = "";

		try {
			InputStream bytes = new FileInputStream(filename);
			Reader chars = new InputStreamReader(bytes, StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(chars);
			String line;
			while ((line = br.readLine()) != null) 
				total = total.concat(line);
			br.close();
		} catch(IOException e) {
			//..
		}

		Stack<String> tokens = new Stack<String>();
		tokens.push("");
		int mode = 0;
		for (char c : total.toCharArray()) {
			if (mode == 3) {
				if (c == '`') {
					mode = 0;
				} else {
					String s = tokens.pop();
					s = s.concat(Character.toString(c));
					tokens.push(s);
				}
				continue;
			}
			if (c == '`') {
				mode = 3;
				tokens.push("");
				continue;
			}
			if (c == '\t' || c == '\n' || c == ' ') {
				mode = 0;
				if (!tokens.peek().equals(""))
					tokens.push("");
				continue;
			}
			if (c != '[' && c != ']'){
				if (mode == 1) {
					String s = tokens.pop();
					s = s.concat(Character.toString(c));
					tokens.push(s);
				} else {
					tokens.push(Character.toString(c));
				}
				mode = 1;
			} else {
				if (tokens.peek().equals(""))
					tokens.pop();
				tokens.push(Character.toString(c));
				mode = 2;
			}
		}

		System.out.println(tokens);

		Stack<Node> stack = new Stack<Node>();

		for (String token : tokens) {
			if (token.equals("]")) {
				ArrayList<Node> nl = new ArrayList<Node>();
				while (stack.peek().value.length() < 2 || !stack.peek().value.subSequence(0, 2).equals("$[")) {
					nl.add(stack.pop());
				}
				Collections.reverse(nl);
				Node f = stack.pop();
				f.value = f.value.substring(2);
				f.subNodes = nl;
				stack.push(f);
			} else if (token.equals("[")) {
				stack.push(new Node("$["));
			} else if (!token.isEmpty()){
				if (stack.peek().value.length() > 2 && stack.peek().value.subSequence(0, 2).equals("$["))
					stack.peek().metadata = stack.peek().metadata.concat(" ").concat(token);
				else
					stack.peek().value = stack.peek().value.concat(" ").concat(token);
			}
		}

		System.out.println(stack);

		return stack.pop();
	}	
}