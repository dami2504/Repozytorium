package Cwiczenia2.Grafika.gra;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class Gra extends JComponent
{

    JPanel panelCzas;
    JPanel panelPrzyciski;
    JPanel panelWynik;
    Integer wynik =0;
    Rectangle2D platforma;
    Ellipse2D dzialkoPrzedmiot;
    Ellipse2D kulka;

    KeyStroke pauza;
    InputMap imap;
    ActionMap amap;

    private int szerokoscObszaruGry =500;
    private int wysokosc=500;

    double dzialkoPrzedmiotDefaultX;
    double dzialkoPrzedmiotDefaultY;
    double dzialkoPrzedmiotSzerokosc =15;
    double dzialkoPrzedmiotWysokosc =15;
    double dzialkoPrzedmiotPredkosc = 1.5;


    double pociskDefaultX;
    double pociskDefaultY;
    double pociskSzerokosc =5;
    double pociskWysokosc =5;
    double pociskPredkosc =-1;


    double platformaDefaultX;
    double platformaDefaultY;
    double platformaSzerokosc;
    double platformaWysokosc;

    double kulkaDefaultX;
    double kulkaDefaultY;
    double kulkaWysokosc;
    double kulkaSzerokosc;
    double kulkaPredkoscY;
    double kulkaPredkoscX;

    double klocekX;
    double klocekY;
    double klocekDefaultSzerokosc;
    double klocekDefaultWysokosc;

    Boolean started = false;
    Timer timer1;
    Timer timer2;
    Timer timer3;
    JLabel stoperLabel;
    JLabel wynikLabel;
    int sekundyStoper;
    int minutyStoper =0;
    int godzinyStoper =0;

    String graczImie;

    Map<Double,Double> klockiWspolrzedne;
    Map<RectangularShape,Integer> klockiLiczbaZbic;
    ArrayList<RectangularShape> pociski;

    Boolean czyDzialkoAktywne =false;
    Boolean czyDzialkoSpada =false;

    Boolean pauzaAktywna =false;
    Random random;
    Integer liczbaUszkodzenKlocka;

    List<Gracz> graczeLista;

    Thread thread1;
    Thread thread2;
    Thread thread3;

    public Gra()
    {
        graczeLista = new LinkedList<>();
        random = new Random();

        panelCzas =new JPanel();
        panelPrzyciski = new JPanel();
        panelWynik =new JPanel();

        //ustawienia rozmiarow i parametrow poszczegolnych elementow
        platformaDefaultX =210;
        platformaDefaultY =400;
        platformaSzerokosc =60;
        platformaWysokosc =20;

        kulkaDefaultX =235;
        kulkaDefaultY =200;
        kulkaSzerokosc =10;
        kulkaWysokosc =10;
        kulkaPredkoscX =1;
        kulkaPredkoscY =-1.5;

        klocekDefaultSzerokosc =60;
        klocekDefaultWysokosc =20;
        klockiLiczbaZbic = new HashMap<>(); // mapa z klockami i wartosciami zbic
        klockiWspolrzedne =new HashMap<>(); //mapa ze wspolrzednymi klockow


        pociski=new ArrayList<>(); // lista z pociskami

        //label ze stoperem
        stoperLabel = new JLabel("00:00:00");
        wynikLabel = new JLabel("0");

        //metoda odpowiedzialna za losowe ustawienie klocków
        ustawienie_klocków();

        //timer odpowiedzialny za stoper
        class timer_thread implements Runnable
        {
            public void run()
            {
                timer1 = new Timer(1000, new label_timer());
                timer1.start();
            }
        }

        //timer odpowiedzialny za poruszanie sie przedmiotow
        class thread_poruszanie1 implements Runnable
        {
            public void run()
            {
                timer2 = new Timer(10, new ruch_timer());
                timer2.start();
            }
        }

        //timer odpowiedzialny za pociski
        class thread_pociski implements Runnable
        {
            public void run()
            {
                timer3 = new Timer(10, new pociski_timer());
                timer3.start();
            }
        }


        //dodawanie przycisku start i stoper do okienka
        setLayout(new BorderLayout());
        JButton jButton_start = new JButton("Start!");
        jButton_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                graczImie = JOptionPane.showInputDialog("Podaj graczImie:");
                if (graczImie !=null)
                {
                    started = true;
                    /*
                    timer1.start();
                    timer2.start();
                    timer3.start();
                    */
                    thread1 = new Thread(new thread_poruszanie1());
                    thread2 = new Thread(new timer_thread());
                    thread3 = new Thread(new thread_pociski());
                    thread1.start();
                    thread2.start();
                    thread3.start();
                }

            }
        });

        //dodawanie przycisku pauzy
        JButton jButton_pauza=new JButton("Pauza");
        pauza =KeyStroke.getKeyStroke("P");
        imap= this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        amap=this.getActionMap();
        imap.put(pauza,"pauza");

        AbstractAction pauza_akcja= new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (pauzaAktywna ==false)
                {
                    pauzaAktywna =true;

                    timer1.stop();
                    timer2.stop();
                    timer3.stop();
                }
                else if(pauzaAktywna ==true)
                {
                    pauzaAktywna =false;
                    timer1.start();
                    timer2.start();
                    timer3.start();
                }
            }
        };
        amap.put("pauza",pauza_akcja);
        jButton_pauza.addActionListener(pauza_akcja);

        panelPrzyciski.setLayout(new BorderLayout());
        panelPrzyciski.add(jButton_start,BorderLayout.SOUTH);
        panelCzas.add(stoperLabel);
        panelWynik.add(wynikLabel);
        panelPrzyciski.add(jButton_pauza,BorderLayout.WEST);
        panelPrzyciski.add(panelCzas,BorderLayout.CENTER);
        panelPrzyciski.add(wynikLabel,BorderLayout.EAST);
        add(panelPrzyciski,BorderLayout.SOUTH);

        //dodawanie motion listenera do platformy
        addMouseMotionListener(new MouseMotionHandler());

        addMouseListener(new MouseHandler());

        setVisible(true);


    }
    private void  koniec_gry(String imie, int wynik, String czas)
    {

        GraczeBazaDanych gracze_bazaDanych = new GraczeBazaDanych();
        gracze_bazaDanych.dodajGracza(imie,wynik,czas);
        graczeLista = gracze_bazaDanych.pobierzListeGraczy();
        gracze_bazaDanych.closeConnection();
    }
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        platforma = new Rectangle();
        platforma.setFrame(platformaDefaultX, platformaDefaultY, platformaSzerokosc, platformaWysokosc);
        kulka = new Ellipse2D.Double(kulkaDefaultX, kulkaDefaultY, kulkaSzerokosc, kulkaWysokosc);
        dzialkoPrzedmiot =new Ellipse2D.Double(dzialkoPrzedmiotDefaultX, dzialkoPrzedmiotDefaultY, dzialkoPrzedmiotSzerokosc, dzialkoPrzedmiotWysokosc);

        g2d.setColor(Color.orange);
        g2d.draw(platforma);
        g2d.fill(platforma);

        g2d.setColor(Color.green);
        g2d.draw(kulka);
        g2d.fill(kulka);

        //rysowanie klockow
        g2d.setColor(Color.blue);
        for (Map.Entry<RectangularShape,Integer> entry: klockiLiczbaZbic.entrySet())
        {
            Rectangle2D shape = (Rectangle2D) entry.getKey();
            g2d.draw(shape);
            g2d.fill(shape);
        }
        g2d.setColor(Color.black);

        //spadanie itemu z dzialkiem
        if (czyDzialkoSpada ==true)
        {
            g2d.draw(dzialkoPrzedmiot);
        }
        if (czyDzialkoAktywne ==true)
        {
            for (RectangularShape shape :pociski)
            {
                g2d.draw(shape);
            }
        }

        //ustawienia komunikatu po zniszczeniu wszystkich klockow
        if (klockiLiczbaZbic.isEmpty()==true)
        {
            started=false;
            timer1.stop();
            timer2.stop();
            timer3.stop();
            JOptionPane.showMessageDialog(null,"Wygrałeś!","Brawo!",1);
        }
    }

    private class MouseMotionHandler extends MouseMotionAdapter
    {
        //ruszanie platforma
        public void mouseMoved(MouseEvent event)
        {
            if (pauzaAktywna ==false)
            {
                Point2D point2D = event.getPoint();
                if (point2D.getX() > 30 && point2D.getX() < 455 && started == true)
                {
                    platformaDefaultX = point2D.getX() - 30;
                    repaint();
                }
            }
        }
    }
    private class MouseHandler extends MouseAdapter
    {
        //strzelanie pociskow przy aktywnym dzialku
        public void mousePressed(MouseEvent e)
        {
            if (czyDzialkoAktywne ==true)
            {
                pociskDefaultX = platformaDefaultX + platformaSzerokosc / 2;
                pociskDefaultY = platformaDefaultY;
                pociski.add(new Ellipse2D.Double(pociskDefaultX, pociskDefaultY, pociskSzerokosc, pociskWysokosc));
                repaint();
            }
        }
    }

    public void get_ranking()
    {
        JDialog jDialog = new JDialog();
        jDialog.setSize(new Dimension(500,500));
        jDialog.setVisible(true);

        int liczba_graczy = graczeLista.size();
        Object[][] data= new Object[liczba_graczy][3];
        String[] kolumny = new String[]{"Imię","Wynik","Czas"};
        JTable jTable_ranking = new JTable(data,kolumny);
        jTable_ranking.setFillsViewportHeight(true); //wypelnia tabela caly dostepny kontener
        int j =0;
        for (Gracz o : graczeLista)
        {

            for (int i=0;i<3;i++)
            {
                if (i==0)
                {
                    data[j][i] = (String)o.getImie();
                }
                if (i==1)
                {
                    data[j][i] = (Integer)o.getPunkty();
                }
                if (i==2)
                {
                    data[j][i] = (String)o.getCzas();
                }
            }
            j++;
        }
        JScrollPane jScrollPane_tabela = new JScrollPane(jTable_ranking);
        jDialog.add(jScrollPane_tabela);


    }
    private void ustawienie_klocków()
    {
        //losowe rozmieszczenie klockow
        int mnoznikx = 0;
        int mnozniky = 0;
        //trzy poziomy klockow do zbicia, mnozniki wykorzystywane do zmiany kolumn i wierszy
        for(int i = 0;i<12;i++)
        {
            if (i<4)
            {
                if (mnoznikx==3)
                {
                    klocekX =random.nextInt(40) + mnoznikx*120;
                    klocekY = random.nextInt(50) + mnozniky*100 + 10;
                    mnoznikx=0;
                    mnozniky++;
                }
                else
                {
                    klocekX =random.nextInt(40) + mnoznikx*120;
                    klocekY = random.nextInt(50) + mnozniky*100 + 10;
                    mnoznikx++;
                }
                klockiLiczbaZbic.put(new Rectangle2D.Double(klocekX, klocekY, klocekDefaultSzerokosc, klocekDefaultWysokosc), 1);
            }
            else if (i>=4&&i<8)
            {
                if (i==7)
                {
                    klocekX =random.nextInt(40) + mnoznikx*120;
                    klocekY = random.nextInt(50) + mnozniky*100 + 10;
                    mnoznikx=0;
                    mnozniky++;
                }
                else
                {
                    klocekX =random.nextInt(40) + mnoznikx*120;
                    klocekY = random.nextInt(50) + mnozniky*100 + 10;
                    mnoznikx++;
                }
                klockiLiczbaZbic.put(new Rectangle2D.Double(klocekX, klocekY, klocekDefaultSzerokosc, klocekDefaultWysokosc), 1);
            }
            else if (i>=8)
            {
                klocekX =random.nextInt(40) + mnoznikx*120;
                klocekY = random.nextInt(50) + mnozniky*100 + 10;
                mnoznikx++;
                klockiLiczbaZbic.put(new Rectangle2D.Double(klocekX, klocekY, klocekDefaultSzerokosc, klocekDefaultWysokosc), 1);
            }

        }
    }
    private class label_timer implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            sekundyStoper++;

            if (sekundyStoper == 60)
            {
                minutyStoper++;
                if (minutyStoper == 60)
                {
                    godzinyStoper++;
                    minutyStoper = 0;
                }
                sekundyStoper = 0;
            }

            if (sekundyStoper < 10)
            {
                if (minutyStoper < 10)
                {
                    if (godzinyStoper < 10)
                    {
                        stoperLabel.setText("0" + Integer.toString(godzinyStoper) + ":0" + Integer.toString(minutyStoper) + ":0" + Integer.toString(sekundyStoper));
                    } else
                    {
                        stoperLabel.setText(Integer.toString(godzinyStoper) + ":0" + Integer.toString(minutyStoper) + ":0" + Integer.toString(sekundyStoper));
                    }

                } else
                {
                    if (godzinyStoper < 10)
                    {
                        stoperLabel.setText("0" + Integer.toString(godzinyStoper) + ":" + Integer.toString(minutyStoper) + ":0" + Integer.toString(sekundyStoper));
                    } else
                    {
                        stoperLabel.setText(Integer.toString(godzinyStoper) + ":" + Integer.toString(minutyStoper) + ":0" + Integer.toString(sekundyStoper));
                    }
                }
            } else
            {
                if (minutyStoper < 10)
                {
                    if (godzinyStoper < 10)
                    {
                        stoperLabel.setText("0" + Integer.toString(godzinyStoper) + ":0" + Integer.toString(minutyStoper) + ":" + Integer.toString(sekundyStoper));
                    } else
                    {
                        stoperLabel.setText(Integer.toString(godzinyStoper) + ":0" + Integer.toString(minutyStoper) + ":" + Integer.toString(sekundyStoper));
                    }
                } else
                {
                    if (godzinyStoper < 10)
                    {
                        stoperLabel.setText("0" + Integer.toString(godzinyStoper) + ":" + Integer.toString(minutyStoper) + ":" + Integer.toString(sekundyStoper));
                    } else
                    {
                        stoperLabel.setText(Integer.toString(godzinyStoper) + ":" + Integer.toString(minutyStoper) + ":" + Integer.toString(sekundyStoper));
                    }
                }
            }
        }
    }

    private class ruch_timer implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            kulkaDefaultX += kulkaPredkoscX;
            kulkaDefaultY += kulkaPredkoscY;

            if (kulkaDefaultY - kulkaWysokosc < 0)
            {
                kulkaPredkoscY = -kulkaPredkoscY;
                kulkaDefaultY = kulkaWysokosc;
            } else if (kulkaDefaultY + kulkaWysokosc > platformaDefaultY && kulkaDefaultX > platformaDefaultX && kulkaDefaultX < platformaDefaultX + 60)
            {
                kulkaPredkoscY = -kulkaPredkoscY;
                kulkaDefaultY = platformaDefaultY - kulkaWysokosc;
            }
            if (kulkaDefaultX - kulkaSzerokosc < 0)
            {
                kulkaPredkoscX = -kulkaPredkoscX;
                kulkaDefaultX = kulkaSzerokosc;
            } else if (kulkaDefaultX + kulkaSzerokosc > szerokoscObszaruGry - 15)
            {
                kulkaPredkoscX = -kulkaPredkoscX;
                kulkaDefaultX = szerokoscObszaruGry - 15 - kulkaSzerokosc;
            } else if (dzialkoPrzedmiotDefaultY + 15 > platformaDefaultY && dzialkoPrzedmiotDefaultX > platformaDefaultX && dzialkoPrzedmiotDefaultX < platformaDefaultX + 60)
            {
                czyDzialkoSpada = false;
                czyDzialkoAktywne = true;
            } else if (kulkaDefaultY - kulkaWysokosc > 450)
            {
                JOptionPane.showMessageDialog(null, "Przegrałeś!\nUzyskałeś:" + wynik + " pkt");
                pauzaAktywna =true;
                timer1.stop();
                timer2.stop();
                timer3.stop();
                koniec_gry(graczImie, wynik, stoperLabel.getText());
            }

            //ustawianie walidacji kulki i klockow
            Point2D point2D_dol = new Point2D.Double(kulka.getCenterX(), kulka.getCenterY() + kulkaWysokosc / 2);
            Point2D point2D_gora = new Point2D.Double(kulka.getCenterX(), kulka.getCenterY() - kulkaWysokosc / 2);
            Point2D point2D_lewa = new Point2D.Double(kulka.getCenterX() - kulkaSzerokosc / 2, kulka.getCenterY());
            Point2D point2D_prawa = new Point2D.Double(kulka.getCenterX() + kulkaSzerokosc / 2, kulka.getCenterY());

            for (Map.Entry<RectangularShape, Integer> entry : klockiLiczbaZbic.entrySet())
            {
                Rectangle2D rect = (Rectangle2D) entry.getKey();
                liczbaUszkodzenKlocka = entry.getValue();
                if (rect.contains(point2D_dol))
                {
                    kulkaPredkoscY = -kulkaPredkoscY;
                    kulkaDefaultY = rect.getY() - kulkaWysokosc - 1;
                    liczbaUszkodzenKlocka++;
                    walidacja_klocków(liczbaUszkodzenKlocka,rect);
                    break;
                }
                if (rect.contains(point2D_gora))
                {
                    kulkaPredkoscY = -kulkaPredkoscY;
                    kulkaDefaultY = rect.getY() + klocekDefaultWysokosc + kulkaWysokosc + 1;
                    liczbaUszkodzenKlocka++;
                    walidacja_klocków(liczbaUszkodzenKlocka,rect);
                    break;
                }
                if (rect.contains(point2D_lewa))
                {
                    kulkaPredkoscX = -kulkaPredkoscX;
                    kulkaDefaultX = rect.getX() + klocekDefaultSzerokosc + kulkaSzerokosc + 1;
                    liczbaUszkodzenKlocka++;
                    walidacja_klocków(liczbaUszkodzenKlocka,rect);
                    break;
                }
                if (rect.contains(point2D_prawa))
                {
                    kulkaPredkoscX = -kulkaPredkoscX;
                    kulkaDefaultX = rect.getX() - kulkaSzerokosc - 1;
                    liczbaUszkodzenKlocka++;
                    walidacja_klocków(liczbaUszkodzenKlocka,rect);
                    break;
                }

            }
            if (czyDzialkoSpada == true)
            {
                dzialkoPrzedmiotDefaultY += dzialkoPrzedmiotPredkosc;
            }

            repaint();
            wynikLabel.setText(Integer.toString(wynik));
        }
    }

    private class pociski_timer implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            for (RectangularShape shape : pociski)
            {
                shape.setFrame(shape.getX(), shape.getY() + pociskPredkosc, pociskSzerokosc, pociskWysokosc);
                if (shape.getY() < 0)
                {
                    pociski.remove(shape);
                    repaint();
                    break;
                }
            }

            //ustawianie kolizji pociskow z klockami
            for (RectangularShape shape : pociski)
            {
                Point2D point2D_pocisk = new Point2D.Double(shape.getCenterX(), shape.getCenterY() - pociskWysokosc / 2);
                for (Map.Entry<RectangularShape, Integer> entry : klockiLiczbaZbic.entrySet())
                {
                    Rectangle2D rect = (Rectangle2D) entry.getKey();
                    liczbaUszkodzenKlocka = entry.getValue();
                    if (rect.contains(point2D_pocisk))
                    {
                        liczbaUszkodzenKlocka++;
                        pociski.remove(shape);
                        if (liczbaUszkodzenKlocka == 2)
                        {
                            klockiLiczbaZbic.remove(rect);
                            klockiLiczbaZbic.put(rect, liczbaUszkodzenKlocka);
                            repaint();
                        }
                        if (liczbaUszkodzenKlocka == 3)
                        {
                            wynik = wynik + 100;
                            klockiLiczbaZbic.remove(rect);
                            int p = random.nextInt(2);
                            if (p == 1)
                            {
                                if (czyDzialkoSpada == false)
                                {
                                    czyDzialkoSpada = true;
                                    dzialkoPrzedmiotDefaultX = rect.getX();
                                    dzialkoPrzedmiotDefaultY = rect.getY();
                                    wynik = wynik + 100;
                                    wynikLabel.setText(Integer.toString(wynik));
                                }
                            }
                            repaint();
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    private void walidacja_klocków(int k, Rectangle2D rect)
    {
        //po 2 uderzeniach klockiLiczbaZbic sa usuwane, moze z nich wypasc czyDzialkoAktywne
        if (k == 2)
        {
            klockiLiczbaZbic.remove(rect);
            klockiLiczbaZbic.put(rect, k);
        } else if (k == 3)
        {
            klockiLiczbaZbic.remove(rect);
            wynik = wynik + 100;
            int p = random.nextInt(2);
            if (p == 1)
            {
                if (czyDzialkoSpada == false)
                {
                    czyDzialkoSpada = true;
                    dzialkoPrzedmiotDefaultX = rect.getX();
                    dzialkoPrzedmiotDefaultY = rect.getY();
                }
            }
            repaint();
        }
    }
}
