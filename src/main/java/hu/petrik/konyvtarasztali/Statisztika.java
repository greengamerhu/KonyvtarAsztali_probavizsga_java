package hu.petrik.konyvtarasztali;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Statisztika {
    private List<Konyv> konyvek;
    private KonyvDB db;

    public Statisztika() {
        listaFeltolt();
        System.out.printf("500 oldalnál hosszabb könyvek száma: %d \n", otszazOldalnalTobb());
        System.out.printf("%s 1950-nél régebbi könyv\n", ezerkilecszazotvenelott() ? "van" : "nincs");
        Konyv leghosszabbik = leghosszabbKonyv();
        System.out.printf("A leghosszabb könyv:\n" +
                "\tSzerző: %s\n" +
                "\tCím: %s\n" +
                "\tKiadás éve: %d\n" +
                "\tOldalszám: %d \n",
                leghosszabbik.getAuthor(),
                leghosszabbik.getTitle(),
                leghosszabbik.getPublish_year(),
                leghosszabbik.getPage_count());
        System.out.printf("A legtöbb könyvvel rendelkező szerző: %s\n", legtobbKonyv());
        Scanner sc = new Scanner(System.in);
        System.out.printf("Adjon meg egy könyv címet: ");
        String userInput = sc.nextLine();
        System.out.printf("Az megadott könyvszerzője %s",konyvkereses(userInput));
    }


    private void listaFeltolt() {
        try {
            db = new KonyvDB();
            konyvek = db.Konyvlistaz();
        } catch (SQLException e) {
            System.out.printf("HIba történt: \n Hibakód: %s", e);
            System.exit(0);
        }
    }
    private int otszazOldalnalTobb() {
        int szam = konyvek.stream().filter(k-> k.getPage_count() > 500).collect(Collectors.toList()).size();
        return szam;
    }

    private boolean ezerkilecszazotvenelott() {
        for(Konyv konyv : konyvek ) {
            if (konyv.getPublish_year() < 1950) {
                return true;
            }
        }
        return false;

    }
    private Konyv leghosszabbKonyv() {
        Konyv max = konyvek.get(0);
        for (Konyv konyv : konyvek) {
            if (konyv.getPage_count() >= max.getPage_count()) {
                max = konyv;
            }
        }
        return max;
    }
    private String legtobbKonyv() {
        HashMap<String, Integer> szerzokEsKonyvekszama= new HashMap<>();
        for (Konyv konyv : konyvek) {
            if (szerzokEsKonyvekszama.containsKey(konyv.getAuthor())) {
                szerzokEsKonyvekszama.put(konyv.getAuthor(), szerzokEsKonyvekszama.get(konyv.getAuthor()) +1);
            } else {
                szerzokEsKonyvekszama.put(konyv.getAuthor(), 1);
            }
        }
        String szerzo = "";
        int szam = 0;
        for (Map.Entry<String, Integer > entry : szerzokEsKonyvekszama.entrySet()) {
            if (entry.getValue() > szam) {
                szerzo = entry.getKey();
                szam = entry.getValue();
            }
        }
        return szerzo;
    }

    private String konyvkereses(String konyvcim) {
        for (Konyv konyv : konyvek) {
            if (konyv.getTitle().equals(konyvcim)) {
                return konyv.getAuthor();
            }
        }
        return "Nincs ilyen könyv";
    }
}
