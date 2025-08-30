/*
package com.guhao.stars.command;

import com.guhao.stars.units.StarDataUnit;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TimeStopCommand {    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("timestop")
            .requires(source -> source.hasPermission(2)) // 需要OP权限(2级)
            .executes(context -> {
                // 获取命令来源
                CommandSourceStack source = context.getSource();

                // 确保命令由玩家执行
                if (source.getEntity() instanceof ServerPlayer player) {
                    boolean newState = !StarDataUnit.isTimeStopped();

                    // 设置时停状态
                    StarDataUnit.setTimeStopped(newState);

                    // 发送反馈消息
                    source.sendSuccess(() ->
                                    Component.literal("时间已" + (newState ? "停止" : "恢复")),
                            true);

                    return Command.SINGLE_SUCCESS;
                } else {
                    source.sendFailure(Component.literal("只有玩家可以执行此命令"));
                    return 0;
                }
            })
            // 添加子命令可以切换特定状态
            .then(Commands.literal("on")
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        if (source.getEntity() instanceof ServerPlayer player) {
                            StarDataUnit.setTimeStopped(true);
                            source.sendSuccess(() -> Component.literal("时间已停止"), true);
                            return Command.SINGLE_SUCCESS;
                        }
                        return 0;
                    }))
            .then(Commands.literal("off")
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        if (source.getEntity() instanceof ServerPlayer player) {
                            StarDataUnit.setTimeStopped(false);
                            source.sendSuccess(() -> Component.literal("时间已恢复"), true);
                            return Command.SINGLE_SUCCESS;
                        }
                        return 0;
                    }))
    );
}
}*/
