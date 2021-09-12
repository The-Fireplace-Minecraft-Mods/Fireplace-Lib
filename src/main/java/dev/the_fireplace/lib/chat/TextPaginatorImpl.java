package dev.the_fireplace.lib.chat;

import com.google.common.collect.Lists;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.chat.injectables.TextPaginator;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.entrypoints.FireplaceLib;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@ThreadSafe
@Implementation
@Singleton
public final class TextPaginatorImpl implements TextPaginator {
    private static final int RESULTS_PER_PAGE = 7;
    private final MessageQueue messageQueue;
    private final Translator translator;
    private final TextStyles textStyles;

    @Inject
    public TextPaginatorImpl(MessageQueue messageQueue, TranslatorFactory translatorFactory, TextStyles textStyles) {
        this.messageQueue = messageQueue;
        this.translator = translatorFactory.getTranslator(FireplaceLib.MODID);
        this.textStyles = textStyles;
    }

    @Override
    public void sendPaginatedChat(ServerCommandSource targetCommandSource, String switchPageCommand, List<? extends Text> allItems, int pageIndex) {
        CommandOutput messageTarget = targetCommandSource.getEntity() != null ? targetCommandSource.getEntity() : targetCommandSource.getMinecraftServer();
        messageQueue.queueMessages(messageTarget, getPaginatedContent(messageTarget, allItems, pageIndex, switchPageCommand));
    }

    private static int getPageCount(int itemCount) {
        int pageCount = itemCount / RESULTS_PER_PAGE;
        if (itemCount % RESULTS_PER_PAGE > 0) {
            pageCount++;
        }
        return pageCount;
    }

    private Text[] getPaginatedContent(CommandOutput target, List<? extends Text> allContent, int page, String switchPageCommand) {
        int totalPageCount = getPageCount(allContent.size());

        Text header = getPaginationHeader(target, page, totalPageCount);
        List<? extends Text> content = getPageContents(allContent, page);
        Text footer = getPaginationFooter(target, switchPageCommand, page, totalPageCount);

        List<Text> outputTexts = Lists.newArrayList();
        outputTexts.add(header);
        outputTexts.addAll(content);
        outputTexts.add(footer);

        return outputTexts.toArray(new Text[]{});
    }

    private Text getPaginationHeader(CommandOutput target, int currentPage, int totalPageCount) {
        Text counter = translator.getTextForTarget(target, "fireplacelib.chat.page.num", currentPage, totalPageCount);
        return new LiteralText("-----------------").setStyle(textStyles.green())
            .append(counter)
            .append("-------------------").setStyle(textStyles.green());
    }

    private static List<? extends Text> getPageContents(List<? extends Text> allContents, int page) {
        return Lists.partition(allContents, RESULTS_PER_PAGE).get(page-1);
    }

    private Text getPaginationFooter(CommandOutput target, String switchPageCommand, int currentPage, int totalPageCount) {
        Text nextButton = getNextButton(target, switchPageCommand, currentPage, totalPageCount);
        Text prevButton = getPreviousButton(target, switchPageCommand, currentPage);
        return new LiteralText("---------------").setStyle(textStyles.green())
            .append(prevButton)
            .append("---").setStyle(textStyles.green())
            .append(nextButton)
            .append("-------------").setStyle(textStyles.green());
    }

    private Text getNextButton(CommandOutput target, String switchPageCommand, int currentPage, int totalPageCount) {
        ClickEvent viewNextPage = new ClickEvent(Action.RUN_COMMAND, String.format(switchPageCommand, currentPage + 1));
        return currentPage < totalPageCount
            ? translator.getTextForTarget(target, "fireplacelib.chat.page.next").setStyle(Style.EMPTY.withClickEvent(viewNextPage))
            : new LiteralText("-----");
    }

    private Text getPreviousButton(CommandOutput target, String switchPageCommand, int currentPage) {
        ClickEvent viewPreviousPage = new ClickEvent(Action.RUN_COMMAND, String.format(switchPageCommand, currentPage - 1));
        return currentPage > 1
            ? translator.getTextForTarget(target, "fireplacelib.chat.page.prev").setStyle(Style.EMPTY.withClickEvent(viewPreviousPage))
            : new LiteralText("------");
    }
}
