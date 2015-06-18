
 
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 
public class AutoTester {
 
    static class TestResult {
        public String phone;
        public int taskNumber;
        public String output;
    }
 
    private static final String TARGET_URL = "http://127.0.0.1:8080/checkAssignment"; // можно пока использовать для тестов
 
    private static final int timeout = 60;
    
    private String phone;
    private int taskNumber;
    private HttpURLConnection conn;
    private ByteArrayOutputStream bos;
    private PrintStream oldPs, ps;
 
    private AutoTester(String phone, int taskNumber) {
        this.phone = phone;
        this.taskNumber = taskNumber;
    }
 
    public static AutoTester start(String phone, int taskNumber) {
        AutoTester res = new AutoTester(phone, taskNumber);
 
        res.bos = new ByteArrayOutputStream();
        res.ps = new PrintStream(res.bos);
 
        res.oldPs = System.out;
        System.setOut(res.ps);
 
        return res;
    }
 
    public void close() {
        ps.flush();
        ps.close();
 
        System.setOut(oldPs);
 
        // create JSON
        TestResult res = new TestResult();
        res.phone = phone;
        res.taskNumber = taskNumber;
 
        try {
            res.output = new String(bos.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            res.output = new String(bos.toByteArray());
        }
 
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(res);
 
        // post it
        try {
            URL url = new URL(TARGET_URL);
            conn = (HttpURLConnection) url.openConnection();
 
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
        
            OutputStream os = null;
            DataOutputStream dos = null;
            InputStream is = null;
            DataInputStream dis = null;
            try {
            	
            	os = conn.getOutputStream();
            	dos = new DataOutputStream(os);
            	
            	dos.writeInt(json.getBytes("UTF-8").length);
                os.write(json.getBytes("UTF-8"));
           
                
 
            int respCode = conn.getResponseCode();
 
            if (respCode == 200)
                System.err.println(this.getClass().getName() + ": OK!");
            else
                System.err.println(this.getClass().getName() + ": error #" + respCode);
            
            } catch (Exception ex) {
                System.err.println(this.getClass().getName() + " error: " + ex.getMessage());
            }
        finally {
        	dos.close();
            os.close();
        }
            is = conn.getInputStream();
            dis = new DataInputStream(is);
            
            try{
            int size;
    		String serverResponse;
    		byte [] data;
    		int i;
    		for(i=0; i<timeout; i++){
    			if(is.available()>0){
    			size = dis.readInt();
    				data = new byte [size];
    				is.read(data);
    				serverResponse = new String (data);
    				System.out.println(serverResponse);
    				break;
    			}
    			Thread.sleep(1000);
    		}	
    		if(i == 59){
    			throw new Exception("Истекло время ожидания сервера проверки домашних заданий, сервер не отвечает. Попробуйте позже...");
    		}
            
        } catch (Exception ex) {
            System.err.println(this.getClass().getName() + " error: " + ex.getMessage());
            
            } finally {
                dis.close();
                is.close();
            } 
        }  catch(Exception e){
        	e.printStackTrace();
        }
            
        }    
    
}