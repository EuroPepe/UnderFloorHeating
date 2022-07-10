package underfloorheating;

import java.util.Scanner;

/**
 *
 * @author EuroPepe
 */
public class Input {
    Input(){
        run();
    }
    private int x, y; //delky sten
    private int pitch; //bezna roztec
    private int increaser;  //vychozi roztec od steny a zaroven hodnota, o kterou se vychozi roztec zvysuje s kazdou dalsi smyckou
    private int radius;  //poomer ohybu trubek
    Scanner sc = new Scanner(System.in);
    
    private void run(){
        System.out.printf("\n  Program vypocitava delku topnych okruhu podlahoveho vytapeni");
        System.out.printf("\nZadej delku steny, soubezne s privodni tubkou:  ");
        setX();
        System.out.printf("\nZadej delku druhe steny:  ");
        setY();
        System.out.printf("\nZadej standartni roztec trubek:  ");
        setPitch();
        System.out.printf("\nZadej roztec okrajove zony:  ");
        setIncreaser();
        System.out.printf("\nZadej polomer ohybu trubek:  ");
        setRadius();
    }
    private void setX(){
        x= Integer.parseInt(sc.nextLine());
    }
    private void setY(){
        y= Integer.parseInt(sc.nextLine());
    }
    private void setPitch(){
        pitch= Integer.parseInt(sc.nextLine());
    }
    private void setIncreaser(){
        increaser= Integer.parseInt(sc.nextLine());
    }
    private void setRadius(){
        radius= Integer.parseInt(sc.nextLine());
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getPitch(){
        return pitch;
    }
    public int getIncreaser(){
        return increaser;
    }
    public int getRadius(){
        return radius;
    }
}
