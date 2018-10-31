package Cwiczenia2.Grafika.gra;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

public class KontenerGry extends JFrame
{
    JFrame jFrame= new JFrame();
    String nowy_tytul;
    String defaultTitle="SwingDxBall";

    private File propertiesFile;
    private File preferencesFile;
    private Properties settings;
    private Preferences preferences;

    //tworzenie komponentu z gra
    Gra rozgrywka = new Gra();

    public KontenerGry() throws IOException
    {
        jFrame.setTitle(defaultTitle);

        //pasek menu
        JMenuBar jMenuBar = new JMenuBar();
        jFrame.setJMenuBar(jMenuBar);

        //menu opcje
        JMenu opcjeJMenu = new JMenu("Opcje");
        jMenuBar.add(opcjeJMenu);

        //opcja zmien tytul
        JMenuItem zmienTytulJMenuItem = new JMenuItem("Zmien_tytuł");
        opcjeJMenu.add(zmienTytulJMenuItem);
        zmienTytulJMenuItem.addActionListener(new zmienTytulAction());

        //opcja zmien rozmiar
        JMenuItem zmienRozmiarJMenuItem = new JMenuItem("Zmien_rozmiar");
        opcjeJMenu.add(zmienRozmiarJMenuItem);
        zmienRozmiarJMenuItem.addActionListener(new zmienRozmiarAction());

        //opcja pokaz ranking
        JMenuItem pokazRankingJMenuItem= new JMenuItem("Pokaz_ranking");
        opcjeJMenu.add(pokazRankingJMenuItem);
        pokazRankingJMenuItem.addActionListener(new pokazRanking());

        //opcja pokaz intrukcje
        JMenuItem pokazInstrukcjeJMenuItem = new JMenuItem("Intrukcja");
        opcjeJMenu.add(pokazInstrukcjeJMenuItem);
        pokazInstrukcjeJMenuItem.addActionListener(new pokazInstrukcje());

        //ustawiania tytulu z pliku .properties
        propertiesFile = new File("program.properties");
        settings = new Properties();
        if (propertiesFile.exists())
        {
            try
            {
                FileInputStream in = new FileInputStream(propertiesFile);
                settings.load(in);
                in.close();
                jFrame.setTitle(settings.getProperty("title"));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            FileOutputStream out = new FileOutputStream("program.properties");
            settings.put("title",defaultTitle);
            settings.store(out,"Ustawienia programu");
            out.close();
        }

        //ustawianie rozmiaru okna .preferences
        preferences=Preferences.userRoot().node("preferences.xml");
        jFrame.setSize(new Dimension(preferences.getInt("witdh",0),preferences.getInt("height",0)));
        preferencesFile=new File("preferences.xml");
        if (preferencesFile.exists())
        {
            try
            {
                InputStream in = new FileInputStream("preferences.xml");
                preferences.importPreferences(in);
                in.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                OutputStream outputStream = new FileOutputStream("preferences.xml");
                preferences.exportNode(outputStream);
                outputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //dodawanie komponentu z gra
        jFrame.add(rozgrywka);

        //reszta ustawien
        jFrame.setVisible(true);
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    //klasa listenera odpowiedzialna za zmiane tytułu okna
    public class zmienTytulAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            nowy_tytul = JOptionPane.showInputDialog("Wprowadz nowy tytuł");
            jFrame.setTitle(nowy_tytul);
            settings.put("title",nowy_tytul);
            try
            {
               FileOutputStream out = new FileOutputStream("program.properties");
                settings.store(out, "Ustawienia programu");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //klasa listenera odpowiedzialna za zmiane rozmiarów okna
    public class zmienRozmiarAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String nowa_szer = JOptionPane.showInputDialog("Podaj szerokosc");
            String nowa_wys = JOptionPane.showInputDialog("Podaj wysokosc");
            try
            {
                if (nowa_szer != "" && nowa_wys != "" && Integer.parseInt(nowa_szer) >= 500 && Integer.parseInt(nowa_wys) > 550)
                {
                    JOptionPane.showMessageDialog(null, "dsadsasd");
                    preferences.put("witdh", nowa_szer);
                    preferences.put("height", nowa_wys);
                    jFrame.setSize(new Dimension(Integer.parseInt(nowa_szer), Integer.parseInt(nowa_wys)));
                    try
                    {
                        FileOutputStream out = new FileOutputStream("preferences.xml");
                        preferences.exportNode(out);
                        out.close();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                } else if (nowa_szer.isEmpty() || nowa_wys.isEmpty() || Integer.parseInt(nowa_szer) < 500 || Integer.parseInt(nowa_wys) < 550)
                {
                    JOptionPane.showMessageDialog(null, "Musisz podać rozmiar (szerokość>500,wysokość>550)", "Ostrzeżenie!", 1);
                }
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Musisz podać rozmiar (szerokość>500,wysokość>550)", "Ostrzeżenie!", 1);
            }
        }
    }

    //klasa listenera odpowiedzialna za pokazanie rankingu
    public class pokazRanking implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            rozgrywka.get_ranking();
        }
    }

    //klasa listenera odpowiedzialna za pokazanie intrukcji
    public class pokazInstrukcje implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null,"Pauza - p\nStrzal - PPM \nZbicie klocka - 100pkt");
        }
    }
}
