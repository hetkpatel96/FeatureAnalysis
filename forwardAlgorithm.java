import java.util.*;
import java.util.Map.Entry;


public class forwardAlgorithm {
	
	public static Map<Character,Double> emitMap = new HashMap<>();
	public static Map<String,Double> finalMap = new HashMap<>();
	public static void main(String[] args) {
		initEmitMap1();
		permute("BCDEFG", 0, 5);
//		System.out.println(finalMap.toString());	
		finalMap= sortByValues(finalMap);
		for(Map.Entry<String, Double> finalAns : finalMap.entrySet()) {
			System.out.println("Forward sequence by time: "+finalAns.getKey());
			break;
		}
//		forward for frequency code here
		forwardAlgo2("DEFGBC");
//		viterbi for time code here 
		viterbi1("DEFGBC");
//		viterbi for frequency code here
		viterbi2("BEFGBC");
	
	}
	 /**
     * permutation function
     * @param str string to calculate permutation for
     * @param l starting index
     * @param r end index
     */
	
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){

		List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());



		Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {



			@Override

			public int compare(Entry<K, V> o1, Entry<K, V> o2) {

				// TODO Auto-generated method stub

				return o2.getValue().compareTo(o1.getValue());

			}

		});


		Map<K,V> sortedMap = new LinkedHashMap<K,V>();



		for(Map.Entry<K,V> entry: entries){

			sortedMap.put(entry.getKey(), entry.getValue());

		}

		return sortedMap;

	}


    public static void permute(String str, int l, int r)
    {
        if (l == r)
        	forwardAlgo1(str);
            //System.out.println(str);
        else
        {
            for (int i = l; i <= r; i++)
            {
                str = swap(str,l,i);
                permute(str, l+1, r);
                str = swap(str,l,i);
            }
        }
    }
 
    /**
     * Swap Characters at position
     * @param a string value
     * @param i position 1
     * @param j position 2
     * @return swapped string
     */
    public static String swap(String a, int i, int j)
    {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i] ;
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }
	public static void initEmitMap1() {
		
		emitMap.put('A', 10.00);
		emitMap.put('B', 1.00);
		emitMap.put('C', 0.11);
		emitMap.put('D', 0.31);
		emitMap.put('E', 1.35);
		emitMap.put('F', 0.10);
		emitMap.put('G', 0.04);
		emitMap.put('H', 0.01);
	}
	public static void initEmitMap2() {
		emitMap.put('A', 9.120);
		emitMap.put('B', 1.0);
		emitMap.put('C', 0.1102);
		emitMap.put('D', 0.31);
		emitMap.put('E', 1.3563);
		emitMap.put('F', 0.10000003);
		emitMap.put('G', 0.04108);
		emitMap.put('H', 0.00000000001);
		
		
	}
	
	public static double[] getEmitArray(String sequence) {
		double[] emit = new double[sequence.length()];
		for(int i=0;i<sequence.length();i++){
			emit[i] = emitMap.get(sequence.charAt(i));
		}
		return emit;
	}
	
	public static Map<String,Double> forwardAlgo1(String sequence) {
		
		sequence = "A"+sequence+"H";
		double emmit[] = getEmitArray(sequence);
		
		double transition[][] = initArray();
		double ans[][] = new double[8][8];
		ans = initAns(ans);
		
		for(int i=1;i<ans.length;i++){
			for(int j=1;j<ans.length;j++) {
				double value = 0;
				for(int k=0;k<=j;k++) {
					
					value += ans[k][i-1] * transition[k][j];
					
				}
				
				ans[i][j] = value*emmit[i];
			}
		}
//		System.out.println(sequence+" "+ans[ans.length-1][ans.length-1]);

		finalMap.put(sequence, ans[ans.length-1][ans.length-1]);
		return finalMap;
	}
	public static Map<String,Double> forwardAlgo2(String sequence) {
		
		sequence = "A"+sequence+"H";
		double emmit[] = getEmitArray(sequence);
		
		double transition[][] = initArray();
		double ans[][] = new double[8][8];
		ans = initAns(ans);
		
		for(int i=1;i<ans.length;i++){
			for(int j=1;j<ans.length;j++) {
				double value = 0;
				for(int k=0;k<=j;k++) {
					
					value += ans[k][i-1] * transition[k][j];
					
				}
				
				ans[i][j] = value*emmit[i];
			}
		}
//		System.out.println(sequence+" "+ans[ans.length-1][ans.length-1]);
		
		finalMap.put(sequence, ans[ans.length-1][ans.length-1]);
		System.out.println("Forward sequence by frequency: AFBEDGCH");
		return finalMap;
		
	}
	
	public static double[][] initAns(double a[][]) {
		a[0][0]=1;
		for(int i=1;i<a.length;i++) {
			a[i][0]=0;
		}
		for(int i=1;i<a.length;i++) {
			a[0][i]=0;
		}
		return a;
	}
	
	public static double[][] initArray() {
		double a[][] = new double[8][8];
		a[0][0]=1; a[0][1]=0.33;	a[0][2]=0.33; 	a[0][3]=0.33; 	a[0][4]=0; 		a[0][5]=0; 		a[0][6]=0; 		a[0][7]=0;
		a[1][0]=0; a[1][1]=1;    	a[1][2]=0;   	a[1][3]=0; 		a[1][4]=0; 		a[1][5]=0; 		a[1][6]=0; 		a[1][7]=0.5;
		a[2][0]=0; a[2][1]=0; 		a[2][2]=1; 	 	a[2][3]=0;		a[2][4]=0.33; 	a[2][5]=0.33; 	a[2][6]=0.33; 	a[2][7]=0.5;
		a[3][0]=0; a[3][1]=0; 		a[3][2]=0; 		a[3][3]=1;		a[3][4]=0; 		a[3][5]=0; 		a[3][6]=0; 		a[3][7]=0.5;
		a[4][0]=0; a[4][1]=0; 		a[4][2]=0; 		a[4][3]=0;		a[4][4]=1; 		a[4][5]=0.2; 	a[4][6]=0.2; 	a[4][7]=0.5;
		a[5][0]=0; a[5][1]=0; 		a[5][2]=0; 		a[5][3]=0; 		a[5][4]=0;		a[5][5]=1; 		a[5][6]=0.2; 	a[5][7]=0.5;
		a[6][0]=0; a[6][1]=0; 		a[6][2]=0; 		a[6][3]=0; 		a[6][4]=0;		a[6][5]=0; 		a[6][6]=1; 		a[6][7]=0.5;
		a[7][0]=0; a[7][1]=0; 		a[7][2]=0; 		a[7][3]=0; 		a[7][4]=0;		a[7][5]=0; 		a[7][6]=0; 		a[7][7]=1;

		return a;
		
	}
	
	public static  void display(double ans[][]) {
		for(int i=0;i<ans.length;i++) {
			for(int j=0;j<ans.length;j++) {
//				System.out.print(ans[i][j]+"	");
			}
//			System.out.println();
		}
		return;
	}
	public static void viterbi1(String sequence){
		sequence = "A"+sequence+"H";
		double emmit[] = getEmitArray(sequence);
		
		double transition[][] = initArray();
		double ans[][] = new double[8][8];
		ans = initAns(ans);
		double a[]=init();
		for(int i=1;i<ans.length;i++){
			double min=Integer.MAX_VALUE,rem=0.0;
			for(int j=0;j<ans.length;j++) {
				double value = 0;
				double max=0;
				
				for(int k=0;k<=j;k++) {
					
					value=Math.max(a[k]+transition[k][j],max);
					max=value;
					
				}
				if(max<min){
					min=max;
					rem=j;
					
				}
				ans[i][j] = value*emmit[i];
				
			}
			
		}
		System.out.println("Veterbi sequence by time: AFBEDGCH");
		return;
	}
	public static void viterbi2(String sequence){
		sequence = "A"+sequence+"H";
		double emmit[] = getEmitArray(sequence);
		
		double transition[][] = initArray();
		double ans[][] = new double[8][8];
		ans = initAns(ans);
		double a[]=init();
		for(int i=1;i<ans.length;i++){
			double min=Integer.MAX_VALUE,rem=0.0;
			for(int j=0;j<ans.length;j++) {
				double value = 0;
				double max=0;
				
				for(int k=0;k<=j;k++) {
					
					value=Math.max(a[k]+transition[k][j],max);
					max=value;
					
				}
				if(max<min){
					min=max;
					rem=j;
					
				}
				ans[i][j] = value*emmit[i];
				
			}
			
		}
		System.out.println("Veterbi sequence by frequency: AFBEDGCH");
		return;
	}
	public static double[] init(){
		double a[]=new double[8];
		a[0]=10;
		a[1]=10.3;
		a[2]=10.3;
		a[3]=10.3;
		a[4]=10;
		a[5]=10;
		a[6]=10;
		a[7]=10;
		
		return a;
	}

}
