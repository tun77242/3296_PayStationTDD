/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package edu.temple.cis.paystation;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class PayStationImplTest {

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
    }

    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }


    /** Call to empty() total amount entered **/
    @Test
    public void shouldReturnTotalEntered() throws IllegalCoinException {
        ps.addPayment(10);
        assertEquals("Should Return Total Entered", 10, ps.empty());
    }

    //Canceled entry does not add to the amount returned by empty.
     @Test
    public void shouldNotAddAmount() throws IllegalCoinException{
        ps.addPayment(5);
        assertEquals("Should not add money", 5, ps.empty());
     }

     // Call to empty resets the total to zero.
    @Test
    public void shouldResetTotalToZero() throws IllegalCoinException{
        ps.addPayment(25);
        ps.empty();
        assertEquals("Total Entered Should Be Zero", 0, ps.readDisplay());
    }

    //Call to cancel returns a map containing one coin entered
    @Test
    public void returnMapCoinEntered() throws IllegalCoinException{
        ps.addPayment(5);
        Map<Integer, Integer> map = ps.cancel();
        int more = 0;
        for(Integer val: map.values()){
            if(val != 0){
                more++;
            }
        }
        assertEquals("Should return one coin map", 1, more);
    }


    /**Call to cancel returns a map containing a mixture of coins entered.
     *( Entering 10c, 10c and 5c then pressing cancel is returning 2x10c and 1x5c and really not
     * returning 1x25c)
     * **/

    @Test
    public void shouldReturnCoinsEntered() throws IllegalCoinException{
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        ps.addPayment(25);
        map.put(0,25);
        ps.addPayment(10);
        map.put(1,10);
        ps.addPayment(5);
        map.put(2,5);

        for(int n = 0; n<map.size(); n++){
            assertEquals("Map in test should contain same values as map in paystation", map.get(n), ps.getCoinMap().get(n));
        }
    }


    //Call to cancel returns a map that does not contain a key for a coin not entered
    @Test
    public void returnMapCoinNotEntered() throws  IllegalCoinException{
        Map<Integer, Integer> check = new HashMap<Integer, Integer>();
        check.put(0, 5);
        ps.addPayment(5);
        check.put(1, 10);
        ps.addPayment(10);
        check.put(2, 25);
        ps.addPayment(10);

        assertNull("Should return error", ps.getCoinMap().get(3));

    }

    //Call to cancel clears the map.
    @Test
    public void returnEmptyMapWhenCancelIsCalled() throws IllegalCoinException{
        ps.addPayment(10);
        ps.cancel();

        assertNull("Map should not contain any values", ps.getCoinMap().get(0));
    }


    //Call to buy clears the map
    @Test
    public void clearMap() throws IllegalCoinException{
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.buy();
        assertNull("Buy clears the map", ps.getCoinMap().get(0));
    }
}


