
public class Main {
    public static void main(String[] args) {
        AutoTester tester = AutoTester.start("1111111", 7); // мобильный клиента и номер задачи
        try {
            // до вызова close весь вывод print/println "улетает" на сервер
           /* for (int i = 0; i <= 20; i += 3)
                System.out.print(i + " ");
 
            System.out.println();*/
        	
        	
        	System.out.println("Привет Мир");
        } finally {
            tester.close();
        }
 
        // после вызова close print/println работает как по умолчанию
        System.out.println("Готово!");
    }
}
