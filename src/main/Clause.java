package main;

import java.util.ArrayList;
import java.util.Iterator;

public class Clause implements Iterable<Integer> {
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

    @Override
    public Iterator<Integer> iterator() {
        return clauseParts.iterator();
    }
}
