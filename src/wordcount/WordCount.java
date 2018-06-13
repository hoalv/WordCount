package wordcount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCount {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		int numThreads = 1;
		int chunksize = 1024;
		String fileName = "D:\\hoalv\\wordcount.txt";
		
		System.out.println("Nhập vào số luồng: ");
		numThreads = input.nextInt();
		input.close();
		
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        BufferedReader reader = null;
        ConcurrentMap<String,Integer> m = new ConcurrentHashMap<String,Integer>();
		try {
			reader = new BufferedReader(new FileReader(fileName));
	        while (true) {
	            String res = ThreadWordCount.readFileAsString(reader,chunksize);
	            if (res.equals("")) {
	            	break;
	            }
	            pool.submit(new ThreadWordCount(res,m));
	        }
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			reader.close();
		}
		pool.shutdown();
		try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.out.println("Pool interrupted!");
            System.exit(1);
        }
		int total = 0;
        for (Map.Entry<String,Integer> entry : m.entrySet()) {
            int count = entry.getValue();
            System.out.format("%-30s %d\n",entry.getKey(),count);
            total += 1;
        }
        System.out.println("Total words = "+total);
	}
}


