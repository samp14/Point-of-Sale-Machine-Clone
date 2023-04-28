
// POS Simulator


/*
create table product (
   product_id varchar(6),
   Product_Description varchar2(100),
   Product_Quantity number(10, 2),
   Price number(10, 2),
   Discount_Quantity number(10, 2),
   discount_Percent number(5, 2),
   Perishable_YN varchar2(1),
   Purchase_Date datetime,
   Perish_Discount_Per_Day number(5, 2)
)
/
insert into product values ('PES001', 'Apple', 25, 225, 20, 10, 'Y', '14-APR-2023', 5);
insert into product values ('PES002', 'Orange', 55, 105, 50, 10, 'Y', '14-APR-2023', 5);
insert into product values ('PES003', 'Mango', 100, 165, 95, 10, 'Y', '14-APR-2023', 5);
insert into product values ('PES004', 'Grapes', 50, 85, 40, 10, 'Y', '14-APR-2023', 5);
insert into product values ('PES005', 'Arial', 15, 300, 13, 10, 'Y', '14-APR-2023', 5);
insert into product values ('PES006', 'Grinder', 15, 300, 13, 10, 'N', '14-APR-2023', 5);
*/



import java.sql.*;  
import java.util.*;  
import java.io.*;




//Controller
public class POS_Simulator {
	public static void main(String args[]){  
		connection obj = new connection();
		float result = obj.model();
		handlePayment.handle(result);
        ReviewCollector reviewCollector = new FileReviewCollector();
        reviewCollector.collectReview();
		}  
	}
	


class handlePayment{

    public static void handle(float invoiceTotal) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Do you want to proceed to payment? (yes/no)");
        String response = input.next();

        if(response.equalsIgnoreCase("yes")) {
            System.out.println("Proceeding to payment...\n");
            //add here
            // code to process payment goes here
            System.out.println("1. Cash Payment");
            System.out.println("2. Credit Card");
            System.out.println("3. Debit Card");
            System.out.println("4. UPI Payment");
            System.out.println("5. Gift Card\n");

            //Scanner input_n = new Scanner(System.in);
            String ans = input.next();
            switch(ans){
                case "1":
                {
                    String[] S = {};
                    PaymentFactory.createPayment("cash", invoiceTotal, S);
                    break;
                }
                case "2":
                {
                    System.out.println("Enter the Credit Card Number");
                    String cn = input.next();
                    if (cn.length() != 16) 
                    {
                        while (cn.length() != 16)
                        {
                            System.out.println("Credit Card Number should be 16 digits. Enter again...");
                            System.out.println("Enter the Credit Card Number");
                            cn = input.next();
                        }
                    }
                    System.out.println("Enter the Credit Card CVV");
                    String ccv = input.next();
                    if (ccv.length() != 3) 
                    {
                        while (ccv.length() != 3)
                        {
                            System.out.println("Credit Card CVV should be 3 digits. Enter again...");
                            ccv = input.next();
                        }
                    }
                    System.out.println("Enter the Expiration Date of the Credit Card");
                    String ced = input.next();
                    if (ced.length() != 10) 
                    {
                        while (ced.length() != 10)
                        {
                            System.out.println("Expiration Date of the Credit Card is not valid. Enter again...");
                            ced = input.next();
                        }
                    }
                    String[] S = {cn, ced, ccv};
                    PaymentFactory.createPayment("creditCard", invoiceTotal, S);
                    break;


                }
                case "3":
                {
                    System.out.println("Enter the Debit Card Number");
                    String dn = input.next();
                    if (dn.length() != 16) 
                    {
                        while (dn.length() != 16)
                        {
                            System.out.println("Debit Card Number should be 16 digits. Enter again...");
                            System.out.println("Enter the Debit Card Number");
                            dn = input.next();
                        }
                    }
                    System.out.println("Enter the Debit Card CVV");
                    String dcv = input.next();
                    if (dcv.length() != 3) 
                    {
                        while (dcv.length() != 3)
                        {
                            System.out.println("Debit Card CVV should be 3 digits. Enter again...");
                            dcv = input.next();
                        }
                    }
                    System.out.println("Enter the Expiration Date of the Debit Card");
                    String ded = input.next();
                    if (ded.length() != 10) 
                    {
                        while (ded.length() != 10)
                        {
                            System.out.println("Expiration Date of the Debit Card is not valid. Enter again...");
                            ded = input.next();
                        }
                    }
                    String[] S = {dn, ded, dcv};
                    PaymentFactory.createPayment("debitCard", invoiceTotal, S);
                    break;
                }
                case "4":
                {
                    {
                        System.out.println("Enter the UPI ID");
                        String un = input.next();
                        if (un.length() != 10) 
                        {
                            while (un.length() != 10)
                            {
                                System.out.println("UPI ID should be 10 digits. Enter again...");
                                System.out.println("Enter the UPI ID");
                                un = input.next();
                            }
                        }
                        String[] S = {un};
                        PaymentFactory.createPayment("upi", invoiceTotal, S);
                        break;
                    }
                }
                case "5":
                {
                    {
                        System.out.println("Enter the Gift Card Number");
                        String gn = input.next();
                        if (gn.length() != 10) 
                        {
                            while (gn.length() != 10)
                            {
                                System.out.println("Gift Card Number should be 10 digits. Enter again...");
                                System.out.println("Enter the Gift Card Number");
                                gn = input.next();
                            }
                        }
                        System.out.println("Enter the Gift Card Pin");
                        String gp = input.next();
                        if (gp.length() != 6) 
                        {
                            while (gp.length() != 6)
                            {
                                System.out.println("Gift Card Pin should be 6 digits. Enter again...");
                                System.out.println("Enter the Gift Card Pin");
                                gp = input.next();
                            }
                        }
                        String[] S = {gn, gp};
                        PaymentFactory.createPayment("giftCard", invoiceTotal, S);
                        break;
                    }
                }
                default:
                {
                    System.out.println("Invalid Option Selected");
                }
            }

            }
            else if(response.equalsIgnoreCase("no")) {
                System.out.println("Exiting...");
                System.exit(0);
                //code to exit the program goes here
            } 
            else {
                System.out.println("Invalid response. Please enter 'yes' or 'no'.");
            }
    }

}























