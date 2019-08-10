import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileIO {

    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "src/Sale.txt";

        String fileNameRaf = "src/Sale.raf";

        DecimalFormat decf = new DecimalFormat("#, ###");

        ArrayList<Sale> list1 = new ArrayList<>(100000);

        long startTime = System.nanoTime();
        readDataStream(fileName, list1);
        long endTime = System.nanoTime();

        System.out.println("Read data stream : " + decf.format(endTime - startTime));
        System.out.println(list1.get(3).toString());


        ArrayList<Sale> list2 = new ArrayList<>(100000);

        long startTime2 = System.nanoTime();
        readBuffer(fileName, list2);
        long endTime2 = System.nanoTime();

        System.out.println("Read buffer : " + decf.format(endTime2 - startTime2));
        System.out.println(list2.get(3).toString());


        ArrayList<Sale> list3 = new ArrayList<>(100000);

        long startTime3 = System.nanoTime();
        readRandomAccessFile(fileNameRaf, list3);
        long endTime3 = System.nanoTime();

        System.out.println("Read from Random Access file : " + decf.format(endTime3 - startTime3));
        System.out.println(list3.get(3).toString());

        long startTime4 = System.nanoTime();
        Sale s = readRAFbyRecordNumber(fileNameRaf, 29070);
        long endTime4 = System.nanoTime();
        System.out.println(s.toString());
        System.out.println("Read RAF by Record Number: " + decf.format(endTime4 - startTime4));


        fileName = "mySale.raf";
        writeRandomAccessFile(fileName, list2);

        s = readRAFbyRecordNumber(fileName, 2970);
        System.out.println("record from my sale" + s.toString());

    }

    public static void readDataStream(String fileName, ArrayList list) throws FileNotFoundException {
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
            DataInputStream dataStream = new DataInputStream(fis);
            String line = "";
            while ((line = dataStream.readLine()) != null) {
                processLine(line, list);
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void processLine(String s, List list) {
        String[] dataInline = new String[3];
        dataInline = s.split(",'");

        dataInline[0] = dataInline[0].substring(1, dataInline[0].length());
        dataInline[1] = dataInline[1].replaceAll("'", "");
        dataInline[2] = dataInline[2].substring(0, dataInline[2].length() - 3);

        Sale sale = new Sale(Integer.parseInt(dataInline[0]), dataInline[1], dataInline[2]);

        list.add(sale);
    }

    public static void readBuffer(String fileName, ArrayList list) {
        FileReader fr;
        try {
            fr = new FileReader(fileName);
            BufferedReader bufR = new BufferedReader(fr);
            String line = "";
            while ((line = bufR.readLine()) != null) {
                processLine(line, list);
            }
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readRandomAccessFile(String fileName, ArrayList list) {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                int saleID = raf.readInt();
                byte[] transaction = new byte[Sale.SALETRANSACTION_SIZE];
                raf.read(transaction);

                byte[] item = new byte[Sale.ITEM_SIZE];
                raf.read(item);

                Sale s = new Sale(saleID, new String(transaction), new String(item));
                list.add(s);
            }

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Sale readRAFbyRecordNumber(String fileName, int recordNumber) {

        Sale s = new Sale();

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {

            long position = (recordNumber - 1) * (4 + Sale.SALETRANSACTION_SIZE + Sale.ITEM_SIZE);
            raf.seek(position);

            int saleID = raf.readInt();
            byte[] transaction = new byte[Sale.SALETRANSACTION_SIZE];
            raf.read(transaction);

            byte[] item = new byte[Sale.ITEM_SIZE];
            raf.read(item);

            s = new Sale(saleID, new String(transaction), new String(item));

            raf.close();
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void  writeRandomAccessFile(String fileName, ArrayList<Sale> list) {

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            for (Sale s: list) {
                raf.writeInt(s.getSaleID());
                raf.write(s.getSaleTransactionInBytes());
                raf.write(s.getItemInBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
