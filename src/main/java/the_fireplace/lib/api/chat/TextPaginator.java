package the_fireplace.lib.api.chat;

import com.google.common.collect.Lists;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

import static the_fireplace.lib.impl.FireplaceLib.MODID;

public final class TextPaginator {
    private static final int RESULTS_PER_PAGE = 7;

    public static void sendPaginatedChat(ServerCommandSource targetCommandSource, String switchPageCommand, List<Text> allItems, int pageIndex) {
        CommandOutput messageTarget = targetCommandSource.getEntity() != null ? targetCommandSource.getEntity() : targetCommandSource.getMinecraftServer();
        SynchronizedMessageQueue.queueMessages(messageTarget, getPaginatedContent(messageTarget, allItems, pageIndex, switchPageCommand));
    }

    private static int getPageCount(int itemCount) {
        int pageCount = itemCount / RESULTS_PER_PAGE;
        if (itemCount % RESULTS_PER_PAGE > 0) {
            pageCount++;
        }
        return pageCount;
    }

    private static Text[] getPaginatedContent(CommandOutput target, List<Text> allContent, int page, String switchPageCommand) {
        int totalPageCount = getPageCount(allContent.size());

        Text header = getPaginationHeader(target, page, totalPageCount);
        List<Text> content = getPageContents(allContent, page);
        Text footer = getPaginationFooter(target, switchPageCommand, page, totalPageCount);

        List<Text> outputTexts = Lists.newArrayList();
        outputTexts.add(header);
        outputTexts.addAll(content);
        outputTexts.add(footer);

        return outputTexts.toArray(new Text[]{});
    }

    private static Text getPaginationHeader(CommandOutput target, int currentPage, int totalPageCount) {
        Text counter = TranslationService.get(MODID).getTextForTarget(target, "fireplacelib.chat.page.num", currentPage, totalPageCount);
        return new LiteralText("-----------------").setStyle(TextStyles.GREEN).append(counter).append("-------------------").setStyle(TextStyles.GREEN);
    }

    private static List<Text> getPageContents(List<Text> allContents, int page) {
        return Lists.partition(allContents, RESULTS_PER_PAGE).get(page-1);
    }

    private static Text getPaginationFooter(CommandOutput target, String switchPageCommand, int currentPage, int totalPageCount) {
        Text nextButton = getNextButton(target, switchPageCommand, currentPage, totalPageCount);
        Text prevButton = getPreviousButton(target, switchPageCommand, currentPage);
        return new LiteralText("---------------").setStyle(TextStyles.GREEN).append(prevButton).append("---").setStyle(TextStyles.GREEN).append(nextButton).append("-------------").setStyle(TextStyles.GREEN);
    }

    private static Text getNextButton(CommandOutput target, String switchPageCommand, int currentPage, int totalPageCount) {
        return currentPage < totalPageCount ? TranslationService.get(MODID).getTextForTarget(target, "fireplacelib.chat.page.next").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, String.format(switchPageCommand, currentPage +1)))) : new LiteralText("-----");
    }

    private static Text getPreviousButton(CommandOutput target, String switchPageCommand, int currentPage) {
        return currentPage > 1 ? TranslationService.get(MODID).getTextForTarget(target, "fireplacelib.chat.page.prev").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.RUN_COMMAND, String.format(switchPageCommand, currentPage -1)))) : new LiteralText("------");
    }
}
