package dev.the_fireplace.lib.config.cloth.custombutton;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import dev.the_fireplace.lib.api.client.interfaces.CustomButtonBuilder;
import dev.the_fireplace.lib.config.cloth.optionbuilder.ClothGenericOption;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class CustomButtonOption extends ClothGenericOption<String, String> implements CustomButtonBuilder<String> {
    public CustomButtonOption(Translator translator, CustomButtonFieldBuilder fieldBuilder, String optionTranslationBase, String defaultValue, Consumer<String> saveFunction) {
        super(translator, fieldBuilder, optionTranslationBase, defaultValue, saveFunction);
    }

    @Override
    public CustomButtonBuilder<String> setButtonTextSupplier(@Nullable Function<String, String> buttonTextSupplier) {
        ((CustomButtonFieldBuilder) this.fieldBuilder).setButtonTextSupplier(buttonTextSupplier);
        return this;
    }
}
