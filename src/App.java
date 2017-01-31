import java.util.HashMap;
import java.util.Map;

public class App {
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    
    void countWords(String line) {
        // System.out.println(line);
        String [] words = line.split("\\s");
        for (String w : words) {
            if(!w.equals("\n")){
            	if (map.containsKey(w)) 
                    map.put(w, new Integer(map.get(w).intValue() + 1));
                else
                    map.put(w, new Integer(1));
            }
        }
    }

    Map.Entry<String, Integer> findMax() {
        Map.Entry<String, Integer> maxEntry = null;
        
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }
    
}
