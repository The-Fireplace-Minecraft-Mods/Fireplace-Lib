package dev.the_fireplace.lib.chat;

import com.google.common.collect.Lists;
import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.FireplaceLibConstants;
import dev.the_fireplace.lib.api.chat.injectables.MessageQueue;
import dev.the_fireplace.lib.api.chat.injectables.TextPaginator;
import dev.the_fireplace.lib.api.chat.injectables.TextStyles;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@ThreadSafe
@Implementation
@Singleton
public final class TextPaginatorImpl implements TextPaginator
{
    private static final int RESULTS_PER_PAGE = 7;
    private final MessageQueue messageQueue;
    private final Translator translator;
    private final TextStyles textStyles;

    @Inject
    public TextPaginatorImpl(MessageQueue messageQueue, TranslatorFactory translatorFactory, TextStyles textStyles) {
        this.messageQueue = messageQueue;
        this.translator = translatorFactory.getTranslator(FireplaceLibConstants.MODID);
        this.textStyles = textStyles;
    }

    @Override
    public void sendPaginatedChat(CommandSourceStack targetCommandSource, String switchPageCommand, List<? extends Component> allItems, int pageIndex) {
        CommandSource messageTarget = targetCommandSource.getEntity() != null ? targetCommandSource.getEntity() : targetCommandSource.getMinecraftServer();
        messageQueue.queueMessages(messageTarget, getPaginatedContent(messageTarget, allItems, pageIndex, switchPageCommand));
    }

    private static int getPageCount(int itemCount) {
        int pageCount = itemCount / RESULTS_PER_PAGE;
        if (itemCount % RESULTS_PER_PAGE > 0) {
            pageCount++;
        }
        return pageCount;
    }

    private Component[] getPaginatedContent(CommandSource target, List<? extends Component> allContent, int page, String switchPageCommand) {
        int totalPageCount = getPageCount(allContent.size());

        Component header = getPaginationHeader(target, page, totalPageCount);
        List<? extends Component> content = getPageContents(allContent, page);
        Component footer = getPaginationFooter(target, switchPageCommand, page, totalPageCount);

        List<Component> outputTexts = Lists.newArrayList();
        outputTexts.add(header);
        outputTexts.addAll(content);
        outputTexts.add(footer);

        return outputTexts.toArray(new Component[]{});
    }

    private Component getPaginationHeader(CommandSource target, int currentPage, int totalPageCount) {
        Component pageNumber = translator.getTextForTarget(target, "fireplacelib.chat.page.num", currentPage, totalPageCount);
        return new TextComponent("-----------------").setStyle(textStyles.green())
            .append(pageNumber)
            .append("-------------------").setStyle(textStyles.green());
    }

    private static List<? extends Component> getPageContents(List<? extends Component> allContents, int page) {
        return Lists.partition(allContents, RESULTS_PER_PAGE).get(page - 1);
    }

    private Component getPaginationFooter(CommandSource target, String switchPageCommand, int currentPage, int totalPageCount) {
        Component nextButton = getNextButton(target, switchPageCommand, currentPage, totalPageCount);
        Component prevButton = getPreviousButton(target, switchPageCommand, currentPage);
        return new TextComponent("---------------").setStyle(textStyles.green())
            .append(prevButton)
            .append("---").setStyle(textStyles.green())
            .append(nextButton)
            .append("-------------").setStyle(textStyles.green());
    }

    private Component getNextButton(CommandSource target, String switchPageCommand, int currentPage, int totalPageCount) {
        ClickEvent viewNextPage = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(switchPageCommand, currentPage + 1));
        return currentPage < totalPageCount
            ? translator.getTextForTarget(target, "fireplacelib.chat.page.next").setStyle(new Style().setClickEvent(viewNextPage))
            : new TextComponent("-----");
    }

    private Component getPreviousButton(CommandSource target, String switchPageCommand, int currentPage) {
        ClickEvent viewPreviousPage = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(switchPageCommand, currentPage - 1));
        return currentPage > 1
            ? translator.getTextForTarget(target, "fireplacelib.chat.page.prev").setStyle(new Style().setClickEvent(viewPreviousPage))
            : new TextComponent("------");
    }
}
