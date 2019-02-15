package com.pcchin.uiplayground.gamedata;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/** Functions specifically used in manipulating coordinates stored in 2D ArrayLists **/
public class CoordsFunctions {

    /** Returns a deep copy of the original coordinates list, useful in collision detection */
    public static ArrayList<ArrayList<Integer>> deepCopy(@NotNull ArrayList<ArrayList<Integer>> originalList) {
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (ArrayList<Integer> e: originalList) {
            ArrayList<Integer> tempList = new ArrayList<>(e);
            returnList.add(tempList);
        }
        return returnList;
    }

    /** Returns only the coordinates at the bottom **/
    @Contract("_ -> param1")
    public static ArrayList<ArrayList<Integer>> downCoords(@NonNull ArrayList<ArrayList<Integer>> originalList) {
        // Checking if the coordinates is above other ones (lower y)
        boolean[] isTop = new boolean[originalList.size()];
        for (int i = 0; i < originalList.size(); i++) {
            isTop[i] = false;
            for (ArrayList<Integer> other: originalList) {
                if (Objects.equals(originalList.get(i).get(0), other.get(0)) && originalList.get(i).get(1) < other.get(1)) {
                    isTop[i] = true;
                }
            }
        }

        // Removing any coordinates that is above other ones
        int index = 0;
        for (Iterator<ArrayList<Integer>> iterator = originalList.iterator(); iterator.hasNext();) {
            iterator.next();
            if (isTop[index]) {
                iterator.remove();
            }
            index++;
        }
        
        return originalList;
    }

    /** Returns only the coordinates at the left **/
    @Contract("_ -> param1")
    public static ArrayList<ArrayList<Integer>> leftCoords(@NonNull ArrayList<ArrayList<Integer>> originalList) {
        // Checking if the coordinates is to the right of other ones (higher x)
        boolean[] isRight = new boolean[originalList.size()];
        for (int i = 0; i < originalList.size(); i++) {
            isRight[i] = false;
            for (ArrayList<Integer> other: originalList) {
                if (Objects.equals(originalList.get(i).get(1), other.get(1)) && originalList.get(i).get(0) > other.get(0)) {
                    isRight[i] = true;
                }
            }
        }

        // Removing any coordinates that is to the right of other ones
        int index = 0;
        for (Iterator<ArrayList<Integer>> iterator = originalList.iterator(); iterator.hasNext();) {
            iterator.next();
            if (isRight[index]) {
                iterator.remove();
            }
            index++;
        }
        
        return originalList;
    }

    /** Returns only the coordinates at the right **/
    @Contract("_ -> param1")
    public static ArrayList<ArrayList<Integer>> rightCoords(@NonNull ArrayList<ArrayList<Integer>> originalList) {
        // Checking if the coordinates is to the left of other ones (lower x)
        boolean[] isLeft = new boolean[originalList.size()];
        for (int i = 0; i < originalList.size(); i++) {
            isLeft[i] = false;
            for (ArrayList<Integer> other: originalList) {
                if (Objects.equals(originalList.get(i).get(1), other.get(1)) && originalList.get(i).get(0) < other.get(0)) {
                    isLeft[i] = true;
                }
            }
        }

        // Removing any coordinates that is to the right of other ones
        int index = 0;
        for (Iterator<ArrayList<Integer>> iterator = originalList.iterator(); iterator.hasNext();) {
            iterator.next();
            if (isLeft[index]) {
                iterator.remove();
            }
            index++;
        }
        
        return originalList;
    }
}
