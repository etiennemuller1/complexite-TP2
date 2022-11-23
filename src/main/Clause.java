package main;

import java.util.ArrayList;

public class Clause {
    ArrayList<Integer> clauseParts = new ArrayList<>();

    public void add(int clausePart) {
        clauseParts.add(clausePart);
    }

    public String createCNFLine() {
        String line = "";
        for (Integer part : clauseParts
             ) {
            line += part.toString() + " ";
        }
        line += "0";
        return line;
    }
}
