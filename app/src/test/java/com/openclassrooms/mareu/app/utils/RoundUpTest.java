package com.openclassrooms.mareu.app.utils;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class RoundUpTest {

    @Test
    public void shouldReturn0IfMinutesIs0() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 0))));
    }

    @Test
    public void shouldReturn5IfMinutesIs5() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 5)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 5))));
    }

    @Test
    public void shouldReturn10IfMinutesIs10() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 10)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 10))));
    }

    @Test
    public void shouldReturn15IfMinutesIs15() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 15)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 15))));
    }

    @Test
    public void shouldReturn5IfMinutesIs1() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 5)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 1))));
    }

    @Test
    public void shouldReturn10IfMinutesIs6() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 10)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 6))));
    }

    @Test
    public void shouldReturn15IfMinutesIs11() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 15)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 11))));
    }

    @Test
    public void shouldReturn20IfMinutesIs16() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 20)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 16))));
    }

    @Test
    public void shouldReturn25IfMinutesIs21() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 25)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 21))));
    }

    @Test
    public void shouldReturn55IfMinutesIs51() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 55)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 51))));
    }

    @Test
    public void shouldReturn60IfMinutesIs56() {
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(2, 0)),
                new CustomDateTimeFormatter().roundUp(LocalDateTime.of(LocalDate.now(), LocalTime.of(1, 56))));
    }



}
