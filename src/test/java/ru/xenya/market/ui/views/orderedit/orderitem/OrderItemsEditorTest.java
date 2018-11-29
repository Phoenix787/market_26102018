package ru.xenya.market.ui.views.orderedit.orderitem;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrderItemsEditorTest {

    @Test
    public void setPriceTest(){
        String amount = "4568";
        Double selectedAmount = (double)Integer.parseInt(amount)/100;
        assertEquals(java.util.Optional.of(45.68), java.util.Optional.of(selectedAmount));
    }
}