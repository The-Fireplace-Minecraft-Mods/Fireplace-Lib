package dev.the_fireplace.lib.api.client;

import com.google.common.collect.Lists;
import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.impl.FireplaceLib;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

abstract class ConfigScreenBuilderBase {
    protected final Translator translator;

    protected ConfigScreenBuilderBase(Translator translator) {
        this.translator = translator;
    }

    protected void attachDescription(String optionTranslationBase, byte descriptionRowCount, FieldBuilder<?, ?> builder) {
        if (descriptionRowCount <= 0) {
            return;
        }
        try {
            Method setTooltip = builder.getClass().getMethod("setTooltip", Text[].class);
            if (descriptionRowCount == 1) {
                setTooltip.invoke(builder, (Object) new Text[]{translator.getTranslatedText(optionTranslationBase + ".desc")});
            } else {
                setTooltip.invoke(builder, (Object) genDescriptionTranslatables(optionTranslationBase + ".desc", descriptionRowCount));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            FireplaceLib.getLogger().error("Unable to set tooltip for field builder of type " + builder.getClass().toString(), e);
        }
    }

    protected Text[] genDescriptionTranslatables(String baseKey, int count) {
        List<Text> texts = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            texts.add(translator.getTranslatedText(baseKey + "[" + i + "]"));
        }
        return texts.toArray(new Text[0]);
    }
}
