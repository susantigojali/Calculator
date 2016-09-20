package calculator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Reader {
	private final String filepathread; // path dari file pda untuk dibaca
	private char start; // start state
	private List<String> finish; // finish state
	private Vector<String> rule;

	public Reader(String _filepath) {
		filepathread = _filepath;

	}

	public void Read() throws IOException {
		// mencari file
		File read = new File(filepathread);

		if (!read.exists()) { // jika file tidak ditemukan
			System.out.println("File not found!");
		}

		try (FileReader Fr = new FileReader(read)) { // membaca isi file
			BufferedReader BR = new BufferedReader(Fr); // membaca satu line

			String dummy; // string dummy sebagai reader
			rule = new Vector<>();
			dummy = BR.readLine();
			System.out.println(dummy);

			int i = 0;
			while (dummy.charAt(i) != '}') {
				i++;
			}
			i++;
                        System.out.println("hoo "+i);

			while (dummy.charAt(i) != '}') {
				i++;
			}
			i = i + 4;
                        System.out.println("hal "+i);


			start = dummy.charAt(i); // startstate
                        System.out.println("start "+start);

                        while (dummy.charAt(i) != '{') {
				i++;
                                System.out.println("haloo "+i);

			}
                        i++;
                        System.out.println(" "+i);

                        finish=new ArrayList<>();
                        while(dummy.charAt(i)!='}' )
                        {
                             System.out.println(" sasda "+i);

                            if(dummy.charAt(i)!= ',')
                            {
                                String temp= String.valueOf(dummy.charAt(i));
                                finish.add(temp);
                                System.out.println(" sasadd "+i);

                            }
                            i++;
                        }
                        
                        i=0;
                        for(;i<finish.size();i++)
                        {
                           System.out.println(finish.get(i));
                           
                        }
 
                        
			while ((dummy = BR.readLine()) != null) {
				String temp = "" + (dummy.charAt(2)) + "" + (dummy.charAt(4))
						+ "" + (dummy.charAt(8));
				rule.addElement(temp);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public String Start(String input, int index) {
		String output = "";
		char now = start;
		boolean dead = false;

		while (!dead && Calculator.cursor < input.length()) {
			boolean found = false;
			int i = 0;
			while (!found && i < rule.size()) {
				if (rule.elementAt(i).charAt(0) == now
						&& rule.elementAt(i).charAt(1) == input.charAt(Calculator.cursor)) {
					found = true;
				} else {
					i++;
				}
			}

			if (found) {
				now = rule.elementAt(i).charAt(2);
				output = output + input.charAt(Calculator.cursor);
				Calculator.cursor++;
			} else {
				dead = true;
			}
		}

                boolean ketemu=false;
                int j=0;
                
                while(!ketemu && j<finish.size())
                {
                    if (now == finish.get(j).charAt(0))// cek apkah final state?
                    {
		
                        ketemu=true;
                    }
                    j++;
                }
		
                if(!ketemu)
                {
                    output=null;
                }
		return output;

	}
}