package ru.unlegit.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record ConjunctiveNormalForm(char[] universe, Operand[][] disjuncts) {

    public static ConjunctiveNormalForm fromString(String string) {
        Operand[][] disjuncts = Arrays.stream(string.replaceAll("\\s+", "").split("&"))
                .map(ConjunctiveNormalForm::disjunctFromString)
                .toArray(Operand[][]::new);
        Character[] symbolSet = Arrays.stream(disjuncts)
                .flatMap(Arrays::stream)
                .map(Operand::symbol)
                .distinct()
                .toArray(Character[]::new);
        char[] universe = new char[symbolSet.length];

        for (int i = 0; i < symbolSet.length; i++) {
            universe[i] = symbolSet[i];
        }

        return new ConjunctiveNormalForm(universe, disjuncts);
    }

    private static Operand[] disjunctFromString(String string) {
        if (string.charAt(0) != '(' || string.charAt(string.length() - 1) != ')') {
            throw new IllegalArgumentException("Ошибка формата КНФ в дюзъюнкте '%s'".formatted(string));
        }

        return Arrays.stream(string.substring(1, string.length() - 1).split("\\|"))
                .map(Operand::fromString)
                .toArray(Operand[]::new);
    }

    @Override
    public String toString() {
        return Arrays.stream(disjuncts).map(operands -> Arrays.stream(operands)
                .map(Operand::toString)
                .collect(Collectors.joining(" | ", "(", ")"))
        ).collect(Collectors.joining(" & "));
    }

    public boolean isGenerallySignificant() {
        if (universe.length > 32) {
            throw new RuntimeException("Невозможно проверить общезначимость КНФ с размер юнивёрсума %d > 32 ".formatted(
                    universe.length
            ));
        }

        int valuesLimit = 1 << universe.length;

        for (int values = 0; values < valuesLimit; values++) {
            for (Operand[] disjunct : disjuncts) {
                if (computeDisjunct(disjunct, values) == 0) return false;
            }
        }

        return true;
    }

    public int[] findFalseInterpretations() {
        List<Integer> interpretations = new ArrayList<>();

        int valuesLimit = 1 << universe.length;

        for (int values = 0; values < valuesLimit; values++) {
            for (Operand[] disjunct : disjuncts) {
                if (computeDisjunct(disjunct, values) == 0) {
                    interpretations.add(values);
                    break;
                }
            }
        }

        int[] interpretationsArray = new int[interpretations.size()];

        for (int i = 0; i < interpretationsArray.length; i++) {
            interpretationsArray[i] = interpretations.get(i);
        }

        return interpretationsArray;
    }

    private int computeDisjunct(Operand[] disjunct, int values) {
        int result = (values & indexOf(disjunct[0].symbol())) & 0x1;

        for (int i = 1; i < disjunct.length; i++) {
            result |= (values & indexOf(disjunct[i].symbol()));
        }

        return result;
    }

    private int indexOf(char symbol) {
        for (int i = 0; i < universe.length; i++) {
            if (universe[i] == symbol) return i;
        }

        throw new RuntimeException("unreachable point");
    }
}