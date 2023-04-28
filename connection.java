import java.sql.*;  
import java.util.*;  
import java.io.*;


//Model
public class connection{
	public float model(){
        float invoiceTotal = 0;
		try{  
		int numberOfProducts;
		int prodFound;
		Scanner sc = new Scanner(System.in);  
		String product_id, prodDesc, perishable;
		float availableQty, price, discQty, discPercent, discPerDay;
		java.sql.Date purchaseDate;
		//load the driver class  
		Class.forName("com.mysql.cj.jdbc.Driver");
		//create  the connection object  
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "root");
		//create the statement object
		Statement stmt=con.createStatement();  
		con.setAutoCommit(false);
		System.out.print("\nEnter Number of Products you want to Buy: ");  
		numberOfProducts = sc.nextInt();
		String[] productID = new String[numberOfProducts];  
		String[] prodDescription = new String[numberOfProducts];
		int prodQty[] = new int[numberOfProducts];
		float prodPrice[] = new float[numberOfProducts];
		float prodDiscount[] = new float[numberOfProducts];
		float prodAmount[] = new float[numberOfProducts];
		float lineAmount = 0;
		int lineQty = 0;
		int perishDays = 0;
		
	  
		for (int i = 0;i < numberOfProducts; i++)
		{
			prodFound = 0;
			System.out.println("Enter Product Code: ");
		   
			product_id = sc.next();  
			ResultSet rs=stmt.executeQuery("select Product_Description, Product_Quantity, Price, Discount_Quantity, discount_Percent, Perishable_YN, Purchase_Date, Perish_Discount_Per_Day, datediff(sysdate(), Purchase_Date) perish_days from product where product_id = '" + product_id + "'"); 
			while(rs.next()) 
			{
				prodFound = 1;
				prodDesc = rs.getString(1);
				availableQty = rs.getFloat(2);
				if (availableQty == 0)
				{
					System.out.printf("Available Qty is 0, Please enter different Product Code!\n");
					i--;
					break;
				}			   
				price = rs.getFloat(3);
				discQty = rs.getFloat(4);
				discPercent = rs.getFloat(5);
				perishable = rs.getString(6);
				purchaseDate = rs.getDate(7);
				discPerDay = rs.getFloat(8);
				perishDays = rs.getInt(9);
				System.out.println("Available Quantity for " + prodDesc +": " + availableQty);
				System.out.println("Is this product perishable (Y/N): " + perishable);
				System.out.println("Enter Quantity: ");
				lineQty =  Integer.parseInt(sc.next()); 
				if (lineQty > (int)availableQty)
				{
					System.out.printf("Available Qty: %.0f\n", availableQty);
					lineQty = (int)availableQty;
				}			   
				productID[i] = product_id;
				prodDescription[i] = prodDesc;
				prodQty[i] = lineQty;
				
				prodPrice[i] = price;
				prodDiscount[i] = 0;
				lineAmount = lineQty * price;
				if (perishable.equals("Y") && perishDays > 0 && discPerDay > 0)
				{
					lineAmount = lineAmount - (lineAmount * (discPerDay * perishDays) / 100);
					prodDiscount[i] = (lineQty * price) - lineAmount;
				}
				else if (availableQty > discQty)
				{
					if ((availableQty - discQty) > lineQty)
					{
						lineAmount = lineAmount - (lineAmount * discPercent / 100);
					}
					else
					{
						lineAmount = lineAmount - (((availableQty - discQty)  * price) * discPercent / 100);
					}
					prodDiscount[i] = (lineQty * price) - lineAmount;
				}
				prodAmount[i] = lineAmount;
				invoiceTotal += lineAmount;
			}
			if (prodFound == 0)
			{
				System.out.println("Error: Product NOT Found");
				i--;
			}
		}
        System.out.printf("\n\n\n\n----------------------------------------------------------------------------\n");
		System.out.printf("                   ****  Product Invoice  ****\n");
        System.out.printf("----------------------------------------------------------------------------\n");
		for (int i = 0;i < numberOfProducts; i++)
		{
			System.out.printf("Product: %s  Quantity: %d  Price: %.2f  Discount: %.2f  Amount: %.2f\n", 
											 prodDescription[i], prodQty[i], prodPrice[i], prodDiscount[i], prodAmount[i]);
		
			prodFound = stmt.executeUpdate("update product set Product_Quantity = Product_Quantity - " + prodQty[i] + " where product_id = '" + productID[i] + "'");
		}
        System.out.printf("----------------------------------------------------------------------------\n");
		if (invoiceTotal > 0)
		{
			System.out.printf("Invoice Total: %.2f\n", invoiceTotal);
			
		}
        System.out.printf("----------------------------------------------------------------------------\n\n\n\n");
		con.commit();
        con.close();

	 }catch(Exception e){ System.out.println(e);}
     return invoiceTotal;
		}	
        
	}


interface Payment {
    void processPayment(float amount);
}

// CashPayment class for cash payments
class CashPayment implements Payment {

    private Float amount;
    public CashPayment(float Amount) {
        this.amount = Amount;
        System.out.printf("\n--------------------------------------------------------------------------\n");
        System.out.println("Cash payment of " + amount + " received.");
		System.out.println("Payment Successfull");
        System.out.println("THANK YOU FOR SHOPPING WITH US!!!");
        System.out.printf("--------------------------------------------------------------------------\n\n");

    }
    public void processPayment(float amount) {
        // no further process
        System.out.println("Nothing Happens");
    }
}

// CreditCardPayment class for credit card payments
class CreditCardPayment implements Payment {
    private String cardNumber;
    private String expirationDate;
    private int cvv;
    private Float amount;


    public CreditCardPayment(float Amount, String cardNumber, String expirationDate, int cvv) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = Amount;
        System.out.printf("\n--------------------------------------------------------------------------\n");
        System.out.println("Credit card payment of " + amount + " received with card number " + cardNumber + ".");
		System.out.println("Payment Successfull");
        System.out.println("THANK YOU FOR SHOPPING WITH US!!!");
        System.out.printf("--------------------------------------------------------------------------\n\n");

    }
    public void processPayment(float amount) {
        // the process of money transaction by connecting to bank server should be implemented here
        System.out.println("Bank Server Connection");
    }

}

// DebitCardPayment class for debit card payments
class DebitCardPayment implements Payment {
    private String cardNumber;
    private String expirationDate;
    private int cvv;
    private Float amount;

    public DebitCardPayment(float Amount, String cardNumber, String expirationDate, int cvv) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = Amount;
        System.out.printf("\n--------------------------------------------------------------------------\n");
        System.out.println("Debit card payment of " + amount + " received with card number " + cardNumber + ".");
		System.out.println("Payment Successfull");
        System.out.println("THANK YOU FOR SHOPPING WITH US!!!");
        System.out.printf("--------------------------------------------------------------------------\n\n");
		
    }

    public void processPayment(float amount) {
        // the process of money transaction by connecting to bank server should be implemented here
        System.out.println("Bank Server Connection");
    }
}

// UPIPayment class for upi payments
class UPIPayment implements Payment {
    private String UpiId;
    private Float amount;

    public UPIPayment(float Amount, String UpiId) {
        this.UpiId = UpiId;
        this.amount = Amount;
        System.out.printf("\n--------------------------------------------------------------------------\n");
        System.out.println("UPI Payment payment of " + amount + " received with Upi Id " + UpiId + ".");
		System.out.println("Payment Successfull");
        System.out.println("THANK YOU FOR SHOPPING WITH US!!!");
        System.out.printf("--------------------------------------------------------------------------\n\n");
    }

    public void processPayment(float amount) {
        // the process of money transaction by connecting to bank server should be implemented here
        System.out.println("Bank Server Connection");
    }
}


// GiftCardPayment class for gift card payments
class GiftCardPayment implements Payment {
    private String giftCardNumber;
    private String pin;
    private Float amount;

    public GiftCardPayment(float Amount, String giftCardNumber, String pin) {
        this.giftCardNumber = giftCardNumber;
        this.pin = pin;
        this.amount = Amount;
        System.out.printf("\n--------------------------------------------------------------------------\n");
        System.out.println("Gift card payment of " + amount + " received with gift card number " + giftCardNumber + ".");
		System.out.println("Payment Successfull");
        System.out.println("THANK YOU FOR SHOPPING WITH US!!!");
        System.out.printf("--------------------------------------------------------------------------\n\n");
    }

    public void processPayment(float amount) {
        // Authentication of the Gift Card happens here
        System.out.println("Gift Card Server Connection");
    }
}

// PaymentFactory class to create payment objects
class PaymentFactory {
    public static Payment createPayment(String paymentType, float Amount, String[] paymentInfo) {
        switch (paymentType) {
            case "cash":
                return new CashPayment(Amount);
            case "creditCard":
                return new CreditCardPayment(Amount, paymentInfo[0], paymentInfo[1], Integer.parseInt(paymentInfo[2]));
            case "debitCard":
                    return new DebitCardPayment(Amount, paymentInfo[0], paymentInfo[1], Integer.parseInt(paymentInfo[2]));
            case "upi":
                    return new UPIPayment(Amount, paymentInfo[0]);
            case "giftCard":
                    return new GiftCardPayment(Amount, paymentInfo[0], paymentInfo[1]);
            default:
                throw new IllegalArgumentException("Invalid payment type: " + paymentType);
        }
    }
}

abstract class ReviewCollector {
    
        public void collectReview() {
        
        System.out.println("star rating (* being worst and *** being the best)");
        System.out.println("Please enter your review along with the star rating with a space:");
        Scanner input = new Scanner(System.in);
        String review = input.nextLine();
        saveReviewToFile(review);
    }
    
    protected abstract void saveReviewToFile(String review);
}

class FileReviewCollector extends ReviewCollector {
    
    @Override
    protected void saveReviewToFile(String review) {
        try {
            FileWriter writer = new FileWriter("reviews.txt", true);
            writer.write(review + "\n");
            writer.close();
            System.out.println("Review saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the review to file.");
            e.printStackTrace();
        }
    } 
}

