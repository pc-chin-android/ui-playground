package com.pcchin.uiplayground.gamedata;

import android.support.annotation.NonNull;

import com.pcchin.uiplayground.tetris.tetrisblock.TetrisBlock;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/** Functions specifically used in manipulating coordinates stored in 2D ArrayLists **/
public class CoordsFunctions {

    /** Returns a deep copy of the original coordinates list, useful in collision detection */
    public static ArrayList<ArrayList<Integer>> deepCopy(@NonNull ArrayList<ArrayList<Integer>> originalList) {
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (ArrayList<Integer> e: originalList) {
            ArrayList<Integer> tempList = new ArrayList<>(e);
            returnList.add(tempList);
        }
        return returnList;
    }

    /** Returns the specific coordinates of a specific side**/
    @Contract("_, _ -> param1")
    public static ArrayList<ArrayList<Integer>> sideCoords(@NonNull ArrayList<ArrayList<Integer>> originalList, int targetSide) {
        // Checking if coordinates is to specific side of other ones
        boolean[] isWrongSide = new boolean[originalList.size()];
        for (int i = 0; i < originalList.size(); i++) {
            isWrongSide[i] = false;
            for (ArrayList<Integer> other: originalList) {
                if ((targetSide == TetrisBlock.DIR_LEFT && Objects.equals(originalList.get(i).get(1), other.get(1)) && originalList.get(i).get(0) < other.get(0))
                || (targetSide == TetrisBlock.DIR_RIGHT && Objects.equals(originalList.get(i).get(1), other.get(1)) && originalList.get(i).get(0) < other.get(0))
                || (targetSide == TetrisBlock.DIR_DOWN && Objects.equals(originalList.get(i).get(0), other.get(0)) && originalList.get(i).get(1) < other.get(1))
                || (targetSide == TetrisBlock.DIR_UP && Objects.equals(originalList.get(i).get(0), other.get(0)) && originalList.get(i).get(1) > other.get(1))) {
                    isWrongSide[i] = true;
                }
            }
        }

        // Removing any coordinates that is to the right of other ones
        int index = 0;
        for (Iterator<ArrayList<Integer>> iterator = originalList.iterator(); iterator.hasNext();) {
            iterator.next();
            if (isWrongSide[index]) {
                iterator.remove();
            }
            index++;
        }

        return originalList;
    }
}