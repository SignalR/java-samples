package microsoft.aspnet.signalr.stockticker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class Program {
	private static final String DEFAULT_SERVER_URL = "http://localhost:8080/";
	private static final int FIELD_COLS = 10;
	private static final int MAX_UPDATE_COUNT = 3;
	
	private static Stock[] mCurrentStocks = null;
	
	private static Object mSync = new Object();
	
	public static void main(String[] args) {
		
		String url;
		if (args.length > 0) {
			url = args[0];
		} else {
			url = DEFAULT_SERVER_URL;
		}
		
		HubConnection connection = new HubConnection(url);
		
		final HubProxy proxy = connection.createHubProxy("StockTicker");
		
		connection.error(new ErrorCallback() {
			
			@Override
			public void onError(Throwable error) {
				System.err.println("There was an error communicating with the server.");
				System.err.println("Error detail: " + error.toString());
				
				error.printStackTrace(System.err);
			}
		});
		
		proxy.subscribe(new Object() {
			@SuppressWarnings("unused")
			public void marketOpened() {
				synchronized (mSync) {
					printf("Market opened!");
				}
			}
			
			@SuppressWarnings("unused")
			public void marketClosed() {
				synchronized (mSync) {
					printf("Market closed!");
				}
			}
			
			@SuppressWarnings("unused")
			public void marketReset() {
				synchronized (mSync) {
					clearScreen();
				}
			}
			
			@SuppressWarnings("unused")
			public void updateStockPrice(Stock stock) {
				synchronized (mSync) {
					if (mCurrentStocks != null) {
						updateStock(stock);
					}					
				}
				drawStocksTable();
			}
		});
		
		connection.start().done(new Action<Void>() {
			
			@Override
			public void run(Void obj) throws Exception {
				proxy.invoke(Stock[].class, "getAllStocks").done(new Action<Stock[]>() {
					
					@Override
					public void run(Stock[] stocks) throws Exception {
						synchronized (mSync) {
							mCurrentStocks = stocks;
						}
						
						drawStocksTable();
					}
				});
			}
		});
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void drawStocksTable() {
		synchronized (mSync) {
			clearScreen();
			printf("------------------------------------------------------------------------------");
			printf("|  Symbol  |  Price   |   Open   |   High   |   Low    |  Change  |    %%     |");
			printf("------------------------------------------------------------------------------");
			
			for (Stock stock: mCurrentStocks) {
				writeStock(stock);
				printf("------------------------------------------------------------------------------");
			}
			
			int max = mCurrentStocks.length;
			if (MAX_UPDATE_COUNT < max) {
				max = MAX_UPDATE_COUNT;
			}
			
			printf("Latest news!");
			
			for (int i = 0; i < MAX_UPDATE_COUNT; i++) {
				String upDown = "=";
				double change = mCurrentStocks[i].getChange();
				
				if (change > 0) {
					upDown = "▲";
				} else if (change < 0) {
					upDown = "▼";
				}
				
				// SYMBOL PRICE UP/DOWN CHANGE PERCENT
				printf("%s %.2f %s %.2f (%.2f %%)",
					mCurrentStocks[i].getSymbol(),
					mCurrentStocks[i].getPrice(),
					upDown,
					change,
					mCurrentStocks[i].getPercentChange());
			}
		}
	}
	
	private static void updateStock(Stock stock) {
		for (int i = 0; i < mCurrentStocks.length; i++) {
			if (mCurrentStocks[i].getSymbol().equals(stock.getSymbol())) {
				mCurrentStocks[i] = stock;
			}
		}
		
		Arrays.sort(mCurrentStocks, new Comparator<Stock>() {

			@Override
			public int compare(Stock o1, Stock o2) {
				return o1.getLastUpdateTime().compareTo(o2.getLastUpdateTime());
			}
		});
	}
	
	private static void writeStock(Stock stock) {
		printf("|%s|%s|%s|%s|%s|%s|%s|",
			expand(stock.getSymbol()),
			expand(stock.getPrice()),
			expand(stock.getDayOpen()),
			expand(stock.getDayHigh()),
			expand(stock.getDayLow()),
			expand(stock.getChange()),
			expand(stock.getPercentChange()));
	}
	
	private static void clearScreen() {
		// TODO: Fix flickering for Win32 or find a nicer, cross-platform
		// way to clear the screen
		for (int i = 0; i < 200; i++) {
			System.out.println();
		}
	}
	
	private static void printf(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	private static String expand(String obj) {
		String str = obj.toString();
		
		if (str.length() > FIELD_COLS) {
			str = str.substring(0, FIELD_COLS);
		} else if (str.length() < FIELD_COLS) {
			double spaces = FIELD_COLS - str.length();
			
			int before = (int)Math.floor(spaces / 2);
			
			int after = (int)Math.ceil(spaces / 2);
			
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < before; i++) {
				sb.append(" ");
			}
			
			sb.append(str);
			
			for (int i = 0; i < after; i++) {
				sb.append(" ");
			}
			
			str = sb.toString();
		}
		
		return str;
	}
	
	private static String expand(double dbl) {
		return expand(String.format("%.2f", dbl));
	}
}
