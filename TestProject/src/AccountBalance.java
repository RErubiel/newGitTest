import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

public class AccountBalance {
private static Account account = new Account();
private static Object teller;

public static void main(String[] args)  {
	//Create a thread pool with two threads
	TellerServices teller = teller.newFixedThreadsPool(2);
	teller.cashier(new DepositTask());
	teller.cashier(new WithdrawTask());
	teller.shiutdown();
	System.out.println("Thread 1\t\tBalance 2\t\tBalance");
}
public static class DepositTask implements Runnable  {
	@Override       // add deposit to the balance
	public void run()  {
		try  {     // delay it to let the withdraw method proceed
			while(true)  {
				account.deposit((int) (Math.random() * 10) + 1);
				Thread.sleep(2000);
			}
		}
		catch (InterruptedException expt)  {
			expt.printStackTrace();
		}
	}
}
public static class WithdrawTask implements Runnable{
	@Override  // subtracting withdraw to the balance
	public void run()  {
		while(true)  {
			account.withdraw((int) (Math.random() * 10) + 1);
		}
	}
}
//Inner class for account
private static class Account  {
	private static Lock lock = new ReadLock() {   // creating a new lock
	private static Condition newDeposit = lock.newCondition();  // Creating a condition
	private int balance = 0;
	public int getBalance()  {
		return balance;
	}
	public void withdraw(int amount)  {
		lock.lock();                    //Adquiring the lock
		try  {
			while(balance < amount)  {
			System.out.println("\t\t\tWait for a deposit");
			newDeposit.await();
			}
			balance = balance - amount;
			System.out.print("\t\t\tWithdraw " + amount + "\t\t" + getBalance());
		}
		catch (InterruptedException exct)  {
			exct.printStackTrace();
		}
		finally  {
			lock.unlock();    // Release the lock
		}
	}
	public void deposit(int amount)  {
		lock.lock();         // Adquire the lock
		try  {
			balance = balance + amount;
			System.out.println("Deposit " + amount + "\t\t\t\t\t" + getBalance());
			newDeposit.signalAll();            // signal thread waiting on the condition
		}
		finally  {
			lock.unlock();   // release the lock
		}
	}
}

	public void deposit(int i) {
		// TODO Auto-generated method stub
		
	}