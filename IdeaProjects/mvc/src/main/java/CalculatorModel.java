public class CalculatorModel {
    private int calcualationValue;

    public void addTwoNumbers(int firstNumber, int secondNumber){
        calcualationValue = firstNumber + secondNumber;
    }

    public int getCalculationValue() {
        return calcualationValue;
    }
}
