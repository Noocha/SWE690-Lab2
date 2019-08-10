public class Sale {
    private int saleID;
    private String saleTransaction;
    private String item;
    static final int SALETRANSACTION_SIZE = 20;
    static final int ITEM_SIZE = 50;

    public Sale(int saleID, String saleTransaction, String item) {
        this.saleID = saleID;
        this.saleTransaction = saleTransaction;
        this.item = item;
    }

    public Sale() {}

    public int getSaleID() {
        return saleID;
    }

    public String getItem() {
        return item;
    }

    public byte[] getItemInBytes() {
        byte[] itemBytes = new byte[ITEM_SIZE];
        System.arraycopy(item.getBytes(), 0, itemBytes, 0, item.length());
        return itemBytes;
    }

    public String getSaleTransaction() {
        return saleTransaction;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public void setSaleTransaction(String saleTransaction) {
        this.saleTransaction = saleTransaction;
    }

    public  byte[] getSaleTransactionInBytes() {
        byte[] transactionBytes = new byte[SALETRANSACTION_SIZE];
        System.arraycopy(saleTransaction.getBytes(), 0, transactionBytes, 0, saleTransaction.length());
        return transactionBytes;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleID=" + saleID +
                ", saleTransaction='" + saleTransaction + '\'' +
                ", item='" + item + '\'' +
                '}';
    }

}
