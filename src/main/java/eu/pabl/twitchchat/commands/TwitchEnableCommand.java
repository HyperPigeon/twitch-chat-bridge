package eu.pabl.twitchchat.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import eu.pabl.twitchchat.TwitchChatMod;
import eu.pabl.twitchchat.config.ModConfig;
import eu.pabl.twitchchat.twitch_integration.Bot;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TwitchEnableCommand implements SubCommand {
  public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
    return ClientCommandManager.literal("enable")
        // The command to be executed if the command "twitch" is entered with the argument "enable"
        // It starts up the irc bot.
        .executes(ctx -> {
          ModConfig config = ModConfig.getConfig();

          if (TwitchChatMod.bot != null && TwitchChatMod.bot.isConnected()) {
            ctx.getSource().sendFeedback(Text.translatable("text.twitchchat.command.enable.already_enabled"));
            return 1;
          }

          if (config.getOauthKey().equals("")) {
            ctx.getSource().sendFeedback(Text.translatable("text.twitchchat.command.enable.set_config"));
            return -1;
          }

          if (config.getChannel().equals("")) {
            ctx.getSource().sendFeedback(Text.translatable("text.twitchchat.command.enable.select_channel"));
          }

          TwitchChatMod.bot = new Bot(config.getOauthKey(), config.getChannel());
          TwitchChatMod.bot.start();
          ctx.getSource().sendFeedback(Text.translatable("text.twitchchat.command.enable.connecting").formatted(Formatting.DARK_GRAY));
          // Return a result. -1 is failure, 0 is a pass and 1 is success.
          return 1;
        });
  }
}
