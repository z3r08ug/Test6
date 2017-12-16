package com.example.chris.test6.JavaAssessment;

/**
 * Created by chris on 12/16/2017.
 */

public class Room
{
    public final boolean isInfected;
    
    public boolean isVisited()
    {
        return visited;
    }
    
    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }
    
    public boolean visited = false;
    Room(boolean infected)
    {
        isInfected = infected;
    }
}
