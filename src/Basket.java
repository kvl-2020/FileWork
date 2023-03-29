import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;
    private String products[];
    private int prices[];

    int[] counts = null;
    int[] summ = null;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;

        counts = new int[products.length];
        summ = new int[products.length];
    }

    public boolean addToCart(int productNum, int amount) {
        if (productNum < 0 || productNum > products.length) {
            return false;
        }

        int tempResult = prices[productNum] * amount;
        summ[productNum] += tempResult;
        counts[productNum] += amount;
        return true;
    }

    public void printCart() {
        int sumProducts = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (counts[i] <= 0) {
                continue;
            }
            sumProducts += prices[i] * counts[i];
            System.out.println(products[i] + " " + counts[i] + " шт " +
                    prices[i] + "руб/шт " + summ[i] + " руб в сумме");
        }
        System.out.println("Итого: " + sumProducts + " руб");
    }

    public boolean saveTxt(File textFile) {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (var product : products) {
                out.print(product + " ");
            }
            out.println();
            for (var price : prices) {
                out.print(price + " ");
            }
            out.println();
            for (var count : counts) {
                out.print(count + " ");
            }
            out.println();
            for (var sum : summ) {
                out.print(sum + " ");
            }
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveBin(File file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            basket = (Basket) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return basket;
    }

    static Basket loadFromTxtFile(File textFile) {
        String[] products = null;
        int[] prices = null;
        int[] counts = null;
        int[] summ = null;
        Basket basket = null;
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(textFile));
            String productsStr = buffReader.readLine();
            String pricesStr = buffReader.readLine();
            String countStr = buffReader.readLine();
            String summStr = buffReader.readLine();

            products = productsStr.split(" ");
            prices = Arrays.stream(pricesStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            counts = Arrays.stream(countStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            summ = Arrays.stream(summStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();

            basket = new Basket(products, prices);
            basket.counts = counts;
            basket.summ = summ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return basket;

    }

    public int getProductsLenght() {
        return products.length;
    }
}
