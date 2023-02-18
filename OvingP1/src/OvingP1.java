import java.util.Collections;
import java.util.Vector;

public class OvingP1 extends Thread{
    int threadNumber;
    int start;
    int end;
    Vector <Integer> list;
    public OvingP1(int threadNumber, int start, int end,Vector <Integer> list){
        this.threadNumber =threadNumber;
        this.start=start;
        this.end=end;
        this.list=list;
    }

    public Vector<Integer> getList() {
        return list;
    }

    public static void main(String[] args) throws InterruptedException {
        int start=0;
        int end=100;
        int numberOfThreads=7;
        Vector<OvingP1> threads=new Vector<>();
        Vector<Integer> finalList=new Vector<>();

        double startTime=System.currentTimeMillis();
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new OvingP1(i, start + ((end - start) / numberOfThreads) * i,
                    start + ((end - start) / numberOfThreads) * (i + 1), finalList));
        }

        for (Thread thread:threads) {
            thread.start();
        }
        for (Thread thread:threads) {
            thread.join();
        }
        double endTime=System.currentTimeMillis();
        Collections.sort(finalList);
        System.out.println(finalList);
        System.out.println("Runtime: "+ (endTime-startTime) + " ms");
    }


    public void addPrimeNumbersToList(){
        Vector<Integer> finalList=getList();
        for (int i = start; i <=end; i++) {
            boolean flag=false;
            for (int j = 2; j <=i/2; j++) {
                if (i % j == 0) {
                    flag = true;
                    break;
                }
            }
            if(!(flag || (i==0 || i==1))){
                finalList.add(i);
            }
        }
    }

    @Override
    public void run() {
        addPrimeNumbersToList();
    }

}
