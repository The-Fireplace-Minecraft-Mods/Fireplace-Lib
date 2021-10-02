package dev.the_fireplace.lib.config.cloth;

import dev.the_fireplace.lib.domain.config.OptionTypeConverter;
import dev.the_fireplace.lib.mixin.clothconfig.AbstractConfigEntryAccessor;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class ClothConfigDependencyHandler {
    public static final Set<AbstractConfigEntry<?>> DISABLED_ENTRIES = new HashSet<>();
    private final Set<ClothOptionDependency<?, ?>> configEntries = new HashSet<>();

    public <S, T> void addOption(AbstractConfigListEntry<T> option, OptionTypeConverter<S, T> typeConverter) {
        configEntries.add(new ClothOptionDependency<S, T>() {
            @Override
            public AbstractConfigListEntry<T> getEntry() {
                return option;
            }

            @Override
            public OptionTypeConverter<S, T> getConverter() {
                return typeConverter;
            }

            @Override
            public S getConvertedValue() {
                return typeConverter.convertFromClothType(option.getValue());
            }
        });
    }

    public void addDependency(
        AbstractConfigListEntry<?> parent,
        AbstractConfigListEntry<?> child,
        Predicate<Object> shouldShowChildBasedOnParentValue
    ) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        Supplier<Optional<Text>> configEntryErrorSupplier = ((AbstractConfigEntryAccessor) parent).getErrorSupplier();
        Supplier<Optional<Text>> dependencyCheckingErrorSupplier = addDependencyCheckToErrorSupplier(
            parent,
            child,
            shouldShowChildBasedOnParentValue,
            configEntryErrorSupplier
        );
        parent.setErrorSupplier(dependencyCheckingErrorSupplier);
    }

    @Nonnull
    private Supplier<Optional<Text>> addDependencyCheckToErrorSupplier(
        AbstractConfigListEntry<?> parentConfigEntry,
        AbstractConfigListEntry<?> childConfigEntry,
        Predicate<Object> shouldShowChildBasedOnParentValue,
        @Nullable Supplier<Optional<Text>> errorSupplier
    ) {
        return () -> {
            Object parentEntryGuiValue = getClothOptionDependency(parentConfigEntry).getConvertedValue();

            if (!DISABLED_ENTRIES.contains(parentConfigEntry) && shouldShowChildBasedOnParentValue.test(parentEntryGuiValue)) {
                showConfigEntry(childConfigEntry);
            } else {
                hideConfigEntry(childConfigEntry);
            }
            return errorSupplier != null ? errorSupplier.get() : Optional.empty();
        };
    }

    private <T> ClothOptionDependency<?, T> getClothOptionDependency(AbstractConfigListEntry<T> entry) {
        for (ClothOptionDependency<?, ?> clothOptionDependency : configEntries) {
            if (clothOptionDependency.getEntry().equals(entry)) {
                //noinspection unchecked
                return (ClothOptionDependency<?, T>) clothOptionDependency;
            }
        }

        throw new IllegalStateException("Cloth Option Dependency not found!");
    }

    private void hideConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.add(dependentEntry);
    }

    private void showConfigEntry(AbstractConfigEntry<?> dependentEntry) {
        DISABLED_ENTRIES.remove(dependentEntry);
    }
}
