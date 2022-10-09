import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        Scanner scanner = new Scanner(System.in);
        File file = new File("basket.txt");
        File jsonFile = new File("basket.json");
        File logFile = new File("log.csv");

        String[] products = {"Молоко", "Яблоки", "Сыр", "картофель", "Хлеб"};
        int[] prices = {80, 50, 200, 35, 45};
        Basket basket = new Basket(products, prices);
        ClientLog clientLog = new ClientLog();

        readsFile();

        if (loadFilename != null) {
            File loadFile = new File(loadFilename);
            try {
                if (!loadFile.createNewFile() || loadFile.length() != 0) {

                    if (loadFormat.equals("json")) {
                        basket = Basket.loadFromJson(loadFile);
                    } else if (loadFormat.equals("txt")) {
                        basket = Basket.loadFromTxtFile(loadFile);
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Список товаров:");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб");
        }

        while (true) {
            System.out.println("Выберите товар и количество.");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                basket.printCart();
                basket.saveJson(jsonFile);
                break;
            }

            String[] parts = input.split(" ");
            int numProduct = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            basket.addToCart(numProduct, amount);
            clientLog.log(numProduct, amount);
        }

        if (saveFilename != null) {
            File saveFile = new File(saveFilename);
            if (saveFormat.equals("json")) {
                basket.saveJson(saveFile);
            } else if (saveFormat.equals("txt")) {
                basket.saveTxt(saveFile);
            }
        }
        clientLog.exportAsCSV(logFile);
    }

    public static void readsFile() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                String block = node.getNodeName();
                Element block_element = (Element) node;
                if (!block_element.getElementsByTagName("enabled").item(0).getTextContent().equals("false")) {
                    NodeList nodeListNext = block_element.getChildNodes();
                    for (int j = 0; j < nodeListNext.getLength(); j++) {
                        Node nodeNext = nodeListNext.item(j); //
                        if (Node.ELEMENT_NODE == nodeNext.getNodeType()) {
                            if (nodeNext.getTextContent().equals("true")) {
                                continue;
                            }
                            String key = nodeNext.getNodeName();
                            String value = nodeNext.getTextContent();
                            switch (block) {
                                case "load":
                                    switch (key) {
                                        case "fileName":
                                            loadFilename = value;
                                            break;
                                        case "format":
                                            loadFormat = value;
                                            break;
                                    }
                                    break;
                                case "save":
                                    switch (key) {
                                        case "fileName":
                                            saveFilename = value;
                                            break;
                                        case "format":
                                            saveFormat = value;
                                            break;
                                    }
                                    break;
                                case "log":
                                    logFilename = value;
                                    break;

                            }
                        }
                    }
                }
            }
        }

    }


    static String loadFilename;
    static String loadFormat;
    static String saveFilename;
    static String saveFormat;
    static String logFilename;
}

