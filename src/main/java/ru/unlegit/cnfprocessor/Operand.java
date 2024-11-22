package ru.unlegit.cnfprocessor;

public record Operand(char symbol, boolean inversion) {

    public static Operand fromString(String string) {
        int length = string.length();
        char firstChar = string.charAt(0);

        if (length > 2 || (length == 2 && firstChar != '!')) {
            throw new IllegalArgumentException("Ошибка формата КНФ в операнде '%s'".formatted(string));
        }

        return length == 1 ? new Operand(firstChar, false) : new Operand(string.charAt(1), true);
    }

    @Override
    public String toString() {
        return (inversion ? "!" : "") + symbol;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Operand operand && symbol == operand.symbol && inversion == operand.inversion;
    }
}