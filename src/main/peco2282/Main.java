package peco2282;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
  private static JDA jda;
  private static final String TOKEN = "token";
  private static final String COMMAND_PREFIX = ".";

  public static void main(String[] args) {
    try {
      jda = JDABuilder.createDefault(TOKEN)
              .addEventListeners(new Main())
              .build();

      jda.awaitReady();
      System.out.println("Finished Building");
    } catch (LoginException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    super.onMessageReceived(event);
    jda = event.getJDA();
    long responseNumber = event.getResponseNumber();

    User author = event.getAuthor();
    Message message = event.getMessage();
    MessageChannel channel = event.getChannel();

    String msg = message.getContentDisplay();

    if (event.isFromType(ChannelType.TEXT)) {
      Guild guild = event.getGuild();
      TextChannel textChannel = event.getTextChannel();
      Member member = event.getMember();

      String name;

      if (message.isWebhookMessage()) {
        name = author.getName();
      } else {
        name = member.getEffectiveName();
      }

      System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
    } else if (event.isFromType(ChannelType.PRIVATE)) {
      PrivateChannel privateChannel = event.getPrivateChannel();
      System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
    }

    if (message.equals(COMMAND_PREFIX + "ping")) {
      String ping = String.valueOf(jda.getGatewayPing());
      channel.sendMessage("pong : " + ping);
    }
  }
}