package com.openclassrooms.mareu.app.utils;

import com.openclassrooms.mareu.app.utils.CustomDateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class CustomDateFormatterTest {

    private CustomDateTimeFormatter formatter;

    @Before
    public void setUp() {
        this.formatter = new CustomDateTimeFormatter();
    }
/*


    @Test
    public void roundUpMinutes() {
        LocalDateTime reference = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0));
        LocalDateTime expected = reference;
        LocalDateTime tested = reference;
        assertEquals(expected, this.formatter.roundUpMinutes(tested));

        expected = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,5));
        for(int i=1; i<=5; i++) {
            tested = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,i));;
            assertEquals(expected, this.formatter.roundUpMinutes(tested));
        }

        expected = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,10));
        for(int i=6; i<=10; i++) {
            tested = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,i));;
            assertEquals(expected, this.formatter.roundUpMinutes(tested));
        }

        expected = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,15));
        for(int i=11; i<=15; i++) {
            tested = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,i));
            assertEquals(expected, this.formatter.roundUpMinutes(tested));
        }

        expected = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(1,0));
        for(int i=56; i<60; i++) {
            tested = LocalDateTime.of(reference.toLocalDate(), LocalTime.of(0,i));
            assertEquals(expected, this.formatter.roundUpMinutes(tested));
        }
    }
    */
}
