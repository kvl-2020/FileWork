import java.io.File;
import java.util.Scanner;

public class Main {
    static File myFile = new File("basket.bin");

    public static void main(String[] args) {

        int sumProducts = 0;
        Scanner scanner = new Scanner(System.in);

        String products[] = {"Хлеб", "Яблоки", "Молоко"};
        int prices[] = {75, 50, 100};
        int count[] = new int[3];
        int summ[] = new int[3];

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }

        Basket basket = null;
        if (myFile.exists()) {
            basket = Basket.loadFromBinFile(myFile);
        } else {
            basket = new Basket(products, prices);
        }


        while (true) {
            int productNumber = 0;
            int productCount = 0;
            System.out.println("Выберите товар и количество или введите 'end'");

            String input = scanner.nextLine();

            if (input.equals("end")) {
                break;
            }

            String splitedInput[] = input.split(" ");
            productNumber = Integer.parseInt(splitedInput[0]) - 1;
            productCount = Integer.parseInt(splitedInput[1]);

            if (productNumber < 0 || productNumber > basket.getProductsLenght()) {
                System.out.println("Не правильно введён номер товара");
                continue;
            }

            basket.addToCart(productNumber, productCount);
            basket.saveBin(myFile);
        }


        basket.printCart();
    }

}
