package ru.unlegit.cnfprocessor;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Resolvent(Operand[] clauses) {
    
    public static Resolvent fromString(String string) {
        return new Resolvent(ConjunctiveNormalForm.disjunctFromString(string));
    }

    public static Resolvent create(Operand... clauses) {
        return new Resolvent(clauses);
    }

    public boolean isNotEmpty() {
        return clauses.length != 0;
    }

    public Resolvent resolve(Resolvent resolvent) {
        for (Operand clause : clauses) {
            for (Operand otherClause : resolvent.clauses) {
                if (!clause.isInversion(otherClause)) continue;

                return new Resolvent(Stream.concat(Arrays.stream(clauses), Arrays.stream(resolvent.clauses))
                        .filter(resultClause -> resultClause.symbol() != clause.symbol())
                        .toArray(Operand[]::new)
                );
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return isNotEmpty() ? Arrays.stream(clauses)
                .map(Operand::toString)
                .collect(Collectors.joining(" | ")) : "<empty resolvent>";
    }
}