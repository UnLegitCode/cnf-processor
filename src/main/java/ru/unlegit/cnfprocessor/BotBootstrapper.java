package ru.unlegit.cnfprocessor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class BotBootstrapper {

    private static void reply(TelegramBot bot, Message message, String... text) {
        bot.execute(new SendMessage(message.chat().id(), String.join("\n", text))
                .replyToMessageId(message.messageId())
        );
    }

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                Message message = update.message();
                String rawCnf = message.text();

                try {
                    ConjunctiveNormalForm cnf = ConjunctiveNormalForm.fromString(rawCnf);

                    reply(bot, message, "Введённая КНФ: %s\nОбщезначимость: %s\nЛожные интерпретации: %s".formatted(
                            cnf.toString(),
                            cnf.isGenerallySignificant() ? "да" : "нет",
                            Arrays.stream(cnf.findFalseInterpretations())
                                    .mapToObj(String::valueOf)
                                    .collect(Collectors.joining(", "))
                    ));
                } catch (Exception exception) {
                    reply(bot, message, exception.getMessage());
                }
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}