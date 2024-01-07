import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Threadislemleri {

    public static void main(String[] args) {
        // 1'den 1.000.000'e kadar olan sayıları içeren ArrayList oluştur.
        List<Integer> tumsayilar = createNumberList(1, 1000000);

        // 4 eşit parçaya bölünmüş ArrayList'ler oluşturuluyor.
        List<List<Integer>> parcalar = Parcala(tumsayilar, 4);

        // Thread'lerin çalışacağı listeler oluştur
        List<Integer> ciftsayilar = new ArrayList<>();
        List<Integer> teksayilar = new ArrayList<>();
        List<Integer> asalsayilar = new ArrayList<>();
        
        Lock lock = new ReentrantLock();
        
        // Her bir parça için ayrı bir thread
        for (int i = 0; i < 4; i++) {
            List<Integer> parca = parcalar.get(i);
            Thread thread = new Thread(() -> Sayilaribul(parca, ciftsayilar, teksayilar, asalsayilar,lock));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        
        System.out.println("cift sayilar: " + ciftsayilar);
        System.out.println("tek sayilar: " + teksayilar);
        System.out.println("asal sayilar: " + asalsayilar);
    }

    // 1'den n'e kadar olan sayıları içeren ArrayList oluşturan fonksiyon
    private static List<Integer> createNumberList(int basla, int bit) {
        List<Integer> sayilar = new ArrayList<>();
        for (int i = basla; i <= bit; i++) {
        	sayilar.add(i);
        }
        return sayilar;
    }

    // Bir listeyi belirli bir sayıda eşit parçaya bölen fonksiyon
    private static List<List<Integer>> Parcala(List<Integer> list, int parcaboyutu) {
        List<List<Integer>> parcalar = new ArrayList<>();
        int parcauzunlugu = list.size() / parcaboyutu;

        for (int i = 0; i < parcaboyutu - 1; i++) {
            List<Integer> parca = list.subList(i * parcauzunlugu, (i + 1) * parcauzunlugu);
            parcalar.add(parca);
        }

        // Son parça, diğerlerinden biraz daha uzun olabilir.
        List<Integer> sonparca = list.subList((parcaboyutu - 1) * parcauzunlugu, list.size());
        parcalar.add(sonparca);

        return parcalar;
    }

    // Çift, tek ve asal sayıları bulan fonksiyon
    private static void Sayilaribul(List<Integer> sayilar, List<Integer> ciftsayilar, List<Integer> teksayilar, List<Integer> asalsayilar, Lock lock) {
    	 for (int sayi : sayilar) {
             if (Cift(sayi)) {
                 lock.lock();
                 try {
                	 ciftsayilar.add(sayi);
                 } finally {
                     lock.unlock();
                 }
             } else {
                 lock.lock();
                 try {
                     teksayilar.add(sayi);
                 } finally {
                     lock.unlock();
                 }
             }

             if (Asal(sayi)) {
                 lock.lock();
                 try {
                     asalsayilar.add(sayi);
                 } finally {
                     lock.unlock();
                 }
             }
         }
     }

    
    private static boolean Cift(int number) {
        return number % 2 == 0;
    }

   
    private static boolean Asal(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
