import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SpreadsheetComputer {
    public void checkAndOpenInputArgument(String args[]){
        try{
            checkArgumentOpenFileAndParse(args);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isArgumentEqualTo1(String args[]){
        if(args.length != 1){
            return false;
        }
        return true;
    }

    public void checkArgumentOpenFileAndParse(String args[]){
        Boolean isOneArgument = isArgumentEqualTo1(args);
        if(!isOneArgument){
            System.out.println("Error: Please input only one text file");
            System.exit(1);
        }else{
            try{
                openInputFile(args);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    String[][] arrayWithParameters;
    int[] arrayParameters;
    public void openInputFile(String args[]) throws Exception{
        File inFile = new File(args[0]);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
        int lineCounter = 0;
        String results = "";
        try{
            String currentFileString = "";
            while((currentFileString = bufferedReader.readLine()) != null){
                lineCounter++;
                if(lineCounter == 1){
                    arrayParameters = parseLineForArraySize(currentFileString);
                    arrayWithParameters = createArray(arrayParameters);
                }else{
                    String[] parsedString = parseStringForEachCharacter(currentFileString);
                    Stack<String> stackOfParsedTokens = parsedStringIntoTokensAndPushedToStack(parsedString);
                    Stack<String>currentStackOfTokens = calculateString(stackOfParsedTokens);
                    //currentStackOfTokens = should be 3 for each
                    while(!currentStackOfTokens.isEmpty()){
                        if(currentStackOfTokens.size() > 1){
                            results = results + currentStackOfTokens.pop() + ", ";
                        }else if(currentStackOfTokens.size() == 1){
                            results = results + currentStackOfTokens.pop() + "\n";
                        }
                    }
                }

            }
            System.out.println("The result string is: " + results);
            //print string to file



        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                bufferedReader.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public int[] parseLineForArraySize(String currentFileString){
        int[] arraySizeArray = new int[2];
        String[]parsedStringTokens = new String[0];

        if(currentFileString.contains(", ")){
            parsedStringTokens = currentFileString.split(", ");
        }else if(currentFileString.contains(",")){
            parsedStringTokens = currentFileString.split(",");
        }else if(currentFileString.contains(" ,")){
            parsedStringTokens = currentFileString.split(" ,");
        }

        for(int tokenCount = 0; tokenCount < parsedStringTokens.length; tokenCount++){
            String token = parsedStringTokens[tokenCount];
            int sizeToken = Integer.parseInt(token);
            arraySizeArray[tokenCount] = sizeToken;
        }
        return arraySizeArray;
    }

    public String[][] createArray(int[] arrayParameters){
        int arrayParameterRow = arrayParameters[0];
        int arrayParameterColumn = arrayParameters[1];
        String[][] arrayWithParameters = new String[arrayParameterRow][arrayParameterColumn];

        return arrayWithParameters;
    }

    public String[] parseStringForEachCharacter(String currentFileString){
        String[] parsedStringForEachCharacters = currentFileString.split(",");
        return parsedStringForEachCharacters;
    }

    public Stack<String> parsedStringIntoTokensAndPushedToStack(String[] parsedString){
        Stack<String> stackOfParsedTokens = new Stack<>();

        for(int parsedStringCount = 0; parsedStringCount < parsedString.length; parsedStringCount++){
            String currentStringToken = parsedString[parsedStringCount];
            if(currentStringToken.contains(" ")){
                String[] arrayOfTokens = arrayOfStringWithSpacesParsed(currentStringToken);
                for(int tokenCounter = 0; tokenCounter < arrayOfTokens.length; tokenCounter++){
                    Pattern pattern = Pattern.compile("^\\s*$");
                    Matcher matcher = pattern.matcher(arrayOfTokens[tokenCounter]);
                    boolean found = matcher.find();
                    if(found == false){
                        stackOfParsedTokens.push(arrayOfTokens[tokenCounter]);
                    }
                }
            }else{
                stackOfParsedTokens.push(currentStringToken);
            }
        }
        return stackOfParsedTokens;
    }

    public String[] arrayOfStringWithSpacesParsed(String currentStringToken){
        String[] parsedStringForEachCharacters = currentStringToken.split("\\s");
        return parsedStringForEachCharacters;
    }

    public Stack<String> calculateString(Stack<String> stackOfParsedTokens){
        Stack<String> resultStack = new Stack<String>();
        String result = "";
        while(!stackOfParsedTokens.isEmpty()){
            String potentialCharacter = stackOfParsedTokens.pop();
            if(potentialCharacter.equals("+")){
                result = calculateValueAddition(stackOfParsedTokens);
                resultStack.push(result);
            }else if(potentialCharacter.equals("*")){
                result = calculateValueMultiplication(stackOfParsedTokens);
                resultStack.push(result);
            }else if(!potentialCharacter.equals("*") && !potentialCharacter.equals("+")){
                if(potentialCharacter.length() > 1){
                    result = checkIfCharacterIsCellReference(potentialCharacter);
                }else{
                    result = potentialCharacter;
                }
                resultStack.push(result);
            }
        }
        //pop from stack and concat to string
        //write to file (a different method)
        System.out.println(resultStack);

        return resultStack;
    }



    public String checkIfCharacterIsCellReference(String potentialCharacter){
        char rowReference = potentialCharacter.charAt(0);
        char convertedRowReference = convertRowReferenceToNumericValue(rowReference);
        char columnReference = potentialCharacter.charAt(1);

        String rowIndex = String.valueOf(convertedRowReference);
        String columnIndex = String.valueOf(columnReference);

        int rowIndexValue = Integer.parseInt(rowIndex);
        int columnIndexValue = Integer.parseInt(columnIndex);
        String returnValue = "";

        if(rowIndexValue >= arrayParameters[0] || columnIndexValue >= arrayParameters[1]
                || rowIndexValue < 0 || columnIndexValue < 0){
            returnValue = "#REF!";
        }else{
            returnValue = arrayWithParameters[rowIndexValue][columnIndexValue];
            if(returnValue == null){
                returnValue = "#REF!";
            }
        }
        return returnValue;
    }

    public String calculateValueAddition(Stack<String>stackOfParsedTokens){
        String resultValue = "";
        if(stackOfParsedTokens.peek().length() > 1){
            resultValue = convertRowToIndexValueAndReturnString(stackOfParsedTokens);
            System.out.println("result value is: " + resultValue);
            if(!resultValue.equals("#REF!")){
                int value1 = Integer.parseInt(resultValue);
                int value2 = Integer.parseInt(stackOfParsedTokens.pop());
                int result = value1 + value2;
                resultValue = Integer.toString(result);
            }else{
                stackOfParsedTokens.pop();
            }
        }else{
            int value1 = Integer.parseInt(stackOfParsedTokens.pop());
            int value2 = Integer.parseInt(stackOfParsedTokens.pop());
            int result = value1 + value2;
            resultValue = Integer.toString(result);
        }
        return resultValue;
    }

    public String calculateValueMultiplication(Stack<String>stackOfParsedTokens){
        String resultValue = "";
        if(stackOfParsedTokens.peek().length() > 1){
            resultValue = convertRowToIndexValueAndReturnString(stackOfParsedTokens);
            if(!resultValue.equals("#REF!")){
                int value1 = Integer.parseInt(resultValue);
                int value2 = Integer.parseInt(stackOfParsedTokens.pop());
                int result = value1 * value2;
                resultValue = Integer.toString(result);
            }
        }else{
            int value1 = Integer.parseInt(stackOfParsedTokens.pop());
            int value2 = Integer.parseInt(stackOfParsedTokens.pop());
            int result = value1 * value2;
            resultValue = Integer.toString(result);
        }
        return resultValue;
    }

    public String convertRowToIndexValueAndReturnString(Stack<String>stackOfParsedTokens){
        String cellReference = stackOfParsedTokens.pop();
        char rowReference = cellReference.charAt(0);
        char convertedRowReference = convertRowReferenceToNumericValue(rowReference);
        char columnReference = cellReference.charAt(1);

        String rowIndex = String.valueOf(convertedRowReference);
        String columnIndex = String.valueOf(columnReference);

        int rowIndexValue = Integer.parseInt(rowIndex);
        int columnIndexValue = Integer.parseInt(columnIndex);
        String returnValue = "";

        if(rowIndexValue >= arrayParameters[0] || columnIndexValue >= arrayParameters[1]
                || rowIndexValue < 0 || columnIndexValue < 0){
            returnValue = "#REF!";
        }else{
            returnValue = arrayWithParameters[rowIndexValue][columnIndexValue];
            if(returnValue == null){
                returnValue = "#REF!";
            }
        }
        return returnValue;
    }

    public char convertRowReferenceToNumericValue(char rowReference){
        char numericValueOfCell = 0;

        if(rowReference == 'A'){
            numericValueOfCell = '1';
        }else if(rowReference == 'B'){
            numericValueOfCell = '2';
        }else if(rowReference == 'C'){
            numericValueOfCell = '3';
        }else if(rowReference == 'D'){
            numericValueOfCell = '4';
        }else if(rowReference == 'E'){
            numericValueOfCell = '5';
        }else if(rowReference == 'F'){
            numericValueOfCell = '6';
        }else if(rowReference == 'G'){
            numericValueOfCell = '7';
        }else if(rowReference == 'H'){
            numericValueOfCell = '8';
        }else if(rowReference == 'I'){
            numericValueOfCell = '9';
        }

        return numericValueOfCell;
    }


    public static void main(String args[]){
        SpreadsheetComputer spreadsheetComputer = new SpreadsheetComputer();
        spreadsheetComputer.checkAndOpenInputArgument(args);
    }
}
