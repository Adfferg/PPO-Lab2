package com.example.task2;

public class TimerSequence {
    int id;
    String name;
    int preparation;
    int workingTime;
    int rest;
    int cycles;
    int sets;
    int restBetweenSets;
    int color;

    TimerSequence(int id, String name, int preparation, int workingTime, int rest, int cycles, int sets, int restBetweenSets, int color)
    {
        this.id = id;
        this.name = name;
        this.preparation = preparation;
        this.workingTime = workingTime;
        this.rest=rest;
        this.color=color;
        this.cycles=cycles;
        this.sets=sets;
        this.restBetweenSets=restBetweenSets;
    }
}
