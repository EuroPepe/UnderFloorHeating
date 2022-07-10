package underfloorheating;

import java.util.LinkedList;

/**
 * trida obsluhuje tridy pro vypocet delky potrubi
 * @author EuroPepe
 */

public class Logic {
    Input input = new Input();
    private int x = input.getX();
    private int y = input.getY();
    private int pitch = input.getPitch();
    private int increaser = input.getIncreaser();
    private int radius = input.getRadius();
    
    private int[] pitchArrayX;
    private int[] pitchArrayY;
    
    public void run(){
        IncreasedPitch increasedPitch = new IncreasedPitch(x, y, pitch, increaser);
        pitchArrayX = increasedPitch.getArrayX();
        pitchArrayY = increasedPitch.getArrayY();
        CountLength countLength = new CountLength(x, y, pitchArrayX, pitchArrayY);
        System.out.printf("\nLength: %d cm\n", countLength.getLength());
        Rounding rounding = new Rounding(radius, countLength.getWallCounter(), countLength.getLoopCounter(), countLength.getLength());
        System.out.printf("\nLength: %.2f m\n", rounding.getLength()/100);
    }
}
/**
 * trida nahradi delku pravouhleho lomeni za delku zaobleni
 * @author EuroPepe
 */
class Rounding{
    Rounding(int r,int wallCounter, int loopCounter, double lengthPipe){
        this.r=r;
        this.wallCounter=wallCounter;
        this.loopCounter=loopCounter;
        this.lengthPipe=lengthPipe;
        resizeLength();
    }
    private final int r, wallCounter, loopCounter;
    private double lengthPipe;
    //metoda vrati celkovy pocet zaobleni
    private int countNumberRounding(){
        //loopCounter vyjadruje pocet smycek, tj.: pocet uplnych smycek okolo sten, proto se nasobi ctyrma
        //wallCounter vyjadruje pocet vypocitanych sten y necele smycky
        // nasobi se cele dvema, bo trubky jsou dve
        int i = ((loopCounter*4)+wallCounter)*2;
        return i;
    }
    //metoda spocita delku vsech zaobleni
    private double countRoundingLength(){
        double length = ((Math.PI*r)/2.0)*(double)countNumberRounding(); //delka ctvrtKruznice krat pocet
        return length;
    }
    //metoda nahradi delku lomeni za delku zaobleni
    private void resizeLength(){
        //lengthPipe je spocitana delka potrubi s pravouhlym lomenim
        //2*r*countNumberRounding() je delka, pravouhleho lomeni, ktera se nahradi delkou zaobleni
        lengthPipe = lengthPipe-(2.0*r*(double)countNumberRounding())+countRoundingLength();
    }
    protected double getLength(){
        return lengthPipe;
    }
}
/**
 * trida vypocita delku potrubi
 * @author EuroPepe
 */
class CountLength{
    CountLength(int X, int Y, int[]PITCH_ARRAY_X, int[]PITCH_ARRAY_Y){
        this.X=X;
        this.Y=Y;
        this.PITCH_ARRAY_X=PITCH_ARRAY_X;
        this.PITCH_ARRAY_Y=PITCH_ARRAY_Y;
        countInput();  
    }
    private int length = 0;  //spocitana delka
    private boolean bool = true;  //podminka pokracovani cyklu
    private int wallCounter = 1; //pocitadlo sten mistnosti, dulezite pro vypocet indexu
    private int loopCounter = 1;
    //indexy pro pole vzdalenosti trubek od steny
    private int a = 1;
    private int b = 2;
    private int c, d;
    
    private final int X, Y;  //delky sten
    private int auxWall;  //pomocna promenna, ktera obsahuje prave potrebnou stenu
    //pomocne promenne do kterych se uklada momentalne spocitana delka trubky
    private int auxHot;
    private int auxCold;
    //pole vydalenosti trubek od sten
    private final int[] PITCH_ARRAY_X;
    private final int[] PITCH_ARRAY_Y;
    
    //cyklus obsluhujici metody pro vypocet a pro nastavovani atributu
    private void looping(){
        do{
            wallCounter++;
            loopCount();
            //changeAxis();
            changeIndex(wallCounter, loopCounter);
            count(changeAxis());
            plusLength();
        }while(bool);
    }
    //vypocet delky vstupniho potrubi (je samostatn2, protoze pocita jinak
    private void countInput(){
        auxWall = X;
        auxHot = X - PITCH_ARRAY_X[a];
        auxCold = X - PITCH_ARRAY_Y[b];
        booling(PITCH_ARRAY_X);
        plusLength();
        looping();
    }
    //vypocet delky potrubi dane casti
    private void count(int[]array){
        auxHot = auxWall-array[a]-array[array.length-c];
        auxCold = auxWall-array[b]-array[array.length-d];
        booling(array);
    }
    //zmena osy (potazmo zdi, od ktere se bude delka pocitat)
    private int[] changeAxis(){
        if(auxWall == X){
            auxWall = Y;
            return PITCH_ARRAY_Y;
        }
        else{
            auxWall = X;
            return PITCH_ARRAY_X;
        }
    }
    //metoda pocita steny, u ctvrte inkrementuje smycku a nastavi pocatecni hodnotu pocitadla
    private void loopCount(){
        if(wallCounter==5){
            loopCounter++;
            wallCounter = 1;
        }
    }
    //metoda zmeni nastaveni jednotlivych indexu pro momentalni vypocet
    private void changeIndex(int i, int j){
        switch(i){
            case 1: a=j*2-1; b=j*2; c=j*2-3; d=j*2-2; break;
            case 2:
            case 3: a=j*2-1; b=j*2; c=a; d=b; break;
            case 4: a=j*2-1; b=j*2; c=j*2+1; d=j*2+3; break;
            default: System.out.printf("\nChyba! Na switch prisla hodnota %d\n", i); break;
        }
    }
    //nastaveni podminky pro pokracovani vypoctu
    //porovnava, zdali je prave vypocitana delka jedne studene a jedne teple trubky delsi,
    //nez posledni vzdalenost ulozena v poli, coz by mela byt standartni roztec
    private void booling(int[]array){
        bool = (auxHot>array[array.length-1])&(auxCold>array[array.length-1]);
    }
    private void plusLength(){
        length += auxHot;
        length += auxCold;
    }
    protected int getLength(){
        return length;
    }
    protected int getLoopCounter(){
        return loopCounter;
    }
    protected int getWallCounter(){
        return wallCounter;
    }
}
/**
 * trida vytvori pole vzdalenosti jednotlivych trubek od zdi pro danou osu,
 * pricemz na zacatku pole jsou stupnujici se roztece, pak uz jen standartni
 * @author EuroPepe
 */
class IncreasedPitch{

    IncreasedPitch(int LENGTH_X,int LENGTH_Y, int PITCH, int INCREASER){
        this.LENGTH_X=LENGTH_X;
        this.LENGTH_Y=LENGTH_Y;
        this.PITCH=PITCH;
        this.INCREASER=INCREASER;
        count();
    }
    
    private final int LENGTH_X;
    private final int LENGTH_Y;
    private final int PITCH;
    private final int INCREASER;
    private int[] pitchArrayX;
    private int[] pitchArrayY;
    
    private int size; //pomocna promenna delka pole
    
    private void count(){
        LinkedList<Integer> pitchList = new LinkedList<>();
        
        increasePitch(LENGTH_X, pitchList);
        pitchArrayX = new int[size];
        makeIncreasedArray(pitchArrayX, pitchList);
        
        pitchList = new LinkedList<>(); //musi se vytvorit nova reference, jinak se nove vypotu pripoji ke stare
        
        increasePitch(LENGTH_Y, pitchList);
        pitchArrayY = new int[size];
        makeIncreasedArray(pitchArrayY, pitchList);
        
        //show(pitchArrayX);
        //show(pitchArrayY);
    }
    
    //metoda vytvori list stupnujicich se rozteci od nejmensi INCREASER az po standartni PITCH
    private void increasePitch(int length, LinkedList<Integer>list){
        
        int auxI = 0;
        //chtelo by to nejakou pojistku proti potencionalnimu nekonecnemu cyklu
        for(int i = INCREASER; i<PITCH; i+=INCREASER){  //prvni cyklus pocita stupnujici se roztece
            list.add(i);
            auxI = i;  //i se uklada do pomocne promenne, abz se dalo pouzit ve druhem nezavislem cyklu
        }
        for(int i = length-auxI; i>PITCH; i-=PITCH){  //druhy cyklus dopocita zbyvajici roztece do konce delky osy
            list.add(PITCH);
        }
        size=list.size();
    }
    //metoda presouva osah listu do pole
    private void makeIncreasedArray(int[]array, LinkedList<Integer>list){
        for(int i = 0; i<array.length; i++){
            array[i]=list.get(i);
        }
        recountArray(array);
    }
    //metoda prepocita hodnoty v poli z relativnich na absolutni
    //relativni hodnoty ulozene v poli jsou roztece mezi trubkama
    //absolutni hodnoty jsou roztece kazde trubky od zdi
    private void recountArray(int[]array){
        size/=2;
        for(int i=0; i<array.length; i++){
            if(i>0 && i<=size){
                array[i]+=array[i-1];
                array[(array.length-1)-i]+=array[(array.length-1)-(i-1)];
            }
        }
    }
    //pomocna metoda pro vypis pole
    protected void show(int[]array){
        for(int i =0; i<array.length; i++){
            System.out.printf("%d ", array[i]);
        }
        System.out.println();
    }
    protected int[] getArrayX(){
        return pitchArrayX;
    }
    protected int[] getArrayY(){
        return pitchArrayY;
    }
}
