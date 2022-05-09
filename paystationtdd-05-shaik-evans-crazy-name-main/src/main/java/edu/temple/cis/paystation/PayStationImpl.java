/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */

/*
* We're in babes 2
* */
//double checking
//checking
package edu.temple.cis.paystation;
import java.util.*;

public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;
    private Map<Integer, Integer> coinMap = new HashMap<Integer, Integer>();
    private int coinCounter = 0;

    public Map<Integer,Integer> getCoinMap(){
        return coinMap;
    }

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5: break;
            case 10:break;
            case 25: break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }

        coinMap.put(coinCounter, coinValue);
        coinCounter++;

        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        cancel();
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel() {
        Map<Integer, Integer> money = new HashMap<>();
        money.put(1, insertedSoFar);
        reset();
        coinMap.clear();
        coinCounter=0;
        //reset();
        return money;
    }
    
    private void reset() {
        timeBought = insertedSoFar = 0;
    }

    public int empty(){
        //create total and compare to sys
        int total = 0;
        int temp = insertedSoFar;
        if(insertedSoFar != total){
            insertedSoFar = 0;
        }
        reset();
        //return total of insertedsofar
        return temp;
        //displays change returned to user, sets total insertedSoFar and timeBought to 0
    }


}
