
public class Main {
    public static void main(String[] args) {
        AutoTester tester = AutoTester.start("1111111", 7); // ��������� ������� � ����� ������
        try {
            // �� ������ close ���� ����� print/println "�������" �� ������
           /* for (int i = 0; i <= 20; i += 3)
                System.out.print(i + " ");
 
            System.out.println();*/
        	
        	
        	System.out.println("������ ���");
        } finally {
            tester.close();
        }
 
        // ����� ������ close print/println �������� ��� �� ���������
        System.out.println("������!");
    }
}
