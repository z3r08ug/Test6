package com.example.chris.test6.JavaAssessment;


/**
 * Created by chris on 12/16/2017.
 */

public class Outbreak
{
    
    public static void main(String[] args)
    {
        RoomBuilder roomBuilder = new RoomBuilder();
        System.out.println(isOutbreak(roomBuilder.getFloor1()));
        System.out.println(isOutbreak(roomBuilder.getFloor2()));
        System.out.println(isOutbreak(roomBuilder.getFloor3()));
        System.out.println(isOutbreak(roomBuilder.getFloor4()));
    }
    
    private static boolean isOutbreak(Room[][] floor)
    {
        //[row][col]
        for (int x = 0; x < floor.length; x++)
        {
            for (int y = 0; y < floor[x].length; y++)
            {
                if (!floor[x][y].visited && floor[x][y].isInfected)
                {
                    if (checkRight(floor, x, y+1, 1))
                        return true;
                    else
                        floor[x][y].setVisited(true);
                }
            }
        }
        return false;
    }
    
    private static boolean checkDown(Room[][] floor, int x, int y, int cnt)
    {
        if (floor[x][y].isInfected && cnt+1 != 5)
        {
            cnt++;
            return checkRight(floor, x, y + 1, cnt);
        }
        else return floor[x][y].isInfected && cnt + 1 == 5;
    }
    
    private static boolean checkRight(Room [][]floor, int x, int y, int cnt)
    {
        if (floor[x][y].isInfected && cnt + 1 != 5)
        {
            cnt++;
            return checkRight(floor, x, y + 1, cnt);
        }
        else
            return floor[x][y].isInfected && cnt + 1 == 5 || checkDown(floor, x + 1, y - 1, cnt);
    }
}
