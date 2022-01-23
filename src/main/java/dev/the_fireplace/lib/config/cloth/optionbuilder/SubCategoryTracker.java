package dev.the_fireplace.lib.config.cloth.optionbuilder;

import dev.the_fireplace.lib.api.client.interfaces.OptionBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public final class SubCategoryTracker implements OptionBuilder<Object>
{
    private OptionBuilder<?> lastEntry = null;
    private final SubCategoryBuilder builder;

    public SubCategoryTracker(SubCategoryBuilder builder) {
        this.builder = builder;
    }

    public void addEntry(OptionBuilder<?> entry) {
        this.lastEntry = entry;
    }

    public boolean hasEntries() {
        return lastEntry != null;
    }

    public OptionBuilder<?> getLastEntry() {
        return lastEntry;
    }

    public SubCategoryBuilder getBuilder() {
        return builder;
    }

    @Override
    public OptionBuilder<Object> setDescriptionRowCount(byte descriptionRowCount) {
        return null;
    }

    @Override
    public OptionBuilder<Object> setErrorSupplier(Function<Object, Optional<Text>> errorSupplier) {
        return null;
    }

    @Override
    public OptionBuilder<Object> addDependency(OptionBuilder<Boolean> parent) {
        return null;
    }

    @Override
    public <U> OptionBuilder<Object> addDependency(OptionBuilder<U> parent, Predicate<U> dependencyMet) {
        return null;
    }
}
