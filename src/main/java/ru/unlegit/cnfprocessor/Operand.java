package ru.unlegit.cnfprocessor;

import org.jetbrains.annotations.NotNull;

public record Operand(char symbol, boolean inversion) implements Comparable<Operand> {

    public static Operand fromString(String string) {
        int length = string.length();
        char firstChar = string.charAt(0);

        if (length > 2 || (length == 2 && firstChar != '!')) {
            throw new IllegalArgumentException("Ошибка формата КНФ в операнде '%s'".formatted(string));
        }

        return length == 1 ? new Operand(firstChar, false) : new Operand(string.charAt(1), true);
    }

    public boolean isInversion(Operand clause) {
        return clause.symbol == symbol() && clause.inversion != inversion;
    }

    @Override
    public String toString() {
        return (inversion ? "!" : "") + symbol;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Operand(char symbol1, boolean inversion1) && symbol == symbol1 && inversion == inversion1;
    }

    @Override
    public int compareTo(@NotNull Operand operand) {
        if (symbol == operand.symbol) {
            if (inversion) {
                if (operand.inversion) return 0;
                return -1;
            }
            return -1;
        }

        return Character.compare(symbol, operand.symbol);
    }
}