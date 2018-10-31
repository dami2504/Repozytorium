package Cwiczenia2.Grafika.gra;

import java.awt.*;
import java.io.IOException;

public class Program
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                    try
                    {
                        new KontenerGry();
                    }
                    catch (IOException e)
                    {

                    }
            }
        });
    }
}
