package mcis.jsu.edu.crosswordmagic;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class CrosswordMagicViewModel extends ViewModel {

    /* Application Context */

    private final MutableLiveData<Context> context = new MutableLiveData<Context>();

    /* Display Properties */

    private final MutableLiveData<Integer> windowOverheadDp = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> windowHeightDp = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> windowWidthDp = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> puzzleHeight = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> puzzleWidth = new MutableLiveData<Integer>();

    /* Puzzle Data */

    private final MutableLiveData<Integer> puzzleID = new MutableLiveData<Integer>();
    private final MutableLiveData<HashMap<String, Word>> words = new MutableLiveData<>();
    private final MutableLiveData<String> aClues = new MutableLiveData<String>();
    private final MutableLiveData<String> dClues = new MutableLiveData<String>();

    private final MutableLiveData<Character[][]> letters = new MutableLiveData<Character[][]>();
    private final MutableLiveData<Integer[][]> numbers = new MutableLiveData<Integer[][]>();

    /* Setters / Getters */

    public void setContext(Context c) {
        context.setValue(c);
    }

    public void setWindowHeightDp(int height) {
        windowHeightDp.setValue(height);
    }

    public void setWindowWidthDp(int width) {
        windowWidthDp.setValue(width);
    }

    public void setPuzzleHeight(int height) {
        puzzleHeight.setValue(height);
    }

    public void setPuzzleWidth(int width) {
        puzzleWidth.setValue(width);
    }

    public void setWindowOverheadDp(int width) {
        windowOverheadDp.setValue(width);
    }

    public void setPuzzleID(int id) {
        if ( (puzzleID.getValue() == null) || (puzzleID.getValue() != id) ) {
            getPuzzleData(id);
            puzzleID.setValue(id);
        }
    }

    public Context getContext() {
        return context.getValue();
    }

    public int getWindowHeightDp() {
        return windowHeightDp.getValue();
    }

    public int getWindowWidthDp() {
        return windowWidthDp.getValue();
    }

    public int getPuzzleHeight() {
        return puzzleHeight.getValue();
    }

    public int getPuzzleWidth() {
        return puzzleWidth.getValue();
    }

    public int getWindowOverheadDp() {
        return windowOverheadDp.getValue();
    }

    public int getPuzzleID() {
        return puzzleID.getValue();
    }

    public String getAClues() {
        return aClues.getValue();
    }

    public String getDClues() {
        return dClues.getValue();
    }

    public Character[][] getLetters() {
        return letters.getValue();
    }

    public Integer[][] getNumbers() {
        return numbers.getValue();
    }

    public HashMap<String, Word> getWords() {
        return words.getValue();
    }

    /* Load Puzzle Data from Input File */

    private void getPuzzleData(int id) {

        BufferedReader br = new BufferedReader(new InputStreamReader(context.getValue().getResources().openRawResource(id)));
        String line;
        String[] fields;

        HashMap<String, Word> wordMap = new HashMap<>();
        StringBuilder aString = new StringBuilder();
        StringBuilder dString = new StringBuilder();

        try {

            // Read from the input file using the "br" input stream shown above.  Your program
            // should get the puzzle height/width from the header row in the first line of the
            // input file.  Replace the placeholder values shown below with the values from the
            // file.  Get the data from the remaining rows, splitting each tab-delimited line
            // into an array of strings, which you can use to initialize a Word object.  Add each
            // Word object to the "wordMap" hash map; for the key names, use the box number
            // followed by the direction (for example, "16D" for Box # 16, Down).

            String[] stringArray = br.readLine().trim().split("\t");
            puzzleHeight.setValue(Integer.parseInt(stringArray[0]));
            puzzleWidth.setValue(Integer.parseInt(stringArray[1]));

            Log.d(stringArray[0], "Puzzle height");

            while(br.ready()) {
                String currentLine = br.readLine();
                stringArray = currentLine.trim().split("\t");
                Word newWord = new Word(stringArray);
                //find the key and then put them in the hashmap
                String key = stringArray[2] + stringArray[3];
                wordMap.put(key , newWord);
                String clueString = stringArray[2] + ": " + stringArray[5] + "\n";
                if(stringArray[3].charAt(0) == 'A') {
                    aString.append(clueString);
                }
                else if (stringArray[3].charAt(0) == 'D'){
                    dString.append(clueString);
                }
            }
            //Log.d("Astring", );
            //*/

        } catch (Exception e) {}

        words.setValue(wordMap);
        aClues.setValue(aString.toString());
        dClues.setValue(dString.toString());

        Character[][] aLetters = new Character[puzzleHeight.getValue()][puzzleWidth.getValue()];
        Integer[][] aNumbers = new Integer[puzzleHeight.getValue()][puzzleWidth.getValue()];

        for (int i = 0; i < aLetters.length; ++i) {
            Arrays.fill(aLetters[i], '*');
        }

        for (int i = 0; i < aNumbers.length; ++i) {
            Arrays.fill(aNumbers[i], 0);
        }

        for (HashMap.Entry<String, Word> e : wordMap.entrySet()) {

            Word w = e.getValue();

            // INSERT YOUR CODE HERE
            int row = w.getRow();
            int col = w.getColumn();
            String word = w.getWord();
            String dir = w.getDirection();
            aNumbers[row][col] = w.getBox();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                aLetters[row][col] = ' ';
                //aLetters[row][col] = c;
                if (dir.equals("A")) {
                    ++col;
                }
                else if (dir.equals("D")) {
                    ++row;
                }//*/
            }

        }

        this.letters.setValue(aLetters);
        this.numbers.setValue(aNumbers);

    }

    public String getWord(String key) {
        Character[][] aLetters = this.getLetters();
        Integer[][] aNumbers = this.getNumbers();
        HashMap<String, Word> map = this.getWords();

        return map.get(key).getWord();


    }

    public void addWordToGrid(String key) {
        Integer[][] aNumbers = this.getNumbers();
        Character[][] aLetters = this.getLetters();
        HashMap<String, Word> map = this.getWords();

        String number = Character.toString(key.charAt(0)) + Character.toString(key.charAt(1));
        int row = 0;
        int col = 0;
        char dir = key.charAt(2);
        String word = map.get(key).getWord();

        for(int i = 0; i > this.getPuzzleHeight();i++) {
            for(int j = 0; j > this.getPuzzleWidth();j++) {
                //see if it's the same and then make i the row and j the column
                if (aNumbers[i][j].equals(number)) {
                    row = i;
                    col = j;
                }
            }
        }

        if (dir == 'A') {
            for(int k = 0; k > word.length(); k++) {
                aLetters[row][col + k] = word.charAt(k);
            }
        }
        else {
            for(int l = 0; l > word.length(); l++) {
                aLetters[row + l][col] = word.charAt(l);
            }
        }

    }
}