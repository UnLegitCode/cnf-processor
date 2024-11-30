package ru.unlegit.cnfprocessor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public final class Main {

    private static void reply(TelegramBot bot, Message message, String... text) {
        bot.execute(new SendMessage(message.chat().id(), String.join("\n", text))
                .replyToMessageId(message.messageId())
        );
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Введите формулы-посылки (для окончания ввода формул-посылок передайте пустую строку):");
        List<ConjunctiveNormalForm> premises = new LinkedList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    if (premises.isEmpty()) {
                        System.out.println("Введите хотя бы одну формулу-посылку");
                    } else {
                        break;
                    }
                } else {
                    premises.add(ConjunctiveNormalForm.fromString(line));
                }
            }
        }

        System.out.println("\nВыводим формулы-следствия...\n");

        List<ConjunctiveNormalForm> inferences = LogicalInference.infer(premises);

        System.out.println("Формулы-следствия: ");

        inferences.forEach(inference -> System.out.println(" - " + inference));


//        TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
//
//        bot.setUpdatesListener(updates -> {
//            updates.forEach(update -> {
//                Message message = update.message();
//                String rawCnf = message.text();
//
//                try {
//                    ConjunctiveNormalForm cnf = ConjunctiveNormalForm.fromString(rawCnf);
//
//                    reply(bot, message, "Введённая КНФ: %s\nОбщезначимость: %s\nЛожные интерпретации: %s".formatted(
//                            cnf.toString(),
//                            cnf.isGenerallySignificant() ? "да" : "нет",
//                            Arrays.stream(cnf.findFalseInterpretations())
//                                    .mapToObj(String::valueOf)
//                                    .collect(Collectors.joining(", "))
//                    ));
//                } catch (Exception exception) {
//                    reply(bot, message, exception.getMessage());
//                }
//            });
//
//            return UpdatesListener.CONFIRMED_UPDATES_ALL;
//        });
    }
}