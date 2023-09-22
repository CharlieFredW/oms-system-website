package OMS.domain;

public class Main {

    public static void main(String[] args) {

        //test adding orders to order database
        //System.out.println(new OrderCRUD().updateOrderStatus(23,orderStatus.PROCESSED));
        //System.out.println(new OrderCRUD().getOrdersByEmail("KalleKasper@gmail.com"));


        //writing to json file
        /*try (PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\danielz\\Desktop\\e-commerce-prototype\\src\\main\\java\\OMS\\testOms\\test.json"))) {
            out.write(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        new OrderPacker().updateOrderStatus(33, orderStatus.SHIPPED);
//        new OrderPacker().updateOrderStatus(33, orderStatus.PACKAGED);
//        new Customer().getOrdersAndProductsByEmail("skjoldtorden345353463463435@hotmail.com");

        /*OrderPacker order = new OrderPacker();
        System.out.println(order.getShippingInfo(2));*/

//        System.out.println(new OrderCRUD().getCurrentOrders());
    }
}
