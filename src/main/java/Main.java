import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    //static File myFile = new File("basket.txt");
    //static File myFile = new File("basket.json");
    static Scanner scanner = new Scanner(System.in);
    static String products[] = {"Хлеб", "Яблоки", "Молоко"};
    static int prices[] = {75, 50, 100};

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        XMLSettingsReader settings = new XMLSettingsReader(new File("shop.xml"));
        File loadFile = new File(settings.loadFile);
        File saveFile = new File(settings.saveFile);
        File logFile = new File(settings.logFile);

        int sumProducts = 0;



        int count[] = new int[3];
        int summ[] = new int[3];

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }

        //Basket basket = null;
        Basket basket = createBasket(loadFile, settings.isLoad, settings.loadFormat);
        ClientLog log = new ClientLog();

        while (true) {
            int productNumber = 0;
            int productCount = 0;
            System.out.println("Выберите товар и количество или введите 'end'");

            String input = scanner.nextLine();

            if (input.equals("end")) {
                if (settings.isLog) {
                    log.exportAsCSV(logFile);
                }
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
            if (settings.isLog) {
                log.log(productNumber, productCount);
            }
            //basket.saveTxt(myFile);
            //basket.saveJSON(saveFile);
            if (settings.isSave) {
                switch (settings.saveFormat) {
                    case "json":
                        basket.saveJSON(saveFile);
                        break;
                    case "txt" :
                        basket.saveTxt(saveFile);
                        break;
                }
            }
        }


        basket.printCart();
    }

    private static Basket createBasket(File loadFile, boolean isLoad, String loadFormat) {
        Basket basket;

        if (isLoad && loadFile.exists()) {
            switch (loadFormat) {
                case "json":
                    basket = Basket.loadFromJSONFile(loadFile);
                    break;
                case "txt" :
                    basket = Basket.loadFromTxtFile(loadFile);
                    break;
                default:
                    basket = new Basket(products, prices);
            }
        } else {
            basket = new Basket(products, prices);
        }

        return basket;
    }

}
