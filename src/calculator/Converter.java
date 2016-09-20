package calculator;
import java.io.*;
import java.util.*;

public class Converter {
    private final String filepathread;  //path dari file pda untuk dibaca
    private final String filepathwrite; //path dari file cfg untuk ditulis
    private int totalstates;        //jumlah state yang ada
    private char startstate;        //start state
    private char startqueue;        //start queue    
    private final List<String> transition = new ArrayList<>();
    private char [] states;
    static public boolean emptystack;     //PDA menuju ke empty stack atau ke final state. True jika emptystack
    private char [] finalstates;
    private int jumfinalstates;
    
    public Converter(String _filepathread, String _filepathwrite){
        filepathread = _filepathread;
        filepathwrite = _filepathwrite;
        totalstates = 0;
        jumfinalstates = 0;
    }

    public void Setemptystack(boolean _emptystack){
        emptystack = _emptystack;
    }
    
    public void ReadPDA() throws IOException{
        String dummy;   //string dummy sebagai reader
        File read = new File(filepathread);
        
        if (!read.exists()){    //jika file tidak ditemukan
            System.out.println("The PDA file doesn't exist.");
            System.exit(0);
        }
        
        try (FileReader Fr = new FileReader(read)) {        //membaca isi file
        //init-----------------------------------------------------------------------
            BufferedReader BR = new BufferedReader(Fr);     //untuk membaca satu line
            dummy = BR.readLine();                          //bagian notasi PDA
            
            int i = 2;      //menghitung totalstates
            while(dummy.charAt(i)!='}'){
                if (dummy.charAt(i)!=','){
                    totalstates++;
                }
                i++;
            }
            states = new char[totalstates];
       //----------------------------------------------------------------------------
            
            i = 2;
            int j = 0;
            while(dummy.charAt(i)!='}'){            //masukan state kedalam array state
                if (dummy.charAt(i)!=','){
                    states[j] = dummy.charAt(i);
                    j++;
                }
                i++;
            }
            
            //mencari start state
            while(dummy.charAt(i)!='d'){    
                i++;
            }
            startstate = dummy.charAt(i+2); //menyimpan start state
            startqueue = dummy.charAt(i+4); //menyimpan start queue
            i=i+4; //Z
            if (dummy.charAt(i+1)==')'){    //kasus PDA menuju ke empty stack
                emptystack=true;
            }else{              //kasus PDA menuje ke final state
                emptystack=false;
                i=i+2;  //{
                int startfinal=i;
                if(dummy.charAt(i)=='{'){
                    i++;
                    startfinal=i;
                    while(dummy.charAt(i)!='}'){
                        if(dummy.charAt(i)!=','){
                            jumfinalstates++;
                        }
                        i++;
                    }
                    finalstates = new char[jumfinalstates];
                    j = 0;
                    int k = startfinal;
                    while(dummy.charAt(k)!='}'){
                        if(dummy.charAt(k)!=','){
                            finalstates[j]=dummy.charAt(k);
                            j++;
                        }
                        k++;
                    }
                } else{
                    jumfinalstates=1;
                    finalstates = new char[jumfinalstates];
                    finalstates[0]=dummy.charAt(startfinal);
                }
            }

            //menyimpan aturan transisi PDA kedalam array List
            while((dummy = BR.readLine())!=null){
                transition.add(dummy);
            }
        }
    }
    
    public void ConvertToCFG() throws IOException{
        try (FileWriter writer = new FileWriter(filepathwrite)) {
            int i=0;
            //Menulis Basis
            if(!emptystack){    //PDA bukan emptystack
                if(jumfinalstates>1){   //jumlah final state lebih dari satu
                    //for(int j=0;j<totalstates;j++){
                        for(int k=0;k<jumfinalstates;k++){
                            writer.write("S -> ");
                            writer.write("[" + startstate + startqueue + finalstates[k] + "]");
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                        i++;
                    //}
                }
                else{      //jumlah final state adalah satu
                    //for(int j=0;j<totalstates;j++){
                            writer.write("S -> ");
                            writer.write("[" + startstate + startqueue + finalstates[0] + "]");
                            writer.write(System.lineSeparator());
                        //}
                        writer.write(System.lineSeparator());
                        i++;
                    }
            }   //jika PDA menuju ke empty stack
            else{
                i=0;
                while(i<totalstates){
                    writer.write("S -> ");
                    writer.write("[" + startstate + startqueue + states[i] + "]");
                    writer.write(System.lineSeparator());
                    i++;
                }
                writer.write(System.lineSeparator()); 
            }
            //menyalin states ke states2 untuk mendapatkan kemungkinan-kemungkinan pasangan states
            char [] states2 = new char[totalstates];
            states2 = states;
            
            i=0;
            //Menerjemahkan transisi satu per satu dari List menjadi CFG
            while(i<transition.size()){
                if (transition.get(i).charAt(13)=='7'){
                    int j = 0;
                    while(j<totalstates){
                        int k = 0;
                        while(k<totalstates){
                            writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(6) + states[j] + "] -> " );
                            writer.write(transition.get(i).charAt(4));
                            writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(4) + states[k] + "]");
                            writer.write("[" + states[k] + transition.get(i).charAt(6) + states[j] + "]");
                            writer.write(System.lineSeparator());
                            k++;
                        }
                    j++;
                    }
                }
                else{
                    //Push
                    if ((transition.get(i).charAt(13)!='e') && (transition.get(i).charAt(13)!='5')){
                        //Kasus Top Stack berubah(dipush biasa)
                        if (transition.get(i).charAt(14)!=')'){
                            //Kasus Top Stack adalah simbol 6
                                int j = 0;
                                while(j<totalstates){
                                    int k = 0;
                                    while(k<totalstates){
                                        writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(6) + states[j] + "] -> " );
                                        writer.write(transition.get(i).charAt(4));
                                        writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(13) + states[k] + "]");
                                        writer.write("[" + states[k] + transition.get(i).charAt(14) + states[j] + "]");
                                        writer.write(System.lineSeparator());
                                        k++;
                                    }
                                j++;
                                }
                        }
                        //Kasus Top Stack diganti/tetap
                        else{
                            if (transition.get(i).charAt(13)=='6'){
                                int j = 0;
                                while(j<totalstates){
                                    int k = 0;
                                    while(k<totalstates){
                                        writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(6) + states[j] + "] -> " );
                                        writer.write(transition.get(i).charAt(4));
                                        writer.write("[" + transition.get(i).charAt(2) + "T" + states[k] + "]");
                                        writer.write("[" + states[k] + transition.get(i).charAt(6) + states[j] + "]");
                                        writer.write(System.lineSeparator());
                                        k++;
                                    }
                                    j++;
                                }
                            }
                            else{
                                int j=0;
                                while(j<totalstates){
                                    writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(6) + states[j] + "] -> " );
                                    writer.write(transition.get(i).charAt(4));
                                    writer.write("[" + transition.get(i).charAt(11) + transition.get(i).charAt(13) + states[j] + "]");
                                    writer.write(System.lineSeparator());
                                    j++;
                                }
                           }
                        }
                }
                //Kasus Top Stack diganti/tetap
                else{
                    writer.write("[" + transition.get(i).charAt(2) + transition.get(i).charAt(6) + transition.get(i).charAt(11) + "] -> " );
                    writer.write(transition.get(i).charAt(4));
                    writer.write(System.lineSeparator());
                }
            }
            i++;
            writer.write(System.lineSeparator());
          }
    }
}
    
    //method untuk membaca file hasil konversi PDA ke CFG
    public void ReadCFG() throws IOException {
        //retstr = null;
        File read = new File(filepathwrite);
        String dummy;
        
        try (FileReader Fr = new FileReader(read)) {        //membaca isi file
            BufferedReader BR = new BufferedReader(Fr);     //membaca satu line
            while((dummy = BR.readLine())!= null){
                //System.out.println(dummy);
                //retstr = dummy;
            }        
        }
    }
}  