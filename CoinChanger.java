import java.util.Set;

public abstract class CoinChanger {
    abstract public int minCoins(int amount, Set<Integer> denominations);

    private static void checkArguments(int amount, Set<Integer> denominations) {
        // Error situations and messages are:
        //   "Amount must be at least 1"
        if (amount < 1){
            throw new IllegalArgumentException("Amount must be at least 1");
        }
        //   "At least one denomination is required"
        if (denominations.isEmpty()){
            throw new IllegalArgumentException("At least one denomination is required");
        }
        //   "Denominations must all be positive"
        denominations.forEach(item -> {
            if(item < 0){
                throw new IllegalArgumentException("Denominations must all be positive");  
              }      
        });
        //   "Denominations must have a 1-unit coin"
        if (!denominations.contains(1)){
            throw new IllegalArgumentException("Denominations must have a 1-unit coin");
        }   
    }

    public static class TopDown extends CoinChanger {
        public int minCoins(int amount, Set<Integer> denominations) {
            checkArguments(amount, denominations);

            return getMinCoins(amount, denominations);
        }

        private static int getMinCoins(int amount, Set<Integer> denominations) 
        {         
            // base case, will never happen because of the exception in checkArguments()
            // but we leave it in case we test this function without checkArguments()
            if (amount == 0) return 0;          
            // Initialize result 
            int result = Integer.MAX_VALUE; 
            
            for (Integer deno : denominations) {
                if(deno <= amount){
                    int subResult = getMinCoins(amount-deno, denominations); 
                    // Check for INT_MAX to avoid overflow and see if 
                    // result can minimized 
                    if (subResult != Integer.MAX_VALUE && subResult + 1 < result){ 
                        result = subResult + 1;
                    }

                }   
            }
            return result; 
        }
    }

    public static class BottomUp extends CoinChanger {
        public int minCoins(int amount, Set<Integer> denominations) {
            checkArguments(amount, denominations);
            return getMinCoins(amount, denominations); 
        }

        private static int getMinCoins(int amount, Set<Integer> denominations) 
        { 
            // table[i] will be storing  
            // the minimum number of coins 
            // required for i value. So  
            // table[amount] will have result 
            Integer table[] = new Integer[amount + 1]; 
       
            // base case (If given value amount is 0), will never happen because of the exception in checkArguments()
            // but we leave it in case we test this function without checkArguments()
            table[0] = 0; 
      
            // Initialize all table values as Infinite 
            for (int i = 1; i <= amount; i++) 
            table[i] = Integer.MAX_VALUE; 
      
            // Compute minimum coins required for all 
            // values from 1 to amount 
            for (int i = 1; i <= amount; i++) 
            { 
                // Go through all coins smaller than i and fill table 
                for (Integer deno : denominations) {
                    if(deno <= i){
                        int subResult = table[i - deno]; 
                        
                        if (subResult != Integer.MAX_VALUE && subResult + 1 < table[i]) 
                           table[i] = subResult + 1; 
                    }   
                }  
            } 
            return table[amount];     
        }

    }
}
