package ru.unlegit.cnfprocessor;

import java.util.*;
import java.util.stream.Collectors;

public final class Main {

    private static void checkTheorem(List<Resolvent> resolvents) {
        boolean modified;
        int iteration = 1;

        do {
            System.out.printf("Iteration #%d:%n", iteration++);

            modified = false;
            Set<Resolvent> resultResolvents = new HashSet<>();
            List<Integer> usedResolvents = new LinkedList<>();

            leftResolventCycle:
            for (int i = 0; i < resolvents.size(); i++) {
                if (usedResolvents.contains(i)) continue;

                Resolvent left = resolvents.get(i);

                for (int j = i + 1; j < resolvents.size(); j++) {
                    if (usedResolvents.contains(j)) continue;

                    Resolvent right = resolvents.get(j);
                    Resolvent result = left.resolve(right);

                    if (result != null) {
                        if (result.isNotEmpty()) resultResolvents.add(result);

                        System.out.printf(" - %s :: %s -> %s%n", left, right, result);

                        usedResolvents.add(i);
                        usedResolvents.add(j);
                        modified = true;

                        continue leftResolventCycle;
                    }
                }

                resultResolvents.add(left);
            }

            if (resolvents.isEmpty()) {
                System.out.println("Final iteration\n");
            } else {
                System.out.printf("%s -> %s%n%n", formatResolvents(resolvents), formatResolvents(resultResolvents));
            }

            resolvents = new ArrayList<>(resultResolvents);
        } while (modified);

        if (resolvents.isEmpty()) {
            System.out.println("Теорема выполняется");
        } else {
            System.out.printf("Теорема опровергнута. Оставшиеся резольвенты: %s%n", formatResolvents(resolvents));
        }
    }

    public static void main(String[] args) {
        List<Resolvent> resolvents = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите кол-во посылок: ");

            int premiseAmount = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\nВведите формулы-посылки:");

            for (int i = 0; i < premiseAmount; i++) {
                resolvents.add(Resolvent.fromString(scanner.nextLine().trim()));
            }

            System.out.print("\nВведите отрицание форулы-следствия: ");

            resolvents.add(Resolvent.fromString(scanner.nextLine().trim()));

            System.out.println("\nПроверяем теорему...\n");
        }

        checkTheorem(resolvents);
    }

    private static String formatResolvents(Collection<Resolvent> resolvents) {
        return resolvents.stream()
                .map(resolvent -> "(".concat(resolvent.toString()).concat(")"))
                .collect(Collectors.joining(", "));
    }
}